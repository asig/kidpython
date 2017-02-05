/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.runtime;

import com.programmablefun.compiler.ast.expr.VarNode;

import java.util.Objects;

public class VarRefValue extends Value {
    private final String varName;
    private final VarType varType;

    public VarRefValue(VarNode varNode) {
        super(Type.REFERENCE);
        this.varName = varNode.getVarName();
        this.varType = varNode.getVarType();
    }

    public String getVarName() {
        return varName;
    }

    public VarType getVarType() {
        return varType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarRefValue that = (VarRefValue) o;
        return Objects.equals(varName, that.varName) &&
                varType == that.varType;
    }

    @Override
    public String asString() {
        return "reference to " + varName;
    }

    @Override
    public String asLiteral() {
        return "reference to " + varName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, varType);
    }

    @Override
    public String toString() {
        return "VarRefValue{" + varName + "(" + varType + ")}";
    }
}
