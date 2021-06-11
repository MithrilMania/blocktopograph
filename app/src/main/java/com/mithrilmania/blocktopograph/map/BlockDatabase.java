package com.mithrilmania.blocktopograph.map;

import com.mithrilmania.blocktopograph.block.Block;
import com.mithrilmania.blocktopograph.block.BlockType;
import com.mithrilmania.blocktopograph.block.KnownBlockRepr;

import java.util.ArrayList;
import android.util.SparseArray;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;

/**
 * Created by korgl on 5/8/2020.
 */

public class BlockDatabase {
    private ArrayList<KnownBlockRepr> _blocks;
    private static BlockDatabase _singleton;

    private BlockDatabase()
    {
        loadDatabase();
    }

    public static BlockDatabase getDatabase()
    {
        if(_singleton == null)
            _singleton = new BlockDatabase();

        return _singleton;
    }

    // need to change these to queries of the list so that == comparisons work
    public KnownBlockRepr B_56_0_DIAMOND_ORE()
    {
        return KnownBlockRepr.B_56_0_DIAMOND_ORE;
    }

    public KnownBlockRepr B_129_0_EMERALD_ORE() {
        return KnownBlockRepr.B_129_0_EMERALD_ORE;
    }

    public KnownBlockRepr B_153_0_QUARTZ_ORE() {
        return KnownBlockRepr.B_153_0_QUARTZ_ORE;
    }

    public KnownBlockRepr B_14_0_GOLD_ORE() {
        return KnownBlockRepr.B_14_0_GOLD_ORE;
    }

    public KnownBlockRepr B_15_0_IRON_ORE() {
        return KnownBlockRepr.B_15_0_IRON_ORE;
    }

    public KnownBlockRepr B_73_0_REDSTONE_ORE() {
        return KnownBlockRepr.B_73_0_REDSTONE_ORE;
    }

    public KnownBlockRepr B_21_0_LAPIS_ORE() {
        return KnownBlockRepr.B_21_0_LAPIS_ORE;
    }

    public KnownBlockRepr B_54_0_CHEST() {
        return KnownBlockRepr.B_54_0_CHEST;
    }

    public KnownBlockRepr B_146_0_TRAPPED_CHEST() {
        return KnownBlockRepr.B_146_0_TRAPPED_CHEST;
    }

    public KnownBlockRepr B_130_0_ENDER_CHEST() {
        return KnownBlockRepr.B_130_0_ENDER_CHEST;
    }

    public KnownBlockRepr B_52_0_MOB_SPAWNER() {
        return KnownBlockRepr.B_52_0_MOB_SPAWNER;
    }

    public KnownBlockRepr B_119_0_END_PORTAL() {
        return KnownBlockRepr.B_119_0_END_PORTAL;
    }

    public KnownBlockRepr B_138_0_BEACON() {
        return KnownBlockRepr.B_138_0_BEACON;
    }
    private void loadDatabase ()
    {
        _blocks = new ArrayList<KnownBlockRepr>();

        for(KnownBlockRepr currBlock : KnownBlockRepr.values())
        {
            _blocks.add(currBlock);
        }
    }

    private final Map<String, KnownBlockRepr> byDataName = new HashMap<>();
    private final SparseArray<SparseArray<KnownBlockRepr>> blockMap;
    {
        if(_blocks == null)
            loadDatabase();

        blockMap = new SparseArray<>();
        SparseArray<KnownBlockRepr> subMap;
        for(KnownBlockRepr b : _blocks){
            subMap = blockMap.get(b.id);
            if(subMap == null){
                subMap = new SparseArray<>();
                blockMap.put(b.id, subMap);
            }
            subMap.put(b.subId, b);
            if(b.subId == 0)
                byDataName.put(b.identifier, b);
            //byDataName.put(b.name + "@" + b.subName, b);
        }
    }

    public KnownBlockRepr getByDataName(String dataName){
        return byDataName.get(dataName);
    }

    public void loadBitmaps(AssetManager assetManager) throws IOException {
        for(KnownBlockRepr b : _blocks){
            if(b.bitmap == null && b.texPath != null){
                try {
                    b.bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(assetManager.open(b.texPath)), 32, 32, false);
                } catch(FileNotFoundException e){
                    //TODO file-paths were generated from block names; some do not actually exist...
                    //Log.w("File not found! "+b.texPath);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public KnownBlockRepr getBlock(int id, int meta){
        if(id < 0) return null;
        SparseArray<KnownBlockRepr> subMap = blockMap.get(id);
        if(subMap == null) return null;
        else {
            if(meta == -1)
                meta = 0;
            return subMap.get(meta);
        }
    }

}
