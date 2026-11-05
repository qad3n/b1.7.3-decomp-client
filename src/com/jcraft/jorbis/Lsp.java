package com.jcraft.jorbis;


class Lsp {
    static final float M_PI = (float)Math.PI;

    Lsp() {
    }

    static void lsp_to_curve(float[] fArray, int[] nArray, int n, int n2, float[] fArray2, int n3, float f, float f2) {
        int n4;
        float f3 = (float)Math.PI / (float)n2;
        for (n4 = 0; n4 < n3; ++n4) {
            fArray2[n4] = Lookup.coslook(fArray2[n4]);
        }
        int n5 = n3 / 2 * 2;
        n4 = 0;
        while (n4 < n) {
            int n6;
            int n7 = nArray[n4];
            float f4 = 0.70710677f;
            float f5 = 0.70710677f;
            float f6 = Lookup.coslook(f3 * (float)n7);
            for (n6 = 0; n6 < n5; n6 += 2) {
                f5 *= fArray2[n6] - f6;
                f4 *= fArray2[n6 + 1] - f6;
            }
            if ((n3 & 1) != 0) {
                f5 *= fArray2[n3 - 1] - f6;
                f5 *= f5;
                f4 *= f4 * (1.0f - f6 * f6);
            } else {
                f5 *= f5 * (1.0f + f6);
                f4 *= f4 * (1.0f - f6);
            }
            f5 = f4 + f5;
            n6 = Float.floatToIntBits(f5);
            int n8 = Integer.MAX_VALUE & n6;
            int n9 = 0;
            if (n8 < 2139095040 && n8 != 0) {
                if (n8 < 0x800000) {
                    f5 = (float)((double)f5 * 3.3554432E7);
                    n6 = Float.floatToIntBits(f5);
                    n8 = Integer.MAX_VALUE & n6;
                    n9 = -25;
                }
                n9 += (n8 >>> 23) - 126;
                n6 = n6 & 0x807FFFFF | 0x3F000000;
                f5 = Float.intBitsToFloat(n6);
            }
            f5 = Lookup.fromdBlook(f * Lookup.invsqlook(f5) * Lookup.invsq2explook(n9 + n3) - f2);
            do {
                fArray[n4++] *= f5;
            } while (n4 < n && nArray[n4] == n7);
        }
    }
}

