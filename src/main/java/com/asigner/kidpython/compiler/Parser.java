package com.asigner.kidpython.compiler;

import com.asigner.kidpython.compiler.ast.AssignmentStmt;
import com.asigner.kidpython.compiler.ast.EmptyStmt;
import com.asigner.kidpython.compiler.ast.EvalStmt;
import com.asigner.kidpython.compiler.ast.ForEachStmt;
import com.asigner.kidpython.compiler.ast.ForStmt;
import com.asigner.kidpython.compiler.ast.IfStmt;
import com.asigner.kidpython.compiler.ast.RepeatStmt;
import com.asigner.kidpython.compiler.ast.ReturnStmt;
import com.asigner.kidpython.compiler.ast.Stmt;
import com.asigner.kidpython.compiler.ast.WhileStmt;
import com.asigner.kidpython.compiler.ast.expr.BinOpNode;
import com.asigner.kidpython.compiler.ast.expr.CallNode;
import com.asigner.kidpython.compiler.ast.expr.ConstNode;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.ast.expr.MakeFuncNode;
import com.asigner.kidpython.compiler.ast.expr.MakeListNode;
import com.asigner.kidpython.compiler.ast.expr.MakeMapNode;
import com.asigner.kidpython.compiler.ast.expr.MapAccessNode;
import com.asigner.kidpython.compiler.ast.expr.UnOpNode;
import com.asigner.kidpython.compiler.ast.expr.VarNode;
import com.asigner.kidpython.compiler.runtime.NumberValue;
import com.asigner.kidpython.compiler.runtime.StringValue;
import com.asigner.kidpython.compiler.runtime.UndefinedValue;
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
import static com.asigner.kidpython.compiler.ast.expr.BinOpNode.Op.ADD;
import static com.asigner.kidpython.compiler.ast.expr.BinOpNode.Op.DIV;
import static com.asigner.kidpython.compiler.ast.expr.BinOpNode.Op.MUL;
import static com.asigner.kidpython.compiler.ast.expr.BinOpNode.Op.SUB;

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

    private Set<Token.Type> FACTOR_START_SET = Sets.newHashSet(
            NUM_LIT,
            STRING_LIT,
            FUNC,
            LBRACK,
            IDENT,
            LPAREN
    );

    private final Scanner scanner;
    private Token lookahead;
    private int inFunction;
    private List<Error> errors;

    public Parser(String text) {
        this.scanner = new Scanner(text);
        this.errors = Lists.newArrayList();
    }

    public Stmt parse() {
        inFunction = 0;
        lookahead = scanner.next();
        Stmt code = stmtBlock().getFirst();
        match(EOT);
        return errors.size() > 0 ? null : code;
    }

    public List<Error> getErrors() {
        return errors;
    }

    private void error(Error error) {
        this.errors.add(error);
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

    private StmtList stmtBlock() {
        StmtList stmts = new StmtList();
        stmts.add(stmt());
        while (STMT_START_SET.contains(lookahead.getType())) {
            stmts.add(stmt());
        }
        return stmts;
    }

    private Stmt stmt() {
        switch (lookahead.getType()) {
            case FUNC:
                return funcDef();
            case IF:
                return ifStmt();
            case FOR:
                return forStmt();
            case WHILE:
                return whileStmt();
            case REPEAT:
                return repeatStmt();
            case RETURN:
                return returnStmt();
            case IDENT:
                return assignmentOrCall();
            default:
                error(Error.unexpectedToken(lookahead, STMT_START_SET));
                return new EmptyStmt(lookahead.getPos());
        }
    }

    private Stmt ifStmt() {
        Position ifPos = lookahead.getPos();
        match(IF);
        ExprNode expr = expr();
        match(THEN);
        Stmt body = stmtBlock().getFirst();

        IfStmt ifStmt = new IfStmt(ifPos, expr, body);
        IfStmt curIf = ifStmt;
        for (;;) {
            if (lookahead.getType() == ELSE) {
                Position pos = lookahead.getPos();
                // ELSE IF or just ELSE
                match(ELSE);
                if (lookahead.getType() == IF) {
                    // ELSE IF: stay in loop
                    ifPos = lookahead.getPos();
                    match(IF);
                    ExprNode cond = expr();
                    match(THEN);
                    body = stmtBlock().getFirst();

                    IfStmt innerIf = new IfStmt(ifPos, cond, body);
                    curIf.setFalseBranch(innerIf);
                    curIf = innerIf;
                } else {
                    // terminating ELSE. break out of loop afterwards
                    curIf.setFalseBranch(stmtBlock().getFirst());
                    break;
                }
            } else {
                // Neither ELSE IF nor ELSE: break out of loop
                break;
            }
        }
        match(END);
        return ifStmt;
    }

    private Stmt forStmt() {
        Position pos = lookahead.getPos();
        match(FOR);
        String varIdent = lookahead.getValue();
        Position varPos = lookahead.getPos();
        match(IDENT);
        ExprNode ctrlVar = new VarNode(varPos, varIdent);
        if (lookahead.getType() == IN) {
            match(IN);
            Position rangePos = lookahead.getPos();
            ExprNode range = expr();
            match(DO);

//            // iter = range.begin()
//            ExprNode iterVar = new VarNode(rangePos, makeTempVarName());
//            stmts.add(new AssignmentStmt(rangePos, iterVar, new MakeIterNode(rangePos, range)));
//
//            // if !iter.hasnext goto end
//            ExprNode condition = new NotNode(rangePos, new IterHasNextNode(rangePos, iterVar));
//            Stmt ifNode = new IfStmt(varPos, condition, end);
//            stmts.add(ifNode);
//
//            // i = iter.next()
//            stmts.add(new AssignmentStmt(rangePos, ctrlVar, new IterNextNode(rangePos, iterVar)));
//
            Stmt body = stmtBlock().getFirst();

//            stmts.add(stmtBlock());
//            stmts.getLast().setNext(ifNode);

            match(END);

            return new ForEachStmt(pos, ctrlVar, range, body);

        } else if (lookahead.getType() == EQ) {
            ExprNode stepExpr = new ConstNode(lookahead.getPos(), new NumberValue(BigDecimal.ONE));
            Position eqPos = lookahead.getPos();
            match(EQ);
            ExprNode fromExpr = expr();
            match(TO);
            ExprNode toExpr = expr();
            if (lookahead.getType() == STEP) {
                match(STEP);
                stepExpr = expr();
            }

//            // i := start
//            stmts.add(new AssignmentStmt(varPos, ctrlVar, fromExpr));
//
//            // if i == stop goto end
//            ExprNode comparison = new RelOpNode(eqPos, RelOpNode.Op.EQ, ctrlVar, toExpr);
//            Stmt ifNode = new IfStmt(varPos, comparison, end);
//            stmts.add(ifNode);

            match(DO);

            Stmt body = stmtBlock().getFirst();

//            // i = i + steop
//            ExprNode addStep = new ArithOpNode(eqPos, ArithOpNode.Op.ADD, ctrlVar, stepExpr);
//            stmts.add(new AssignmentStmt(eqPos, ctrlVar, addStep));
//            // Goto comparisong
//            stmts.getLast().setNext(ifNode);

            match(END);

            return new ForStmt(pos, ctrlVar, fromExpr, toExpr, stepExpr, body);

        } else {
            sync(Sets.newHashSet(DO));
            return new EmptyStmt(pos);
        }
    }

    private Stmt whileStmt() {
        Position pos = lookahead.getPos();
        match(WHILE);
        Position condPos = lookahead.getPos();
        ExprNode condition = expr();
        match(DO);
        Stmt body = stmtBlock().getFirst();
        match(END);

        return new WhileStmt(pos, condition, body);
    }

    private Stmt repeatStmt() {
        Position pos = lookahead.getPos();
        match(REPEAT);
        Stmt body = stmtBlock().getFirst();
        match(UNTIL);
        ExprNode condition = expr();

        return new RepeatStmt(pos, condition, body);
    }

    private Stmt returnStmt() {
        Position pos = lookahead.getPos();
        match(RETURN);
        ExprNode expr = new ConstNode(lookahead.getPos(), UndefinedValue.INSTANCE);
        if (EXPR_START_SET.contains(lookahead.getType())) {
            expr = expr();
        }
        if (inFunction == 0) {
            error(Error.returnNotAllowedOutsideFunction(pos));
        }
        return new ReturnStmt(pos, expr);
    }

    private Stmt assignmentOrCall() {
        Position pos = lookahead.getPos();
        String ident = lookahead.getValue();
        match(IDENT);
        ExprNode varExpr = new VarNode(pos, ident);
        while (SELECTOR_OR_CALL_START_SET.contains(lookahead.getType())) {
            varExpr = selectorOrCall(varExpr);
        }
        ExprNode expr;
        if (lookahead.getType() == EQ) {
            match(EQ);
            expr = expr();
            return new AssignmentStmt(pos, varExpr, expr);
        } else {
            return new EvalStmt(pos, varExpr);
        }
    }

    private ExprNode selectorOrCall(ExprNode base) {
        ExprNode curExpr = base;
        Position pos = lookahead.getPos();
        switch (lookahead.getType()) {
            case LBRACK:
                match(LBRACK);
                ExprNode index = expr();
                match(RBRACK);
                curExpr = new MapAccessNode(pos, curExpr, index);
                break;
            case DOT:
                match(DOT);
                pos = lookahead.getPos();
                String prop = lookahead.getValue();
                match(IDENT);
                curExpr = new MapAccessNode(pos, curExpr, new ConstNode(pos, new StringValue(prop)));
                break;
            case LPAREN:
                match(LPAREN);
                List<ExprNode> params = Lists.newArrayList();
                if (lookahead.getType() != RPAREN) {
                    params.add(expr());
                    while (lookahead.getType() == COMMA) {
                        match(COMMA);
                        params.add(expr());
                    }
                }
                match(RPAREN);
                curExpr = new CallNode(pos, base, params);
                break;
            default:
                error(Error.unexpectedToken(lookahead, SELECTOR_OR_CALL_START_SET));
        }
        return curExpr;
    }

    private Stmt funcDef() {
        inFunction++;
        Position pos = lookahead.getPos();
        match(FUNC);
        String funcName = lookahead.getValue();
        match(IDENT);
        match(LPAREN);
        List<String> params = optIdentList();
        match(RPAREN);
        Stmt body = funcBody();
        inFunction--;
        return new AssignmentStmt(pos, new VarNode(pos, funcName), new MakeFuncNode(pos, body, params));
    }

    private ExprNode expr() {
        ExprNode node = andExpr();
        while(lookahead.getType() == AND) {
            match(AND);
            ExprNode node2 = andExpr();
            node = new BinOpNode(node.getPos(), BinOpNode.Op.AND, node, node2);
        }
        return node;
    }

    private ExprNode andExpr() {
        ExprNode node = orExpr();
        while(lookahead.getType() == OR) {
            match(OR);
            ExprNode node2 = orExpr();
            node = new BinOpNode(node.getPos(), BinOpNode.Op.OR, node, node2);
        }
        return node;
    }

    private ExprNode orExpr() {
        ExprNode node = arithExpr();
        if(RELOPS.contains(lookahead.getType())) {
            BinOpNode.Op op = null;
            switch (lookahead.getType()) {
                case EQ: op = BinOpNode.Op.EQ; break;
                case NE: op = BinOpNode.Op.NE; break;
                case LE: op = BinOpNode.Op.LE; break;
                case LT: op = BinOpNode.Op.LT; break;
                case GE: op = BinOpNode.Op.GE; break;
                case GT: op = BinOpNode.Op.GT; break;
            }
            match(lookahead.getType());
            ExprNode node2 = arithExpr();
            node = new BinOpNode(node.getPos(), op, node, node2);
        }
        return node;
    }

    private ExprNode arithExpr() {
        ExprNode node = term();
        while (lookahead.getType() == PLUS || lookahead.getType() == MINUS) {
            BinOpNode.Op op = lookahead.getType() == PLUS ? ADD : SUB;
            match(lookahead.getType());
            ExprNode node2 = term();
            node = new BinOpNode(node.getPos(), op, node, node2);
        }
        return node;
    }

    private ExprNode term() {
        ExprNode node = factor();
        while (lookahead.getType() == ASTERISK || lookahead.getType() == SLASH) {
            BinOpNode.Op op = lookahead.getType() == ASTERISK ? MUL : DIV;
            match(lookahead.getType());
            ExprNode node2 = factor();
            node = new BinOpNode(node.getPos(), op, node, node2);
        }
        return node;
    }

    private ExprNode factor() {
        Position pos = lookahead.getPos();
        ExprNode node;
        switch (lookahead.getType()) {
            case MINUS:
                match(MINUS);
                switch(lookahead.getType()) {
                    case NUM_LIT:
                        node = numLitNode();
                        return new UnOpNode(pos, UnOpNode.Op.NEG, node);
                    case IDENT:
                        node = varNode();
                        return new UnOpNode(pos, UnOpNode.Op.NEG, node);
                    case LPAREN:
                        node = subExprNode();
                        return new UnOpNode(pos, UnOpNode.Op.NEG, node);
                }
                error(Error.unexpectedToken(lookahead, Sets.newHashSet(NUM_LIT, IDENT, LPAREN)));
                return new ConstNode(pos, UndefinedValue.INSTANCE);
            case NUM_LIT:
                return numLitNode();
            case STRING_LIT:
                node = new ConstNode(pos, new StringValue(lookahead.getValue()));
                match(STRING_LIT);
                return node;
            case LBRACK:
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
                List<Pair<ExprNode, ExprNode>> mapNodes = Lists.newLinkedList();
                match(LBRACE);
                if (lookahead.getType() != RBRACE) {
                    mapNodes.add(mapEntry());
                    while (lookahead.getType() == COMMA) {
                        match(COMMA);
                        mapNodes.add(mapEntry());
                    }
                }
                match(RBRACE);
                return new MakeMapNode(pos, mapNodes);
            case IDENT:
                return varNode();
            case LPAREN:
                return subExprNode();
            case FUNC:
                match(FUNC);
                match(LPAREN);
                List<String> params = optIdentList();
                match(RPAREN);
                inFunction++;
                Stmt body = funcBody();
                inFunction--;
                return new MakeFuncNode(pos, body, params);
            default:
                error(Error.unexpectedToken(lookahead, FACTOR_START_SET));
                return new ConstNode(pos, new NumberValue(new BigDecimal(0)));
        }
    }

    private ExprNode varNode() {
        Position pos = lookahead.getPos();
        String varName = lookahead.getValue();
        match(IDENT);
        ExprNode node = new VarNode(pos, varName);
        while (SELECTOR_OR_CALL_START_SET.contains(lookahead.getType())) {
            node = selectorOrCall(node);
        }
        return node;
    }

    private ExprNode numLitNode() {
        Position pos = lookahead.getPos();
        ExprNode node = new ConstNode(pos, new NumberValue((new BigDecimal(lookahead.getValue()))));
        match(NUM_LIT);
        return node;
    }

    private ExprNode subExprNode() {
        match(LPAREN);
        ExprNode node = expr();
        match(RPAREN);
        while (SELECTOR_OR_CALL_START_SET.contains(lookahead.getType())) {
            node = selectorOrCall(node);
        }
        return node;
    }

    private Stmt funcBody() {
        Position pos = lookahead.getPos();
        StmtList stmts = new StmtList();
        if (STMT_START_SET.contains(lookahead.getType())) {
            stmts = stmtBlock();
        }
        match(END);
        stmts.add(new ReturnStmt(pos, new ConstNode(pos, UndefinedValue.INSTANCE)));
        return stmts.getFirst();
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
        return idents;
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
