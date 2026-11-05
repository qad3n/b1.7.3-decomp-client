package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet52MultiBlockChange
extends Packet {
    public int xPosition;
    public int zPosition;
    public short[] coordinateArray;
    public byte[] typeArray;
    public byte[] metadataArray;
    public int size;

    public Packet52MultiBlockChange() {
        this.isChunkDataPacket = true;
    }

    public void readPacketData(DataInputStream input) throws IOException {
        this.xPosition = input.readInt();
        this.zPosition = input.readInt();
        this.size = input.readShort() & 0xFFFF;
        this.coordinateArray = new short[this.size];
        this.typeArray = new byte[this.size];
        this.metadataArray = new byte[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.coordinateArray[i] = input.readShort();
        }
        input.readFully(this.typeArray);
        input.readFully(this.metadataArray);
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeInt(this.xPosition);
        output.writeInt(this.zPosition);
        output.writeShort((short)this.size);
        for (int i = 0; i < this.size; ++i) {
            output.writeShort(this.coordinateArray[i]);
        }
        output.write(this.typeArray);
        output.write(this.metadataArray);
    }

    public void processPacket(NetHandler handler) {
        handler.handleMultiBlockChange(this);
    }

    public int getPacketSize() {
        return 10 + this.size * 4;
    }
}

