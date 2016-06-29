package com.asigner.kidpython.compiler;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.asigner.kidpython.compiler.Token.Type.DO;
import static com.asigner.kidpython.compiler.Token.Type.ELSE;
import static com.asigner.kidpython.compiler.Token.Type.ELSEIF;
import static com.asigner.kidpython.compiler.Token.Type.END;
import static com.asigner.kidpython.compiler.Token.Type.EOT;
import static com.asigner.kidpython.compiler.Token.Type.EQ;
import static com.asigner.kidpython.compiler.Token.Type.FOR;
import static com.asigner.kidpython.compiler.Token.Type.FUNC;
import static com.asigner.kidpython.compiler.Token.Type.IDENT;
import static com.asigner.kidpython.compiler.Token.Type.IF;
import static com.asigner.kidpython.compiler.Token.Type.IN;
import static com.asigner.kidpython.compiler.Token.Type.LBRACK;
import static com.asigner.kidpython.compiler.Token.Type.LPAREN;
import static com.asigner.kidpython.compiler.Token.Type.NUM_LIT;
import static com.asigner.kidpython.compiler.Token.Type.REPEAT;
import static com.asigner.kidpython.compiler.Token.Type.RETURN;
import static com.asigner.kidpython.compiler.Token.Type.STEP;
import static com.asigner.kidpython.compiler.Token.Type.STRING_LIT;
import static com.asigner.kidpython.compiler.Token.Type.THEN;
import static com.asigner.kidpython.compiler.Token.Type.TO;
import static com.asigner.kidpython.compiler.Token.Type.UNTIL;
import static com.asigner.kidpython.compiler.Token.Type.WHILE;

public class Parser {

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
        while (lookahead.getType() == ELSEIF) {
            match(ELSEIF);
            expr();
            match(THEN);
            stmtBlock();
        }
        if (lookahead.getType() == ELSE) {
            stmtBlock();
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
    }

    private void funcDef() {
    }

    private void expr() {
    }

}
