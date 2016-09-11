package com.asigner.kidpython.ide.editor;

import com.asigner.kidpython.compiler.Error;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.COMMENT;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.EOF;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.IDENT;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.KEYWORD;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.NUMBER;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.OTHER;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.STRING;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.WELL_KNOWN;
import static com.asigner.kidpython.ide.editor.CodeLineStyler.Token.WHITESPACE;

public class CodeLineStyler implements LineStyleListener {

    private static final String LINE_NUMBER_FORMAT_STRING = "%3d";
    private static final int BULLET_MARGIN = 4;
    private static final int BULLET_IN_BETWEEN = 6;

    private final StyledText styledText;

    private Stylesheet stylesheet;
    private CodeScanner scanner;
    private List<Range> multiLineComments;
    private Font lineNumberFont;
    private Set<String> wellKnown = Sets.newHashSet();
    private Multimap<Integer, Error> errors = ArrayListMultimap.create();

    private Image errorIcon;
    private int bulletWidth;


    private static class Range {
        int start;
        int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(int pos) {
            return start <= pos && pos < end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Range range = (Range) o;
            return start == range.start &&
                    end == range.end;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

    public enum Token {
        IDENT,
        KEYWORD,
        COMMENT,
        STRING,
        NUMBER,
        OTHER,
        WELL_KNOWN,
        WHITESPACE,
        EOF
    }

    private static final Map<Token, Stylesheet.Entity> tokenToStyle = ImmutableMap.<Token, Stylesheet.Entity>builder()
            .put(Token.IDENT, Stylesheet.Entity.IDENT)
            .put(Token.KEYWORD, Stylesheet.Entity.KEYWORD)
            .put(Token.COMMENT, Stylesheet.Entity.COMMENT)
            .put(Token.STRING, Stylesheet.Entity.STRING)
            .put(Token.NUMBER, Stylesheet.Entity.NUMBER)
            .put(Token.OTHER, Stylesheet.Entity.OTHER)
            .put(Token.WELL_KNOWN, Stylesheet.Entity.WELL_KNOWN_STRING)
            .build();

    public CodeLineStyler(StyledText styledText, Stylesheet stylesheet) {
        this.styledText = styledText;
        this.stylesheet = stylesheet;
        scanner = new CodeScanner();
        multiLineComments = new ArrayList<>();
        lineNumberFont = new Font(Display.getDefault(), "Roboto Mono", 8, SWT.NONE);
        errorIcon = new Image(Display.getDefault(), CodeLineStyler.class.getResourceAsStream("error.png"));

        // Figure out bullet width
        GC gc = new GC(styledText);
        gc.setFont(lineNumberFont);
        bulletWidth = 2 * BULLET_MARGIN + gc.textExtent(String.format(LINE_NUMBER_FORMAT_STRING, 0)).x + 2*BULLET_IN_BETWEEN + 1 + errorIcon.getBounds().width;
        gc.dispose();

        styledText.addPaintObjectListener(this::drawBullet);
    }

    public void setStylesheet(Stylesheet stylesheet) {
        this.stylesheet = stylesheet;
    }

    public void setWellKnownWords(Set<String> wellKnown) {
        this.wellKnown = ImmutableSet.copyOf(wellKnown);
    }

    private Range findMultiLineComment(int pos) {
        for (Range r : multiLineComments) {
            if (r.contains(pos)) {
                return r;
            }
        }
        return null;
    }

    void dispose() {
        this.errorIcon.dispose();
        this.stylesheet.dispose();
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
        Token token;
        StyleRange lastStyle;

        Color defaultFgColor = ((Control)event.widget).getForeground();
        scanner.setRange(event.lineOffset, event.lineText);
        token = scanner.nextToken();
        while (token != EOF) {
            if (token == WHITESPACE) {
                // Whitespace take over the previous style to reduce the number of style changes
                if (styles.size() > 0) {
                    int start = scanner.getStartOffset() + event.lineOffset;
                    lastStyle = styles.get(styles.size() - 1);
                    if (lastStyle.start + lastStyle.length == start) {
                        lastStyle.length += scanner.getLength();
                    }
                }
            } else {
                Stylesheet.Style s = stylesheet.getStyle(tokenToStyle.getOrDefault(token, Stylesheet.Entity.OTHER));
                // Only create a style if the token color is different than the
                // widget's default foreground color and the token's style is bold or italic
                if (!s.getFg().equals(defaultFgColor) || s.getFontStyle() != SWT.NONE) {
                    StyleRange style = new StyleRange(scanner.getStartOffset() + event.lineOffset, scanner.getLength(), s.getFg(), s.getBg());
                    style.fontStyle = s.getFontStyle();
                    if (styles.isEmpty()) {
                        styles.add(style);
                    } else {
                        // Merge similar styles.  Doing so will improve performance.
                        lastStyle = styles.get(styles.size() - 1);
                        if (lastStyle.similarTo(style) && (lastStyle.start + lastStyle.length == style.start)) {
                            lastStyle.length += style.length;
                        } else {
                            styles.add(style);
                        }
                    }
                }
            }
            token= scanner.nextToken();
        }
        event.styles = styles.toArray(new StyleRange[styles.size()]);

        addLineBullets(event);
    }

    private void addLineBullets(LineStyleEvent e) {
        // See http://stackoverflow.com/questions/11057442/java-swt-show-line-numbers-for-styledtext for general idea.
        StyleRange styleRange = new StyleRange();
        styleRange.metrics = new GlyphMetrics(0, 0, bulletWidth); // (bulletLength + 1) * styledText.getLineHeight() / 2);
        e.bullet = new Bullet(ST.BULLET_CUSTOM, styleRange);
        e.bulletIndex = styledText.getLineAtOffset(e.lineOffset) + 1;
    }

    private void drawBullet(PaintObjectEvent event) {
        Rectangle b = errorIcon.getBounds();
        int lineHeight = styledText.getLineHeight(event.bulletIndex - 1);

        // Draw line number
        TextLayout layout = new TextLayout(event.display);
        event.gc.setForeground(stylesheet.getStyle(Stylesheet.Entity.LINE_NUMBER_FG).getFg());
        layout.setAscent(event.ascent);
        layout.setDescent(event.descent);
        layout.setFont(lineNumberFont);
        layout.setText(String.format(LINE_NUMBER_FORMAT_STRING, event.bulletIndex));
        layout.draw(event.gc, event.x + BULLET_MARGIN + b.width + BULLET_IN_BETWEEN, event.y);
        layout.dispose();

        // Draw separator
        int xOfs = bulletWidth - BULLET_MARGIN - 1;
        event.gc.drawLine(event.x + xOfs, event.y, event.x + xOfs, event.y + lineHeight);

        // Draw errors, if there are any
        Collection<Error> errors = this.errors.get(event.bulletIndex);
        if (errors != null && errors.size() > 0) {
            event.gc.drawImage(errorIcon, event.x + BULLET_MARGIN, event.y + (lineHeight - b.height) / 2);
        }

    }

    public boolean parseMultiLineComments(String text) {
        List<Range> ofs = Lists.newArrayList();
        text = text + "  "; // Sentinel
        char chars[] = text.toCharArray();
        boolean inComment = false;
        int begin = 0;
        int i;
        for (i = 0; i < chars.length - 2; i++) {
            if (chars[i] == '/' && chars[i+1] == '*' ) {
                inComment = true;
                begin = i;
                i++; // skip both chars
            } else if (chars[i] == '*' && chars[i+1] == '/') {
                inComment = false;
                ofs.add(new Range(begin, i+2));
                i++; // skip both chars
            }
        }
        if (inComment) {
            ofs.add(new Range(begin, i+3));
        }

        boolean changed = false;
        if (ofs.size() != multiLineComments.size()) {
            changed = true;
        } else {
            for (i = 0; i < ofs.size(); i++) {
                if (!ofs.get(i).equals(multiLineComments.get(i))) {
                    changed = true;
                    break;
                }
            }
        }
        multiLineComments = ofs;
        return changed;
    }

    void setErrors(Multimap<Integer, Error> errors) {
        this.errors = errors;
    }

    Multimap<Integer, Error> getErrors() {
        return errors;
    }

    /**
     * A simple fuzzy scanner for Python
     */
    private class CodeScanner {

        protected StringBuffer buffer = new StringBuffer();
        private int offset;
        protected String text;
        protected int pos;
        protected int end;
        protected int startToken;

        private Set<String> keywords = Sets.newHashSet(
                "function",
                "for",
                "end",
                "if",
                "then",
                "else",
                "step",
                "in",
                "to",
                "do",
                "while",
                "repeat",
                "until",
                "return",
                "and",
                "or"
        );

        /**
         * Returns the ending location of the current token in the document.
         */
        public final int getLength() {
            return pos - startToken;
        }

        /**
         * Returns the starting location of the current token in the document.
         */
        public final int getStartOffset() {
            return startToken;
        }

        /**
         * Returns the next lexical token in the document.
         */
        public Token nextToken() {
            int c;
            int sep;
            startToken = pos;
            Range r = findMultiLineComment(offset + pos);
            if (r != null) {
                pos = r.end - offset;
                return COMMENT;
            }
            while (true) {
                switch (c= read()) {
                    case -1:
                        return EOF;
                    case '/': {
                        int c2 = read();
                        if (c2 == '/') {
                            while (true) {
                                c= read();
                                if ((c == -1) || (c == '\n')) {
                                    unread(c);
                                    return COMMENT;
                                }
                            }
                        } else {
                            unread(c2);
                        }
                        return OTHER;
                    }
                    case '\'':
                    case '"': // string
                        sep = c;
                        while(true) {
                            c= read();
                            if (c == sep) {
                                return STRING;
                            } else if (c == -1) {
                                unread(c);
                                return STRING;
                            } else if (c == '\\') {
                                read();
                            }
                        }

                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        return readNumber();

                    default:
                        if (Character.isWhitespace((char)c)) {
                            do {
                                c= read();
                            } while(Character.isWhitespace((char)c));
                            unread(c);
                            return WHITESPACE;
                        }
                        if (Character.isJavaIdentifierStart((char)c)) {
                            buffer.setLength(0);
                            do {
                                buffer.append((char)c);
                                c= read();
                            } while(Character.isJavaIdentifierPart((char)c));
                            unread(c);
                            if (keywords.contains(buffer.toString())) {
                                return KEYWORD;
                            } else if (wellKnown.contains(buffer.toString())) {
                                return WELL_KNOWN;
                            } else {
                                return IDENT;
                            }
                        }
                        return OTHER;
                }
            }
        }

        private Token readNumber() {
            int c = read();
            while (Character.isDigit(c)) {
                c = read();
            }
            if (c == '.') {
                c = read();
                while (Character.isDigit(c)) {
                    c = read();
                }
            }
            unread(c);
            return NUMBER;
        }

        /**
         * Returns next character.
         */
        protected int read() {
            if (pos <= end) {
                return text.charAt(pos++);
            }
            return -1;
        }

        public void setRange(int offset, String text) {
            this.offset = offset;
            this.text = text;
            this.pos = 0;
            this.end = this.text.length() -1;
        }

        protected void unread(int c) {
            if (c != -1) {
                pos--;
            }
        }
    }
}
