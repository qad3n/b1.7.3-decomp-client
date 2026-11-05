package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet100OpenWindow
extends Packet {
    public int windowId;
    public int inventoryType;
    public String windowTitle;
    public int slotsCount;

    public void processPacket(NetHandler handler) {
        handler.handleOpenInventoryMenu(this);
    }

    public void readPacketData(DataInputStream input) throws IOException {
        this.windowId = input.readByte();
        this.inventoryType = input.readByte();
        this.windowTitle = input.readUTF();
        this.slotsCount = input.readByte();
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeByte(this.windowId);
        output.writeByte(this.inventoryType);
        output.writeUTF(this.windowTitle);
        output.writeByte(this.slotsCount);
    }

    public int getPacketSize() {
        return 3 + this.windowTitle.length();
    }
}

