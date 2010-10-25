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

import com.sk89q.worldedit.*;

/**
 * Constitutes an operation that can be threaded.
 *
 * @author sk89q
 */
public abstract class Operation {
    /**
     * Stores the player.
     */
    public final WorldEditPlayer player;

    /**
     * Construct the object.
     * @param player
     */
    public Operation(WorldEditPlayer player) {
        this.player = player;
    }

    /**
     * Get player.
     * 
     * @return
     */
    public WorldEditPlayer getPlayer() {
        return player;
    }
    
    /**
     * Handles exceptions.
     */
    public static void printException(WorldEditPlayer player, Throwable t) {
        if (t instanceof NumberFormatException) {
            NumberFormatException e = (NumberFormatException)t;
            player.printError("Number expected; string given.");

        } else if (t instanceof IncompleteRegionException) {
            IncompleteRegionException e = (IncompleteRegionException)t;
            player.printError("The edit region has not been fully defined.");

        } else if (t instanceof UnknownItemException) {
            UnknownItemException e = (UnknownItemException)t;
            player.printError("Item name is not recognized.");

        } else if (t instanceof DisallowedItemException) {
            DisallowedItemException e = (DisallowedItemException)t;
            player.printError("Item is not allowed (see WorldEdit configuration).");

        } else if (t instanceof MaxChangedBlocksException) {
            MaxChangedBlocksException e = (MaxChangedBlocksException)t;
            player.printError("The maximum number of blocks changed ("
                    + e.getBlockLimit() + ") in an instance was reached.");

        } else if (t instanceof UnknownDirectionException) {
            UnknownDirectionException e = (UnknownDirectionException)t;
            player.printError("Unknown direction: " + e.getDirection());

        } else if (t instanceof InsufficientArgumentsException) {
            InsufficientArgumentsException e = (InsufficientArgumentsException)t;
            player.printError(e.getMessage());

        } else if (t instanceof EmptyClipboardException) {
            EmptyClipboardException e = (EmptyClipboardException)t;
            player.printError("Your clipboard is empty.");

        } else if (t instanceof OperationQueueFullException) {
            OperationQueueFullException e = (OperationQueueFullException)t;
            player.printError("Someone is already busy performing a task.");

        } else if (t instanceof WorldEditException) {
            WorldEditException e = (WorldEditException)t;
            player.printError(e.getMessage());

        } else {
            player.printError("Please report this error: [See console]");
            player.print(t.getClass().getName() + ": " + t.getMessage());
            t.printStackTrace();
        }
    }

    /**
     * Runs the operation.
     * 
     * @throws Throwable
     */
    public abstract void run() throws Throwable;
}
