package net.minecraft.src;

import java.util.List;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class J_JsonStringNode
extends J_JsonNode
implements Comparable<J_JsonStringNode> {
    private final String value;

    J_JsonStringNode(String value) {
        if (value == null) {
            throw new NullPointerException("Attempt to construct a JsonString with a null value.");
        }
        this.value = value;
    }

    @Override
    public EnumJsonNodeType getType() {
        return EnumJsonNodeType.STRING;
    }

    @Override
    public String getText() {
        return this.value;
    }

    @Override
    public Map<J_JsonStringNode, J_JsonNode> getFields() {
        throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
    }

    @Override
    public List<J_JsonNode> getElements() {
        throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        J_JsonStringNode j_JsonStringNode = (J_JsonStringNode)object;
        return this.value.equals(j_JsonStringNode.value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return "JsonStringNode value:[" + this.value + "]";
    }

    @Override
    public int compareTo(J_JsonStringNode j_JsonStringNode) {
        return this.value.compareTo(j_JsonStringNode.value);
    }
}

