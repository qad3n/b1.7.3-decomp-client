package net.minecraft.src;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class J_JsonNodeSelectors {
    private J_JsonNodeSelectors() {
    }

    public static J_JsonNodeSelector<J_JsonNode, String> aStringNode(Object ... pathElements) {
        return J_JsonNodeSelectors.chainOn(pathElements, new J_JsonNodeSelector<J_JsonNode, String>(new J_LeafFunctor<J_JsonNode, String>(){

            @Override
            public boolean matchesNode(J_JsonNode j_JsonNode) {
                return EnumJsonNodeType.STRING == j_JsonNode.getType();
            }

            @Override
            public String shortForm() {
                return "A short form string";
            }

            @Override
            public String typeSafeApplyTo(J_JsonNode j_JsonNode) {
                return j_JsonNode.getText();
            }

            public String toString() {
                return "a value that is a string";
            }
        }));
    }

    public static J_JsonNodeSelector<J_JsonNode, List<J_JsonNode>> anArrayNode(Object ... pathElements) {
        return J_JsonNodeSelectors.chainOn(pathElements, new J_JsonNodeSelector<J_JsonNode, List<J_JsonNode>>(new J_LeafFunctor<J_JsonNode, List<J_JsonNode>>(){

            @Override
            public boolean matchesNode(J_JsonNode j_JsonNode) {
                return EnumJsonNodeType.ARRAY == j_JsonNode.getType();
            }

            @Override
            public String shortForm() {
                return "A short form array";
            }

            @Override
            public List<J_JsonNode> typeSafeApplyTo(J_JsonNode j_JsonNode) {
                return j_JsonNode.getElements();
            }

            public String toString() {
                return "an array";
            }
        }));
    }

    public static J_JsonNodeSelector<J_JsonNode, Map<J_JsonStringNode, J_JsonNode>> anObjectNode(Object ... pathElements) {
        return J_JsonNodeSelectors.chainOn(pathElements, new J_JsonNodeSelector<J_JsonNode, Map<J_JsonStringNode, J_JsonNode>>(new J_LeafFunctor<J_JsonNode, Map<J_JsonStringNode, J_JsonNode>>(){

            @Override
            public boolean matchesNode(J_JsonNode j_JsonNode) {
                return EnumJsonNodeType.OBJECT == j_JsonNode.getType();
            }

            @Override
            public String shortForm() {
                return "A short form object";
            }

            @Override
            public Map<J_JsonStringNode, J_JsonNode> typeSafeApplyTo(J_JsonNode j_JsonNode) {
                return j_JsonNode.getFields();
            }

            public String toString() {
                return "an object";
            }
        }));
    }

    public static J_JsonNodeSelector<Map<J_JsonStringNode, J_JsonNode>, J_JsonNode> aField(String fieldName) {
        return J_JsonNodeSelectors.aField(J_JsonNodeFactories.string(fieldName));
    }

    public static J_JsonNodeSelector<Map<J_JsonStringNode, J_JsonNode>, J_JsonNode> aField(final J_JsonStringNode fieldName) {
        return new J_JsonNodeSelector<Map<J_JsonStringNode, J_JsonNode>, J_JsonNode>(new J_LeafFunctor<Map<J_JsonStringNode, J_JsonNode>, J_JsonNode>(){

            @Override
            public boolean matchesNode(Map<J_JsonStringNode, J_JsonNode> map) {
                return map.containsKey(fieldName);
            }

            @Override
            public String shortForm() {
                return "\"" + fieldName.getText() + "\"";
            }

            @Override
            public J_JsonNode typeSafeApplyTo(Map<J_JsonStringNode, J_JsonNode> map) {
                return map.get(fieldName);
            }

            public String toString() {
                return "a field called [\"" + fieldName.getText() + "\"]";
            }
        });
    }

    public static J_JsonNodeSelector<J_JsonNode, J_JsonNode> anObjectNodeWithField(String fieldName) {
        return J_JsonNodeSelectors.anObjectNode(new Object[0]).with(J_JsonNodeSelectors.aField(fieldName));
    }

    public static J_JsonNodeSelector<List<J_JsonNode>, J_JsonNode> anElement(final int index) {
        return new J_JsonNodeSelector<List<J_JsonNode>, J_JsonNode>(new J_LeafFunctor<List<J_JsonNode>, J_JsonNode>(){

            @Override
            public boolean matchesNode(List<J_JsonNode> list) {
                return list.size() > index;
            }

            @Override
            public String shortForm() {
                return Integer.toString(index);
            }

            @Override
            public J_JsonNode typeSafeApplyTo(List<J_JsonNode> list) {
                return list.get(index);
            }

            public String toString() {
                return "an element at index [" + index + "]";
            }
        });
    }

    public static J_JsonNodeSelector<J_JsonNode, J_JsonNode> anArrayNodeWithElement(int index) {
        return J_JsonNodeSelectors.anArrayNode(new Object[0]).with(J_JsonNodeSelectors.anElement(index));
    }

    private static <T> J_JsonNodeSelector<J_JsonNode, T> chainOn(Object[] pathElements, J_JsonNodeSelector<J_JsonNode, T> parentSelector) {
        J_JsonNodeSelector<J_JsonNode, T> j_JsonNodeSelector = parentSelector;
        for (int i = pathElements.length - 1; i >= 0; --i) {
            if (pathElements[i] instanceof Integer) {
                j_JsonNodeSelector = J_JsonNodeSelectors.chainedJsonNodeSelector(J_JsonNodeSelectors.anArrayNodeWithElement((Integer)pathElements[i]), j_JsonNodeSelector);
                continue;
            }
            if (pathElements[i] instanceof String) {
                j_JsonNodeSelector = J_JsonNodeSelectors.chainedJsonNodeSelector(J_JsonNodeSelectors.anObjectNodeWithField((String)pathElements[i]), j_JsonNodeSelector);
                continue;
            }
            throw new IllegalArgumentException("Element [" + pathElements[i] + "] of path elements" + " [" + Arrays.toString(pathElements) + "] was of illegal type [" + pathElements[i].getClass().getCanonicalName() + "]; only Integer and String are valid.");
        }
        return j_JsonNodeSelector;
    }

    private static <T, U, V> J_JsonNodeSelector<T, V> chainedJsonNodeSelector(J_JsonNodeSelector<T, U> parent, J_JsonNodeSelector<U, V> child) {
        return new J_JsonNodeSelector(new J_ChainedFunctor<T, U, V>(parent, child));
    }
}

