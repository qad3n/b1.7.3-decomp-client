package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet29DestroyEntity
extends Packet {
    public int entityId;

    public void readPacketData(DataInputStream input) throws IOException {
        this.entityId = input.readInt();
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeInt(this.entityId);
    }

    public void processPacket(NetHandler handler) {
        handler.handleDestroyEntity(this);
    }

    public int getPacketSize() {
        return 4;
    }
}

