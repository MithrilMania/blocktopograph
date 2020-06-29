package com.mithrilmania.blocktopograph.map;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.mithrilmania.blocktopograph.util.NamedBitmapProvider;
import com.mithrilmania.blocktopograph.util.NamedBitmapProviderHandle;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class TileEntity implements NamedBitmapProviderHandle, NamedBitmapProvider {

    private static BlockDatabase _blockDB = BlockDatabase.getDatabase();

    // will have to allow these to be treated like enum
    public static TileEntity CHEST() {
        return new TileEntity(0, "Chest", "Chest", _blockDB.B_54_0_CHEST());
    }

    public static TileEntity TRAPPED_CHEST()
    {
        return new TileEntity(1, "Trapped Chest", "TrappedChest", _blockDB.B_146_0_TRAPPED_CHEST());
    }

    public static TileEntity ENDER_CHEST() {
        return new TileEntity(2, "Ender Chest", "EnderChest", _blockDB.B_130_0_ENDER_CHEST());
    }

    public static TileEntity MOB_SPAWNER() {
        return new TileEntity(3, "Mob Spawner", "MobSpawner", _blockDB.B_52_0_MOB_SPAWNER());
    }

    public static TileEntity END_PORTAL() {
        return new TileEntity(4, "End Portal", "EndPortal", _blockDB.B_119_0_END_PORTAL());
    }

    public static TileEntity BEACON() {
        return new TileEntity(5, "Beacon", "Beacon", _blockDB.B_138_0_BEACON());
    }

    private static ArrayList<TileEntity> _values;
    public static ArrayList<TileEntity> values()
    {
        if(_values == null)
        {
            _values = new ArrayList<TileEntity>();
            _values.add(CHEST());
            _values.add(TRAPPED_CHEST());
            _values.add(ENDER_CHEST());
            _values.add(MOB_SPAWNER());
            _values.add(END_PORTAL());
            _values.add(BEACON());
        }
        return _values;
    }
    public final int id;
    public final String displayName, dataName;

    public final Block block;

    TileEntity(int id, String displayName, String dataName, Block block){
         this.id = id;
        this.displayName = displayName;
        this.dataName = dataName;
        this.block = block;
    }

    @Override
    public Bitmap getBitmap(){
        return block.bitmap;
    }

    @NonNull
    @Override
    public NamedBitmapProvider getNamedBitmapProvider(){
        return this;
    }

    @NonNull
    @Override
    public String getBitmapDisplayName(){
        return this.displayName;
    }

    @NonNull
    @Override
    public String getBitmapDataName() {
        return this.dataName;
    }

    private static final Map<String, TileEntity> tileEntityMap;
    private static final Map<Integer, TileEntity> tileEntityByID;

    static {
        tileEntityMap = new HashMap<>();
        tileEntityByID = new HashMap<>();

        tileEntityMap.put(CHEST().dataName, CHEST());
        tileEntityMap.put(TRAPPED_CHEST().dataName, TRAPPED_CHEST());
        tileEntityMap.put(ENDER_CHEST().dataName, ENDER_CHEST());
        tileEntityMap.put(MOB_SPAWNER().dataName, MOB_SPAWNER());
        tileEntityMap.put(END_PORTAL().dataName, END_PORTAL());
        tileEntityMap.put(BEACON().dataName, BEACON());

        tileEntityByID.put(CHEST().id, CHEST());
        tileEntityByID.put(TRAPPED_CHEST().id, TRAPPED_CHEST());
        tileEntityByID.put(ENDER_CHEST().id, ENDER_CHEST());
        tileEntityByID.put(MOB_SPAWNER().id, MOB_SPAWNER());
        tileEntityByID.put(END_PORTAL().id, END_PORTAL());
        tileEntityByID.put(BEACON().id, BEACON());
    }

    public static TileEntity getTileEntity(int id){
        return tileEntityByID.get(id);
    }

    public static TileEntity getTileEntity(String dataName){
        return tileEntityMap.get(dataName);
    }
}
