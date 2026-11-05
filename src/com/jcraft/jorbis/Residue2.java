package com.jcraft.jorbis;


class Residue2
extends Residue0 {
    Residue2() {
    }

    int inverse(Block block, Object object, float[][] fArray, int[] nArray, int n) {
        int n2 = 0;
        for (n2 = 0; n2 < n && nArray[n2] == 0; ++n2) {
        }
        if (n2 == n) {
            return 0;
        }
        return Residue2._2inverse(block, object, fArray, n);
    }
}

