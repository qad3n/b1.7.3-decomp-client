package net.minecraft.src;

public class UnexpectedThrowable {
    public final String description;
    public final Throwable exception;

    public UnexpectedThrowable(String description, Throwable exception) {
        this.description = description;
        this.exception = exception;
    }
}

