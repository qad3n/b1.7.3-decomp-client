package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn
extends Packet {
    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int velocityX;
    public int velocityY;
    public int velocityZ;
    public int type;
    public int data;

    public void readPacketData(DataInputStream input) throws IOException {
        this.entityId = input.readInt();
        this.type = input.readByte();
        this.xPosition = input.readInt();
        this.yPosition = input.readInt();
        this.zPosition = input.readInt();
        this.data = input.readInt();
        if (this.data > 0) {
            this.velocityX = input.readShort();
            this.velocityY = input.readShort();
            this.velocityZ = input.readShort();
        }
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeInt(this.entityId);
        output.writeByte(this.type);
        output.writeInt(this.xPosition);
        output.writeInt(this.yPosition);
        output.writeInt(this.zPosition);
        output.writeInt(this.data);
        if (this.data > 0) {
            output.writeShort(this.velocityX);
            output.writeShort(this.velocityY);
            output.writeShort(this.velocityZ);
        }
    }

    public void processPacket(NetHandler handler) {
        handler.handleVehicleSpawn(this);
    }

    public int getPacketSize() {
        return 21 + this.data > 0 ? 6 : 0;
    }
}

