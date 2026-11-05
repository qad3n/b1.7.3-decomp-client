package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Packet40EntityMetadata
extends Packet {
    public int entityId;
    private List<DataWatcher.WatchableObject> dataEntries;

    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        this.entityId = input.readInt();
        this.dataEntries = DataWatcher.readWatchableObjects(input);
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeInt(this.entityId);
        DataWatcher.writeObjectsInListToStream(this.dataEntries, output);
    }

    @Override
    public void processPacket(NetHandler handler) {
        handler.handleEntityData(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }

    public List<DataWatcher.WatchableObject> getDataEntries() {
        return this.dataEntries;
    }
}

