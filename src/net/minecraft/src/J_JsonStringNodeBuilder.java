package net.minecraft.src;


public final class J_JsonStringNodeBuilder
implements J_JsonNodeBuilder {
    private final String value;

    J_JsonStringNodeBuilder(String value) {
        this.value = value;
    }

    public J_JsonStringNode build() {
        return J_JsonNodeFactories.string(this.value);
    }
}

