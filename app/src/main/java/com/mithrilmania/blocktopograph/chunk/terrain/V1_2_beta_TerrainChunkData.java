package com.mithrilmania.blocktopograph.chunk.terrain;

import com.mithrilmania.blocktopograph.WorldData;
import com.mithrilmania.blocktopograph.chunk.Chunk;
import com.mithrilmania.blocktopograph.chunk.ChunkTag;
import com.mithrilmania.blocktopograph.map.Biome;
import com.mithrilmania.blocktopograph.map.Block;
import com.mithrilmania.blocktopograph.nbt.convert.DataConverter;
import com.mithrilmania.blocktopograph.nbt.tags.CompoundTag;
import com.mithrilmania.blocktopograph.nbt.tags.Tag;
import com.mithrilmania.blocktopograph.util.Noise;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/*
* Beta palleted chunk format.
* This version was not used very long; it was introduced around the first Update Aquatic and replaced
* around the second.
* Chunk data is:
* [1 byte: version (value of 1 indicates this version]
* [1 byte: sub-version; indicates size of each runtime ID; see setChunkSubversion method]
* [runtime ids: for each block, a reference to the Pallet entry for that block. Arranged into 32-bit "words" (d-words)]
* [4 bytes: number of pallet entries]
* [pallet entries: NBT tags for each block type in the chunk. Each one stores block name (not ID) and variant]
*
* Note that the number of bits per runtime ID will be the minimum needed to express the number of pallets available.
* In some cases, it will not be an even byte or half-bite; it could be 3, 5, or 6 bits, for example. In those cases, there will be padding in each d-word
* With 4096 blocks per chunk, there will never be more than 4096 entries, so the maximum specified value (16) will be sufficient.
* See https://gist.github.com/Tomcc/a96af509e275b1af483b25c543cfbf37 for further details.
 */
public class V1_2_beta_TerrainChunkData extends TerrainChunkData {
    public volatile ByteBuffer terrainData, data2D;

    public static final int chunkW = 16, chunkL = 16, chunkH = 16;

    public static final int area = chunkW * chunkL;
    public static final int vol = area * chunkH;

    public static final int POS_VERSION = 0;
    public static final int POS_BLOCK_IDS = POS_VERSION + 1;
    public static final int POS_META_DATA = POS_BLOCK_IDS + vol;
    public static final int POS_SKY_LIGHT = POS_META_DATA + (vol >> 1);
    public static final int TERRAIN_LENGTH = POS_SKY_LIGHT + (vol >> 1);

    public static final int POS_HEIGHTMAP = 0;
    // it looks like each biome takes 2 bytes, and the first 1 byte of every 2 bytes is always 0!?
    public static final int POS_BIOME_DATA = POS_HEIGHTMAP + area + area;
    public static final int DATA2D_LENGTH = POS_BIOME_DATA + area;

    public int chunkSubVersion;
    public int bitsPerBlock;
    public int blocksPerDWord;
    public int palletStartPos;
    public boolean hasPadding;

    public ArrayList<Tag> pallet;

    public void setChunkSubversion(int newVers)
    {
        chunkSubVersion = newVers;
        switch(newVers) {
            case 1: {
                blocksPerDWord = 32;
                bitsPerBlock = 1;
                hasPadding = false;
                break;
            }
            case 2: {
                blocksPerDWord = 16;
                bitsPerBlock = 2;
                hasPadding = false;
                break;
            }
            case 3: {
                blocksPerDWord = 10;
                bitsPerBlock = 3;
                hasPadding = true;
                break;
            }
            case 4: {
                blocksPerDWord = 8;
                bitsPerBlock = 4;
                hasPadding = false;
                break;
            }
            case 5: {
                blocksPerDWord = 6;
                bitsPerBlock = 5;
                hasPadding = true;
                break;
            }
            case 6: {
                blocksPerDWord = 5;
                bitsPerBlock = 6;
                hasPadding = true;
                break;
            }
            case 8: {
                blocksPerDWord = 4;
                bitsPerBlock = 8;
                hasPadding = false;
                break;
            }
            case 10: {
                blocksPerDWord = 2;
                bitsPerBlock = 16;
                hasPadding = false;
                break;
            }
            default: { // invalid
                blocksPerDWord = 8;
                bitsPerBlock = 4;
                hasPadding = false;
            }
        }

        palletStartPos = (int)Math.ceil(4096.0 / blocksPerDWord) * 4 + 6;
    }

    public int[] runtimeIDs;


    public V1_2_beta_TerrainChunkData(Chunk chunk, byte subChunk) {
        super(chunk, subChunk);
    }

    @Override
    public void write() throws WorldData.WorldDBException {
        this.chunk.worldData.writeChunkData(chunk.x, chunk.z, ChunkTag.TERRAIN, chunk.dimension, subChunk, true, terrainData.array());
        this.chunk.worldData.writeChunkData(chunk.x, chunk.z, ChunkTag.DATA_2D, chunk.dimension, subChunk, true, data2D.array());
    }
    public String val;
    @Override
    public boolean loadTerrain() {
        if(terrainData == null){
            int dwordCount = 0;
            int blockCount = 0;
            String currDWordString;
            int byteIndex = 2;
            int mask = 0, finalID;
            int unsigned = 0, origUnsigned = 0;
            int currBlock = 0;
            int vers = 0;
            byte[] palletData;
            try {
                runtimeIDs = new int[4096];

                byte[] rawData = this.chunk.worldData.getChunkData(chunk.x, chunk.z, ChunkTag.TERRAIN, chunk.dimension, subChunk, true);
                if(rawData == null) return false;

                val = WorldData.bytesToHex(rawData, 0, rawData.length);

                vers = rawData[1] >> 1;// There is an extra bit at the end of the byte that holds sub version; discard it
                setChunkSubversion(vers);

                // gather the pallet data from the end of the chunk data
                palletData = new byte[rawData.length - palletStartPos];
                System.arraycopy(rawData, palletStartPos, palletData, 0, rawData.length - palletStartPos);
                pallet = DataConverter.read(palletData);

                this.terrainData = ByteBuffer.wrap(rawData);

                terrainData.order(ByteOrder.LITTLE_ENDIAN);

                // build a mask to eliminate unneeded high bits
                // first add 1's to preserve the desired bits
                for(int j = 0; j < bitsPerBlock; j++) {
                    mask = mask << 1;
                    mask = mask | 1;
                }

                for(int currDWord = 0; currDWord <= 4096 / blocksPerDWord; currDWord++) {
                    unsigned = terrainData.getInt(byteIndex);

                    // now read each blockID from the current word
                    for(int i = 0; i < blocksPerDWord && currBlock < 4096; i++)
                    {
                        // shift off unneeded low bits
                        finalID = unsigned >>> i * bitsPerBlock;

                        // now apply the mask to leave only the runtimeID
                        finalID = finalID & mask;

                        // get the pallet tag that tells which block this is
                        Tag palletEntry = pallet.get((int)finalID);

                        // save off the actual ID for easier access later
                        runtimeIDs[currBlock] = (int)finalID;
                        currBlock++;
                        blockCount++;
                    }

                    byteIndex += 4;
                }

                return true;
            } catch (Exception e){
                //data is not present
                return false;
            }
        }
        else return true;
    }

    @Override
    public boolean load2DData() {
        if(data2D == null){
            try {
                byte[] rawData = this.chunk.worldData.getChunkData(chunk.x, chunk.z, ChunkTag.DATA_2D, chunk.dimension, subChunk, false);
                if(rawData == null) return false;
                this.data2D = ByteBuffer.wrap(rawData);
                return true;
            } catch (Exception e){
                //data is not present
                return false;
            }
        }
        else return true;
    }

    @Override
    public void createEmpty() {

        byte[] terrain = new byte[TERRAIN_LENGTH];

        //version byte
        terrain[0] = terrainData.get(0);

        int x, y, z, i = 1, realY;
        byte bedrock = (byte) 7;
        byte sandstone = (byte) 24;

        //generate super basic terrain (one layer of bedrock, 31 layers of sandstone)
        for(x = 0; x < chunkW; x++){
            for(z = 0; z < chunkL; z++){
                for(y = 0, realY = chunkH * this.subChunk; y < chunkH; y++, i++, realY++){
                    terrain[i] = (realY == 0 ? bedrock : (realY < 32 ? sandstone : 0));
                }
            }
        }


        //fill meta-data with 0
        for(; i < POS_META_DATA; i++){
            terrain[i] = (byte) 0;
        }

        //fill block-light with 0xff
        for(; i < TERRAIN_LENGTH; i++){
            terrain[i] = (byte) 0xff;
        }

        this.terrainData = ByteBuffer.wrap(terrain);
        i = 0;


        if(this.subChunk == (byte) 0){

            byte[] data2d = new byte[DATA2D_LENGTH];

            //fill heightmap
            for(; i < POS_BIOME_DATA;){
                data2d[i++] = 0;
                data2d[i++] = 32;
            }

            //fill biome data
            for(; i < DATA2D_LENGTH;){
                data2d[i++] = 1;//biome: plains
                data2d[i++] = (byte) 42;//r
                data2d[i++] = (byte) 42;//g
                data2d[i++] = (byte) 42;//b
            }

            this.data2D = ByteBuffer.wrap(data2d);
        }


    }

    @Override
    public byte getBlockTypeId(int x, int y, int z) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return 0;
        }
        int realID = 1;
        int runtimeID = runtimeIDs[getOffset(x, y, z)];

        Tag palletEntry = pallet.get(runtimeID);

        String name = (String)((CompoundTag)palletEntry).getChildTagByKey("name").getValue();

        Block blk = _blockDB.getByDataName(name);
        if(blk != null)
            realID = blk.id;
        return (byte)realID;
    }

    @Override
    public short getBlockData(int x, int y, int z) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return 0;
        }
        int runtimeID = runtimeIDs[getOffset(x, y, z)];

        Tag palletEntry = pallet.get(runtimeID);

        Short shrt = (Short) ((CompoundTag)palletEntry).getChildTagByKey("val").getValue();

        return shrt.shortValue();
    }

    public String getBlockKeyValue(int x, int y, int z, String key) { return ""; }

    @Override
    public byte getSkyLightValue(int x, int y, int z) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return 0;
        }
        int offset = getOffset(x, y, z);
        byte dualData = terrainData.get(POS_SKY_LIGHT + (offset >>> 1));
        return (byte) ((offset & 1) == 1 ? (dualData >>> 4) & 0xf : dualData & 0xf);
    }

    @Override
    public byte getBlockLightValue(int x, int y, int z) {
        //block light is not stored anymore
        return 0;
    }

    @Override
    public boolean supportsBlockLightValues() {
        return false;
    }

    /**
     * Sets a block type, and also set the corresponding dirty table entry and set the saving flag.
     */
    @Override
    public void setBlockTypeId(int x, int y, int z, int type) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return;
        }
        terrainData.put(POS_BLOCK_IDS + getOffset(x, y, z), (byte) type);
    }

    @Override
    public void setBlockData(int x, int y, int z, int newData) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return;
        }
        int offset = getOffset(x, y, z);
        int pos = POS_META_DATA + (offset >> 1);
        byte oldData = terrainData.get(POS_META_DATA + (offset >> 1));
        if ((offset & 1) == 1) {
            terrainData.put(pos, (byte) ((newData << 4) | (oldData & 0xf)));
        } else {
            terrainData.put(pos, (byte) ((oldData & 0xf0) | (newData & 0xf)));
        }
    }

    private int getOffset(int x, int y, int z) {
        return (x * chunkW + z) * chunkH + y;
    }

    @Override
    public byte getBiome(int x, int z) {
        return data2D.get(POS_BIOME_DATA + get2Di(x, z));
    }

    private int getNoise(int base, int x, int z){
        // noise values are between -1 and 1
        // 0.0001 is added to the coordinates because integer values result in 0
        double oct1 = Noise.noise(
                ((double) (this.chunk.x * chunkW + x) / 100.0) + 0.0001,
                ((double) (this.chunk.z * chunkL + z) / 100.0) + 0.0001);
        double oct2 = Noise.noise(
                ((double) (this.chunk.x * chunkW + x) / 20.0) + 0.0001,
                ((double) (this.chunk.z * chunkL + z) / 20.0) + 0.0001);
        double oct3 = Noise.noise(
                ((double) (this.chunk.x * chunkW + x) / 3.0) + 0.0001,
                ((double) (this.chunk.z * chunkL + z) / 3.0) + 0.0001);
        return (int) (base + 60 + (40 * oct1) + (14 * oct2) + (6 * oct3));
    }

    /*
        MCPE 1.0 stopped embedding foliage color data in the chunk data,
         so now we fake the colors by combining biome colors with Perlin noise
     */

    @Override
    public byte getGrassR(int x, int z) {
        Biome biome = Biome.getBiome(getBiome(x, z) & 0xff);
        int red = 0;
        if(biome != null)
            red = biome.color.red;
        int res = getNoise(30 + (red / 5), x, z);
        return (byte) (res > 0xff ? 0xff : (res < 0 ? 0 : res));
    }

    @Override
    public byte getGrassG(int x, int z) {
        Biome biome = Biome.getBiome(getBiome(x, z) & 0xff);
        int green = 0;
        if(biome != null)
            green = biome.color.green;
        int res = getNoise(120 + (green / 5), x, z);
        return (byte) (res > 0xff ? 0xff : (res < 0 ? 0 : res));
    }

    @Override
    public byte getGrassB(int x, int z) {
        Biome biome = Biome.getBiome(getBiome(x, z) & 0xff);
        int blue = 0;
        if(biome != null)
            blue = biome.color.blue;
        int res = getNoise(30 + (blue / 5), x, z);
        return (byte) (res > 0xff ? 0xff : (res < 0 ? 0 : res));
    }

    private int get2Di(int x, int z) {
        return z * chunkL + x;
    }

    @Override
    public int getHeightMapValue(int x, int z) {
        short h = data2D.getShort(POS_HEIGHTMAP + (get2Di(x, z) << 1));
        return ((h & 0xff) << 8) | ((h >> 8) & 0xff);//little endian to big endian
    }
}
