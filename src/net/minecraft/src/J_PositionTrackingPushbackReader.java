package net.minecraft.src;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

final class J_PositionTrackingPushbackReader
implements J_ThingWithPosition {
    private final PushbackReader pushbackReader;
    private int characterCount = 0;
    private int lineCount = 1;
    private boolean lastCharacterWasCariageReturn = false;

    public J_PositionTrackingPushbackReader(Reader reader) {
        this.pushbackReader = new PushbackReader(reader);
    }

    public void unread(char c) throws IOException {
        --this.characterCount;
        if (this.characterCount < 0) {
            this.characterCount = 0;
        }
        this.pushbackReader.unread(c);
    }

    public void uncount(char[] resultCharArray) {
        this.characterCount -= resultCharArray.length;
        if (this.characterCount < 0) {
            this.characterCount = 0;
        }
    }

    public int read() throws IOException {
        int n = this.pushbackReader.read();
        this.updateCharacterAndLineCounts(n);
        return n;
    }

    public int read(char[] buffer) throws IOException {
        int n = this.pushbackReader.read(buffer);
        for (char c : buffer) {
            this.updateCharacterAndLineCounts(c);
        }
        return n;
    }

    private void updateCharacterAndLineCounts(int result) {
        if (13 == result) {
            this.characterCount = 0;
            ++this.lineCount;
            this.lastCharacterWasCariageReturn = true;
        } else {
            if (10 == result && !this.lastCharacterWasCariageReturn) {
                this.characterCount = 0;
                ++this.lineCount;
            } else {
                ++this.characterCount;
            }
            this.lastCharacterWasCariageReturn = false;
        }
    }

    public int getColumn() {
        return this.characterCount;
    }

    public int getRow() {
        return this.lineCount;
    }
}

