package net.minecraft.src;


public final class J_InvalidSyntaxException
extends Exception {
    private final int column;
    private final int row;

    J_InvalidSyntaxException(String s, J_ThingWithPosition thingWithPosition) {
        super("At line " + thingWithPosition.getRow() + ", column " + thingWithPosition.getColumn() + ":  " + s);
        this.column = thingWithPosition.getColumn();
        this.row = thingWithPosition.getRow();
    }

    J_InvalidSyntaxException(String s, Throwable throwable, J_ThingWithPosition thingWithPosition) {
        super("At line " + thingWithPosition.getRow() + ", column " + thingWithPosition.getColumn() + ":  " + s, throwable);
        this.column = thingWithPosition.getColumn();
        this.row = thingWithPosition.getRow();
    }
}

