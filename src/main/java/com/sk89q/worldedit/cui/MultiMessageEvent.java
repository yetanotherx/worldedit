// $Id$
/*
 * WorldEdit
 * Copyright (C) 2010, 2011 sk89q <http://www.sk89q.com>
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
package com.sk89q.worldedit.cui;

import java.math.BigInteger;
import java.security.SecureRandom;

public abstract class MultiMessageEvent implements CUIEvent {

    protected String data;
    protected String[] splitData;
    protected String hash;
    protected int pointer = -1;

    public MultiMessageEvent(String data) {
        this.data = data;
        
        int dataSize = data.length();
        int id = 0;
        splitData = new String[(int)Math.ceil(dataSize/200) + 1];
        
        for (int i = 0; i < dataSize; i += 200) {
            splitData[id] = data.substring(i, Math.min(dataSize, i + 200));
            id++;
        }
        
        this.hash = new BigInteger(130, new SecureRandom()).toString(32).substring(0, 8);
    }

    public abstract String getTypeId();

    public String[] getParameters() {
        if( pointer > -1 ) {
            return new String[]{ hash, Integer.toString(pointer), splitData[pointer] };
        }
        else {
            return new String[] { hash, Integer.toString(splitData.length) };
        }
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public String[] getSplitData() {
        return splitData;
    }

}
