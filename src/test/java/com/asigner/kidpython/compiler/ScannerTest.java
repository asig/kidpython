package com.asigner.kidpython.compiler;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScannerTest {

    @Test
    public void testPositions() throws Exception {
        Scanner scanner = new Scanner("FOO\nBAR\n");
        char c;

        c = scanner.getch(); assertEquals('F', c); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('O', c); assertEquals(2, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('O', c); assertEquals(3, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('\n', c); assertEquals(4, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(3, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(2, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(0, scanner.getCol()); assertEquals(1, scanner.getLine());

        c = scanner.getch(); assertEquals('F', c); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('O', c); assertEquals(2, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('O', c); assertEquals(3, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('\n', c); assertEquals(4, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('B', c); assertEquals(1, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('A', c); assertEquals(2, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('R', c); assertEquals(3, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('\n', c); assertEquals(4, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('\0', c); assertEquals(1, scanner.getCol()); assertEquals(3, scanner.getLine());
        c = scanner.getch(); assertEquals('\0', c); assertEquals(2, scanner.getCol()); assertEquals(3, scanner.getLine());
        scanner.ungetch(); assertEquals(1, scanner.getCol()); assertEquals(3, scanner.getLine());
        scanner.ungetch(); assertEquals(4, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(3, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(2, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(1, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(4, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(3, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(2, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(0, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('F', c); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
    }

    @Test
    public void testToken() throws Exception {
        Scanner scanner = new Scanner("func for to end if then elseif else step in do while repeat until return and or ()[]+-*/= <> < <= > >= , 1 12. 12.34 abc a2b \"foo\" 'bar'");
        Token t;
        t = scanner.next(); assertEquals(Token.Type.FUNC, t.getType());
        t = scanner.next(); assertEquals(Token.Type.FOR, t.getType());
        t = scanner.next(); assertEquals(Token.Type.TO, t.getType());
        t = scanner.next(); assertEquals(Token.Type.END, t.getType());
        t = scanner.next(); assertEquals(Token.Type.IF, t.getType());
        t = scanner.next(); assertEquals(Token.Type.THEN, t.getType());
        t = scanner.next(); assertEquals(Token.Type.ELSEIF, t.getType());
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

        t = scanner.next(); assertEquals(Token.Type.NUM_LIT, t.getType());  assertEquals("1", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.NUM_LIT, t.getType());  assertEquals("12.", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.NUM_LIT, t.getType());  assertEquals("12.34", t.getValue());

        t = scanner.next(); assertEquals(Token.Type.IDENT, t.getType()); assertEquals("abc", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.IDENT, t.getType()); assertEquals("a2b", t.getValue());

        t = scanner.next(); assertEquals(Token.Type.STRING_LIT, t.getType()); assertEquals("foo", t.getValue());
        t = scanner.next(); assertEquals(Token.Type.STRING_LIT, t.getType()); assertEquals("bar", t.getValue());
    }
}
