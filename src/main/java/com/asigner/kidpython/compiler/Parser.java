package com.asigner.kidpython.compiler;

import com.asigner.kidpython.compiler.ast.expr.ArithOpNode;
import com.asigner.kidpython.compiler.ast.expr.BoolNode;
import com.asigner.kidpython.compiler.ast.expr.ConstNode;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.ast.expr.MakeListNode;
import com.asigner.kidpython.compiler.ast.expr.MakeMapNode;
import com.asigner.kidpython.compiler.ast.expr.RelOpNode;
import com.asigner.kidpython.compiler.runtime.Value;
import com.asigner.kidpython.util.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
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
import static com.asigner.kidpython.compiler.ast.expr.ArithOpNode.Op.ADD;
import static com.asigner.kidpython.compiler.ast.expr.ArithOpNode.Op.DIV;
import static com.asigner.kidpython.compiler.ast.expr.ArithOpNode.Op.MUL;
import static com.asigner.kidpython.compiler.ast.expr.ArithOpNode.Op.SUB;

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
        ExprNode expr = expr();
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
        ExprNode node = term();
        while (lookahead.getType() == PLUS || lookahead.getType() == MINUS) {
            ArithOpNode.Op op = lookahead.getType() == PLUS ? ADD : SUB;
            match(lookahead.getType());
            ExprNode node2 = term();
            node = new ArithOpNode(node.getPos(), op, node, node2);
        }
        return node;
    }

    private ExprNode term() {
        Position pos;
        ExprNode node;
        switch (lookahead.getType()) {
            case NUM_LIT:
                node = new ConstNode(lookahead.getPos(), new Value(new BigDecimal(lookahead.getValue())));
                match(NUM_LIT);
                return node;
            case STRING_LIT:
                node = new ConstNode(lookahead.getPos(), new Value(lookahead.getValue()));
                match(STRING_LIT);
                return node;
            case LBRACK:
                pos = lookahead.getPos();
                match(LBRACK);
                List<ExprNode> nodes = Lists.newLinkedList();
                if (lookahead.getType() != RBRACK) {
                    nodes.add(expr());
                    while (lookahead.getType() == COMMA) {
                        match(COMMA);
                        nodes.add(expr());
                    }
                }
                match(RBRACK);
                return new MakeListNode(pos, nodes);
            case LBRACE:
                pos = lookahead.getPos();
                List<Pair<ExprNode, ExprNode>> mapNodes = Lists.newLinkedList();
                match(LBRACE);
                if (lookahead.getType() != RBRACE) {
                    mapNodes.add(mapEntry());
                    while (lookahead.getType() == COMMA) {
                        match(COMMA);
                        mapNodes.add(mapEntry());
                    }
                }
                match(RBRACK);
                return new MakeMapNode(pos, mapNodes);
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
                funcBody();

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

    private List<String> optIdentList() {
        List<String> idents = Lists.newArrayList();
        if (lookahead.getType() == IDENT) {
            idents.add(lookahead.getValue());
            match(IDENT);
            while (lookahead.getType() == COMMA) {
                match(COMMA);
                if (lookahead.getType() == IDENT) {
                    idents.add(lookahead.getValue());
                }
                match(IDENT);
            }
        }
    }

    private Pair<ExprNode, ExprNode> mapEntry() {
        ExprNode e1 = expr();
        match(COLON);
        ExprNode e2 = expr();
        return Pair.of(e1, e2);
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
