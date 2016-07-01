package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.Node;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class Value {
    enum Type {
        BOOLEAN,
        STRING,
        FUNCTION,
        NUMBER,
        LIST,
        MAP
    }

    private final Type type;
    private final Boolean boolVal;
    private final String strVal;
    private final BigDecimal numVal;
    private final Node funcVal;
    private final Map<Value, Value> mapVal;
    private final List<Value> listVal;

    public Value(Boolean val) {
        this.type = Type.BOOLEAN;
        this.boolVal = val;
        this.numVal = null;
        this.strVal = null;
        this.funcVal = null;
        this.mapVal = null;
        this.listVal = null;
    }

    public Value(BigDecimal val) {
        this.type = Type.NUMBER;
        this.boolVal = null;
        this.numVal = val;
        this.strVal = null;
        this.funcVal = null;
        this.mapVal = null;
        this.listVal = null;
    }

    public Value(String val) {
        this.type = Type.STRING;
        this.boolVal = null;
        this.numVal = null;
        this.strVal = val;
        this.funcVal = null;
        this.mapVal = null;
        this.listVal = null;
    }

    public Value(Node val) {
        this.type = Type.FUNCTION;
        this.boolVal = null;
        this.numVal = null;
        this.strVal = null;
        this.funcVal = val;
        this.mapVal = null;
        this.listVal = null;
    }

    public Value(Map<Value, Value> val) {
        this.type = Type.MAP;
        this.boolVal = null;
        this.numVal = null;
        this.strVal = null;
        this.funcVal = null;
        this.mapVal = val;
        this.listVal = null;
    }

    public Value(List<Value> val) {
        this.type = Type.LIST;
        this.boolVal = null;
        this.numVal = null;
        this.strVal = null;
        this.funcVal = null;
        this.mapVal = null;
        this.listVal = val;
    }

    public boolean asBool() {
        switch (type) {
            case BOOLEAN: return boolVal;
            case NUMBER: return numVal.compareTo(BigDecimal.ZERO) != 0;
            case FUNCTION: return true;
            case STRING: return !strVal.isEmpty();
            case LIST: return listVal.size() > 0;
            case MAP: return mapVal.size() > 0;
        }
        throw new IllegalStateException("Can't happen");
    }

    public String asString() {
        switch (type) {
            case BOOLEAN: return boolVal.toString();
            case NUMBER: return numVal.toString();
            case FUNCTION: return "func";
            case STRING: return strVal;
            case LIST: return "[" + listVal.stream().map(Value::asString).collect(joining(",")) + "]";
            case MAP: return "{" + mapVal.entrySet().stream().map(e -> e.getKey().asString() + ":" + e.getValue().asString()).collect(joining(",")) + "}";

        }
        throw new IllegalStateException("Can't happen");
    }

    public BigDecimal asNumber() {
        switch (type) {
            case BOOLEAN: return boolVal ? BigDecimal.ONE : BigDecimal.ZERO;
            case NUMBER: return numVal;
            case FUNCTION: return new BigDecimal(funcVal.hashCode());
            case STRING: try {
                return new BigDecimal(strVal);
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
            case LIST: throw new IllegalStateException("Can't convert List to Number");
            case MAP: throw new IllegalStateException("Can't convert Map to Number");
        }
        throw new IllegalStateException("Can't happen");
    }

    public List<Value> asList() {
        switch (type) {
            case BOOLEAN:
            case NUMBER:
            case FUNCTION:
            case STRING:
                return Lists.newArrayList(this);
            case LIST: return listVal;
            case MAP: return mapVal.values().stream().collect(Collectors.toList());

        }
        throw new IllegalStateException("Can't happen");
    }

    public Map<Value, Value> asMap() {
        Map<Value, Value> res = Maps.newHashMap();
        switch (type) {
            case BOOLEAN:
            case NUMBER:
            case FUNCTION:
            case STRING:
                res.put(new Value(BigDecimal.ZERO), this);
                break;
            case LIST:
                int i = 0;
                for (Value v : listVal) {
                    res.put( new Value(new BigDecimal(i)), v);
                }
                break;
            case MAP:
                res.putAll(mapVal);
                break;
            default:
                throw new IllegalStateException("Can't happen");
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Value{").append(type.toString()).append(":");
        switch (type) {
            case BOOLEAN:
                builder.append(boolVal.toString());
                break;
            case STRING:
                builder.append(strVal);
                break;
            case FUNCTION:
                builder.append(funcVal.toString());
                break;
            case NUMBER:
                builder.append(numVal.toString());
                break;
            case LIST:
                builder.append(listVal.toString());
                break;
            case MAP:
                builder.append(mapVal.toString());
                break;
        }
        return builder.toString();
    }

}
