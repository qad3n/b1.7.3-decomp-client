package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet53BlockChange
extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int type;
    public int metadata;

    public Packet53BlockChange() {
        this.isChunkDataPacket = true;
    }

    public void readPacketData(DataInputStream input) throws IOException {
        this.xPosition = input.readInt();
        this.yPosition = input.read();
        this.zPosition = input.readInt();
        this.type = input.read();
        this.metadata = input.read();
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeInt(this.xPosition);
        output.write(this.yPosition);
        output.writeInt(this.zPosition);
        output.write(this.type);
        output.write(this.metadata);
    }

    public void processPacket(NetHandler handler) {
        handler.handleBlockChange(this);
    }

    public int getPacketSize() {
        return 11;
    }
}

