package com.jcraft.jorbis;

class Util {
    Util() {
    }

    static int ilog(int n) {
        int n2 = 0;
        while (n != 0) {
            ++n2;
            n >>>= 1;
        }
        return n2;
    }

    static int ilog2(int n) {
        int n2 = 0;
        while (n > 1) {
            ++n2;
            n >>>= 1;
        }
        return n2;
    }

    static int icount(int n) {
        int n2 = 0;
        while (n != 0) {
            n2 += n & 1;
            n >>>= 1;
        }
        return n2;
    }
}

