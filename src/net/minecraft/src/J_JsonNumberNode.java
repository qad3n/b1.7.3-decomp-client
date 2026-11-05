package net.minecraft.src;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class J_JsonNumberNode
extends J_JsonNode {
    private static final Pattern PATTERN = Pattern.compile("(-?)(0|([1-9]([0-9]*)))(\\.[0-9]+)?((e|E)(\\+|-)?[0-9]+)?");
    private final String value;

    J_JsonNumberNode(String value) {
        if (value == null) {
            throw new NullPointerException("Attempt to construct a JsonNumber with a null value.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Attempt to construct a JsonNumber with a String [" + value + "] that does not match the JSON number specification.");
        }
        this.value = value;
    }

    @Override
    public EnumJsonNodeType getType() {
        return EnumJsonNodeType.NUMBER;
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
        J_JsonNumberNode j_JsonNumberNode = (J_JsonNumberNode)object;
        return this.value.equals(j_JsonNumberNode.value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return "JsonNumberNode value:[" + this.value + "]";
    }
}

