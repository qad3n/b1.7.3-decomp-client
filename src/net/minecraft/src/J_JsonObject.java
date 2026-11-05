package net.minecraft.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class J_JsonObject
extends J_JsonRootNode {
    private final Map<J_JsonStringNode, J_JsonNode> fields;

    J_JsonObject(Map<J_JsonStringNode, J_JsonNode> fields) {
        this.fields = new HashMap<J_JsonStringNode, J_JsonNode>(fields);
    }

    @Override
    public Map<J_JsonStringNode, J_JsonNode> getFields() {
        return new HashMap<J_JsonStringNode, J_JsonNode>(this.fields);
    }

    @Override
    public EnumJsonNodeType getType() {
        return EnumJsonNodeType.OBJECT;
    }

    @Override
    public String getText() {
        throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
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
        J_JsonObject j_JsonObject = (J_JsonObject)object;
        return ((Object)this.fields).equals(j_JsonObject.fields);
    }

    public int hashCode() {
        return ((Object)this.fields).hashCode();
    }

    public String toString() {
        return "JsonObject fields:[" + this.fields + "]";
    }
}

