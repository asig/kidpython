package com.asigner.kidpython.controls;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PythonLineStyler implements LineStyleListener {
    PythonScanner scanner = new PythonScanner();
    int[] tokenColors;
    Color[] colors;
    List<Integer> multiLineStrings = new ArrayList<>();

    public static final int EOF= -1;
    public static final int EOL= 10;

    public static final int WORD=		0;
    public static final int WHITE=		1;
    public static final int KEYWORD =			2;
    public static final int COMMENT=		3;
    public static final int STRING=		5;
    public static final int OTHER=		6;
    public static final int NUMBER=		7;

    public static final int MAXIMUM_TOKEN= 8;

    public PythonLineStyler() {
        initializeColors();
        scanner = new PythonScanner();
    }

    Color getColor(int type) {
        if (type < 0 || type >= tokenColors.length) {
            return null;
        }
        return colors[tokenColors[type]];
    }

    boolean inMultiLineString(int start, int end) {
        for (int i=0; i<multiLineStrings.size(); i+=2) {
            int sStart = multiLineStrings.get(i);
            int sEnd = multiLineStrings.get(i+1);
            // start of string in the line
            if ((sStart >= start) && (sStart <= end)) return true;
            // end of string in the line
            if ((sEnd >= start) && (sEnd <= end)) return true;
            if ((sStart <= start) && (sEnd >= end)) return true;
        }
        return false;
    }

    void initializeColors() {
        Display display = Display.getDefault();
        colors= new Color[] {
                new Color(display, new RGB(0, 0, 0)),		// black
                new Color(display, new RGB(255, 0, 0)),	// red
                new Color(display, new RGB(0, 255, 0)),	// green
                new Color(display, new RGB(0,   0, 255))	// blue
        };
        tokenColors= new int[MAXIMUM_TOKEN];
        tokenColors[WORD]=		0;
        tokenColors[WHITE]=		0;
        tokenColors[KEYWORD]=		3;
        tokenColors[COMMENT]=	1;
        tokenColors[STRING]= 	2;
        tokenColors[OTHER]=		0;
        tokenColors[NUMBER]=	0;
    }

    void disposeColors() {
        for (int i=0;i<colors.length;i++) {
            colors[i].dispose();
        }
    }

    /**
     * Event.detail			line start offset (input)
     * Event.text 			line text (input)
     * LineStyleEvent.styles 	Enumeration of StyleRanges, need to be in order. (output)
     * LineStyleEvent.background 	line background color (output)
     */
    @Override
    public void lineGetStyle(LineStyleEvent event) {
        List<StyleRange> styles = new ArrayList<>();
        int token;
        StyleRange lastStyle;
        // If the line is part of a multiline string, create one style for the entire line.
        if (inMultiLineString(event.lineOffset, event.lineOffset + event.lineText.length())) {
            styles.add(new StyleRange(event.lineOffset, event.lineText.length(), getColor(STRING), null));
            event.styles = styles.toArray(new StyleRange[styles.size()]);
            return;
        }
        Color defaultFgColor = ((Control)event.widget).getForeground();
        scanner.setRange(event.lineText);
        token = scanner.nextToken();
        while (token != EOF) {
            if (token == OTHER) {
                // do nothing for non-colored tokens
            } else if (token != WHITE) {
                Color color = getColor(token);
                // Only create a style if the token color is different than the
                // widget's default foreground color and the token's style is not
                // bold.  Keywords are bolded.
                if ((!color.equals(defaultFgColor)) || (token == KEYWORD)) {
                    StyleRange style = new StyleRange(scanner.getStartOffset() + event.lineOffset, scanner.getLength(), color, null);
                    if (token == KEYWORD) {
                        style.fontStyle = SWT.BOLD;
                    }
                    if (styles.isEmpty()) {
                        styles.add(style);
                    } else {
                        // Merge similar styles.  Doing so will improve performance.
                        lastStyle = styles.get(styles.size()-1);
                        if (lastStyle.similarTo(style) && (lastStyle.start + lastStyle.length == style.start)) {
                            lastStyle.length += style.length;
                        } else {
                            styles.add(style);
                        }
                    }
                }
            } else if ((!styles.isEmpty()) && ((lastStyle=styles.get(styles.size()-1)).fontStyle == SWT.BOLD)) {
                int start = scanner.getStartOffset() + event.lineOffset;
                lastStyle = styles.get(styles.size() - 1);
                // A font style of SWT.BOLD implies that the last style
                // represents a java keyword.
                if (lastStyle.start + lastStyle.length == start) {
                    // Have the white space take on the style before it to
                    // minimize the number of style ranges created and the
                    // number of font style changes during rendering.
                    lastStyle.length += scanner.getLength();
                }
            }
            token= scanner.nextToken();
        }
        event.styles = styles.toArray(new StyleRange[styles.size()]);
    }

    public boolean parseMultilineStrings(String text) {
        List<Integer> ofs = Lists.newArrayList();
        text = text + "  "; // Sentinel
        char chars[] = text.toCharArray();
        boolean inString = false;
        int begin = 0;
        int i;
        for (i = 0; i < chars.length - 3; i++) {
            if (chars[i] == '"' && chars[i+1] == '"' && chars[i+2] == '"') {
                if (!inString) {
                    begin = i;
                    inString = true;
                } else {
                    ofs.add(begin);
                    ofs.add(i+3);
                    inString = false;
                }
            }
        }
        if (inString) {
            ofs.add(begin);
            ofs.add(i+3);
        }

        boolean changed = false;
        if (ofs.size() != multiLineStrings.size()) {
            changed = true;
        } else {
            for (i = 0; i < ofs.size(); i++) {
                if (ofs.get(i) != multiLineStrings.get(i)) {
                    changed = true;
                    break;
                }
            }
        }
        multiLineStrings = ofs;
        return changed;
    }

    /**
     * A simple fuzzy scanner for Python
     */
    public class PythonScanner {

        protected StringBuffer fBuffer= new StringBuffer();
        protected String fDoc;
        protected int fPos;
        protected int fEnd;
        protected int fStartToken;

        private Set<String> keywords = Sets.newHashSet(
                "and",
                "elif",
                "is",
                "global",
                "as",
                "in",
                "if",
                "from",
                "raise",
                "for",
                "except",
                "finally",
                "print",
                "import",
                "pass",
                "return",
                "exec",
                "else",
                "break",
                "not",
                "with",
                "class",
                "assert",
                "yield",
                "try",
                "while",
                "continue",
                "del",
                "or",
                "def",
                "lambda"
        );

        /**
         * Returns the ending location of the current token in the document.
         */
        public final int getLength() {
            return fPos - fStartToken;
        }

        /**
         * Returns the starting location of the current token in the document.
         */
        public final int getStartOffset() {
            return fStartToken;
        }

        /**
         * Returns the next lexical token in the document.
         */
        public int nextToken() {
            int c;
            fStartToken= fPos;
            while (true) {
                switch (c= read()) {
                    case EOF:
                        return EOF;
                    case '#':	// comment
                        while (true) {
                            c= read();
                            if ((c == EOF) || (c == EOL)) {
                                unread(c);
                                return COMMENT;
                            }
                        }
                    case '\'':	// char const
                        while(true) {
                            c= read();
                            switch (c) {
                                case '\'':
                                    return STRING;
                                case EOF:
                                    unread(c);
                                    return STRING;
                                case '\\':
                                    c= read();
                                    break;
                            }
                        }

                    case '"':	// string
                        while(true) {
                            c= read();
                            switch (c) {
                                case '"':
                                    return STRING;
                                case EOF:
                                    unread(c);
                                    return STRING;
                                case '\\':
                                    c= read();
                                    break;
                            }
                        }

                    case '0': case '1': case '2': case '3': case '4':
                    case '5': case '6': case '7': case '8': case '9':
                        do {
                            c= read();
                        } while(Character.isDigit((char)c));
                        unread(c);
                        return NUMBER;
                    default:
                        if (Character.isWhitespace((char)c)) {
                            do {
                                c= read();
                            } while(Character.isWhitespace((char)c));
                            unread(c);
                            return WHITE;
                        }
                        if (Character.isJavaIdentifierStart((char)c)) {
                            fBuffer.setLength(0);
                            do {
                                fBuffer.append((char)c);
                                c= read();
                            } while(Character.isJavaIdentifierPart((char)c));
                            unread(c);
                            if (keywords.contains(fBuffer.toString())) {
                                return KEYWORD;
                            } else {
                                return WORD;
                            }
                        }
                        return OTHER;
                }
            }
        }

        /**
         * Returns next character.
         */
        protected int read() {
            if (fPos <= fEnd) {
                return fDoc.charAt(fPos++);
            }
            return EOF;
        }

        public void setRange(String text) {
            fDoc= text;
            fPos= 0;
            fEnd= fDoc.length() -1;
        }

        protected void unread(int c) {
            if (c != EOF)
                fPos--;
        }
    }
}
