package com.sk89q.worldedit.events;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;

public class WorldEditBlockCreateEvent extends WorldEditBlockEvent {

    protected BaseBlock newBlock;
    protected Vector adj;

    public BaseBlock getNewBlock() {
        return newBlock;
    }

    public void setNewBlock(BaseBlock newBlock) {
        this.newBlock = newBlock;
    }

    public Vector getAdjacent() {
        return adj;
    }

    public WorldEditBlockCreateEvent(Vector vector, Vector placedOn, BaseBlock newBlock, WorldEdit we, LocalSession lS) {
        super(Type.BLOCK_CREATE, vector, we, lS);
        this.newBlock = newBlock;
        this.adj = placedOn;
    }

}
