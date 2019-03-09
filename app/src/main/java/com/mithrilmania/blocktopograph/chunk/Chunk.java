package com.mithrilmania.blocktopograph.chunk;

import com.mithrilmania.blocktopograph.Log;
import com.mithrilmania.blocktopograph.WorldData;
import com.mithrilmania.blocktopograph.map.Dimension;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;

public abstract class Chunk {

    protected final WeakReference<WorldData> mWorldData;
    protected final Version mVersion;
    public final int mChunkX;
    public final int mChunkZ;
    public final Dimension mDimension;
    protected NBTChunkData mEntity;
    protected NBTChunkData mTileEntity;
    boolean mIsVoid;
    boolean mIsError;

    Chunk(WorldData worldData, Version version, int chunkX, int chunkZ, Dimension dimension) {
        mWorldData = new WeakReference<>(worldData);
        mVersion = version;
        mChunkX = chunkX;
        mChunkZ = chunkZ;
        mDimension = dimension;
        mIsVoid = false;
        mIsError = false;
        try {
            mEntity = version.createEntityChunkData(this);
            mTileEntity = version.createBlockEntityChunkData(this);
        } catch (Version.VersionException e) {
            Log.d(this, e);
        }
    }

    public static Chunk create(@NotNull WorldData worldData, int chunkX, int chunkZ, Dimension dimension) {
        Version version;
        try {
            byte[] data = worldData.getChunkData(chunkX, chunkZ, ChunkTag.VERSION, dimension, (byte) 0, false);
            version = Version.getVersion(data);
        } catch (WorldData.WorldDBLoadException | WorldData.WorldDBException e) {
            Log.d(Chunk.class, e);
            version = Version.ERROR;
        }
        Chunk chunk;
        switch (version) {
            case ERROR:
            case OLD_LIMITED:
                chunk = new VoidChunk(worldData, version, chunkX, chunkZ, dimension);
                chunk.mIsError = true;
                break;
            case v0_9:
                chunk = new PocketChunk(worldData, version, chunkX, chunkZ, dimension);
                break;
            case V1_0:
            case V1_1:
            case V1_2_PLUS:
                chunk = new BedrockChunk(worldData, version, chunkX, chunkZ, dimension);
                break;
            case NULL:
            default:
                chunk = new VoidChunk(worldData, version, chunkX, chunkZ, dimension);
        }
        return chunk;
    }

    public final WorldData getWorldData() {
        return mWorldData.get();
    }

    @Contract(pure = true)
    public final boolean isVoid() {
        return mIsVoid;
    }

    @Contract(pure = true)
    public final boolean isError() {
        return mIsError;
    }

    abstract public boolean supportsBlockLightValues();

    abstract public boolean supportsHeightMap();

    abstract public int getHeightLimit();

    abstract public int getHeightMapValue(int x, int z);

    abstract public int getBiome(int x, int z);

    abstract public int getGrassColor(int x, int z);

    abstract public int getBlockRuntimeId(int x, int y, int z);

    abstract public int getBlockRuntimeId(int x, int y, int z, int layer);

    abstract public void setBlockRuntimeId(int x, int y, int z, int layer, int runtimeId);

    abstract public int getBlockLightValue(int x, int y, int z);

    abstract public int getSkyLightValue(int x, int y, int z);

    abstract public int getHighestBlockYUnderAt(int x, int z, int y);

    abstract public int getCaveYUnderAt(int x, int z, int y);

    abstract public void save() throws WorldData.WorldDBException, IOException;

    @Contract(pure = true)
    public final NBTChunkData getEntity() {
        return mEntity;
    }

    @Contract(pure = true)
    public final NBTChunkData getBlockEntity() {
        return mTileEntity;
    }
}
