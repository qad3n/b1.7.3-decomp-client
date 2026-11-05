package net.minecraft.src;


public final class J_JsonNodeDoesNotMatchPathElementsException
extends J_JsonNodeDoesNotMatchJsonNodeSelectorException {
    private static final J_JsonFormatter JSON_FORMATTER = new J_CompactJsonFormatter();

    static J_JsonNodeDoesNotMatchPathElementsException jsonNodeDoesNotMatchPathElementsException(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException delegate, Object[] pathElements, J_JsonRootNode rootNode) {
        return new J_JsonNodeDoesNotMatchPathElementsException(delegate, pathElements, rootNode);
    }

    private J_JsonNodeDoesNotMatchPathElementsException(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException delegate, Object[] pathElements, J_JsonRootNode rootNode) {
        super(J_JsonNodeDoesNotMatchPathElementsException.formatMessage(delegate, pathElements, rootNode));
    }

    private static String formatMessage(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException delegate, Object[] pathElements, J_JsonRootNode rootNode) {
        return "Failed to find " + delegate.failedNode.toString() + " at [" + J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.getShortFormFailPath(delegate.failPath) + "] while resolving [" + J_JsonNodeDoesNotMatchPathElementsException.commaSeparate(pathElements) + "] in " + JSON_FORMATTER.format(rootNode) + ".";
    }

    private static String commaSeparate(Object[] pathElements) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = true;
        for (Object object : pathElements) {
            if (!bl) {
                stringBuilder.append(".");
            }
            bl = false;
            if (object instanceof String) {
                stringBuilder.append("\"").append(object).append("\"");
                continue;
            }
            stringBuilder.append(object);
        }
        return stringBuilder.toString();
    }
}

