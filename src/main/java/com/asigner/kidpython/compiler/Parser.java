package com.asigner.kidpython.compiler;

import com.asigner.kidpython.compiler.ast.ArithOpNode;
import com.asigner.kidpython.compiler.ast.BoolNode;
import com.asigner.kidpython.compiler.ast.ExprNode;
import com.asigner.kidpython.compiler.ast.RelOpNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import static com.asigner.kidpython.compiler.Token.Type.AND;
import static com.asigner.kidpython.compiler.Token.Type.ASTERISK;
import static com.asigner.kidpython.compiler.Token.Type.COLON;
import static com.asigner.kidpython.compiler.Token.Type.COMMA;
import static com.asigner.kidpython.compiler.Token.Type.DO;
import static com.asigner.kidpython.compiler.Token.Type.DOT;
import static com.asigner.kidpython.compiler.Token.Type.ELSE;
import static com.asigner.kidpython.compiler.Token.Type.END;
import static com.asigner.kidpython.compiler.Token.Type.EOT;
import static com.asigner.kidpython.compiler.Token.Type.EQ;
import static com.asigner.kidpython.compiler.Token.Type.FOR;
import static com.asigner.kidpython.compiler.Token.Type.FUNC;
import static com.asigner.kidpython.compiler.Token.Type.GE;
import static com.asigner.kidpython.compiler.Token.Type.GT;
import static com.asigner.kidpython.compiler.Token.Type.IDENT;
import static com.asigner.kidpython.compiler.Token.Type.IF;
import static com.asigner.kidpython.compiler.Token.Type.IN;
import static com.asigner.kidpython.compiler.Token.Type.LBRACE;
import static com.asigner.kidpython.compiler.Token.Type.LBRACK;
import static com.asigner.kidpython.compiler.Token.Type.LE;
import static com.asigner.kidpython.compiler.Token.Type.LPAREN;
import static com.asigner.kidpython.compiler.Token.Type.LT;
import static com.asigner.kidpython.compiler.Token.Type.MINUS;
import static com.asigner.kidpython.compiler.Token.Type.NE;
import static com.asigner.kidpython.compiler.Token.Type.NUM_LIT;
import static com.asigner.kidpython.compiler.Token.Type.OR;
import static com.asigner.kidpython.compiler.Token.Type.PLUS;
import static com.asigner.kidpython.compiler.Token.Type.RBRACE;
import static com.asigner.kidpython.compiler.Token.Type.RBRACK;
import static com.asigner.kidpython.compiler.Token.Type.REPEAT;
import static com.asigner.kidpython.compiler.Token.Type.RETURN;
import static com.asigner.kidpython.compiler.Token.Type.RPAREN;
import static com.asigner.kidpython.compiler.Token.Type.SLASH;
import static com.asigner.kidpython.compiler.Token.Type.STEP;
import static com.asigner.kidpython.compiler.Token.Type.STRING_LIT;
import static com.asigner.kidpython.compiler.Token.Type.THEN;
import static com.asigner.kidpython.compiler.Token.Type.TO;
import static com.asigner.kidpython.compiler.Token.Type.UNTIL;
import static com.asigner.kidpython.compiler.Token.Type.WHILE;
import static com.asigner.kidpython.compiler.ast.ArithOpNode.Op.DIV;
import static com.asigner.kidpython.compiler.ast.ArithOpNode.Op.MUL;

public class Parser {

    private Set<Token.Type> RELOPS = Sets.newHashSet(EQ, NE, LE, LT, GE, GT);

    private Set<Token.Type> STMT_START_SET = Sets.newHashSet(
            IF,
            FOR,
            WHILE,
            REPEAT,
            RETURN,
            FUNC,
            IDENT
    );

    private Set<Token.Type> EXPR_START_SET = Sets.newHashSet(
            NUM_LIT,
            LBRACK,
            STRING_LIT,
            IDENT,
            LPAREN
    );

    private Set<Token.Type> SELECTOR_OR_CALL_START_SET = Sets.newHashSet(
            LBRACK,
            LPAREN,
            DOT
    );

    private Set<Token.Type> TERM_START_SET = Sets.newHashSet(
            NUM_LIT,
            STRING_LIT,
            FUNC,
            LBRACK,
            IDENT,
            LPAREN
    );

    private final Scanner scanner;
    private Token lookahead;

    public Parser(String text) {
        this.scanner = new Scanner(text);
    }

    public void parse() {
        stmtBlock();
    }

    private void error(Error error) {

    }

    private void match(Token.Type type) {
        if (lookahead.getType() != type) {
            error(Error.unexpectedToken(lookahead, Sets.newHashSet(type)));
            return;
        }
        lookahead = scanner.next();
    }

    private void sync(Set<Token.Type> startSet) {
        while (!startSet.contains(lookahead.getType())) {
            lookahead = scanner.next();
        }
    }

    private void stmtBlock() {
        stmt();
        while (STMT_START_SET.contains(lookahead.getType())) {
            stmt();
        }
        match(EOT);
    }

    private void stmt() {
        switch (lookahead.getType()) {
            case FUNC:
                funcDef();
                break;
            case IF:
                ifStmt();
                break;
            case FOR:
                forStmt();
                break;
            case WHILE:
                whileStmt();
                break;
            case REPEAT:
                repeatStmt();
                break;
            case RETURN:
                returnStmt();
                break;
            case IDENT:
                assignmentOrCall();
                break;
            default:
                error(Error.unexpectedToken(lookahead, STMT_START_SET));
        }
    }

    private void ifStmt() {
        match(IF);
        expr();
        match(THEN);
        stmtBlock();
        for (;;) {
            if (lookahead.getType() == ELSE) {
                // ELSE IF or just ELSE
                match(ELSE);
                if (lookahead.getType() == IF) {
                    // ELSE IF: stay in loop
                    match(IF);
                    expr();
                    match(THEN);
                    stmtBlock();
                } else {
                    // terminating ELSE. break out of loop afterwards
                    stmtBlock();
                    break;
                }
            } else {
                // Neither ELSE IF nor ELSE: break out of loop
                break;
            }
        }
        match(END);
    }

    private void forStmt() {
        match(FOR);
        match(IDENT);
        if (lookahead.getType() == IN) {
            match(IN);
            expr();
        } else if (lookahead.getType() == EQ) {
            match(EQ);
            expr();
            match(TO);
            expr();
            if (lookahead.getType() == STEP) {
                match(STEP);
                expr();
            }

        } else {
            sync(Sets.newHashSet(DO));
        }
        match(DO);
        stmtBlock();
        match(END);
    }

    private void whileStmt() {
        match(WHILE);
        expr();
        match(DO);
        stmtBlock();
        match(END);
    }

    private void repeatStmt() {
        match(REPEAT);
        stmtBlock();
        match(UNTIL);
        expr();
    }

    private void returnStmt() {
        match(RETURN);
        if (EXPR_START_SET.contains(lookahead.getType())) {
            expr();
        }
    }

    private void assignmentOrCall() {
        match(IDENT);
        while (SELECTOR_OR_CALL_START_SET.contains(lookahead.getType())) {
            selectorOrCall();
        }
        if (lookahead.getType() == EQ) {
            match(EQ);
            expr();
        }
    }

    private void selectorOrCall() {
        switch (lookahead.getType()) {
            case LBRACK:
                match(LBRACK);
                expr();
                match(RBRACK);
                break;
            case DOT:
                match(DOT);
                match(IDENT);
                break;
            case LPAREN:
                match(LPAREN);
                if (lookahead.getType() != RPAREN) {
                    expr();
                    while (lookahead.getType() == COMMA) {
                        match(COMMA);
                        expr();
                    }
                }
                match(RPAREN);
            default:
                error(Error.unexpectedToken(lookahead, SELECTOR_OR_CALL_START_SET));
        }
    }

    private void funcDef() {
        match(FUNC);
        match(IDENT);
        match(LPAREN);
        optIdentList();
        match(RPAREN);
        funcBody();
    }

    private ExprNode expr() {
        ExprNode node = andExpr();
        while(lookahead.getType() == AND) {
            match(AND);
            ExprNode node2 = andExpr();
            node = new BoolNode(node.getPos(), BoolNode.Op.AND, node, node2);
        }
        return node;
    }

    private ExprNode andExpr() {
        ExprNode node = orExpr();
        while(lookahead.getType() == OR) {
            match(OR);
            ExprNode node2 = orExpr();
            node = new BoolNode(node.getPos(), BoolNode.Op.OR, node, node2);
        }
        return node;
    }

    private ExprNode orExpr() {
        ExprNode node = arithExpr();
        if(RELOPS.contains(lookahead.getType())) {
            RelOpNode.Op op = null;
            switch (lookahead.getType()) {
                case EQ: op = RelOpNode.Op.EQ; break;
                case NE: op = RelOpNode.Op.NE; break;
                case LE: op = RelOpNode.Op.LE; break;
                case LT: op = RelOpNode.Op.LT; break;
                case GE: op = RelOpNode.Op.GE; break;
                case GT: op = RelOpNode.Op.GT; break;
            }
            match(lookahead.getType());
            ExprNode node2 = arithExpr();
            node = new RelOpNode(node.getPos(), op, node, node2);
        }
        return node;
    }

    private ExprNode arithExpr() {
        ExprNode node = factor();
        while (lookahead.getType() == ASTERISK || lookahead.getType() == SLASH) {
            ArithOpNode.Op op = lookahead.getType() == ASTERISK ? MUL : DIV;
            match(lookahead.getType());
            ExprNode node2 = factor();
            node = new ArithOpNode(node.getPos(), op, node, node2);
        }
        return node;
    }

    private ExprNode factor() {
        term();
        while (lookahead.getType() == PLUS || lookahead.getType() == MINUS) {
            match(lookahead.getType());
            term();
        }
        return null;
    }

    private ExprNode term() {
        switch (lookahead.getType()) {
            case NUM_LIT:
                match(NUM_LIT);
                break;
            case STRING_LIT:
                match(STRING_LIT);
                break;
            case LBRACK:
                match(LBRACK);
                if (lookahead.getType() != RBRACK) {
                    expr();
                    while (lookahead.getType() == COMMA) {
                        match(COMMA);
                        expr();
                    }
                }
                match(RBRACK);
                break;
            case LBRACE:
                match(LBRACE);
                if (lookahead.getType() != RBRACE) {
                    mapEntry();
                    while (lookahead.getType() == COMMA) {
                        match(COMMA);
                        mapEntry();
                    }
                }
                match(RBRACK);
                break;
            case IDENT:
                match(IDENT);
                while (SELECTOR_OR_CALL_START_SET.contains(lookahead.getType())) {
                    selectorOrCall();
                }
                break;
            case LPAREN:
                match(LPAREN);
                expr();
                match(RPAREN);
                while (SELECTOR_OR_CALL_START_SET.contains(lookahead.getType())) {
                    selectorOrCall();
                }
                break;
            case FUNC:
                match(FUNC);
                match(LPAREN);
                optIdentList();
                match(RPAREN);

            default:
                error(Error.unexpectedToken(lookahead, TERM_START_SET));
        }
        return null;
    }

    private void funcBody() {
        if (STMT_START_SET.contains(lookahead.getType())) {
            stmtBlock();
        }
        match(END);
    }

    private void optIdentList() {
        if (lookahead.getType() == IDENT) {
            match(IDENT);
            while (lookahead.getType() == COMMA) {
                match(COMMA);
                match(IDENT);
            }
        }
    }

    private void mapEntry() {
        expr();
        match(COLON);
        expr();
    }

    private ExprNode rightMergeNodes(List<ExprNode> nodes, BiFunction<ExprNode, ExprNode, ExprNode> merger) {
        while (nodes.size() > 1) {
            ExprNode left = nodes.get(nodes.size() - 2);
            ExprNode right = nodes.get(nodes.size() - 1);
            ExprNode newNode = merger.apply(left, right);;
            nodes.set(nodes.size() - 2, newNode);
            nodes.remove(nodes.size() - 1);
        }
        return nodes.get(0);
    }

}
