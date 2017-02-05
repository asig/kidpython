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

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ByteBuffer {

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private final byte[] buf;
    int read, write, used;

    public ByteBuffer(int size) {
        this.buf = new byte[size];
        this.read = 0;
        this.write = 0;
        this.used = 0;
    }

    public int getSize() {
        return buf.length;
    }

    public int getAvailable() {
        lock.lock();
        try {
            return used;
        } finally {
            lock.unlock();
        }
    }

    public void write(byte b) throws InterruptedException {
        lock.lock();
        try {
            if (used == buf.length) {
                notFull.await();
            }
            buf[write] = b;
            write = (write + 1) % buf.length;
            used++;
            if (used == 1) {
                notEmpty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public byte read() throws InterruptedException {
        byte[] buf = new byte[1];
        read(buf, 0, 1);
        return buf[0];
    }

    public int read(byte b[], int ofs, int len) throws InterruptedException {
        lock.lock();
        try {
            if (used == 0) {
                notEmpty.await();
            }
            int toCopy = Math.min(len, used);
            for (int i = 0; i < toCopy; i++) {
                b[ofs + i] = buf[(read + i) % buf.length];
            }
            used -= toCopy;
            read = (read + toCopy) % buf.length;
            notFull.signal();
            return toCopy;
        } finally {
            lock.unlock();
        }
    }
}
