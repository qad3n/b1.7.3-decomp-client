package net.minecraft.src;

public final class J_JsonNodeSelector<T, U> {
    final J_Functor<T, U> valueGetter;

    J_JsonNodeSelector(J_Functor<T, U> valueGetter) {
        this.valueGetter = valueGetter;
    }

    public boolean matches(T jsonNode) {
        return this.valueGetter.matchesNode((T)jsonNode);
    }

    public U getValue(T argument) {
        return this.valueGetter.applyTo((T)argument);
    }

    public <V> J_JsonNodeSelector<T, V> with(J_JsonNodeSelector<U, V> childJsonNodeSelector) {
        return new J_JsonNodeSelector<>(new J_ChainedFunctor<>(this, childJsonNodeSelector));
    }

    String shortForm() {
        return this.valueGetter.shortForm();
    }

    @Override
    public String toString() {
        return this.valueGetter.toString();
    }
}
