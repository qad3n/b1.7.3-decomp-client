package com.jcraft.jorbis;


class Lpc {
    Drft fft = new Drft();
    int ln;
    int m;

    Lpc() {
    }

    static float lpc_from_data(float[] fArray, float[] fArray2, int n, int n2) {
        int n3;
        float f;
        float[] fArray3 = new float[n2 + 1];
        int n4 = n2 + 1;
        while (n4-- != 0) {
            f = 0.0f;
            for (n3 = n4; n3 < n; ++n3) {
                f += fArray[n3] * fArray[n3 - n4];
            }
            fArray3[n4] = f;
        }
        float f2 = fArray3[0];
        for (n3 = 0; n3 < n2; ++n3) {
            f = -fArray3[n3 + 1];
            if (f2 == 0.0f) {
                for (int i = 0; i < n2; ++i) {
                    fArray2[i] = 0.0f;
                }
                return 0.0f;
            }
            for (n4 = 0; n4 < n3; ++n4) {
                f -= fArray2[n4] * fArray3[n3 - n4];
            }
            fArray2[n3] = f /= f2;
            for (n4 = 0; n4 < n3 / 2; ++n4) {
                float f3 = fArray2[n4];
                int n5 = n4;
                fArray2[n5] = fArray2[n5] + f * fArray2[n3 - 1 - n4];
                int n6 = n3 - 1 - n4;
                fArray2[n6] = fArray2[n6] + f * f3;
            }
            if (n3 % 2 != 0) {
                int n7 = n4;
                fArray2[n7] = fArray2[n7] + fArray2[n4] * f;
            }
            f2 = (float)((double)f2 * (1.0 - (double)(f * f)));
        }
        return f2;
    }

    float lpc_from_curve(float[] fArray, float[] fArray2) {
        int n;
        int n2 = this.ln;
        float[] fArray3 = new float[n2 + n2];
        float f = (float)(0.5 / (double)n2);
        for (n = 0; n < n2; ++n) {
            fArray3[n * 2] = fArray[n] * f;
            fArray3[n * 2 + 1] = 0.0f;
        }
        fArray3[n2 * 2 - 1] = fArray[n2 - 1] * f;
        this.fft.backward(fArray3);
        n = 0;
        int n3 = (n2 *= 2) / 2;
        while (n < n2 / 2) {
            float f2 = fArray3[n];
            fArray3[n++] = fArray3[n3];
            fArray3[n3++] = f2;
        }
        return Lpc.lpc_from_data(fArray3, fArray2, n2, this.m);
    }

    void init(int n, int n2) {
        this.ln = n;
        this.m = n2;
        this.fft.init(n * 2);
    }

    void clear() {
        this.fft.clear();
    }

    static float FAST_HYPOT(float f, float f2) {
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    void lpc_to_curve(float[] fArray, float[] fArray2, float f) {
        int n;
        for (n = 0; n < this.ln * 2; ++n) {
            fArray[n] = 0.0f;
        }
        if (f == 0.0f) {
            return;
        }
        for (n = 0; n < this.m; ++n) {
            fArray[n * 2 + 1] = fArray2[n] / (4.0f * f);
            fArray[n * 2 + 2] = -fArray2[n] / (4.0f * f);
        }
        this.fft.backward(fArray);
        n = this.ln * 2;
        float f2 = (float)(1.0 / (double)f);
        fArray[0] = (float)(1.0 / (double)(fArray[0] * 2.0f + f2));
        for (int i = 1; i < this.ln; ++i) {
            float f3 = fArray[i] + fArray[n - i];
            float f4 = fArray[i] - fArray[n - i];
            float f5 = f3 + f2;
            fArray[i] = (float)(1.0 / (double)Lpc.FAST_HYPOT(f5, f4));
        }
    }
}

