package com.mithrilmania.blocktopograph.chunk;


import android.util.SparseArray;

import com.mithrilmania.blocktopograph.chunk.terrain.TerrainChunkData;
import com.mithrilmania.blocktopograph.chunk.terrain.V0_9_TerrainChunkData;
import com.mithrilmania.blocktopograph.chunk.terrain.V1_0_TerrainChunkData;
import com.mithrilmania.blocktopograph.chunk.terrain.V1_1_TerrainChunkData;
import com.mithrilmania.blocktopograph.chunk.terrain.V1_2_TerrainChunkData;
import com.mithrilmania.blocktopograph.chunk.terrain.V1_2_beta_TerrainChunkData;

public enum Version {

    ERROR("ERROR", "failed to retrieve version number", -2, 16, 16),
    NULL("NULL", "no data", -1, 16, 16),
    V0("V0", "Old format", 0, 16, 16),
    Beta_Palletted("v1.2.0", "Beta Palletted chunks", 1, 16, 16),
    v0_9("v0.9.0", "infinite xz, zlib leveldb; introduced in v0.9.0", 2, 128, 1),
    V1_0("v1.0.0", "Stacked sub-chunks, 256 world-height, 16 high sub-chunks; introduced in alpha v1.0.0 (v0.17)", 3, 16, 16),
    V1_1("v1.1.0", "Block-light is not stored anymore", 4, 16, 16),
    V7("v7", "Old format", 7, 16, 16),
    V8("v1.1.0", "Palletted chunks; ID from chunk data", 8, 16, 16),
    V10("v1.2.0", "Palletted chunks; ID from NBT tag", 10, 16, 16);

    public static final int LATEST_SUPPORTED_VERSION = V10.id;

    public final String displayName, description;
    public final int id, subChunkHeight, subChunks;

    Version(String displayName, String description, int id, int subChunkHeight, int subChunks){
        this.displayName = displayName;
        this.description = description;
        this.id = id;
        this.subChunkHeight = subChunkHeight;
        this.subChunks = subChunks;
    }

    private static final SparseArray<Version> versionMap;
    static {
        versionMap = new SparseArray<>();
        for(Version b : Version.values()){
            versionMap.put(b.id, b);
        }
    }
    public static Version getVersion(byte data){
        int versionNumber = data & 0xff;

        //fallback version
        if(versionNumber > LATEST_SUPPORTED_VERSION) {
            versionNumber = LATEST_SUPPORTED_VERSION;
        }

        Version version = versionMap.get(versionNumber);
        //check if the returned version exists, fallback on ERROR otherwise.
        return version == null ? ERROR : version;

    }

    public TerrainChunkData createTerrainChunkData(Chunk chunk, byte subChunk) throws VersionException {
        switch (this){
            case ERROR:
            case NULL:
                return null;
            case Beta_Palletted:
                return new V1_2_beta_TerrainChunkData(chunk, subChunk);
                //throw new VersionException("Handling terrain chunk data is NOT supported for this version!", this);
            case v0_9:
                return new V0_9_TerrainChunkData(chunk, subChunk);
            case V1_0:
                return new V1_0_TerrainChunkData(chunk, subChunk);
            case V8:
                return new V1_2_TerrainChunkData(chunk, subChunk);
            case V10:
                return new V1_2_TerrainChunkData(chunk, subChunk);
            default:
                //use the old version
                return new V1_1_TerrainChunkData(chunk, subChunk);
        }
    }

    public NBTChunkData createEntityChunkData(Chunk chunk) throws VersionException {
        switch (this){
            case ERROR:
            case NULL:
                return null;
            default:
                //use the latest version, like nothing will ever happen...
                return new NBTChunkData(chunk, ChunkTag.ENTITY);
        }
    }

    public NBTChunkData createBlockEntityChunkData(Chunk chunk) throws VersionException {
        switch (this){
            case ERROR:
            case NULL:
                return null;
            case Beta_Palletted:
                throw new VersionException("Handling terrain chunk data is NOT supported for this version!", this);
            default:
                //use the latest version, like nothing will ever happen...
                return new NBTChunkData(chunk, ChunkTag.BLOCK_ENTITY);
        }
    }

    @Override
    public String toString(){
        return "[MCPE version \""+displayName+"\" (version-code: "+id+")]";
    }

    public static class VersionException extends Exception {

        VersionException(String msg, Version version){
            super(msg + " " + version.toString());
        }
    }
}
