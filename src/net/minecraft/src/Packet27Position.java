package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet27Position
extends Packet {
    private float sidewaysSpeed;
    private float forwardSpeed;
    private boolean jumping;
    private boolean sneaking;
    private float pitch;
    private float yaw;

    public void readPacketData(DataInputStream input) throws IOException {
        this.sidewaysSpeed = input.readFloat();
        this.forwardSpeed = input.readFloat();
        this.pitch = input.readFloat();
        this.yaw = input.readFloat();
        this.jumping = input.readBoolean();
        this.sneaking = input.readBoolean();
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeFloat(this.sidewaysSpeed);
        output.writeFloat(this.forwardSpeed);
        output.writeFloat(this.pitch);
        output.writeFloat(this.yaw);
        output.writeBoolean(this.jumping);
        output.writeBoolean(this.sneaking);
    }

    public void processPacket(NetHandler handler) {
        handler.handlePlayerInput(this);
    }

    public int getPacketSize() {
        return 18;
    }
}

