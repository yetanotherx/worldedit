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
 *
 * @author sk89q
 */
public class ThreadedOperationHost implements Runnable {
    /**
     * Stores the current operation being performed.
     */
    private Operation current;

    /**
     * Queue an operation. May throw an exception if the queue is busy.
     * 
     * @param op
     * @throws OperationQueueFullException
     */
    public synchronized void queue(Operation op)
            throws OperationQueueFullException {
        if (current != null) {
            throw new OperationQueueFullException();
        }
        current = op;
    }

    /**
     * Runs the queue.
     */
    public void run() {
        while (true) {
            Operation op = current;

            if (current != null) {
                try {
                    op.run();
                } catch (Throwable t) {
                    Operation.printException(op.getPlayer(), t);
                }
            }

            current = null;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
