package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException
extends J_JsonNodeDoesNotMatchJsonNodeSelectorException {
    final J_Functor failedNode;
    final List<J_JsonNodeSelector> failPath;

    static J_JsonNodeDoesNotMatchJsonNodeSelectorException createJsonNodeDoesNotMatchJsonNodeSelectorException(J_Functor failedNode) {
        return new J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(failedNode, new LinkedList<J_JsonNodeSelector>());
    }

    static J_JsonNodeDoesNotMatchJsonNodeSelectorException createChainedJsonNodeDoesNotMatchJsonNodeSelectorException(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException e, J_JsonNodeSelector parentJsonNodeSelector) {
        LinkedList<J_JsonNodeSelector> linkedList = new LinkedList<J_JsonNodeSelector>(e.failPath);
        linkedList.add(parentJsonNodeSelector);
        return new J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(e.failedNode, linkedList);
    }

    static J_JsonNodeDoesNotMatchJsonNodeSelectorException createUnchainedJsonNodeDoesNotMatchJsonNodeSelectorException(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException e, J_JsonNodeSelector parentJsonNodeSelector) {
        LinkedList<J_JsonNodeSelector> linkedList = new LinkedList<J_JsonNodeSelector>();
        linkedList.add(parentJsonNodeSelector);
        return new J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(e.failedNode, linkedList);
    }

    private J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(J_Functor failedNode, List<J_JsonNodeSelector> failPath) {
        super("Failed to match any JSON node at [" + J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.getShortFormFailPath(failPath) + "]");
        this.failedNode = failedNode;
        this.failPath = failPath;
    }

    static String getShortFormFailPath(List<J_JsonNodeSelector> failPath) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = failPath.size() - 1; i >= 0; --i) {
            stringBuilder.append(failPath.get(i).shortForm());
            if (i == 0) continue;
            stringBuilder.append(".");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "JsonNodeDoesNotMatchJsonNodeSelectorException{failedNode=" + this.failedNode + ", failPath=" + this.failPath + '}';
    }
}

