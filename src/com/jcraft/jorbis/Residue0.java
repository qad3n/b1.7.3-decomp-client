package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Residue0
extends FuncResidue {
    private static int[][][] _01inverse_partword = new int[2][][];
    static int[][] _2inverse_partword = null;

    Residue0() {
    }

    void pack(Object object, Buffer buffer) {
        int n;
        InfoResidue0 infoResidue0 = (InfoResidue0)object;
        int n2 = 0;
        buffer.write(infoResidue0.begin, 24);
        buffer.write(infoResidue0.end, 24);
        buffer.write(infoResidue0.grouping - 1, 24);
        buffer.write(infoResidue0.partitions - 1, 6);
        buffer.write(infoResidue0.groupbook, 8);
        for (n = 0; n < infoResidue0.partitions; ++n) {
            int n3 = infoResidue0.secondstages[n];
            if (Util.ilog(n3) > 3) {
                buffer.write(n3, 3);
                buffer.write(1, 1);
                buffer.write(n3 >>> 3, 5);
            } else {
                buffer.write(n3, 4);
            }
            n2 += Util.icount(n3);
        }
        for (n = 0; n < n2; ++n) {
            buffer.write(infoResidue0.booklist[n], 8);
        }
    }

    Object unpack(Info info, Buffer buffer) {
        int n;
        int n2 = 0;
        InfoResidue0 infoResidue0 = new InfoResidue0();
        infoResidue0.begin = buffer.read(24);
        infoResidue0.end = buffer.read(24);
        infoResidue0.grouping = buffer.read(24) + 1;
        infoResidue0.partitions = buffer.read(6) + 1;
        infoResidue0.groupbook = buffer.read(8);
        for (n = 0; n < infoResidue0.partitions; ++n) {
            int n3 = buffer.read(3);
            if (buffer.read(1) != 0) {
                n3 |= buffer.read(5) << 3;
            }
            infoResidue0.secondstages[n] = n3;
            n2 += Util.icount(n3);
        }
        for (n = 0; n < n2; ++n) {
            infoResidue0.booklist[n] = buffer.read(8);
        }
        if (infoResidue0.groupbook >= info.books) {
            this.free_info(infoResidue0);
            return null;
        }
        for (n = 0; n < n2; ++n) {
            if (infoResidue0.booklist[n] < info.books) continue;
            this.free_info(infoResidue0);
            return null;
        }
        return infoResidue0;
    }

    Object look(DspState dspState, InfoMode infoMode, Object object) {
        int n;
        int n2;
        int n3;
        int n4;
        InfoResidue0 infoResidue0 = (InfoResidue0)object;
        LookResidue0 lookResidue0 = new LookResidue0();
        int n5 = 0;
        int n6 = 0;
        lookResidue0.info = infoResidue0;
        lookResidue0.map = infoMode.mapping;
        lookResidue0.parts = infoResidue0.partitions;
        lookResidue0.fullbooks = dspState.fullbooks;
        lookResidue0.phrasebook = dspState.fullbooks[infoResidue0.groupbook];
        int n7 = lookResidue0.phrasebook.dim;
        lookResidue0.partbooks = new int[lookResidue0.parts][];
        for (n4 = 0; n4 < lookResidue0.parts; ++n4) {
            n3 = infoResidue0.secondstages[n4];
            n2 = Util.ilog(n3);
            if (n2 == 0) continue;
            if (n2 > n6) {
                n6 = n2;
            }
            lookResidue0.partbooks[n4] = new int[n2];
            for (n = 0; n < n2; ++n) {
                if ((n3 & 1 << n) == 0) continue;
                lookResidue0.partbooks[n4][n] = infoResidue0.booklist[n5++];
            }
        }
        lookResidue0.partvals = (int)Math.rint(Math.pow(lookResidue0.parts, n7));
        lookResidue0.stages = n6;
        lookResidue0.decodemap = new int[lookResidue0.partvals][];
        for (n4 = 0; n4 < lookResidue0.partvals; ++n4) {
            n3 = n4;
            n2 = lookResidue0.partvals / lookResidue0.parts;
            lookResidue0.decodemap[n4] = new int[n7];
            for (n = 0; n < n7; ++n) {
                int n8 = n3 / n2;
                n3 -= n8 * n2;
                n2 /= lookResidue0.parts;
                lookResidue0.decodemap[n4][n] = n8;
            }
        }
        return lookResidue0;
    }

    void free_info(Object object) {
    }

    void free_look(Object object) {
    }

    static synchronized int _01inverse(Block block, Object object, float[][] fArray, int n, int n2) {
        int n3;
        LookResidue0 lookResidue0 = (LookResidue0)object;
        InfoResidue0 infoResidue0 = lookResidue0.info;
        int n4 = infoResidue0.grouping;
        int n5 = lookResidue0.phrasebook.dim;
        int n6 = infoResidue0.end - infoResidue0.begin;
        int n7 = n6 / n4;
        int n8 = (n7 + n5 - 1) / n5;
        if (_01inverse_partword.length < n) {
            _01inverse_partword = new int[n][][];
        }
        for (n3 = 0; n3 < n; ++n3) {
            if (_01inverse_partword[n3] != null && _01inverse_partword[n3].length >= n8) continue;
            Residue0._01inverse_partword[n3] = new int[n8][];
        }
        for (int i = 0; i < lookResidue0.stages; ++i) {
            int n9 = 0;
            int n10 = 0;
            while (n9 < n7) {
                int n11;
                if (i == 0) {
                    for (n3 = 0; n3 < n; ++n3) {
                        n11 = lookResidue0.phrasebook.decode(block.opb);
                        if (n11 == -1) {
                            return 0;
                        }
                        Residue0._01inverse_partword[n3][n10] = lookResidue0.decodemap[n11];
                        if (_01inverse_partword[n3][n10] != null) continue;
                        return 0;
                    }
                }
                for (int j = 0; j < n5 && n9 < n7; ++j, ++n9) {
                    for (n3 = 0; n3 < n; ++n3) {
                        CodeBook codeBook;
                        n11 = infoResidue0.begin + n9 * n4;
                        int n12 = _01inverse_partword[n3][n10][j];
                        if ((infoResidue0.secondstages[n12] & 1 << i) == 0 || (codeBook = lookResidue0.fullbooks[lookResidue0.partbooks[n12][i]]) == null || !(n2 == 0 ? codeBook.decodevs_add(fArray[n3], n11, block.opb, n4) == -1 : n2 == 1 && codeBook.decodev_add(fArray[n3], n11, block.opb, n4) == -1)) continue;
                        return 0;
                    }
                }
                ++n10;
            }
        }
        return 0;
    }

    static synchronized int _2inverse(Block block, Object object, float[][] fArray, int n) {
        LookResidue0 lookResidue0 = (LookResidue0)object;
        InfoResidue0 infoResidue0 = lookResidue0.info;
        int n2 = infoResidue0.grouping;
        int n3 = lookResidue0.phrasebook.dim;
        int n4 = infoResidue0.end - infoResidue0.begin;
        int n5 = n4 / n2;
        int n6 = (n5 + n3 - 1) / n3;
        if (_2inverse_partword == null || _2inverse_partword.length < n6) {
            _2inverse_partword = new int[n6][];
        }
        for (int i = 0; i < lookResidue0.stages; ++i) {
            int n7 = 0;
            int n8 = 0;
            while (n7 < n5) {
                int n9;
                if (i == 0) {
                    n9 = lookResidue0.phrasebook.decode(block.opb);
                    if (n9 == -1) {
                        return 0;
                    }
                    Residue0._2inverse_partword[n8] = lookResidue0.decodemap[n9];
                    if (_2inverse_partword[n8] == null) {
                        return 0;
                    }
                }
                for (int j = 0; j < n3 && n7 < n5; ++j, ++n7) {
                    CodeBook codeBook;
                    n9 = infoResidue0.begin + n7 * n2;
                    int n10 = _2inverse_partword[n8][j];
                    if ((infoResidue0.secondstages[n10] & 1 << i) == 0 || (codeBook = lookResidue0.fullbooks[lookResidue0.partbooks[n10][i]]) == null || codeBook.decodevv_add(fArray, n9, n, block.opb, n2) != -1) continue;
                    return 0;
                }
                ++n8;
            }
        }
        return 0;
    }

    int inverse(Block block, Object object, float[][] fArray, int[] nArray, int n) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (nArray[i] == 0) continue;
            fArray[n2++] = fArray[i];
        }
        if (n2 != 0) {
            return Residue0._01inverse(block, object, fArray, n2, 0);
        }
        return 0;
    }

    class InfoResidue0 {
        int begin;
        int end;
        int grouping;
        int partitions;
        int groupbook;
        int[] secondstages = new int[64];
        int[] booklist = new int[256];
        float[] entmax = new float[64];
        float[] ampmax = new float[64];
        int[] subgrp = new int[64];
        int[] blimit = new int[64];

        InfoResidue0() {
        }
    }

    class LookResidue0 {
        InfoResidue0 info;
        int map;
        int parts;
        int stages;
        CodeBook[] fullbooks;
        CodeBook phrasebook;
        int[][] partbooks;
        int partvals;
        int[][] decodemap;
        int postbits;
        int phrasebits;
        int frames;

        LookResidue0() {
        }
    }
}

