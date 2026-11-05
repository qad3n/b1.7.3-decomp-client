package net.minecraft.src;


final class J_JsonNumberNodeBuilder
implements J_JsonNodeBuilder {
    private final J_JsonNode value;

    J_JsonNumberNodeBuilder(String value) {
        this.value = J_JsonNodeFactories.number(value);
    }

    public J_JsonNode build() {
        return this.value;
    }
}

