package net.minecraft.src;


/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
abstract class J_LeafFunctor<T, V>
implements J_Functor<T, V> {
    J_LeafFunctor() {
    }

    @Override
    public final V applyTo(T jsonNode) {
        if (!this.matchesNode(jsonNode)) {
            throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.createJsonNodeDoesNotMatchJsonNodeSelectorException(this);
        }
        return this.typeSafeApplyTo(jsonNode);
    }

    protected abstract V typeSafeApplyTo(T var1);
}

