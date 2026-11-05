package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class J_JsonArray
extends J_JsonRootNode {
    private final List<J_JsonNode> elements;

    J_JsonArray(Iterable<J_JsonNode> elements) {
        this.elements = J_JsonArray.asList(elements);
    }

    @Override
    public EnumJsonNodeType getType() {
        return EnumJsonNodeType.ARRAY;
    }

    @Override
    public List<J_JsonNode> getElements() {
        return new ArrayList<J_JsonNode>(this.elements);
    }

    @Override
    public String getText() {
        throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
    }

    @Override
    public Map<J_JsonStringNode, J_JsonNode> getFields() {
        throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        J_JsonArray j_JsonArray = (J_JsonArray)object;
        return ((Object)this.elements).equals(j_JsonArray.elements);
    }

    public int hashCode() {
        return ((Object)this.elements).hashCode();
    }

    public String toString() {
        return "JsonArray elements:[" + this.elements + "]";
    }

    private static List<J_JsonNode> asList(final Iterable<J_JsonNode> elements) {
        return new ArrayList<J_JsonNode>(){
            {
                for (J_JsonNode j_JsonNode : elements) {
                    this.add(j_JsonNode);
                }
            }
        };
    }
}

