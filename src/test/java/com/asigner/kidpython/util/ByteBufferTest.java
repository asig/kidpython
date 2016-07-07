package com.asigner.kidpython.util;

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
