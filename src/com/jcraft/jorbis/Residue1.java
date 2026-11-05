package com.jcraft.jorbis;


class Residue1
extends Residue0 {
    Residue1() {
    }

    int inverse(Block block, Object object, float[][] fArray, int[] nArray, int n) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (nArray[i] == 0) continue;
            fArray[n2++] = fArray[i];
        }
        if (n2 != 0) {
            return Residue1._01inverse(block, object, fArray, n2, 1);
        }
        return 0;
    }
}

