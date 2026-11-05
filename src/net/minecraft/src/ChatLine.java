package net.minecraft.src;

public class ChatLine {
    public String message;
    public int updateCounter;

    public ChatLine(String text) {
        this.message = text;
        this.updateCounter = 0;
    }
}

