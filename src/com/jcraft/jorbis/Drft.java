package com.jcraft.jorbis;

class Drft {
    int n;
    float[] trigcache;
    int[] splitcache;
    static int[] ntryh = new int[]{4, 2, 3, 5};
    static float tpi = (float) (Math.PI * 2);
    static float hsqt2 = 0.70710677F;
    static float taui = 0.8660254F;
    static float taur = -0.5F;
    static float sqrt2 = 1.4142135F;

    void backward(float[] var1) {
        if (this.n != 1) {
            drftb1(this.n, var1, this.trigcache, this.trigcache, this.n, this.splitcache);
        }
    }

    void init(int var1) {
        this.n = var1;
        this.trigcache = new float[3 * var1];
        this.splitcache = new int[32];
        fdrffti(var1, this.trigcache, this.splitcache);
    }

    void clear() {
        if (this.trigcache != null) {
            this.trigcache = null;
        }

        if (this.splitcache != null) {
            this.splitcache = null;
        }
    }

    static void drfti1(int var0, float[] var1, int var2, int[] var3) {
        int i = 0;
        int k = -1;
        int l3 = var0;
        int i4 = 0;
        byte b1 = 101;

        while (true) {
            switch (b1) {
                case 101:
                    if (++k < 4) {
                        i = ntryh[k];
                    } else {
                        i += 2;
                    }
                case 104:
                    int k2 = l3 / i;
                    int l2 = l3 - i * k2;
                    if (l2 != 0) {
                        b1 = 101;
                        break;
                    } else {
                        var3[++i4 + 1] = i;
                        l3 = k2;
                        if (i != 2) {
                            b1 = 107;
                            break;
                        } else if (i4 == 1) {
                            b1 = 107;
                            break;
                        } else {
                            for (int j = 1; j < i4; j++) {
                                int k1 = i4 - j + 1;
                                var3[k1 + 1] = var3[k1];
                            }

                            var3[2] = 2;
                        }
                    }
                case 107:
                    if (l3 == 1) {
                        var3[0] = var0;
                        var3[1] = i4;
                        float f1 = tpi / var0;
                        int j2 = 0;
                        int k3 = i4 - 1;
                        int i1 = 1;
                        if (k3 == 0) {
                            return;
                        }

                        for (int l = 0; l < k3; l++) {
                            int i2 = var3[l + 2];
                            int l1 = 0;
                            int j1 = i1 * i2;
                            int i3 = var0 / j1;
                            int j3 = i2 - 1;

                            for (int k4 = 0; k4 < j3; k4++) {
                                l1 += i1;
                                int j4 = j2;
                                float f2 = l1 * f1;
                                float f3 = 0.0F;

                                for (int b0 = 2; b0 < i3; b0 += 2) {
                                    float f = ++f3 * f2;
                                    var1[var2 + j4++] = (float)Math.cos(f);
                                    var1[var2 + j4++] = (float)Math.sin(f);
                                }

                                j2 += i3;
                            }

                            i1 = j1;
                        }

                        return;
                    }

                    b1 = 104;
            }
        }
    }

    static void fdrffti(int var0, float[] var1, int[] var2) {
        if (var0 != 1) {
            drfti1(var0, var1, var0, var2);
        }
    }

    static void dradf2(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5) {
        int k = 0;
        int l;
        int j = l = var1 * var0;
        int i1 = var0 << 1;

        for (int i = 0; i < var1; i++) {
            var3[k << 1] = var2[k] + var2[l];
            var3[(k << 1) + i1 - 1] = var2[k] - var2[l];
            k += var0;
            l += var0;
        }

        if (var0 >= 2) {
            if (var0 != 2) {
                k = 0;
                l = j;

                for (int i2 = 0; i2 < var1; i2++) {
                    i1 = l;
                    int j1 = (k << 1) + (var0 << 1);
                    int k1 = k;
                    int l1 = k + k;

                    for (int b0 = 2; b0 < var0; b0 += 2) {
                        i1 += 2;
                        j1 -= 2;
                        k1 += 2;
                        l1 += 2;
                        float f1 = var4[var5 + b0 - 2] * var2[i1 - 1] + var4[var5 + b0 - 1] * var2[i1];
                        float f = var4[var5 + b0 - 2] * var2[i1] - var4[var5 + b0 - 1] * var2[i1 - 1];
                        var3[l1] = var2[k1] + f;
                        var3[j1] = f - var2[k1];
                        var3[l1 - 1] = var2[k1 - 1] + f1;
                        var3[j1 - 1] = var2[k1 - 1] - f1;
                    }

                    k += var0;
                    l += var0;
                }

                if (var0 % 2 == 1) {
                    return;
                }
            }

            k = var0;
            i1 = l = var0 - 1;
            l += j;

            for (int j2 = 0; j2 < var1; j2++) {
                var3[k] = -var2[l];
                var3[k - 1] = var2[i1];
                k += var0 << 1;
                l += var0;
                i1 += var0;
            }
        }
    }

    static void dradf4(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
        int j = var1 * var0;
        int k = j;
        int j1 = k << 1;
        int l = k + (k << 1);
        int i1 = 0;

        for (int i = 0; i < var1; i++) {
            float f10 = var2[k] + var2[l];
            float f11 = var2[i1] + var2[j1];
            int k1;
            var3[k1 = i1 << 2] = f10 + f11;
            var3[(var0 << 2) + k1 - 1] = f11 - f10;
            int k2;
            var3[(k2 = k1 + (var0 << 1)) - 1] = var2[i1] - var2[j1];
            var3[k2] = var2[l] - var2[k];
            k += var0;
            l += var0;
            i1 += var0;
            j1 += var0;
        }

        if (var0 >= 2) {
            if (var0 != 2) {
                k = 0;

                for (int i2 = 0; i2 < var1; i2++) {
                    l = k;
                    j1 = k << 2;
                    int l1;
                    int l2 = (l1 = var0 << 1) + j1;

                    for (int b0 = 2; b0 < var0; b0 += 2) {
                        l += 2;
                        i1 = l;
                        j1 += 2;
                        l2 -= 2;
                        i1 += j;
                        float f3 = var4[var5 + b0 - 2] * var2[i1 - 1] + var4[var5 + b0 - 1] * var2[i1];
                        float f = var4[var5 + b0 - 2] * var2[i1] - var4[var5 + b0 - 1] * var2[i1 - 1];
                        i1 += j;
                        float f4 = var6[var7 + b0 - 2] * var2[i1 - 1] + var6[var7 + b0 - 1] * var2[i1];
                        float f1 = var6[var7 + b0 - 2] * var2[i1] - var6[var7 + b0 - 1] * var2[i1 - 1];
                        i1 += j;
                        float f5 = var8[var9 + b0 - 2] * var2[i1 - 1] + var8[var9 + b0 - 1] * var2[i1];
                        float f2 = var8[var9 + b0 - 2] * var2[i1] - var8[var9 + b0 - 1] * var2[i1 - 1];
                        float f15 = f3 + f5;
                        float f13 = f5 - f3;
                        float f6 = f + f2;
                        float f9 = f - f2;
                        float f7 = var2[l] + f1;
                        float f8 = var2[l] - f1;
                        float f17 = var2[l - 1] + f4;
                        float f12 = var2[l - 1] - f4;
                        var3[j1 - 1] = f15 + f17;
                        var3[j1] = f6 + f7;
                        var3[l2 - 1] = f12 - f9;
                        var3[l2] = f13 - f8;
                        var3[j1 + l1 - 1] = f9 + f12;
                        var3[j1 + l1] = f13 + f8;
                        var3[l2 + l1 - 1] = f17 - f15;
                        var3[l2 + l1] = f6 - f7;
                    }

                    k += var0;
                }

                if ((var0 & 1) != 0) {
                    return;
                }
            }

            l = (k = j + var0 - 1) + (j << 1);
            i1 = var0 << 2;
            j1 = var0;
            int i3 = var0 << 1;
            int j3 = var0;

            for (int j2 = 0; j2 < var1; j2++) {
                float f14 = -hsqt2 * (var2[k] + var2[l]);
                float f16 = hsqt2 * (var2[k] - var2[l]);
                var3[j1 - 1] = f16 + var2[j3 - 1];
                var3[j1 + i3 - 1] = var2[j3 - 1] - f16;
                var3[j1] = f14 - var2[k + j];
                var3[j1 + i3] = f14 + var2[k + j];
                k += var0;
                l += var0;
                j1 += i1;
                j3 += var0;
            }
        }
    }

    static void dradfg(int var0, int var1, int var2, int var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8, float[] var9, int var10) {
        int k2 = 0;
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f7 = tpi / var1;
        f6 = (float)Math.cos(f7);
        f8 = (float)Math.sin(f7);
        int j = var1 + 1 >> 1;
        int j5 = var1;
        int i5 = var0;
        int l4 = var0 - 1 >> 1;
        int i2 = var2 * var0;
        int k4 = var1 * var0;
        short short1 = 100;

        while (true) {
            switch (short1) {
                case 101:
                    if (var0 == 1) {
                        short1 = 119;
                        break;
                    } else {
                        for (int k1 = 0; k1 < var3; k1++) {
                            var8[k1] = var6[k1];
                        }

                        int l12 = 0;

                        for (int l6 = 1; l6 < var1; l6++) {
                            l12 += i2;
                            k2 = l12;

                            for (int k9 = 0; k9 < var2; k9++) {
                                var7[k2] = var5[k2];
                                k2 += var0;
                            }
                        }

                        int l1 = -var0;
                        l12 = 0;
                        if (l4 > var2) {
                            for (int j7 = 1; j7 < var1; j7++) {
                                l12 += i2;
                                l1 += var0;
                                k2 = -var0 + l12;

                                for (int i10 = 0; i10 < var2; i10++) {
                                    int k5 = l1 - 1;
                                    k2 += var0;
                                    int l13 = k2;

                                    for (int b3 = 2; b3 < var0; b3 += 2) {
                                        k5 += 2;
                                        l13 += 2;
                                        var7[l13 - 1] = var9[var10 + k5 - 1] * var5[l13 - 1] + var9[var10 + k5] * var5[l13];
                                        var7[l13] = var9[var10 + k5 - 1] * var5[l13] - var9[var10 + k5] * var5[l13 - 1];
                                    }
                                }
                            }
                        } else {
                            for (int i7 = 1; i7 < var1; i7++) {
                                l1 += var0;
                                int i = l1 - 1;
                                l12 += i2;
                                k2 = l12;

                                for (int b2 = 2; b2 < var0; b2 += 2) {
                                    i += 2;
                                    k2 += 2;
                                    int k13 = k2;

                                    for (int l9 = 0; l9 < var2; l9++) {
                                        var7[k13 - 1] = var9[var10 + i - 1] * var5[k13 - 1] + var9[var10 + i] * var5[k13];
                                        var7[k13] = var9[var10 + i - 1] * var5[k13] - var9[var10 + i] * var5[k13 - 1];
                                        k13 += var0;
                                    }
                                }
                            }
                        }

                        l12 = 0;
                        k2 = j5 * i2;
                        if (l4 < var2) {
                            for (int k7 = 1; k7 < j; k7++) {
                                l12 += i2;
                                k2 -= i2;
                                int i14 = l12;
                                int j15 = k2;

                                for (int b4 = 2; b4 < var0; b4 += 2) {
                                    i14 += 2;
                                    j15 += 2;
                                    int i17 = i14 - var0;
                                    int j18 = j15 - var0;

                                    for (int j10 = 0; j10 < var2; j10++) {
                                        i17 += var0;
                                        j18 += var0;
                                        var5[i17 - 1] = var7[i17 - 1] + var7[j18 - 1];
                                        var5[j18 - 1] = var7[i17] - var7[j18];
                                        var5[i17] = var7[i17] + var7[j18];
                                        var5[j18] = var7[j18 - 1] - var7[i17 - 1];
                                    }
                                }
                            }
                        } else {
                            for (int l7 = 1; l7 < j; l7++) {
                                l12 += i2;
                                k2 -= i2;
                                int j14 = l12;
                                int k15 = k2;

                                for (int k10 = 0; k10 < var2; k10++) {
                                    int j17 = j14;
                                    int k18 = k15;

                                    for (int b5 = 2; b5 < var0; b5 += 2) {
                                        j17 += 2;
                                        k18 += 2;
                                        var5[j17 - 1] = var7[j17 - 1] + var7[k18 - 1];
                                        var5[k18 - 1] = var7[j17] - var7[k18];
                                        var5[j17] = var7[j17] + var7[k18];
                                        var5[k18] = var7[k18 - 1] - var7[j17 - 1];
                                    }

                                    j14 += var0;
                                    k15 += var0;
                                }
                            }
                        }
                    }
                case 119:
                    for (int j11 = 0; j11 < var3; j11++) {
                        var6[j11] = var8[j11];
                    }

                    int i13 = 0;
                    k2 = j5 * var3;

                    for (int i8 = 1; i8 < j; i8++) {
                        i13 += i2;
                        k2 -= i2;
                        int k14 = i13 - var0;
                        int l15 = k2 - var0;

                        for (int l10 = 0; l10 < var2; l10++) {
                            k14 += var0;
                            l15 += var0;
                            var5[k14] = var7[k14] + var7[l15];
                            var5[l15] = var7[l15] - var7[k14];
                        }
                    }

                    float f3 = 1.0F;
                    float f1 = 0.0F;
                    i13 = 0;
                    k2 = j5 * var3;
                    int l14 = (var1 - 1) * var3;

                    for (int i1 = 1; i1 < j; i1++) {
                        i13 += var3;
                        k2 -= var3;
                        float f9 = f6 * f3 - f8 * f1;
                        f1 = f6 * f1 + f8 * f3;
                        f3 = f9;
                        int i16 = i13;
                        int k17 = k2;
                        int l18 = l14;
                        int k19 = var3;

                        for (int k11 = 0; k11 < var3; k11++) {
                            var8[i16++] = var6[k11] + f3 * var6[k19++];
                            var8[k17++] = f1 * var6[l18++];
                        }

                        float f = f3;
                        float f5 = f1;
                        float f4 = f3;
                        float f2 = f1;
                        i16 = var3;
                        k17 = (j5 - 1) * var3;

                        for (int j8 = 2; j8 < j; j8++) {
                            i16 += var3;
                            k17 -= var3;
                            float f10 = f * f4 - f5 * f2;
                            f2 = f * f2 + f5 * f4;
                            f4 = f10;
                            l18 = i13;
                            k19 = k2;
                            int i20 = i16;
                            int k20 = k17;

                            for (int l11 = 0; l11 < var3; l11++) {
                                var8[l18++] += f4 * var6[i20++];
                                var8[k19++] += f2 * var6[k20++];
                            }
                        }
                    }

                    i13 = 0;

                    for (int k8 = 1; k8 < j; k8++) {
                        i13 += var3;
                        k2 = i13;

                        for (int i12 = 0; i12 < var3; i12++) {
                            var8[i12] += var6[k2++];
                        }
                    }

                    if (var0 < var2) {
                        short1 = 132;
                        break;
                    }

                    i13 = 0;
                    k2 = 0;

                    for (int i11 = 0; i11 < var2; i11++) {
                        l14 = i13;
                        int j16 = k2;

                        for (int i6 = 0; i6 < var0; i6++) {
                            var4[j16++] = var7[l14++];
                        }

                        i13 += var0;
                        k2 += k4;
                    }

                    short1 = 135;
                    break;
                case 132:
                    for (int l5 = 0; l5 < var0; l5++) {
                        int j12 = l5;
                        k2 = l5;

                        for (int l8 = 0; l8 < var2; l8++) {
                            var4[k2] = var7[j12];
                            j12 += var0;
                            k2 += k4;
                        }
                    }
                case 135:
                    int k12 = 0;
                    k2 = var0 << 1;
                    int j13 = 0;
                    int i15 = j5 * i2;

                    for (int j6 = 1; j6 < j; j6++) {
                        k12 += k2;
                        j13 += i2;
                        i15 -= i2;
                        int k16 = k12;
                        int l17 = j13;
                        int i19 = i15;

                        for (int i9 = 0; i9 < var2; i9++) {
                            var4[k16 - 1] = var7[l17];
                            var4[k16] = var7[i19];
                            k16 += k4;
                            l17 += var0;
                            i19 += var0;
                        }
                    }

                    if (var0 == 1) {
                        return;
                    }

                    if (l4 >= var2) {
                        k12 = -var0;
                        j13 = 0;
                        i15 = 0;
                        int l16 = j5 * i2;

                        for (int k6 = 1; k6 < j; k6++) {
                            k12 += k2;
                            j13 += k2;
                            i15 += i2;
                            l16 -= i2;
                            int i18 = k12;
                            int j19 = j13;
                            int l19 = i15;
                            int j20 = l16;

                            for (int j9 = 0; j9 < var2; j9++) {
                                for (int b1 = 2; b1 < var0; b1 += 2) {
                                    int j1 = i5 - b1;
                                    var4[b1 + j19 - 1] = var7[b1 + l19 - 1] + var7[b1 + j20 - 1];
                                    var4[j1 + i18 - 1] = var7[b1 + l19 - 1] - var7[b1 + j20 - 1];
                                    var4[b1 + j19] = var7[b1 + l19] + var7[b1 + j20];
                                    var4[j1 + i18] = var7[b1 + j20] - var7[b1 + l19];
                                }

                                i18 += k4;
                                j19 += k4;
                                l19 += var0;
                                j20 += var0;
                            }
                        }

                        return;
                    }

                    short1 = 141;
                    break;
                case 141:
                    int j2 = -var0;
                    int l2 = 0;
                    int i3 = 0;
                    int j3 = j5 * i2;

                    for (int k = 1; k < j; k++) {
                        j2 += k2;
                        l2 += k2;
                        i3 += i2;
                        j3 -= i2;

                        for (int b0 = 2; b0 < var0; b0 += 2) {
                            int k3 = i5 + j2 - b0;
                            int l3 = b0 + l2;
                            int i4 = b0 + i3;
                            int j4 = b0 + j3;

                            for (int l = 0; l < var2; l++) {
                                var4[l3 - 1] = var7[i4 - 1] + var7[j4 - 1];
                                var4[k3 - 1] = var7[i4 - 1] - var7[j4 - 1];
                                var4[l3] = var7[i4] + var7[j4];
                                var4[k3] = var7[j4] - var7[i4];
                                k3 += k4;
                                l3 += k4;
                                i4 += var0;
                                j4 += var0;
                            }
                        }
                    }

                    return;
            }
        }
    }

    static void drftf1(int var0, float[] var1, float[] var2, float[] var3, int[] var4) {
        int k1 = var4[1];
        int i1 = 1;
        int l = var0;
        int i2 = var0;

        for (int j = 0; j < k1; j++) {
            int j1 = k1 - j;
            int l1 = var4[j1 + 1];
            int k = l / l1;
            int j2 = var0 / l;
            int k2 = j2 * k;
            i2 -= (l1 - 1) * j2;
            i1 = 1 - i1;
            byte b0 = 100;

            label59:
            while (true) {
                switch (b0) {
                    case 100:
                        if (l1 != 4) {
                            b0 = 102;
                        } else {
                            int l2 = i2 + j2;
                            int i3 = l2 + j2;
                            if (i1 != 0) {
                                dradf4(j2, k, var2, var1, var3, i2 - 1, var3, l2 - 1, var3, i3 - 1);
                            } else {
                                dradf4(j2, k, var1, var2, var3, i2 - 1, var3, l2 - 1, var3, i3 - 1);
                            }

                            b0 = 110;
                        }
                    case 101:
                    case 105:
                    case 106:
                    case 107:
                    case 108:
                    default:
                        break;
                    case 102:
                        if (l1 != 2) {
                            b0 = 104;
                        } else if (i1 != 0) {
                            b0 = 103;
                        } else {
                            dradf2(j2, k, var1, var2, var3, i2 - 1);
                            b0 = 110;
                        }
                        break;
                    case 103:
                        dradf2(j2, k, var2, var1, var3, i2 - 1);
                    case 104:
                        if (j2 == 1) {
                            i1 = 1 - i1;
                        }

                        if (i1 != 0) {
                            b0 = 109;
                        } else {
                            dradfg(j2, l1, k, k2, var1, var1, var1, var2, var2, var3, i2 - 1);
                            i1 = 1;
                            b0 = 110;
                        }
                        break;
                    case 109:
                        dradfg(j2, l1, k, k2, var2, var2, var2, var1, var1, var3, i2 - 1);
                        i1 = 0;
                    case 110:
                        break label59;
                }
            }

            l = k;
        }

        if (i1 != 1) {
            for (int i = 0; i < var0; i++) {
                var1[i] = var2[i];
            }
        }
    }

    static void dradb2(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5) {
        int j = var1 * var0;
        int k = 0;
        int l = 0;
        int i1 = (var0 << 1) - 1;

        for (int i = 0; i < var1; i++) {
            var3[k] = var2[l] + var2[i1 + l];
            var3[k + j] = var2[l] - var2[i1 + l];
            l = (k += var0) << 1;
        }

        if (var0 >= 2) {
            if (var0 != 2) {
                k = 0;
                l = 0;

                for (int i2 = 0; i2 < var1; i2++) {
                    i1 = k;
                    int j1 = l;
                    int k1 = l + (var0 << 1);
                    int l1 = j + k;

                    for (int b0 = 2; b0 < var0; b0 += 2) {
                        i1 += 2;
                        j1 += 2;
                        k1 -= 2;
                        l1 += 2;
                        var3[i1 - 1] = var2[j1 - 1] + var2[k1 - 1];
                        float f1 = var2[j1 - 1] - var2[k1 - 1];
                        var3[i1] = var2[j1] - var2[k1];
                        float f = var2[j1] + var2[k1];
                        var3[l1 - 1] = var4[var5 + b0 - 2] * f1 - var4[var5 + b0 - 1] * f;
                        var3[l1] = var4[var5 + b0 - 2] * f + var4[var5 + b0 - 1] * f1;
                    }

                    l = (k += var0) << 1;
                }

                if (var0 % 2 == 1) {
                    return;
                }
            }

            k = var0 - 1;
            l = var0 - 1;

            for (int j2 = 0; j2 < var1; j2++) {
                var3[k] = var2[l] + var2[l];
                var3[k + j] = -(var2[l + 1] + var2[l + 1]);
                k += var0;
                l += var0 << 1;
            }
        }
    }

    static void dradb3(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7) {
        int j = var1 * var0;
        int k = 0;
        int l = j << 1;
        int i1 = var0 << 1;
        int j1 = var0 + (var0 << 1);
        int k1 = 0;

        for (int i = 0; i < var1; i++) {
            float f9 = var2[i1 - 1] + var2[i1 - 1];
            float f4 = var2[k1] + taur * f9;
            var3[k] = var2[k1] + f9;
            float f1 = taui * (var2[i1] + var2[i1]);
            var3[k + j] = f4 - f1;
            var3[k + l] = f4 + f1;
            k += var0;
            i1 += j1;
            k1 += j1;
        }

        if (var0 != 1) {
            k = 0;
            i1 = var0 << 1;

            for (int i3 = 0; i3 < var1; i3++) {
                int i2 = k + (k << 1);
                int l1 = k1 = i2 + i1;
                int j2 = k;
                int k2;
                int l2 = (k2 = k + j) + j;

                for (int b0 = 2; b0 < var0; b0 += 2) {
                    k1 += 2;
                    l1 -= 2;
                    i2 += 2;
                    j2 += 2;
                    k2 += 2;
                    l2 += 2;
                    float f12 = var2[k1 - 1] + var2[l1 - 1];
                    float f11 = var2[i2 - 1] + taur * f12;
                    var3[j2 - 1] = var2[i2 - 1] + f12;
                    float f8 = var2[k1] - var2[l1];
                    float f = var2[i2] + taur * f8;
                    var3[j2] = var2[i2] + f8;
                    float f5 = taui * (var2[k1 - 1] - var2[l1 - 1]);
                    float f10 = taui * (var2[k1] + var2[l1]);
                    float f6 = f11 - f10;
                    float f7 = f11 + f10;
                    float f2 = f + f5;
                    float f3 = f - f5;
                    var3[k2 - 1] = var4[var5 + b0 - 2] * f6 - var4[var5 + b0 - 1] * f2;
                    var3[k2] = var4[var5 + b0 - 2] * f2 + var4[var5 + b0 - 1] * f6;
                    var3[l2 - 1] = var6[var7 + b0 - 2] * f7 - var6[var7 + b0 - 1] * f3;
                    var3[l2] = var6[var7 + b0 - 2] * f3 + var6[var7 + b0 - 1] * f7;
                }

                k += var0;
            }
        }
    }

    static void dradb4(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
        int j = var1 * var0;
        int k = 0;
        int l = var0 << 2;
        int i1 = 0;
        int l1 = var0 << 1;

        for (int i = 0; i < var1; i++) {
            int j1 = i1 + l1;
            int k1 = k;
            float f12 = var2[j1 - 1] + var2[j1 - 1];
            float f13 = var2[j1] + var2[j1];
            int i3;
            float f10 = var2[i1] - var2[(i3 = j1 + l1) - 1];
            float f11 = var2[i1] + var2[i3 - 1];
            var3[k1] = f11 + f12;
            int l3;
            var3[l3 = k1 + j] = f10 - f13;
            var3[k1 = l3 + j] = f11 - f12;
            int i4;
            var3[i4 = k1 + j] = f10 + f13;
            k += var0;
            i1 += l;
        }

        if (var0 >= 2) {
            if (var0 != 2) {
                k = 0;

                for (int k2 = 0; k2 < var1; k2++) {
                    int j3;
                    int j4 = (j3 = i1 = (l = k << 2) + l1) + l1;
                    int i2 = k;

                    for (int b0 = 2; b0 < var0; b0 += 2) {
                        l += 2;
                        i1 += 2;
                        j3 -= 2;
                        j4 -= 2;
                        i2 += 2;
                        float f6 = var2[l] + var2[j4];
                        float f7 = var2[l] - var2[j4];
                        float f8 = var2[i1] - var2[j3];
                        float f21 = var2[i1] + var2[j3];
                        float f16 = var2[l - 1] - var2[j4 - 1];
                        float f18 = var2[l - 1] + var2[j4 - 1];
                        float f9 = var2[i1 - 1] - var2[j3 - 1];
                        float f20 = var2[i1 - 1] + var2[j3 - 1];
                        var3[i2 - 1] = f18 + f20;
                        float f4 = f18 - f20;
                        var3[i2] = f7 + f8;
                        float f1 = f7 - f8;
                        float f3 = f16 - f21;
                        float f5 = f16 + f21;
                        float f = f6 + f9;
                        float f2 = f6 - f9;
                        int j2;
                        var3[(j2 = i2 + j) - 1] = var4[var5 + b0 - 2] * f3 - var4[var5 + b0 - 1] * f;
                        var3[j2] = var4[var5 + b0 - 2] * f + var4[var5 + b0 - 1] * f3;
                        int j5;
                        var3[(j5 = j2 + j) - 1] = var6[var7 + b0 - 2] * f4 - var6[var7 + b0 - 1] * f1;
                        var3[j5] = var6[var7 + b0 - 2] * f1 + var6[var7 + b0 - 1] * f4;
                        var3[(j2 = j5 + j) - 1] = var8[var9 + b0 - 2] * f5 - var8[var9 + b0 - 1] * f2;
                        var3[j2] = var8[var9 + b0 - 2] * f2 + var8[var9 + b0 - 1] * f5;
                    }

                    k += var0;
                }

                if (var0 % 2 == 1) {
                    return;
                }
            }

            k = var0;
            l = var0 << 2;
            i1 = var0 - 1;
            int k3 = var0 + (var0 << 1);

            for (int l2 = 0; l2 < var1; l2++) {
                int k4 = i1;
                float f14 = var2[k] + var2[k3];
                float f15 = var2[k3] - var2[k];
                float f17 = var2[k - 1] - var2[k3 - 1];
                float f19 = var2[k - 1] + var2[k3 - 1];
                var3[k4] = f19 + f19;
                int l4;
                var3[l4 = k4 + j] = sqrt2 * (f17 - f14);
                var3[k4 = l4 + j] = f15 + f15;
                int i5;
                var3[i5 = k4 + j] = -sqrt2 * (f17 + f14);
                i1 += var0;
                k += l;
                k3 += l;
            }
        }
    }

    static void dradbg(int var0, int var1, int var2, int var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8, float[] var9, int var10) {
        int j = 0;
        int l1 = 0;
        int j4 = 0;
        int i5 = 0;
        float f6 = 0.0F;
        float f8 = 0.0F;
        int j5 = 0;
        short short1 = 100;

        while (true) {
            switch (short1) {
                case 100:
                    j4 = var1 * var0;
                    l1 = var2 * var0;
                    float f7 = tpi / var1;
                    f6 = (float)Math.cos(f7);
                    f8 = (float)Math.sin(f7);
                    i5 = var0 - 1 >>> 1;
                    j5 = var1;
                    j = var1 + 1 >>> 1;
                    if (var0 < var2) {
                        short1 = 103;
                        break;
                    }

                    int l13 = 0;
                    int l15 = 0;

                    for (int i11 = 0; i11 < var2; i11++) {
                        int l17 = l13;
                        int k19 = l15;

                        for (int i6 = 0; i6 < var0; i6++) {
                            var7[l17] = var4[k19];
                            l17++;
                            k19++;
                        }

                        l13 += var0;
                        l15 += j4;
                    }

                    short1 = 106;
                    break;
                case 103:
                    int j13 = 0;

                    for (int l5 = 0; l5 < var0; l5++) {
                        int j15 = j13;
                        int i17 = j13;

                        for (int j10 = 0; j10 < var2; j10++) {
                            var7[j15] = var4[i17];
                            j15 += var0;
                            i17 += j4;
                        }

                        j13++;
                    }
                case 106:
                    int k13 = 0;
                    int k15 = j5 * l1;
                    int k20;
                    int j22 = k20 = var0 << 1;

                    for (int j8 = 1; j8 < j; j8++) {
                        k13 += l1;
                        k15 -= l1;
                        int j17 = k13;
                        int i19 = k15;
                        int k21 = k20;

                        for (int k10 = 0; k10 < var2; k10++) {
                            var7[j17] = var4[k21 - 1] + var4[k21 - 1];
                            var7[i19] = var4[k21] + var4[k21];
                            j17 += var0;
                            i19 += var0;
                            k21 += j4;
                        }

                        k20 += j22;
                    }

                    if (var0 == 1) {
                        short1 = 116;
                    } else {
                        if (i5 < var2) {
                            short1 = 112;
                            break;
                        }

                        k13 = 0;
                        k15 = j5 * l1;
                        j22 = 0;

                        for (int k8 = 1; k8 < j; k8++) {
                            k13 += l1;
                            k15 -= l1;
                            int k17 = k13;
                            int j19 = k15;
                            j22 += var0 << 1;
                            int l22 = j22;

                            for (int l10 = 0; l10 < var2; l10++) {
                                k20 = k17;
                                int l21 = j19;
                                int j23 = l22;
                                int l23 = l22;

                                for (int b5 = 2; b5 < var0; b5 += 2) {
                                    k20 += 2;
                                    l21 += 2;
                                    j23 += 2;
                                    l23 -= 2;
                                    var7[k20 - 1] = var4[j23 - 1] + var4[l23 - 1];
                                    var7[l21 - 1] = var4[j23 - 1] - var4[l23 - 1];
                                    var7[k20] = var4[j23] - var4[l23];
                                    var7[l21] = var4[j23] + var4[l23];
                                }

                                k17 += var0;
                                j19 += var0;
                                l22 += j4;
                            }
                        }

                        short1 = 116;
                    }
                    break;
                case 112:
                    int l12 = 0;
                    int l14 = j5 * l1;
                    int k3 = 0;

                    for (int i7 = 1; i7 < j; i7++) {
                        l12 += l1;
                        l14 -= l1;
                        int k16 = l12;
                        int i18 = l14;
                        k3 += var0 << 1;
                        int l3 = k3;
                        int i4 = k3;

                        for (int b3 = 2; b3 < var0; b3 += 2) {
                            k16 += 2;
                            i18 += 2;
                            l3 += 2;
                            i4 -= 2;
                            int l19 = k16;
                            int l20 = i18;
                            int k4 = l3;
                            int l4 = i4;

                            for (int k9 = 0; k9 < var2; k9++) {
                                var7[l19 - 1] = var4[k4 - 1] + var4[l4 - 1];
                                var7[l20 - 1] = var4[k4 - 1] - var4[l4 - 1];
                                var7[l19] = var4[k4] - var4[l4];
                                var7[l20] = var4[k4] + var4[l4];
                                l19 += var0;
                                l20 += var0;
                                k4 += j4;
                                l4 += j4;
                            }
                        }
                    }
                case 116:
                    float f3 = 1.0F;
                    float f1 = 0.0F;
                    int i13 = 0;
                    int i15;
                    int i23 = i15 = j5 * var3;
                    int l16 = (var1 - 1) * var3;

                    for (int i1 = 1; i1 < j; i1++) {
                        i13 += var3;
                        i15 -= var3;
                        float f9 = f6 * f3 - f8 * f1;
                        f1 = f6 * f1 + f8 * f3;
                        f3 = f9;
                        int j18 = i13;
                        int i20 = i15;
                        int i21 = 0;
                        int i22 = var3;
                        int k22 = l16;

                        for (int j11 = 0; j11 < var3; j11++) {
                            var6[j18++] = var8[i21++] + f3 * var8[i22++];
                            var6[i20++] = f1 * var8[k22++];
                        }

                        float f = f3;
                        float f5 = f1;
                        float f4 = f3;
                        float f2 = f1;
                        i21 = var3;
                        i22 = i23 - var3;

                        for (int j7 = 2; j7 < j; j7++) {
                            i21 += var3;
                            i22 -= var3;
                            float f10 = f * f4 - f5 * f2;
                            f2 = f * f2 + f5 * f4;
                            f4 = f10;
                            j18 = i13;
                            i20 = i15;
                            int k23 = i21;
                            int i24 = i22;

                            for (int k11 = 0; k11 < var3; k11++) {
                                var6[j18++] += f4 * var8[k23++];
                                var6[i20++] += f2 * var8[i24++];
                            }
                        }
                    }

                    i13 = 0;

                    for (int k7 = 1; k7 < j; k7++) {
                        i13 += var3;
                        i15 = i13;

                        for (int l11 = 0; l11 < var3; l11++) {
                            var8[l11] += var8[i15++];
                        }
                    }

                    i13 = 0;
                    i15 = j5 * l1;

                    for (int l7 = 1; l7 < j; l7++) {
                        i13 += l1;
                        i15 -= l1;
                        l16 = i13;
                        int k18 = i15;

                        for (int l9 = 0; l9 < var2; l9++) {
                            var7[l16] = var5[l16] - var5[k18];
                            var7[k18] = var5[l16] + var5[k18];
                            l16 += var0;
                            k18 += var0;
                        }
                    }

                    if (var0 == 1) {
                        short1 = 132;
                    } else {
                        if (i5 < var2) {
                            short1 = 128;
                            break;
                        }

                        i13 = 0;
                        i15 = j5 * l1;

                        for (int i8 = 1; i8 < j; i8++) {
                            i13 += l1;
                            i15 -= l1;
                            l16 = i13;
                            int l18 = i15;

                            for (int i10 = 0; i10 < var2; i10++) {
                                int j20 = l16;
                                int j21 = l18;

                                for (int b4 = 2; b4 < var0; b4 += 2) {
                                    j20 += 2;
                                    j21 += 2;
                                    var7[j20 - 1] = var5[j20 - 1] - var5[j21];
                                    var7[j21 - 1] = var5[j20 - 1] + var5[j21];
                                    var7[j20] = var5[j20] + var5[j21 - 1];
                                    var7[j21] = var5[j20] - var5[j21 - 1];
                                }

                                l16 += var0;
                                l18 += var0;
                            }
                        }

                        short1 = 132;
                    }
                    break;
                case 128:
                    int j12 = 0;
                    int i14 = j5 * l1;

                    for (int j6 = 1; j6 < j; j6++) {
                        j12 += l1;
                        i14 -= l1;
                        int i16 = j12;
                        int l2 = i14;

                        for (int b1 = 2; b1 < var0; b1 += 2) {
                            i16 += 2;
                            l2 += 2;
                            int i3 = i16;
                            int j3 = l2;

                            for (int l8 = 0; l8 < var2; l8++) {
                                var7[i3 - 1] = var5[i3 - 1] - var5[j3];
                                var7[j3 - 1] = var5[i3 - 1] + var5[j3];
                                var7[i3] = var5[i3] + var5[j3 - 1];
                                var7[j3] = var5[i3] - var5[j3 - 1];
                                i3 += var0;
                                j3 += var0;
                            }
                        }
                    }
                case 132:
                    if (var0 == 1) {
                        return;
                    }

                    for (int j1 = 0; j1 < var3; j1++) {
                        var6[j1] = var8[j1];
                    }

                    int k12 = 0;

                    for (int k6 = 1; k6 < var1; k6++) {
                        int j14 = k12 += l1;

                        for (int i9 = 0; i9 < var2; i9++) {
                            var5[j14] = var7[j14];
                            j14 += var0;
                        }
                    }

                    if (i5 <= var2) {
                        int i12 = -var0 - 1;
                        k12 = 0;

                        for (int l6 = 1; l6 < var1; l6++) {
                            i12 += var0;
                            k12 += l1;
                            int k5 = i12;
                            int k14 = k12;

                            for (int b2 = 2; b2 < var0; b2 += 2) {
                                k14 += 2;
                                k5 += 2;
                                int j16 = k14;

                                for (int j9 = 0; j9 < var2; j9++) {
                                    var5[j16 - 1] = var9[var10 + k5 - 1] * var7[j16 - 1] - var9[var10 + k5] * var7[j16];
                                    var5[j16] = var9[var10 + k5 - 1] * var7[j16] + var9[var10 + k5] * var7[j16 - 1];
                                    j16 += var0;
                                }
                            }
                        }

                        return;
                    }

                    short1 = 139;
                    break;
                case 139:
                    int k1 = -var0 - 1;
                    int i2 = 0;

                    for (int k = 1; k < var1; k++) {
                        k1 += var0;
                        i2 += l1;
                        int j2 = i2;

                        for (int l = 0; l < var2; l++) {
                            int i = k1;
                            int k2 = j2;

                            for (int b0 = 2; b0 < var0; b0 += 2) {
                                i += 2;
                                k2 += 2;
                                var5[k2 - 1] = var9[var10 + i - 1] * var7[k2 - 1] - var9[var10 + i] * var7[k2];
                                var5[k2] = var9[var10 + i - 1] * var7[k2] + var9[var10 + i] * var7[k2 - 1];
                            }

                            j2 += var0;
                        }
                    }

                    return;
            }
        }
    }

    static void drftb1(int var0, float[] var1, float[] var2, float[] var3, int var4, int[] var5) {
        int l = 0;
        int k1 = 0;
        int k2 = 0;
        int l2 = 0;
        int j1 = var5[1];
        int i1 = 0;
        int k = 1;
        int l1 = 1;

        for (int j = 0; j < j1; j++) {
            byte b0 = 100;

            label68:
            while (true) {
                switch (b0) {
                    case 100:
                        k1 = var5[j + 2];
                        l = k1 * k;
                        k2 = var0 / l;
                        l2 = k2 * k;
                        if (k1 != 4) {
                            b0 = 103;
                        } else {
                            int i3 = l1 + k2;
                            int j2 = i3 + k2;
                            if (i1 != 0) {
                                dradb4(k2, k, var2, var1, var3, var4 + l1 - 1, var3, var4 + i3 - 1, var3, var4 + j2 - 1);
                            } else {
                                dradb4(k2, k, var1, var2, var3, var4 + l1 - 1, var3, var4 + i3 - 1, var3, var4 + j2 - 1);
                            }

                            i1 = 1 - i1;
                            b0 = 115;
                        }
                        break;
                    case 103:
                        if (k1 != 2) {
                            b0 = 106;
                        } else {
                            if (i1 != 0) {
                                dradb2(k2, k, var2, var1, var3, var4 + l1 - 1);
                            } else {
                                dradb2(k2, k, var1, var2, var3, var4 + l1 - 1);
                            }

                            i1 = 1 - i1;
                            b0 = 115;
                        }
                        break;
                    case 106:
                        if (k1 != 3) {
                            b0 = 109;
                        } else {
                            int i2 = l1 + k2;
                            if (i1 != 0) {
                                dradb3(k2, k, var2, var1, var3, var4 + l1 - 1, var3, var4 + i2 - 1);
                            } else {
                                dradb3(k2, k, var1, var2, var3, var4 + l1 - 1, var3, var4 + i2 - 1);
                            }

                            i1 = 1 - i1;
                            b0 = 115;
                        }
                        break;
                    case 109:
                        if (i1 != 0) {
                            dradbg(k2, k1, k, l2, var2, var2, var2, var1, var1, var3, var4 + l1 - 1);
                        } else {
                            dradbg(k2, k1, k, l2, var1, var1, var1, var2, var2, var3, var4 + l1 - 1);
                        }

                        if (k2 == 1) {
                            i1 = 1 - i1;
                        }
                    case 115:
                        break label68;
                }
            }

            k = l;
            l1 += (k1 - 1) * k2;
        }

        if (i1 != 0) {
            for (int i = 0; i < var0; i++) {
                var1[i] = var2[i];
            }
        }
    }
}
