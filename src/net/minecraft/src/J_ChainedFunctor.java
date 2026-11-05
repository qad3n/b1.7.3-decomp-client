package net.minecraft.src;


/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class J_ChainedFunctor<T, U, V>
implements J_Functor<T, V> {
    private final J_JsonNodeSelector<T, U> parentJsonNodeSelector;
    private final J_JsonNodeSelector<U, V> childJsonNodeSelector;

    J_ChainedFunctor(J_JsonNodeSelector<T, U> parentJsonNodeSelector, J_JsonNodeSelector<U, V> childJsonNodeSelector) {
        this.parentJsonNodeSelector = parentJsonNodeSelector;
        this.childJsonNodeSelector = childJsonNodeSelector;
    }

    @Override
    public boolean matchesNode(T jsonNode) {
        return this.parentJsonNodeSelector.matches(jsonNode) && this.childJsonNodeSelector.matches(this.parentJsonNodeSelector.getValue(jsonNode));
    }

    @Override
    public V applyTo(T jsonNode) {
        V v;
        U u;
        try {
            u = this.parentJsonNodeSelector.getValue(jsonNode);
        }
        catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException) {
            throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.createUnchainedJsonNodeDoesNotMatchJsonNodeSelectorException(j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException, this.parentJsonNodeSelector);
        }
        try {
            v = this.childJsonNodeSelector.getValue(u);
        }
        catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException) {
            throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.createChainedJsonNodeDoesNotMatchJsonNodeSelectorException(j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException, this.parentJsonNodeSelector);
        }
        return v;
    }

    @Override
    public String shortForm() {
        return this.childJsonNodeSelector.shortForm();
    }

    public String toString() {
        return this.parentJsonNodeSelector.toString() + ", with " + this.childJsonNodeSelector.toString();
    }
}

