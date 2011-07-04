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

package com.sk89q.worldedit.threaded;

public class ThreadedOperationState {

    private Thread operationThread;
    private boolean isWaiting;

    public ThreadedOperationState(Thread operationThread) {
        this.operationThread = operationThread;
    }

    public void yield() {
        synchronized (this) {
            isWaiting = true;

            while (isWaiting) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void yieldTo() {
        synchronized (this) {
            isWaiting = false;
            
            this.notify();
        }

        while (!isWaiting && operationThread.isAlive()) {
            try {
                operationThread.join(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDone() {
        return !isWaiting && !operationThread.isAlive();
    }

}
