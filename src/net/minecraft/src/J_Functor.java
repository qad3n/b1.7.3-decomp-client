package net.minecraft.src;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
interface J_Functor<T, V> {
    public boolean matchesNode(T var1);

    public V applyTo(T var1);

    public String shortForm();
}

