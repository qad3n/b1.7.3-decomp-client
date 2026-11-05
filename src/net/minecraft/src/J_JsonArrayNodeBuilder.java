package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

public final class J_JsonArrayNodeBuilder
implements J_JsonNodeBuilder {
    private final List<J_JsonNodeBuilder> elementBuilders = new LinkedList<J_JsonNodeBuilder>();

    J_JsonArrayNodeBuilder() {
    }

    public J_JsonArrayNodeBuilder withElement(J_JsonNodeBuilder elementBuilder) {
        this.elementBuilders.add(elementBuilder);
        return this;
    }

    public J_JsonRootNode build() {
        LinkedList<J_JsonNode> linkedList = new LinkedList<J_JsonNode>();
        for (J_JsonNodeBuilder j_JsonNodeBuilder : this.elementBuilders) {
            linkedList.add(j_JsonNodeBuilder.build());
        }
        return J_JsonNodeFactories.array(linkedList);
    }
}

