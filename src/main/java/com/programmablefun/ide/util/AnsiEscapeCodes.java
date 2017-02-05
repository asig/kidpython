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

public class AnsiEscapeCodes {
    public final static String RESET = "\u001b[0m";
    public final static String BOLD = "\u001b[1m";
    public final static String ITALIC = "\u001b[2m";

    public final static String IMAGE_NEGATIVE = "\u001b[7m";
    public final static String IMAGE_POSITIVE = "\u001b[27m";

    public final static String FG_BLACK   = "\u001b[30m";
    public final static String FG_RED     = "\u001b[31m";
    public final static String FG_GREEN   = "\u001b[32m";
    public final static String FG_YELLOW  = "\u001b[33m";
    public final static String FG_BLUE    = "\u001b[34m";
    public final static String FG_MAGENTA = "\u001b[35m";
    public final static String FG_CYAN    = "\u001b[36m";
    public final static String FG_WHITE   = "\u001b[37m";

    public final static String BG_BLACK   = "\u001b[40m";
    public final static String BG_RED     = "\u001b[41m";
    public final static String BG_GREEN   = "\u001b[42m";
    public final static String BG_YELLOW  = "\u001b[43m";
    public final static String BG_BLUE    = "\u001b[44m";
    public final static String BG_MAGENTA = "\u001b[45m";
    public final static String BG_CYAN    = "\u001b[46m";
    public final static String BG_WHITE   = "\u001b[47m";
}
