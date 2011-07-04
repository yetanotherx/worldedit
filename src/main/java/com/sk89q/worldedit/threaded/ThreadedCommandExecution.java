// $Id$
/*
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 * All rights reserved.
*/
package com.sk89q.worldedit.threaded;

import com.sk89q.minecraft.util.commands.*;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.ItemType;
import com.sk89q.worldedit.commands.InsufficientArgumentsException;

public class ThreadedCommandExecution implements Runnable {
    private CommandsManager<LocalPlayer> commandsManager;
    private String[] args;
    private Object[] methodArgs;
    private ThreadedOperationState threadState;
    private EditSession editSession;
    private LocalPlayer player;

    public ThreadedCommandExecution(CommandsManager<LocalPlayer> commandsManager,
                                    String[] args,
                                    LocalPlayer player,
                                    EditSession editSession,
                                    Object ... methodArgs) {
        this.commandsManager = commandsManager;
        this.args = args;
        this.player = player;
        this.editSession = editSession;
        this.methodArgs = methodArgs;
    }

    public void setThreadState(ThreadedOperationState threadState) {
        this.threadState = threadState;
    }

    public void run() {
        threadState.yield();

        try {
            try {
                commandsManager.execute(args, player, methodArgs);
            } catch (CommandPermissionsException e) {
                player.printError("You don't have permission to do this.");
            } catch (MissingNestedCommandException e) {
                player.printError(e.getUsage());
            } catch (CommandUsageException e) {
                player.printError(e.getMessage());
                player.printError(e.getUsage());
            } catch (WrappedCommandException e) {
                throw e.getCause();
            } catch (UnhandledCommandException e) {
            }
        } catch (NumberFormatException e) {
            player.printError("Number expected; string given.");
        } catch (IncompleteRegionException e) {
            player.printError("Make a region selection first.");
        } catch (UnknownItemException e) {
            player.printError("Block name '" + e.getID() + "' was not recognized.");
        } catch (InvalidItemException e) {
            player.printError(e.getMessage());
        } catch (DisallowedItemException e) {
            player.printError("Block '" + e.getID() + "' not allowed (see WorldEdit configuration).");
        } catch (MaxChangedBlocksException e) {
            player.printError("Max blocks changed in an operation reached ("
                    + e.getBlockLimit() + ").");
        } catch (MaxRadiusException e) {
            //player.printError("Maximum radius: " + config.maxRadius);
        } catch (UnknownDirectionException e) {
            player.printError("Unknown direction: " + e.getDirection());
        } catch (InsufficientArgumentsException e) {
            player.printError(e.getMessage());
        } catch (EmptyClipboardException e) {
            player.printError("Your clipboard is empty. Use //copy first.");
        } catch (InvalidFilenameException e) {
            player.printError("Filename '" + e.getFilename() + "' invalid: "
                    + e.getMessage());
        } catch (FilenameResolutionException e) {
            player.printError("File '" + e.getFilename() + "' resolution error: "
                    + e.getMessage());
        } catch (InvalidToolBindException e) {
            player.printError("Can't bind tool to "
                    + ItemType.toHeldName(e.getItemId()) + ": " + e.getMessage());
        } catch (FileSelectionAbortedException e) {
            player.printError("File selection aborted.");
        } catch (WorldEditException e) {
            player.printError(e.getMessage());
        } catch (Throwable excp) {
            player.printError("Please report this error: [See console]");
            player.printRaw(excp.getClass().getName() + ": " + excp.getMessage());
            excp.printStackTrace();
        } finally {
            player.print("-- OPERATION " + Thread.currentThread().toString() + " FLUSHING --");
            editSession.flushQueue();
            player.print("-- OPERATION " + Thread.currentThread().toString() + " FINISHED --");
        }
    }
}
