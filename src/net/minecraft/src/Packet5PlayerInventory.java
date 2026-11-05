package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory
extends Packet {
    public int entityID;
    public int slot;
    public int itemID;
    public int itemDamage;

    public void readPacketData(DataInputStream input) throws IOException {
        this.entityID = input.readInt();
        this.slot = input.readShort();
        this.itemID = input.readShort();
        this.itemDamage = input.readShort();
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeInt(this.entityID);
        output.writeShort(this.slot);
        output.writeShort(this.itemID);
        output.writeShort(this.itemDamage);
    }

    public void processPacket(NetHandler handler) {
        handler.handlePlayerInventory(this);
    }

    public int getPacketSize() {
        return 8;
    }
}

