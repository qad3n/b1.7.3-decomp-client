package net.minecraft.src;


public final class J_JsonNodeBuilders {
    private J_JsonNodeBuilders() {
    }

    public static J_JsonNodeBuilder aNullBuilder() {
        return new J_JsonNodeBuilder(){

            public J_JsonNode build() {
                return J_JsonNodeFactories.nullNode();
            }
        };
    }

    public static J_JsonNodeBuilder aTrueBuilder() {
        return new J_JsonNodeBuilder(){

            public J_JsonNode build() {
                return J_JsonNodeFactories.trueNode();
            }
        };
    }

    public static J_JsonNodeBuilder aFalseBuilder() {
        return new J_JsonNodeBuilder(){

            public J_JsonNode build() {
                return J_JsonNodeFactories.falseNode();
            }
        };
    }

    public static J_JsonNodeBuilder aNumberBuilder(String value) {
        return new J_JsonNumberNodeBuilder(value);
    }

    public static J_JsonStringNodeBuilder aStringBuilder(String value) {
        return new J_JsonStringNodeBuilder(value);
    }

    public static J_JsonObjectNodeBuilder anObjectBuilder() {
        return new J_JsonObjectNodeBuilder();
    }

    public static J_JsonArrayNodeBuilder anArrayBuilder() {
        return new J_JsonArrayNodeBuilder();
    }
}

