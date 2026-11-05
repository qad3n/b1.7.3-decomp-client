package com.jcraft.jogg;

public class Buffer {
    private static final int BUFFER_INCREMENT = 256;
    private static final int[] mask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, Short.MAX_VALUE, 65535, 131071, 262143, 524287, 1048575, 0x1FFFFF, 0x3FFFFF, 0x7FFFFF, 0xFFFFFF, 0x1FFFFFF, 0x3FFFFFF, 0x7FFFFFF, 0xFFFFFFF, 0x1FFFFFFF, 0x3FFFFFFF, Integer.MAX_VALUE, -1};
    int ptr = 0;
    byte[] buffer = null;
    int endbit = 0;
    int endbyte = 0;
    int storage = 0;

    public void writeinit() {
        this.buffer = new byte[256];
        this.ptr = 0;
        this.buffer[0] = 0;
        this.storage = 256;
    }

    public void write(byte[] byArray) {
        for (int i = 0; i < byArray.length && byArray[i] != 0; ++i) {
            this.write(byArray[i], 8);
        }
    }

    public void read(byte[] byArray, int n) {
        int n2 = 0;
        while (n-- != 0) {
            byArray[n2++] = (byte)this.read(8);
        }
    }

    void reset() {
        this.ptr = 0;
        this.buffer[0] = 0;
        this.endbyte = 0;
        this.endbit = 0;
    }

    public void writeclear() {
        this.buffer = null;
    }

    public void readinit(byte[] byArray, int n) {
        this.readinit(byArray, 0, n);
    }

    public void readinit(byte[] byArray, int n, int n2) {
        this.ptr = n;
        this.buffer = byArray;
        this.endbyte = 0;
        this.endbit = 0;
        this.storage = n2;
    }

    public void write(int n, int n2) {
        if (this.endbyte + 4 >= this.storage) {
            byte[] byArray = new byte[this.storage + 256];
            System.arraycopy(this.buffer, 0, byArray, 0, this.storage);
            this.buffer = byArray;
            this.storage += 256;
        }
        n &= mask[n2];
        int n3 = this.ptr;
        this.buffer[n3] = (byte)(this.buffer[n3] | (byte)(n << this.endbit));
        if ((n2 += this.endbit) >= 8) {
            this.buffer[this.ptr + 1] = (byte)(n >>> 8 - this.endbit);
            if (n2 >= 16) {
                this.buffer[this.ptr + 2] = (byte)(n >>> 16 - this.endbit);
                if (n2 >= 24) {
                    this.buffer[this.ptr + 3] = (byte)(n >>> 24 - this.endbit);
                    if (n2 >= 32) {
                        this.buffer[this.ptr + 4] = this.endbit > 0 ? (byte)(n >>> 32 - this.endbit) : (byte)0;
                    }
                }
            }
        }
        this.endbyte += n2 / 8;
        this.ptr += n2 / 8;
        this.endbit = n2 & 7;
    }

    public int look(int n) {
        int n2 = mask[n];
        n += this.endbit;
        if (this.endbyte + 4 >= this.storage && this.endbyte + (n - 1) / 8 >= this.storage) {
            return -1;
        }
        int n3 = (this.buffer[this.ptr] & 0xFF) >>> this.endbit;
        if (n > 8) {
            n3 |= (this.buffer[this.ptr + 1] & 0xFF) << 8 - this.endbit;
            if (n > 16) {
                n3 |= (this.buffer[this.ptr + 2] & 0xFF) << 16 - this.endbit;
                if (n > 24) {
                    n3 |= (this.buffer[this.ptr + 3] & 0xFF) << 24 - this.endbit;
                    if (n > 32 && this.endbit != 0) {
                        n3 |= (this.buffer[this.ptr + 4] & 0xFF) << 32 - this.endbit;
                    }
                }
            }
        }
        return n2 & n3;
    }

    public int look1() {
        if (this.endbyte >= this.storage) {
            return -1;
        }
        return this.buffer[this.ptr] >> this.endbit & 1;
    }

    public void adv(int n) {
        this.ptr += (n += this.endbit) / 8;
        this.endbyte += n / 8;
        this.endbit = n & 7;
    }

    public void adv1() {
        ++this.endbit;
        if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
        }
    }

    public int read(int n) {
        int n2;
        int n3 = mask[n];
        n += this.endbit;
        if (this.endbyte + 4 >= this.storage) {
            n2 = -1;
            if (this.endbyte + (n - 1) / 8 >= this.storage) {
                this.ptr += n / 8;
                this.endbyte += n / 8;
                this.endbit = n & 7;
                return n2;
            }
        }
        n2 = (this.buffer[this.ptr] & 0xFF) >>> this.endbit;
        if (n > 8) {
            n2 |= (this.buffer[this.ptr + 1] & 0xFF) << 8 - this.endbit;
            if (n > 16) {
                n2 |= (this.buffer[this.ptr + 2] & 0xFF) << 16 - this.endbit;
                if (n > 24) {
                    n2 |= (this.buffer[this.ptr + 3] & 0xFF) << 24 - this.endbit;
                    if (n > 32 && this.endbit != 0) {
                        n2 |= (this.buffer[this.ptr + 4] & 0xFF) << 32 - this.endbit;
                    }
                }
            }
        }
        this.ptr += n / 8;
        this.endbyte += n / 8;
        this.endbit = n & 7;
        return n2 &= n3;
    }

    public int readB(int n) {
        int n2;
        int n3 = 32 - n;
        n += this.endbit;
        if (this.endbyte + 4 >= this.storage) {
            n2 = -1;
            if (this.endbyte * 8 + n > this.storage * 8) {
                this.ptr += n / 8;
                this.endbyte += n / 8;
                this.endbit = n & 7;
                return n2;
            }
        }
        n2 = (this.buffer[this.ptr] & 0xFF) << 24 + this.endbit;
        if (n > 8) {
            n2 |= (this.buffer[this.ptr + 1] & 0xFF) << 16 + this.endbit;
            if (n > 16) {
                n2 |= (this.buffer[this.ptr + 2] & 0xFF) << 8 + this.endbit;
                if (n > 24) {
                    n2 |= (this.buffer[this.ptr + 3] & 0xFF) << this.endbit;
                    if (n > 32 && this.endbit != 0) {
                        n2 |= (this.buffer[this.ptr + 4] & 0xFF) >> 8 - this.endbit;
                    }
                }
            }
        }
        n2 = n2 >>> (n3 >> 1) >>> (n3 + 1 >> 1);
        this.ptr += n / 8;
        this.endbyte += n / 8;
        this.endbit = n & 7;
        return n2;
    }

    public int read1() {
        if (this.endbyte >= this.storage) {
            int n = -1;
            ++this.endbit;
            if (this.endbit > 7) {
                this.endbit = 0;
                ++this.ptr;
                ++this.endbyte;
            }
            return n;
        }
        int n = this.buffer[this.ptr] >> this.endbit & 1;
        ++this.endbit;
        if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
        }
        return n;
    }

    public int bytes() {
        return this.endbyte + (this.endbit + 7) / 8;
    }

    public int bits() {
        return this.endbyte * 8 + this.endbit;
    }

    public byte[] buffer() {
        return this.buffer;
    }

    public static int ilog(int n) {
        int n2 = 0;
        while (n > 0) {
            ++n2;
            n >>>= 1;
        }
        return n2;
    }

    public static void report(String string) {
        System.err.println(string);
        System.exit(1);
    }
}

