package net.minecraft.src;


final class J_JsonFieldBuilder {
    private J_JsonNodeBuilder keyBuilder;
    private J_JsonNodeBuilder valueBuilder;

    private J_JsonFieldBuilder() {
    }

    static J_JsonFieldBuilder aJsonFieldBuilder() {
        return new J_JsonFieldBuilder();
    }

    J_JsonFieldBuilder withKey(J_JsonNodeBuilder keyBuilder) {
        this.keyBuilder = keyBuilder;
        return this;
    }

    J_JsonFieldBuilder withValue(J_JsonNodeBuilder valueBuilder) {
        this.valueBuilder = valueBuilder;
        return this;
    }

    J_JsonStringNode buildKey() {
        return (J_JsonStringNode)this.keyBuilder.build();
    }

    J_JsonNode buildValue() {
        return this.valueBuilder.build();
    }
}

