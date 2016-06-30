package com.asigner.kidpython.compiler;

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
        Scanner scanner = new Scanner("func for to end if then elseif else step in do while repeat until return and or ()[]+-*/= <> < <= > >= , . 1 12. 12.34 abc a2b \"foo\" 'bar'");
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
        t = scanner.next(); assertEquals(Token.Type.DOT, t.getType());

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
        Scanner scanner = new Scanner("func // UNTIL END OF LINE\nfor /* to end if */ /* then */ elseif");
        Token t;
        t = scanner.next(); assertEquals(Token.Type.FUNC, t.getType());
        t = scanner.next(); assertEquals(Token.Type.FOR, t.getType());
        t = scanner.next(); assertEquals(Token.Type.ELSEIF, t.getType());
    }

}
