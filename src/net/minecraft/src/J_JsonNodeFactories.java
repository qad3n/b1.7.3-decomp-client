package net.minecraft.src;

import java.util.Arrays;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class J_JsonNodeFactories {
    private J_JsonNodeFactories() {
    }

    public static J_JsonNode nullNode() {
        return J_JsonConstants.NULL;
    }

    public static J_JsonNode trueNode() {
        return J_JsonConstants.TRUE;
    }

    public static J_JsonNode falseNode() {
        return J_JsonConstants.FALSE;
    }

    public static J_JsonStringNode string(String value) {
        return new J_JsonStringNode(value);
    }

    public static J_JsonNode number(String value) {
        return new J_JsonNumberNode(value);
    }

    public static J_JsonRootNode array(Iterable<J_JsonNode> elements) {
        return new J_JsonArray(elements);
    }

    public static J_JsonRootNode array(J_JsonNode ... elements) {
        return J_JsonNodeFactories.array(Arrays.asList(elements));
    }

    public static J_JsonRootNode object(Map<J_JsonStringNode, J_JsonNode> fields) {
        return new J_JsonObject(fields);
    }
}

