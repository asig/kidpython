/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteBufferTest {

    @Test
    public void testReadPartial_NonBlocking() throws Exception {

        ByteBuffer buf = new ByteBuffer(20);

        buf.write((byte) 1);
        buf.write((byte) 2);
        buf.write((byte) 3);
        buf.write((byte) 4);
        assertEquals(4, buf.getAvailable());

        byte res[] = new byte[10];
        buf.read(res,0,2);
        assertEquals((byte)1, res[0]);
        assertEquals((byte)2, res[1]);

        assertEquals(2, buf.getAvailable());

        int read = buf.read(res,0,10);
        assertEquals(2, read);
        assertEquals((byte)3, res[0]);
        assertEquals((byte)4, res[1]);

        assertEquals(0, buf.getAvailable());
    }
}
