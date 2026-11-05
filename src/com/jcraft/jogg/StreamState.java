package com.jcraft.jogg;

public class StreamState {
    byte[] body_data;
    int body_storage;
    int body_fill;
    private int body_returned;
    int[] lacing_vals;
    long[] granule_vals;
    int lacing_storage;
    int lacing_fill;
    int lacing_packet;
    int lacing_returned;
    byte[] header = new byte[282];
    int header_fill;
    public int e_o_s;
    int b_o_s;
    int serialno;
    int pageno;
    long packetno;
    long granulepos;

    public StreamState() {
        this.init();
    }

    StreamState(int var1) {
        this();
        this.init(var1);
    }

    void init() {
        this.body_storage = 16384;
        this.body_data = new byte[this.body_storage];
        this.lacing_storage = 1024;
        this.lacing_vals = new int[this.lacing_storage];
        this.granule_vals = new long[this.lacing_storage];
    }

    public void init(int var1) {
        if (this.body_data == null) {
            this.init();
        } else {
            for (int i = 0; i < this.body_data.length; i++) {
                this.body_data[i] = 0;
            }

            for (int j = 0; j < this.lacing_vals.length; j++) {
                this.lacing_vals[j] = 0;
            }

            for (int k = 0; k < this.granule_vals.length; k++) {
                this.granule_vals[k] = 0L;
            }
        }

        this.serialno = var1;
    }

    public void clear() {
        this.body_data = null;
        this.lacing_vals = null;
        this.granule_vals = null;
    }

    void destroy() {
        this.clear();
    }

    void body_expand(int var1) {
        if (this.body_storage <= this.body_fill + var1) {
            this.body_storage += var1 + 1024;
            byte[] abyte = new byte[this.body_storage];
            System.arraycopy(this.body_data, 0, abyte, 0, this.body_data.length);
            this.body_data = abyte;
        }
    }

    void lacing_expand(int var1) {
        if (this.lacing_storage <= this.lacing_fill + var1) {
            this.lacing_storage += var1 + 32;
            int[] aint = new int[this.lacing_storage];
            System.arraycopy(this.lacing_vals, 0, aint, 0, this.lacing_vals.length);
            this.lacing_vals = aint;
            long[] along = new long[this.lacing_storage];
            System.arraycopy(this.granule_vals, 0, along, 0, this.granule_vals.length);
            this.granule_vals = along;
        }
    }

    public int packetin(Packet var1) {
        int i = var1.bytes / 255 + 1;
        if (this.body_returned != 0) {
            this.body_fill = this.body_fill - this.body_returned;
            if (this.body_fill != 0) {
                System.arraycopy(this.body_data, this.body_returned, this.body_data, 0, this.body_fill);
            }

            this.body_returned = 0;
        }

        this.body_expand(var1.bytes);
        this.lacing_expand(i);
        System.arraycopy(var1.packet_base, var1.packet, this.body_data, this.body_fill, var1.bytes);
        this.body_fill = this.body_fill + var1.bytes;

        int j;
        for (j = 0; j < i - 1; j++) {
            this.lacing_vals[this.lacing_fill + j] = 255;
            this.granule_vals[this.lacing_fill + j] = this.granulepos;
        }

        this.lacing_vals[this.lacing_fill + j] = var1.bytes % 255;
        this.granulepos = this.granule_vals[this.lacing_fill + j] = var1.granulepos;
        this.lacing_vals[this.lacing_fill] = this.lacing_vals[this.lacing_fill] | 256;
        this.lacing_fill += i;
        this.packetno++;
        if (var1.e_o_s != 0) {
            this.e_o_s = 1;
        }

        return 0;
    }

    public int packetout(Packet var1) {
        int i = this.lacing_returned;
        if (this.lacing_packet <= i) {
            return 0;
        }

        if ((this.lacing_vals[i] & 1024) != 0) {
            this.lacing_returned++;
            this.packetno++;
            return -1;
        }

        int j = this.lacing_vals[i] & 0xFF;
        int k = 0;
        var1.packet_base = this.body_data;
        var1.packet = this.body_returned;
        var1.e_o_s = this.lacing_vals[i] & 512;
        var1.b_o_s = this.lacing_vals[i] & 256;

        for (k += j; j == 255; k += j) {
            int l = this.lacing_vals[++i];
            j = l & 0xFF;
            if ((l & 512) != 0) {
                var1.e_o_s = 512;
            }
        }

        var1.packetno = this.packetno;
        var1.granulepos = this.granule_vals[i];
        var1.bytes = k;
        this.body_returned += k;
        this.lacing_returned = i + 1;
        this.packetno++;
        return 1;
    }

    public int pagein(Page var1) {
        byte[] abyte = var1.header_base;
        int i = var1.header;
        byte[] abyte1 = var1.body_base;
        int j = var1.body;
        int k = var1.body_len;
        int l = 0;
        int i1 = var1.version();
        int j1 = var1.continued();
        int k1 = var1.bos();
        int l1 = var1.eos();
        long i2 = var1.granulepos();
        int j2 = var1.serialno();
        int k2 = var1.pageno();
        int l2 = abyte[i + 26] & 255;
        int i3 = this.lacing_returned;
        int j3 = this.body_returned;
        if (j3 != 0) {
            this.body_fill -= j3;
            if (this.body_fill != 0) {
                System.arraycopy(this.body_data, j3, this.body_data, 0, this.body_fill);
            }

            this.body_returned = 0;
        }

        if (i3 != 0) {
            if (this.lacing_fill - i3 != 0) {
                System.arraycopy(this.lacing_vals, i3, this.lacing_vals, 0, this.lacing_fill - i3);
                System.arraycopy(this.granule_vals, i3, this.granule_vals, 0, this.lacing_fill - i3);
            }

            this.lacing_fill -= i3;
            this.lacing_packet -= i3;
            this.lacing_returned = 0;
        }

        if (j2 != this.serialno) {
            return -1;
        }

        if (i1 > 0) {
            return -1;
        }

        this.lacing_expand(l2 + 1);
        if (k2 != this.pageno) {
            for (int k3 = this.lacing_packet; k3 < this.lacing_fill; k3++) {
                this.body_fill -= this.lacing_vals[k3] & 0xFF;
            }

            this.lacing_fill = this.lacing_packet;
            if (this.pageno != -1) {
                this.lacing_vals[this.lacing_fill++] = 1024;
                this.lacing_packet++;
            }

            if (j1 != 0) {
                k1 = 0;

                while (l < l2) {
                    j3 = abyte[i + 27 + l] & 255;
                    j += j3;
                    k -= j3;
                    if (j3 < 255) {
                        l++;
                        break;
                    }

                    l++;
                }
            }
        }

        if (k != 0) {
            this.body_expand(k);
            System.arraycopy(abyte1, j, this.body_data, this.body_fill, k);
            this.body_fill += k;
        }

        i3 = -1;

        while (l < l2) {
            j3 = abyte[i + 27 + l] & 255;
            this.lacing_vals[this.lacing_fill] = j3;
            this.granule_vals[this.lacing_fill] = -1L;
            if (k1 != 0) {
                this.lacing_vals[this.lacing_fill] |= 256;
                k1 = 0;
            }

            if (j3 < 255) {
                i3 = this.lacing_fill;
            }

            this.lacing_fill++;
            l++;
            if (j3 < 255) {
                this.lacing_packet = this.lacing_fill;
            }
        }

        if (i3 != -1) {
            this.granule_vals[i3] = i2;
        }

        if (l1 != 0) {
            this.e_o_s = 1;
            if (this.lacing_fill > 0) {
                this.lacing_vals[this.lacing_fill - 1] |= 512;
            }
        }

        this.pageno = k2 + 1;
        return 0;
    }

    public int flush(Page var1) {
        int j = 0;
        int k = this.lacing_fill > 255 ? 255 : this.lacing_fill;
        int l = 0;
        int i1 = 0;
        long j1 = this.granule_vals[0];
        if (k == 0) {
            return 0;
        }

        if (this.b_o_s == 0) {
            j1 = 0L;

            for (j = 0; j < k; j++) {
                if ((this.lacing_vals[j] & 0xFF) < 255) {
                    j++;
                    break;
                }
            }
        } else {
            for (j = 0; j < k && i1 <= 4096; j++) {
                i1 += this.lacing_vals[j] & 0xFF;
                j1 = this.granule_vals[j];
            }
        }

        System.arraycopy("OggS".getBytes(), 0, this.header, 0, 4);
        this.header[4] = 0;
        this.header[5] = 0;
        if ((this.lacing_vals[0] & 256) == 0) {
            this.header[5] = (byte)(this.header[5] | 1);
        }

        if (this.b_o_s == 0) {
            this.header[5] = (byte)(this.header[5] | 2);
        }

        if (this.e_o_s != 0 && this.lacing_fill == j) {
            this.header[5] = (byte)(this.header[5] | 4);
        }

        this.b_o_s = 1;

        for (int i = 6; i < 14; i++) {
            this.header[i] = (byte)j1;
            j1 >>>= 8;
        }

        int k1 = this.serialno;

        for (int l1 = 14; l1 < 18; l1++) {
            this.header[l1] = (byte)k1;
            k1 >>>= 8;
        }

        if (this.pageno == -1) {
            this.pageno = 0;
        }

        k1 = this.pageno++;

        for (int i2 = 18; i2 < 22; i2++) {
            this.header[i2] = (byte)k1;
            k1 >>>= 8;
        }

        this.header[22] = 0;
        this.header[23] = 0;
        this.header[24] = 0;
        this.header[25] = 0;
        this.header[26] = (byte)j;

        for (int j2 = 0; j2 < j; j2++) {
            this.header[j2 + 27] = (byte)this.lacing_vals[j2];
            l += this.header[j2 + 27] & 255;
        }

        var1.header_base = this.header;
        var1.header = 0;
        var1.header_len = this.header_fill = j + 27;
        var1.body_base = this.body_data;
        var1.body = this.body_returned;
        var1.body_len = l;
        this.lacing_fill -= j;
        System.arraycopy(this.lacing_vals, j, this.lacing_vals, 0, this.lacing_fill * 4);
        System.arraycopy(this.granule_vals, j, this.granule_vals, 0, this.lacing_fill * 8);
        this.body_returned += l;
        var1.checksum();
        return 1;
    }

    public int pageout(Page var1) {
        return (this.e_o_s == 0 || this.lacing_fill == 0)
                && this.body_fill - this.body_returned <= 4096
                && this.lacing_fill < 255
                && (this.lacing_fill == 0 || this.b_o_s != 0)
            ? 0
            : this.flush(var1);
    }

    public int eof() {
        return this.e_o_s;
    }

    public int reset() {
        this.body_fill = 0;
        this.body_returned = 0;
        this.lacing_fill = 0;
        this.lacing_packet = 0;
        this.lacing_returned = 0;
        this.header_fill = 0;
        this.e_o_s = 0;
        this.b_o_s = 0;
        this.pageno = -1;
        this.packetno = 0L;
        this.granulepos = 0L;
        return 0;
    }
}
