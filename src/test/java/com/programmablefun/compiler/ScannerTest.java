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

package com.programmablefun.compiler;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScannerTest {

    @Test
    public void testPositions() throws Exception {
        Scanner scanner = new Scanner("FOO\nBAR\n");
        char c;

        c = scanner.getch(); assertEquals('F', c); checkPos(scanner, 1, 1);
        c = scanner.getch(); assertEquals('O', c); checkPos(scanner, 1, 2);
        c = scanner.getch(); assertEquals('O', c); checkPos(scanner, 1, 3);
        c = scanner.getch(); assertEquals('\n', c); checkPos(scanner, 1, 4);
        scanner.ungetch(); checkPos(scanner, 1, 3);
        scanner.ungetch(); checkPos(scanner, 1, 2);
        scanner.ungetch(); checkPos(scanner, 1, 1);
        scanner.ungetch(); checkPos(scanner, 1, 0);

        c = scanner.getch(); assertEquals('F', c); checkPos(scanner, 1, 1);
        c = scanner.getch(); assertEquals('O', c); checkPos(scanner, 1, 2);
        c = scanner.getch(); assertEquals('O', c); checkPos(scanner, 1, 3);
        c = scanner.getch(); assertEquals('\n', c); checkPos(scanner, 1, 4);
        c = scanner.getch(); assertEquals('B', c); checkPos(scanner, 2, 1);
        c = scanner.getch(); assertEquals('A', c); checkPos(scanner, 2, 2);
        c = scanner.getch(); assertEquals('R', c); checkPos(scanner, 2, 3);
        c = scanner.getch(); assertEquals('\n', c); checkPos(scanner, 2, 4);
        c = scanner.getch(); assertEquals('\0', c); checkPos(scanner, 3, 1);
        c = scanner.getch(); assertEquals('\0', c); checkPos(scanner, 3, 2);
        scanner.ungetch(); checkPos(scanner, 3, 1);
        scanner.ungetch(); checkPos(scanner, 2, 4);
        scanner.ungetch(); checkPos(scanner, 2, 3);
        scanner.ungetch(); checkPos(scanner, 2, 2);
        scanner.ungetch(); checkPos(scanner, 2, 1);
        scanner.ungetch(); checkPos(scanner, 1, 4);
        scanner.ungetch(); checkPos(scanner, 1, 3);
        scanner.ungetch(); checkPos(scanner, 1, 2);
        scanner.ungetch(); checkPos(scanner, 1, 1);
        scanner.ungetch(); checkPos(scanner, 1, 0);
        c = scanner.getch(); assertEquals('F', c); checkPos(scanner, 1, 1);
    }

    private void checkPos(Scanner scanner, int line, int col) {
        Position p = scanner.getPos();
        assertEquals(line, p.getLine());
        assertEquals(col, p.getCol());
    }

    @Test
    public void testToken() throws Exception {
        Scanner scanner = new Scanner("case of function for to end if then else step in do while repeat until return and or ()[]{}+-*/= <> < <= > >= , . .. | : 1 12. 12.34 abc a2b \"foo\" 'bar'");
        Token t;
        t = scanner.next(); assertEquals(Token.Type.CASE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.OF, t.getType());
        t = scanner.next(); assertEquals(Token.Type.FUNC, t.getType());
        t = scanner.next(); assertEquals(Token.Type.FOR, t.getType());
        t = scanner.next(); assertEquals(Token.Type.TO, t.getType());
        t = scanner.next(); assertEquals(Token.Type.END, t.getType());
        t = scanner.next(); assertEquals(Token.Type.IF, t.getType());
        t = scanner.next(); assertEquals(Token.Type.THEN, t.getType());
        t = scanner.next(); assertEquals(Token.Type.ELSE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.STEP, t.getType());
        t = scanner.next(); assertEquals(Token.Type.IN, t.getType());
        t = scanner.next(); assertEquals(Token.Type.DO, t.getType());
        t = scanner.next(); assertEquals(Token.Type.WHILE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.REPEAT, t.getType());
        t = scanner.next(); assertEquals(Token.Type.UNTIL, t.getType());
        t = scanner.next(); assertEquals(Token.Type.RETURN, t.getType());
        t = scanner.next(); assertEquals(Token.Type.AND, t.getType());
        t = scanner.next(); assertEquals(Token.Type.OR, t.getType());
        t = scanner.next(); assertEquals(Token.Type.LPAREN, t.getType());
        t = scanner.next(); assertEquals(Token.Type.RPAREN, t.getType());
        t = scanner.next(); assertEquals(Token.Type.LBRACK, t.getType());
        t = scanner.next(); assertEquals(Token.Type.RBRACK, t.getType());
        t = scanner.next(); assertEquals(Token.Type.LBRACE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.RBRACE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.PLUS, t.getType());
        t = scanner.next(); assertEquals(Token.Type.MINUS, t.getType());
        t = scanner.next(); assertEquals(Token.Type.ASTERISK, t.getType());
        t = scanner.next(); assertEquals(Token.Type.SLASH, t.getType());
        t = scanner.next(); assertEquals(Token.Type.EQ, t.getType());
        t = scanner.next(); assertEquals(Token.Type.NE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.LT, t.getType());
        t = scanner.next(); assertEquals(Token.Type.LE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.GT, t.getType());
        t = scanner.next(); assertEquals(Token.Type.GE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.COMMA, t.getType());
        t = scanner.next(); assertEquals(Token.Type.DOT, t.getType());
        t = scanner.next(); assertEquals(Token.Type.DOTDOT, t.getType());
        t = scanner.next(); assertEquals(Token.Type.BAR, t.getType());
        t = scanner.next(); assertEquals(Token.Type.COLON, t.getType());

        t = scanner.next(); assertEquals(Token.Type.NUM_LIT, t.getType());  assertEquals("1", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.NUM_LIT, t.getType());  assertEquals("12.", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.NUM_LIT, t.getType());  assertEquals("12.34", t.getValue());

        t = scanner.next(); assertEquals(Token.Type.IDENT, t.getType()); assertEquals("abc", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.IDENT, t.getType()); assertEquals("a2b", t.getValue());

        t = scanner.next(); assertEquals(Token.Type.STRING_LIT, t.getType()); assertEquals("foo", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.STRING_LIT, t.getType()); assertEquals("bar", t.getValue());
    }

    @Test
    public void testComments() throws Exception {
        Scanner scanner = new Scanner("function // UNTIL END OF LINE\nfor /* to end if */ /* then */ else if");
        Token t;
        t = scanner.next(); assertEquals(Token.Type.FUNC, t.getType());
        t = scanner.next(); assertEquals(Token.Type.FOR, t.getType());
        t = scanner.next(); assertEquals(Token.Type.ELSE, t.getType());
        t = scanner.next(); assertEquals(Token.Type.IF, t.getType());
    }

}
