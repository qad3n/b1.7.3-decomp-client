package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import paulscode.sound.codecs.CodecJOrbis;

public class CodecMus
extends CodecJOrbis {
    protected InputStream openInputStream() throws IOException {
        return new MusInputStream(this.url, this.urlConnection.getInputStream());
    }

    class MusInputStream
    extends InputStream {
        private int hash;
        private InputStream inputStream;
        byte[] buffer = new byte[1];

        public MusInputStream(URL fileURL, InputStream inputStream) {
            this.inputStream = inputStream;
            String string = fileURL.getPath();
            string = string.substring(string.lastIndexOf("/") + 1);
            this.hash = string.hashCode();
        }

        public int read() throws IOException {
            int n = this.read(this.buffer, 0, 1);
            if (n < 0) {
                return n;
            }
            return this.buffer[0];
        }

        public int read(byte[] byArray, int n, int n2) throws IOException {
            n2 = this.inputStream.read(byArray, n, n2);
            for (int i = 0; i < n2; ++i) {
                int n3 = n + i;
                byte by = (byte)(byArray[n3] ^ this.hash >> 8);
                byArray[n3] = by;
                byte by2 = by;
                this.hash = this.hash * 498729871 + 85731 * by2;
            }
            return n2;
        }
    }
}

