package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Packet60Explosion
extends Packet {
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;
    public Set<ChunkPosition> destroyedBlockPositions;

    public void readPacketData(DataInputStream input) throws IOException {
        this.explosionX = input.readDouble();
        this.explosionY = input.readDouble();
        this.explosionZ = input.readDouble();
        this.explosionSize = input.readFloat();
        int n = input.readInt();
        this.destroyedBlockPositions = new HashSet<ChunkPosition>();
        int n2 = (int)this.explosionX;
        int n3 = (int)this.explosionY;
        int n4 = (int)this.explosionZ;
        for (int i = 0; i < n; ++i) {
            int n5 = input.readByte() + n2;
            int n6 = input.readByte() + n3;
            int n7 = input.readByte() + n4;
            this.destroyedBlockPositions.add(new ChunkPosition(n5, n6, n7));
        }
    }

    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeDouble(this.explosionX);
        output.writeDouble(this.explosionY);
        output.writeDouble(this.explosionZ);
        output.writeFloat(this.explosionSize);
        output.writeInt(this.destroyedBlockPositions.size());
        int n = (int)this.explosionX;
        int n2 = (int)this.explosionY;
        int n3 = (int)this.explosionZ;
        for (ChunkPosition chunkPosition : this.destroyedBlockPositions) {
            int n4 = chunkPosition.x - n;
            int n5 = chunkPosition.y - n2;
            int n6 = chunkPosition.z - n3;
            output.writeByte(n4);
            output.writeByte(n5);
            output.writeByte(n6);
        }
    }

    public void processPacket(NetHandler handler) {
        handler.handleExplosion(this);
    }

    public int getPacketSize() {
        return 32 + this.destroyedBlockPositions.size() * 3;
    }
}

