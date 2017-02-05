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

package com.programmablefun.ide.util;

/**
 * Created by asigner on 15/10/16.
 */
public class SWTUtils {

    public static final double FONT_SCALE;

    static {
        String os = System.getProperty("os.name").split(" ")[0];
        if ("Mac".equals(os)) {
            FONT_SCALE = 1.6;
        } else {
            FONT_SCALE = 1.0;
        }
    }

    public static int scaleFont(int size) {
        return (int)(size * FONT_SCALE);
    }
}
