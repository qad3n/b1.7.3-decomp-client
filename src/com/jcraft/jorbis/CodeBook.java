package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook {
    int dim;
    int entries;
    StaticCodeBook c = new StaticCodeBook();
    float[] valuelist;
    int[] codelist;
    DecodeAux decode_tree;
    private int[] t = new int[15];

    CodeBook() {
    }

    int encode(int n, Buffer buffer) {
        buffer.write(this.codelist[n], this.c.lengthlist[n]);
        return this.c.lengthlist[n];
    }

    int errorv(float[] fArray) {
        int n = this.best(fArray, 1);
        for (int i = 0; i < this.dim; ++i) {
            fArray[i] = this.valuelist[n * this.dim + i];
        }
        return n;
    }

    int encodev(int n, float[] fArray, Buffer buffer) {
        for (int i = 0; i < this.dim; ++i) {
            fArray[i] = this.valuelist[n * this.dim + i];
        }
        return this.encode(n, buffer);
    }

    int encodevs(float[] fArray, Buffer buffer, int n, int n2) {
        int n3 = this.besterror(fArray, n, n2);
        return this.encode(n3, buffer);
    }

    synchronized int decodevs_add(float[] fArray, int n, Buffer buffer, int n2) {
        int n3;
        int n4 = n2 / this.dim;
        if (this.t.length < n4) {
            this.t = new int[n4];
        }
        for (n3 = 0; n3 < n4; ++n3) {
            int n5 = this.decode(buffer);
            if (n5 == -1) {
                return -1;
            }
            this.t[n3] = n5 * this.dim;
        }
        n3 = 0;
        int n6 = 0;
        while (n3 < this.dim) {
            for (int i = 0; i < n4; ++i) {
                int n7 = n + n6 + i;
                fArray[n7] = fArray[n7] + this.valuelist[this.t[i] + n3];
            }
            ++n3;
            n6 += n4;
        }
        return 0;
    }

    int decodev_add(float[] fArray, int n, Buffer buffer, int n2) {
        if (this.dim > 8) {
            int n3 = 0;
            while (n3 < n2) {
                int n4 = this.decode(buffer);
                if (n4 == -1) {
                    return -1;
                }
                int n5 = n4 * this.dim;
                int n6 = 0;
                while (n6 < this.dim) {
                    int n7 = n + n3++;
                    fArray[n7] = fArray[n7] + this.valuelist[n5 + n6++];
                }
            }
        } else {
            int n8 = 0;
            while (n8 < n2) {
                int n9 = this.decode(buffer);
                if (n9 == -1) {
                    return -1;
                }
                int n10 = n9 * this.dim;
                int n11 = 0;
                switch (this.dim) {
                    case 8: {
                        int n12 = n + n8++;
                        fArray[n12] = fArray[n12] + this.valuelist[n10 + n11++];
                    }
                    case 7: {
                        int n13 = n + n8++;
                        fArray[n13] = fArray[n13] + this.valuelist[n10 + n11++];
                    }
                    case 6: {
                        int n14 = n + n8++;
                        fArray[n14] = fArray[n14] + this.valuelist[n10 + n11++];
                    }
                    case 5: {
                        int n15 = n + n8++;
                        fArray[n15] = fArray[n15] + this.valuelist[n10 + n11++];
                    }
                    case 4: {
                        int n16 = n + n8++;
                        fArray[n16] = fArray[n16] + this.valuelist[n10 + n11++];
                    }
                    case 3: {
                        int n17 = n + n8++;
                        fArray[n17] = fArray[n17] + this.valuelist[n10 + n11++];
                    }
                    case 2: {
                        int n18 = n + n8++;
                        fArray[n18] = fArray[n18] + this.valuelist[n10 + n11++];
                    }
                    case 1: {
                        int n19 = n + n8++;
                        fArray[n19] = fArray[n19] + this.valuelist[n10 + n11++];
                    }
                }
            }
        }
        return 0;
    }

    int decodev_set(float[] fArray, int n, Buffer buffer, int n2) {
        int n3 = 0;
        while (n3 < n2) {
            int n4 = this.decode(buffer);
            if (n4 == -1) {
                return -1;
            }
            int n5 = n4 * this.dim;
            int n6 = 0;
            while (n6 < this.dim) {
                fArray[n + n3++] = this.valuelist[n5 + n6++];
            }
        }
        return 0;
    }

    int decodevv_add(float[][] fArray, int n, int n2, Buffer buffer, int n3) {
        int n4 = 0;
        int n5 = n / n2;
        while (n5 < (n + n3) / n2) {
            int n6 = this.decode(buffer);
            if (n6 == -1) {
                return -1;
            }
            int n7 = n6 * this.dim;
            for (int i = 0; i < this.dim; ++i) {
                fArray[n4++][n5] += this.valuelist[n7 + i];
                if (n4 == n2) {
                    n4 = 0;
                    ++n5;
                }
            }
        }
        return 0;
    }

    int decode(Buffer buffer) {
        int n = 0;
        DecodeAux decodeAux = this.decode_tree;
        int n2 = buffer.look(decodeAux.tabn);
        if (n2 >= 0) {
            n = decodeAux.tab[n2];
            buffer.adv(decodeAux.tabl[n2]);
            if (n <= 0) {
                return -n;
            }
        }
        do {
            switch (buffer.read1()) {
                case 0: {
                    n = decodeAux.ptr0[n];
                    break;
                }
                case 1: {
                    n = decodeAux.ptr1[n];
                    break;
                }
                default: {
                    return -1;
                }
            }
        } while (n > 0);
        return -n;
    }

    int decodevs(float[] fArray, int n, Buffer buffer, int n2, int n3) {
        int n4 = this.decode(buffer);
        if (n4 == -1) {
            return -1;
        }
        switch (n3) {
            case -1: {
                int n5 = 0;
                int n6 = 0;
                while (n5 < this.dim) {
                    fArray[n + n6] = this.valuelist[n4 * this.dim + n5];
                    ++n5;
                    n6 += n2;
                }
                break;
            }
            case 0: {
                int n7 = 0;
                int n8 = 0;
                while (n7 < this.dim) {
                    int n9 = n + n8;
                    fArray[n9] = fArray[n9] + this.valuelist[n4 * this.dim + n7];
                    ++n7;
                    n8 += n2;
                }
                break;
            }
            case 1: {
                int n10 = 0;
                int n11 = 0;
                while (n10 < this.dim) {
                    int n12 = n + n11;
                    fArray[n12] = fArray[n12] * this.valuelist[n4 * this.dim + n10];
                    ++n10;
                    n11 += n2;
                }
                break;
            }
        }
        return n4;
    }

    int best(float[] fArray, int n) {
        int n2 = -1;
        float f = 0.0f;
        int n3 = 0;
        for (int i = 0; i < this.entries; ++i) {
            if (this.c.lengthlist[i] > 0) {
                float f2 = CodeBook.dist(this.dim, this.valuelist, n3, fArray, n);
                if (n2 == -1 || f2 < f) {
                    f = f2;
                    n2 = i;
                }
            }
            n3 += this.dim;
        }
        return n2;
    }

    int besterror(float[] fArray, int n, int n2) {
        int n3 = this.best(fArray, n);
        switch (n2) {
            case 0: {
                int n4 = 0;
                int n5 = 0;
                while (n4 < this.dim) {
                    int n6 = n5;
                    fArray[n6] = fArray[n6] - this.valuelist[n3 * this.dim + n4];
                    ++n4;
                    n5 += n;
                }
                break;
            }
            case 1: {
                int n7 = 0;
                int n8 = 0;
                while (n7 < this.dim) {
                    float f = this.valuelist[n3 * this.dim + n7];
                    if (f == 0.0f) {
                        fArray[n8] = 0.0f;
                    } else {
                        int n9 = n8;
                        fArray[n9] = fArray[n9] / f;
                    }
                    ++n7;
                    n8 += n;
                }
                break;
            }
        }
        return n3;
    }

    void clear() {
    }

    private static float dist(int n, float[] fArray, int n2, float[] fArray2, int n3) {
        float f = 0.0f;
        for (int i = 0; i < n; ++i) {
            float f2 = fArray[n2 + i] - fArray2[i * n3];
            f += f2 * f2;
        }
        return f;
    }

    int init_decode(StaticCodeBook staticCodeBook) {
        this.c = staticCodeBook;
        this.entries = staticCodeBook.entries;
        this.dim = staticCodeBook.dim;
        this.valuelist = staticCodeBook.unquantize();
        this.decode_tree = this.make_decode_tree();
        if (this.decode_tree == null) {
            this.clear();
            return -1;
        }
        return 0;
    }

    static int[] make_words(int[] nArray, int n) {
        int n2;
        int n3;
        int n4;
        int[] nArray2 = new int[33];
        int[] nArray3 = new int[n];
        for (n4 = 0; n4 < n; ++n4) {
            n3 = nArray[n4];
            if (n3 <= 0) continue;
            n2 = nArray2[n3];
            if (n3 < 32 && n2 >>> n3 != 0) {
                return null;
            }
            nArray3[n4] = n2;
            int n5 = n3;
            while (n5 > 0) {
                if ((nArray2[n5] & 1) != 0) {
                    if (n5 == 1) {
                        nArray2[1] = nArray2[1] + 1;
                        break;
                    }
                    nArray2[n5] = nArray2[n5 - 1] << 1;
                    break;
                }
                int n6 = n5--;
                nArray2[n6] = nArray2[n6] + 1;
            }
            for (n5 = n3 + 1; n5 < 33 && nArray2[n5] >>> 1 == n2; ++n5) {
                n2 = nArray2[n5];
                nArray2[n5] = nArray2[n5 - 1] << 1;
            }
        }
        for (n4 = 0; n4 < n; ++n4) {
            n3 = 0;
            for (n2 = 0; n2 < nArray[n4]; ++n2) {
                n3 <<= 1;
                n3 |= nArray3[n4] >>> n2 & 1;
            }
            nArray3[n4] = n3;
        }
        return nArray3;
    }

    DecodeAux make_decode_tree() {
        int n;
        int n2;
        int n3;
        int n4;
        int n5 = 0;
        DecodeAux decodeAux = new DecodeAux();
        int[] nArray = decodeAux.ptr0 = new int[this.entries * 2];
        int[] nArray2 = decodeAux.ptr1 = new int[this.entries * 2];
        int[] nArray3 = CodeBook.make_words(this.c.lengthlist, this.c.entries);
        if (nArray3 == null) {
            return null;
        }
        decodeAux.aux = this.entries * 2;
        for (n4 = 0; n4 < this.entries; ++n4) {
            if (this.c.lengthlist[n4] <= 0) continue;
            n3 = 0;
            for (n2 = 0; n2 < this.c.lengthlist[n4] - 1; ++n2) {
                n = nArray3[n4] >>> n2 & 1;
                if (n == 0) {
                    if (nArray[n3] == 0) {
                        nArray[n3] = ++n5;
                    }
                    n3 = nArray[n3];
                    continue;
                }
                if (nArray2[n3] == 0) {
                    nArray2[n3] = ++n5;
                }
                n3 = nArray2[n3];
            }
            if ((nArray3[n4] >>> n2 & 1) == 0) {
                nArray[n3] = -n4;
                continue;
            }
            nArray2[n3] = -n4;
        }
        decodeAux.tabn = Util.ilog(this.entries) - 4;
        if (decodeAux.tabn < 5) {
            decodeAux.tabn = 5;
        }
        n4 = 1 << decodeAux.tabn;
        decodeAux.tab = new int[n4];
        decodeAux.tabl = new int[n4];
        for (n3 = 0; n3 < n4; ++n3) {
            n2 = 0;
            n = 0;
            for (n = 0; n < decodeAux.tabn && (n2 > 0 || n == 0); ++n) {
                if ((n3 & 1 << n) != 0) {
                    n2 = nArray2[n2];
                }
                else {
                    n2 = nArray[n2];
                }
            }
            decodeAux.tab[n3] = n2;
            decodeAux.tabl[n3] = n;
        }
        return decodeAux;
    }

    class DecodeAux {
        int[] tab;
        int[] tabl;
        int tabn;
        int[] ptr0;
        int[] ptr1;
        int aux;

        DecodeAux() {
        }
    }
}

