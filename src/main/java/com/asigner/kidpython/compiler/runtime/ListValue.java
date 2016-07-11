// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.asigner.kidpython.compiler.runtime.Value.Type.LIST;

public class ListValue extends Value {
    private final List<Value> listVal;

    public ListValue(List<Value> val) {
        super(LIST);
        this.listVal = val;
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return listVal.iterator();
    }

    public List<Value> asList() {
        return listVal;
    }

    @Override
    public String toString() {
        return "ListValue{" + getType().toString() + ":" + listVal + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListValue listValue = (ListValue) o;

        if (this.listVal.size() != listValue.listVal.size()) {
            return false;
        }
        Iterator<Value> i1 = this.listVal.iterator();
        Iterator<Value> i2 = listValue.listVal.iterator();
        while (i1.hasNext()) {
            if (!(i1.next().equals(i2.next()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listVal);
    }
}
