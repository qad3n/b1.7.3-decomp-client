package net.minecraft.src;

import java.util.List;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class J_JsonNode {
    J_JsonNode() {
    }

    public abstract EnumJsonNodeType getType();

    public abstract String getText();

    public abstract Map<J_JsonStringNode, J_JsonNode> getFields();

    public abstract List<J_JsonNode> getElements();

    public final String getStringValue(Object ... pathElements) {
        return this.wrapExceptionsFor(J_JsonNodeSelectors.aStringNode(pathElements), this, pathElements);
    }

    public final List<J_JsonNode> getArrayNode(Object ... pathElements) {
        return this.wrapExceptionsFor(J_JsonNodeSelectors.anArrayNode(pathElements), this, pathElements);
    }

    private <T, V extends J_JsonNode> T wrapExceptionsFor(J_JsonNodeSelector<V, T> value, V node, Object[] pathElements) throws J_JsonNodeDoesNotMatchPathElementsException {
        try {
            return value.getValue(node);
        }
        catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException) {
            throw J_JsonNodeDoesNotMatchPathElementsException.jsonNodeDoesNotMatchPathElementsException(j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException, pathElements, J_JsonNodeFactories.array(node));
        }
    }
}

