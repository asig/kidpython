package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;
import com.asigner.kidpython.runtime.VirtualMachine;
import com.asigner.kidpython.runtime.Value;

public class ConstNode extends ExprNode {

    private final Value val;

    public ConstNode(Position pos, Value val) {
        super(pos);
        this.val = val;
    }

    public Value getVal() {
        return val;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
