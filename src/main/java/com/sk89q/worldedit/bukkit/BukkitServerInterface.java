// $Id$
/*
 * WorldEdit
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.sk89q.worldedit.bukkit;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.events.WEBlockBreakEvent;
import com.sk89q.worldedit.bukkit.events.WEBlockCreateEvent;
import com.sk89q.worldedit.events.WorldEditBlockBreakEvent;
import com.sk89q.worldedit.events.WorldEditBlockCreateEvent;
import com.sk89q.worldedit.events.WorldEditBlockEvent;
import com.sk89q.worldedit.events.WorldEditEvent;

public class BukkitServerInterface extends ServerInterface {
    public Server server;
    public WorldEditPlugin plugin;
    
    public BukkitServerInterface(WorldEditPlugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public int resolveItem(String name) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isValidMobType(String type) {
        return CreatureType.fromName(type) != null;
    }

    @Override
    public void reload() {
        plugin.loadConfiguration();
    }

    @Override
    public boolean callEvent(LocalPlayer lPlayer, WorldEditEvent event) {
        switch (event.getType()) {
        case BLOCK_DESTROY:
            WorldEditBlockBreakEvent destroyEvent = (WorldEditBlockBreakEvent) event;
            Vector dest = destroyEvent.getVector();
            Player destroyer = server.getPlayer(lPlayer.getName());
            Block destroyedBlock = destroyer.getWorld().getBlockAt(dest.getBlockX(), dest.getBlockY(), dest.getBlockZ());
            WEBlockBreakEvent bBEvent = new WEBlockBreakEvent(destroyedBlock, destroyer, destroyEvent);
            server.getPluginManager().callEvent(bBEvent);
            return bBEvent.isCancelled();
        case BLOCK_CREATE:
            WorldEditBlockCreateEvent createEvent = (WorldEditBlockCreateEvent) event;
            BaseBlock replacing = createEvent.getNewBlock();
            Vector placed = createEvent.getVector();
            Vector placedAdj = createEvent.getAdjacent();
            Player creator = server.getPlayer(lPlayer.getName());
            Block placedBlock = creator.getWorld().getBlockAt(placed.getBlockX(), placed.getBlockY(), placed.getBlockZ());
            Block placedOn = creator.getWorld().getBlockAt(placedAdj.getBlockX(), placedAdj.getBlockY(), placedAdj.getBlockZ());
            BlockState replacedBlock = placedBlock.getState();
            replacedBlock.setData(Material.getMaterial(replacing.getType()).getNewData((byte) replacing.getData()));
            WEBlockCreateEvent bCEvent = new WEBlockCreateEvent(placedBlock, replacedBlock, placedOn, creator, createEvent);
            return bCEvent.isCancelled();
        default:
            throw new UnsupportedOperationException();
        }
    }

}
