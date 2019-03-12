package com.mithrilmania.blocktopograph.chunk;

import com.mithrilmania.blocktopograph.WorldData;
import com.mithrilmania.blocktopograph.chunk.terrain.TerrainChunkData;
import com.mithrilmania.blocktopograph.map.Dimension;

import java.util.concurrent.atomic.AtomicReferenceArray;


public class Chunk {

    public final WorldData worldData;

    public final int x, z;
    public final Dimension dimension;

    private Version version;

    private AtomicReferenceArray<TerrainChunkData>
            terrain = new AtomicReferenceArray<>(256);

    private AtomicReferenceArray<Version>
            versions = new AtomicReferenceArray<>(256);

    private volatile NBTChunkData entity, blockEntity;

    public Chunk(WorldData worldData, int x, int z, Dimension dimension) {
        this.worldData = worldData;
        this.x = x;
        this.z = z;
        this.dimension = dimension;
    }

    public TerrainChunkData getTerrain(byte subChunk) throws Version.VersionException {
        TerrainChunkData data = terrain.get(subChunk & 0xff);
        if(data == null){
            data = this.getVersion(subChunk).createTerrainChunkData(this, subChunk);
            terrain.set(subChunk & 0xff, data);
        }
        return data;
    }

    public NBTChunkData getEntity() throws Version.VersionException {
        if(entity == null) entity = this.getVersion().createEntityChunkData(this);
        return entity;
    }


    public NBTChunkData getBlockEntity() throws Version.VersionException {
        if(blockEntity == null) blockEntity = this.getVersion().createBlockEntityChunkData(this);
        return blockEntity;
    }

    public Version getVersion(){
        return this.getVersion((byte)0);
    }

    public Version getVersion(byte subChunk){
        Version vers = null;
        byte[] rawData = null;
        byte versionNo;
        try {
            vers = versions.get(subChunk);
            if(vers == null) {
                byte[] data = this.worldData.getChunkData(x, z, ChunkTag.VERSION, dimension, subChunk, false);

                if(data == null || data.length <= 0)
                    vers = Version.NULL;
                else {
                    versionNo = data[0];
                    rawData = worldData.getChunkData(x, z, ChunkTag.TERRAIN, dimension, subChunk, true);

                    // for some reason, the above read doesn't always work; in particular, for some newer newer chunks
                    // in that case, continue relying on the NBT data
                    if (rawData != null) {
                        versionNo = rawData[0];
                    }

                    vers = Version.getVersion(versionNo);
                }
                versions.set(subChunk, vers);
            }
        } catch (WorldData.WorldDBLoadException | WorldData.WorldDBException e) {
            e.printStackTrace();
            vers = Version.ERROR;
        }

        return vers;
    }

    //TODO: should we use the heightmap???
    public int getHighestBlockYAt(int x, int z) throws Version.VersionException {
        Version cVersion = getVersion();
        TerrainChunkData data;
        for(int subChunk = cVersion.subChunks - 1; subChunk >= 0; subChunk--) {
            data = this.getTerrain((byte) subChunk);
            if (data == null || !data.loadTerrain()) continue;

            for (int y = cVersion.subChunkHeight; y >= 0; y--) {
                if (data.getBlockTypeId(x & 15, y, z & 15) != 0)
                    return (subChunk * cVersion.subChunkHeight) + y;
            }
        }
        return -1;
    }

    public int getHighestBlockYUnderAt(int x, int z, int y) throws Version.VersionException {
        Version cVersion = getVersion();
        int offset = y % cVersion.subChunkHeight;
        int subChunk = y / cVersion.subChunkHeight;
        TerrainChunkData data;

        for(; subChunk >= 0; subChunk--) {
            data = this.getTerrain((byte) subChunk);
            if (data == null || !data.loadTerrain()) continue;

            for (y = offset; y >= 0; y--) {
                if (data.getBlockTypeId(x & 15, y, z & 15) != 0)
                    return (subChunk * cVersion.subChunkHeight) + y;
            }

            //start at the top of the next chunk! (current offset might differ)
            offset = cVersion.subChunkHeight - 1;
        }
        return -1;
    }

    public int getCaveYUnderAt(int x, int z, int y) throws Version.VersionException {
        Version cVersion = getVersion();
        int offset = y % cVersion.subChunkHeight;
        int subChunk = y / cVersion.subChunkHeight;
        TerrainChunkData data;

        for(; subChunk >= 0; subChunk--) {
            data = this.getTerrain((byte) subChunk);
            if (data == null || !data.loadTerrain()) continue;
            for (y = offset; y >= 0; y--) {
                if (data.getBlockTypeId(x & 15, y, z & 15) == 0)
                    return (subChunk * cVersion.subChunkHeight) + y;
            }

            //start at the top of the next chunk! (current offset might differ)
            offset = cVersion.subChunkHeight - 1;
        }
        return -1;
    }


}
