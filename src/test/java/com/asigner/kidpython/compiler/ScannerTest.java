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
        c = scanner.getch(); assertEquals('B', c); assertEquals(1, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('A', c); assertEquals(2, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('R', c); assertEquals(3, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('\n', c); assertEquals(4, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('\0', c); assertEquals(5, scanner.getCol()); assertEquals(2, scanner.getLine());
        c = scanner.getch(); assertEquals('\0', c); assertEquals(6, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(5, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(4, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(3, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(2, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(1, scanner.getCol()); assertEquals(2, scanner.getLine());
        scanner.ungetch(); assertEquals(4, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(3, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(2, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
        scanner.ungetch(); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
        c = scanner.getch(); assertEquals('F', c); assertEquals(1, scanner.getCol()); assertEquals(1, scanner.getLine());
    }

}
