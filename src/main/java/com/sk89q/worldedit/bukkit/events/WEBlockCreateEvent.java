package com.sk89q.worldedit.bukkit.events;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sk89q.worldedit.events.WorldEditBlockEvent;

public class WEBlockCreateEvent extends BlockPlaceEvent implements BukkitBlockEvent {

    private WorldEditBlockEvent weEvent;

    public WEBlockCreateEvent(Block block, BlockState newBlock, Block adj, Player player, WorldEditBlockEvent orig) {
        super(block, newBlock, adj, player.getItemInHand(), player, true);
        this.weEvent = orig;
    }

    public WorldEditBlockEvent getWorldEditEvent() {
        return weEvent;
    }

}
