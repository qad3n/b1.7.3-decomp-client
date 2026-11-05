package net.minecraft.src;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class J_JsonObjectNodeBuilder
implements J_JsonNodeBuilder {
    private final List<J_JsonFieldBuilder> fieldBuilders = new LinkedList<J_JsonFieldBuilder>();

    J_JsonObjectNodeBuilder() {
    }

    public J_JsonObjectNodeBuilder withField(J_JsonFieldBuilder fieldBuilder) {
        this.fieldBuilders.add(fieldBuilder);
        return this;
    }

    public J_JsonRootNode build() {
        return J_JsonNodeFactories.object((Map<J_JsonStringNode, J_JsonNode>)new HashMap<J_JsonStringNode, J_JsonNode>(){
            {
                for (J_JsonFieldBuilder j_JsonFieldBuilder : J_JsonObjectNodeBuilder.this.fieldBuilders) {
                    this.put(j_JsonFieldBuilder.buildKey(), j_JsonFieldBuilder.buildValue());
                }
            }
        });
    }
}

