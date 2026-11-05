package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import java.io.Reader;

public class NetClientHandler
extends NetHandler {
    private boolean disconnected = false;
    private NetworkManager netManager;
    public String message;
    private Minecraft mc;
    private WorldClient worldClient;
    private boolean started = false;
    public MapStorage savedDataStorage = new MapStorage(null);
    Random rand = new Random();

    public NetClientHandler(Minecraft minecraft, String address, int port) throws IOException, UnknownHostException {
        this.mc = minecraft;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        this.netManager = new NetworkManager(socket, "Client", this);
    }

    public void processReadPackets() {
        if (!this.disconnected) {
            this.netManager.processReadPackets();
        }
        this.netManager.wakeThreads();
    }

    public void handleLogin(Packet1Login packet) {
        this.mc.playerController = new PlayerControllerMP(this.mc, this);
        this.mc.statFileWriter.readStat(StatList.joinMultiplayerStat, 1);
        this.worldClient = new WorldClient(this, packet.mapSeed, packet.dimension);
        this.worldClient.multiplayerWorld = true;
        this.mc.changeWorld1(this.worldClient);
        this.mc.thePlayer.dimension = packet.dimension;
        this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
        this.mc.thePlayer.entityId = packet.protocolVersion;
    }

    public void handlePickupSpawn(Packet21PickupSpawn packet) {
        double d = (double)packet.xPosition / 32.0;
        double d2 = (double)packet.yPosition / 32.0;
        double d3 = (double)packet.zPosition / 32.0;
        EntityItem entityItem = new EntityItem(this.worldClient, d, d2, d3, new ItemStack(packet.itemID, packet.count, packet.itemDamage));
        entityItem.motionX = (double)packet.rotation / 128.0;
        entityItem.motionY = (double)packet.pitch / 128.0;
        entityItem.motionZ = (double)packet.roll / 128.0;
        entityItem.serverPosX = packet.xPosition;
        entityItem.serverPosY = packet.yPosition;
        entityItem.serverPosZ = packet.zPosition;
        this.worldClient.forceEntity(packet.entityId, entityItem);
    }

    @Override
public void handleVehicleSpawn(Packet23VehicleSpawn packet) {
    double d0 = packet.xPosition / 32.0;
    double d1 = packet.yPosition / 32.0;
    double d2 = packet.zPosition / 32.0;
    Entity entity = null;
    if (packet.type == 10) {
        entity = new EntityMinecart(this.worldClient, d0, d1, d2, 0);
    }
    if (packet.type == 11) {
        entity = new EntityMinecart(this.worldClient, d0, d1, d2, 1);
    }
    if (packet.type == 12) {
        entity = new EntityMinecart(this.worldClient, d0, d1, d2, 2);
    }
    if (packet.type == 90) {
        entity = new EntityFish(this.worldClient, d0, d1, d2);
    }
    if (packet.type == 60) {
        entity = new EntityArrow(this.worldClient, d0, d1, d2);
    }
    if (packet.type == 61) {
        entity = new EntitySnowball(this.worldClient, d0, d1, d2);
    }
    if (packet.type == 63) {
        entity = new EntityFireball(this.worldClient, d0, d1, d2, packet.velocityX / 8000.0, packet.velocityY / 8000.0, packet.velocityZ / 8000.0);
        packet.data = 0;
    }
    if (packet.type == 62) {
        entity = new EntityEgg(this.worldClient, d0, d1, d2);
    }
    if (packet.type == 1) {
        entity = new EntityBoat(this.worldClient, d0, d1, d2);
    }
    if (packet.type == 50) {
        entity = new EntityTNTPrimed(this.worldClient, d0, d1, d2);
    }
    if (packet.type == 70) {
        entity = new EntityFallingSand(this.worldClient, d0, d1, d2, Block.sand.blockID);
    }
    if (packet.type == 71) {
        entity = new EntityFallingSand(this.worldClient, d0, d1, d2, Block.gravel.blockID);
    }
    if (entity != null) {
        entity.serverPosX = packet.xPosition;
        entity.serverPosY = packet.yPosition;
        entity.serverPosZ = packet.zPosition;
        entity.rotationYaw = 0.0F;
        entity.rotationPitch = 0.0F;
        entity.entityId = packet.entityId;
        this.worldClient.forceEntity(packet.entityId, entity);
        if (packet.data > 0) {
            if (packet.type == 60) {
                Entity entity1 = this.getEntityByID(packet.data);
                if (entity1 instanceof EntityLiving) {
                    ((EntityArrow) entity).owner = (EntityLiving) entity1;
                }
            }
            entity.setVelocity(packet.velocityX / 8000.0, packet.velocityY / 8000.0, packet.velocityZ / 8000.0);
        }
    }
}

    @Override
public void handleWeather(final Packet71Weather packet) {
    final double x = packet.x / 32.0;
    final double y = packet.y / 32.0;
    final double z = packet.z / 32.0;
    Entity entity = null;
    if (packet.type == 1) {
        entity = new EntityLightningBolt(this.worldClient, x, y, z);
    }
    if (entity != null) {
        entity.serverPosX = packet.x;
        entity.serverPosY = packet.y;
        entity.serverPosZ = packet.z;
        entity.rotationYaw = 0.0f;
        entity.rotationPitch = 0.0f;
        entity.entityId = packet.id;
        this.worldClient.addWeatherEffect(entity);
    }
}

    public void handleAddPainting(Packet25EntityPainting packet) {
        EntityPainting entityPainting = new EntityPainting(this.worldClient, packet.xPosition, packet.yPosition, packet.zPosition, packet.direction, packet.title);
        this.worldClient.forceEntity(packet.entityId, entityPainting);
    }

    public void handleEntityVelocity(Packet28EntityVelocity packet) {
        Entity entity = this.getEntityByID(packet.entityId);
        if (entity == null) {
            return;
        }
        entity.setVelocity((double)packet.motionX / 8000.0, (double)packet.motionY / 8000.0, (double)packet.motionZ / 8000.0);
    }

    public void handleEntityData(Packet40EntityMetadata packet) {
        Entity entity = this.getEntityByID(packet.entityId);
        if (entity != null && packet.getDataEntries() != null) {
            entity.getDataWatcher().updateWatchedObjectsFromList(packet.getDataEntries());
        }
    }

    @Override
public void handleNamedEntitySpawn(Packet20NamedEntitySpawn packet) {
    double d0 = packet.xPosition / 32.0;
    double d1 = packet.yPosition / 32.0;
    double d2 = packet.zPosition / 32.0;
    float f = packet.rotation * 360 / 256.0F;
    float f1 = packet.pitch * 360 / 256.0F;
    EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.mc.theWorld, packet.name);
    entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = entityotherplayermp.serverPosX = packet.xPosition;
    entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = entityotherplayermp.serverPosY = packet.yPosition;
    entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = entityotherplayermp.serverPosZ = packet.zPosition;
    int i = packet.currentItem;
    if (i == 0) {
        entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
    } else {
        entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(i, 1, 0);
    }
    entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
    this.worldClient.forceEntity(packet.entityId, entityotherplayermp);
}

    public void handleEntityTeleport(Packet34EntityTeleport packet) {
        Entity entity = this.getEntityByID(packet.entityId);
        if (entity == null) {
            return;
        }
        entity.serverPosX = packet.xPosition;
        entity.serverPosY = packet.yPosition;
        entity.serverPosZ = packet.zPosition;
        double d = (double)entity.serverPosX / 32.0;
        double d2 = (double)entity.serverPosY / 32.0 + 0.015625;
        double d3 = (double)entity.serverPosZ / 32.0;
        float f = (float)(packet.yaw * 360) / 256.0f;
        float f2 = (float)(packet.pitch * 360) / 256.0f;
        entity.setPositionAndRotation2(d, d2, d3, f, f2, 3);
    }

    public void handleEntity(Packet30Entity packet) {
        Entity entity = this.getEntityByID(packet.entityId);
        if (entity == null) {
            return;
        }
        entity.serverPosX += packet.xPosition;
        entity.serverPosY += packet.yPosition;
        entity.serverPosZ += packet.zPosition;
        double d = (double)entity.serverPosX / 32.0;
        double d2 = (double)entity.serverPosY / 32.0;
        double d3 = (double)entity.serverPosZ / 32.0;
        float f = packet.rotating ? (float)(packet.yaw * 360) / 256.0f : entity.rotationYaw;
        float f2 = packet.rotating ? (float)(packet.pitch * 360) / 256.0f : entity.rotationPitch;
        entity.setPositionAndRotation2(d, d2, d3, f, f2, 3);
    }

    public void handleDestroyEntity(Packet29DestroyEntity packet) {
        this.worldClient.removeEntityFromWorld(packet.entityId);
    }

    public void handleFlying(Packet10Flying packet) {
        EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
        double d = entityPlayerSP.posX;
        double d2 = entityPlayerSP.posY;
        double d3 = entityPlayerSP.posZ;
        float f = entityPlayerSP.rotationYaw;
        float f2 = entityPlayerSP.rotationPitch;
        if (packet.moving) {
            d = packet.xPosition;
            d2 = packet.yPosition;
            d3 = packet.zPosition;
        }
        if (packet.rotating) {
            f = packet.yaw;
            f2 = packet.pitch;
        }
        entityPlayerSP.ySize = 0.0f;
        entityPlayerSP.motionZ = 0.0;
        entityPlayerSP.motionY = 0.0;
        entityPlayerSP.motionX = 0.0;
        entityPlayerSP.setPositionAndRotation(d, d2, d3, f, f2);
        packet.xPosition = entityPlayerSP.posX;
        packet.yPosition = entityPlayerSP.boundingBox.minY;
        packet.zPosition = entityPlayerSP.posZ;
        packet.stance = entityPlayerSP.posY;
        this.netManager.addToSendQueue(packet);
        if (!this.started) {
            this.mc.thePlayer.prevPosX = this.mc.thePlayer.posX;
            this.mc.thePlayer.prevPosY = this.mc.thePlayer.posY;
            this.mc.thePlayer.prevPosZ = this.mc.thePlayer.posZ;
            this.started = true;
            this.mc.displayGuiScreen(null);
        }
    }

    public void handlePreChunk(Packet50PreChunk packet) {
        this.worldClient.doPreChunk(packet.xPosition, packet.yPosition, packet.mode);
    }

    public void handleMultiBlockChange(Packet52MultiBlockChange packet) {
        Chunk chunk = this.worldClient.getChunkFromChunkCoords(packet.xPosition, packet.zPosition);
        int n = packet.xPosition * 16;
        int n2 = packet.zPosition * 16;
        for (int i = 0; i < packet.size; ++i) {
            short s = packet.coordinateArray[i];
            int n3 = packet.typeArray[i] & 0xFF;
            byte by = packet.metadataArray[i];
            int n4 = s >> 12 & 0xF;
            int n5 = s >> 8 & 0xF;
            int n6 = s & 0xFF;
            chunk.setBlockIDWithMetadata(n4, n6, n5, n3, by);
            this.worldClient.clearBlockResets(n4 + n, n6, n5 + n2, n4 + n, n6, n5 + n2);
            this.worldClient.markBlocksDirty(n4 + n, n6, n5 + n2, n4 + n, n6, n5 + n2);
        }
    }

    public void handleMapChunk(Packet51MapChunk packet) {
        this.worldClient.clearBlockResets(packet.xPosition, packet.yPosition, packet.zPosition, packet.xPosition + packet.xSize - 1, packet.yPosition + packet.ySize - 1, packet.zPosition + packet.zSize - 1);
        this.worldClient.setChunkData(packet.xPosition, packet.yPosition, packet.zPosition, packet.xSize, packet.ySize, packet.zSize, packet.chunk);
    }

    public void handleBlockChange(Packet53BlockChange packet) {
        this.worldClient.setBlockWithMetadataFromPacket(packet.xPosition, packet.yPosition, packet.zPosition, packet.type, packet.metadata);
    }

    public void handleKickDisconnect(Packet255KickDisconnect packet) {
        this.netManager.networkShutdown("disconnect.kicked", new Object[0]);
        this.disconnected = true;
        this.mc.changeWorld1(null);
        this.mc.displayGuiScreen(new GuiConnectFailed("disconnect.disconnected", "disconnect.genericReason", packet.reason));
    }

    public void handleErrorMessage(String reason, Object[] args) {
        if (this.disconnected) {
            return;
        }
        this.disconnected = true;
        this.mc.changeWorld1(null);
        this.mc.displayGuiScreen(new GuiConnectFailed("disconnect.lost", reason, args));
    }

    public void sendPacketAndDisconnect(Packet packet) {
        if (this.disconnected) {
            return;
        }
        this.netManager.addToSendQueue(packet);
        this.netManager.close();
    }

    public void addToSendQueue(Packet packet) {
        if (this.disconnected) {
            return;
        }
        this.netManager.addToSendQueue(packet);
    }

    public void handleCollect(Packet22Collect packet) {
        Entity entity = this.getEntityByID(packet.collectedEntityId);
        EntityLiving entityLiving = (EntityLiving)this.getEntityByID(packet.collectorEntityId);
        if (entityLiving == null) {
            entityLiving = this.mc.thePlayer;
        }
        if (entity != null) {
            this.worldClient.playSoundAtEntity(entity, "random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, entityLiving, -0.5f));
            this.worldClient.removeEntityFromWorld(packet.collectedEntityId);
        }
    }

    public void handleChat(Packet3Chat packet) {
        this.mc.ingameGUI.addChatMessage(packet.message);
    }

    public void handleArmAnimation(Packet18Animation packet) {
        Entity entity = this.getEntityByID(packet.entityId);
        if (entity == null) {
            return;
        }
        if (packet.animate == 1) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            entityPlayer.swingItem();
        } else if (packet.animate == 2) {
            entity.performHurtAnimation();
        } else if (packet.animate == 3) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            entityPlayer.wakeUpPlayer(false, false, false);
        } else if (packet.animate == 4) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            entityPlayer.addRespawnParticles();
        }
    }

    public void handlePlayerSleep(Packet17Sleep packet) {
        Entity entity = this.getEntityByID(packet.id);
        if (entity == null) {
            return;
        }
        if (packet.action == 0) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            entityPlayer.sleepInBedAt(packet.x, packet.y, packet.z);
        }
    }

    public void handleHandshake(Packet2Handshake packet) {
        if (packet.username.equals("-")) {
            this.addToSendQueue(new Packet1Login(this.mc.session.username, 14));
        } else {
            try {
                URL uRL = new URL("http://www.minecraft.net/game/joinserver.jsp?user=" + this.mc.session.username + "&sessionId=" + this.mc.session.sessionId + "&serverId=" + packet.username);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
                String string = bufferedReader.readLine();
                bufferedReader.close();
                if (string.equalsIgnoreCase("ok")) {
                    this.addToSendQueue(new Packet1Login(this.mc.session.username, 14));
                } else {
                    this.netManager.networkShutdown("disconnect.loginFailedInfo", string);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                this.netManager.networkShutdown("disconnect.genericReason", "Internal client error: " + exception.toString());
            }
        }
    }

    public void disconnect() {
        this.disconnected = true;
        this.netManager.wakeThreads();
        this.netManager.networkShutdown("disconnect.closed", new Object[0]);
    }

    public void handleMobSpawn(Packet24MobSpawn packet) {
        double d = (double)packet.xPosition / 32.0;
        double d2 = (double)packet.yPosition / 32.0;
        double d3 = (double)packet.zPosition / 32.0;
        float f = (float)(packet.yaw * 360) / 256.0f;
        float f2 = (float)(packet.pitch * 360) / 256.0f;
        EntityLiving entityLiving = (EntityLiving)EntityList.createEntity(packet.type, this.mc.theWorld);
        entityLiving.serverPosX = packet.xPosition;
        entityLiving.serverPosY = packet.yPosition;
        entityLiving.serverPosZ = packet.zPosition;
        entityLiving.entityId = packet.entityId;
        entityLiving.setPositionAndRotation(d, d2, d3, f, f2);
        entityLiving.isMultiplayerEntity = true;
        this.worldClient.forceEntity(packet.entityId, entityLiving);
        List<DataWatcher.WatchableObject> list = packet.getMetadata();
        if (list != null) {
            entityLiving.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    public void handleUpdateTime(Packet4UpdateTime packet) {
        this.mc.theWorld.setWorldTime(packet.time);
    }

    public void handleSpawnPosition(Packet6SpawnPosition packet) {
        this.mc.thePlayer.setPlayerSpawnCoordinate(new ChunkCoordinates(packet.xPosition, packet.yPosition, packet.zPosition));
        this.mc.theWorld.getWorldInfo().setSpawn(packet.xPosition, packet.yPosition, packet.zPosition);
    }

    public void handleRideEntity(Packet39AttachEntity packet) {
        Entity entity = this.getEntityByID(packet.entityId);
        Entity entity2 = this.getEntityByID(packet.vehicleEntityId);
        if (packet.entityId == this.mc.thePlayer.entityId) {
            entity = this.mc.thePlayer;
        }
        if (entity == null) {
            return;
        }
        entity.mountEntity(entity2);
    }

    public void handleEntityEvent(Packet38EntityStatus packet) {
        Entity entity = this.getEntityByID(packet.entityId);
        if (entity != null) {
            entity.handleHealthUpdate(packet.entityStatus);
        }
    }

    private Entity getEntityByID(int networkId) {
        if (networkId == this.mc.thePlayer.entityId) {
            return this.mc.thePlayer;
        }
        return this.worldClient.getEntity(networkId);
    }

    public void handleHealth(Packet8UpdateHealth packet) {
        this.mc.thePlayer.setHealth(packet.healthMP);
    }

    public void handlePlayerRespawn(Packet9Respawn packet) {
        if (packet.dimension != this.mc.thePlayer.dimension) {
            this.started = false;
            this.worldClient = new WorldClient(this, this.worldClient.getWorldInfo().getRandomSeed(), packet.dimension);
            this.worldClient.multiplayerWorld = true;
            this.mc.changeWorld1(this.worldClient);
            this.mc.thePlayer.dimension = packet.dimension;
            this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
        }
        this.mc.respawn(true, packet.dimension);
    }

    public void handleExplosion(Packet60Explosion packet) {
        Explosion explosion = new Explosion(this.mc.theWorld, null, packet.explosionX, packet.explosionY, packet.explosionZ, packet.explosionSize);
        explosion.destroyedBlockPositions = packet.destroyedBlockPositions;
        explosion.doExplosionB(true);
    }

    public void handleOpenInventoryMenu(Packet100OpenWindow packet) {
        if (packet.inventoryType == 0) {
            InventoryBasic inventoryBasic = new InventoryBasic(packet.windowTitle, packet.slotsCount);
            this.mc.thePlayer.displayGUIChest(inventoryBasic);
            this.mc.thePlayer.craftingInventory.windowId = packet.windowId;
        } else if (packet.inventoryType == 2) {
            TileEntityFurnace tileEntityFurnace = new TileEntityFurnace();
            this.mc.thePlayer.displayGUIFurnace(tileEntityFurnace);
            this.mc.thePlayer.craftingInventory.windowId = packet.windowId;
        } else if (packet.inventoryType == 3) {
            TileEntityDispenser tileEntityDispenser = new TileEntityDispenser();
            this.mc.thePlayer.displayGUIDispenser(tileEntityDispenser);
            this.mc.thePlayer.craftingInventory.windowId = packet.windowId;
        } else if (packet.inventoryType == 1) {
            EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
            this.mc.thePlayer.displayWorkbenchGUI(MathHelper.floor_double(entityPlayerSP.posX), MathHelper.floor_double(entityPlayerSP.posY), MathHelper.floor_double(entityPlayerSP.posZ));
            this.mc.thePlayer.craftingInventory.windowId = packet.windowId;
        }
    }

    public void handleInventoryMenuSlotContent(Packet103SetSlot packet) {
        if (packet.windowId == -1) {
            this.mc.thePlayer.inventory.setItemStack(packet.myItemStack);
        } else if (packet.windowId == 0 && packet.itemSlot >= 36 && packet.itemSlot < 45) {
            ItemStack itemStack = this.mc.thePlayer.inventorySlots.getSlot(packet.itemSlot).getStack();
            if (packet.myItemStack != null && (itemStack == null || itemStack.stackSize < packet.myItemStack.stackSize)) {
                packet.myItemStack.animationsToGo = 5;
            }
            this.mc.thePlayer.inventorySlots.putStackInSlot(packet.itemSlot, packet.myItemStack);
        } else if (packet.windowId == this.mc.thePlayer.craftingInventory.windowId) {
            this.mc.thePlayer.craftingInventory.putStackInSlot(packet.itemSlot, packet.myItemStack);
        }
    }

    public void handleInventoryMenuConfirm(Packet106Transaction packet) {
        Container container = null;
        if (packet.windowId == 0) {
            container = this.mc.thePlayer.inventorySlots;
        } else if (packet.windowId == this.mc.thePlayer.craftingInventory.windowId) {
            container = this.mc.thePlayer.craftingInventory;
        }
        if (container != null) {
            if (packet.accepted) {
                container.deleteBackup(packet.interactionId);
            } else {
                container.rollbackToBackup(packet.interactionId);
                this.addToSendQueue(new Packet106Transaction(packet.windowId, packet.interactionId, true));
            }
        }
    }

    public void handleInventoryMenuContent(Packet104WindowItems packet) {
        if (packet.windowId == 0) {
            this.mc.thePlayer.inventorySlots.putStacksInSlots(packet.itemStack);
        } else if (packet.windowId == this.mc.thePlayer.craftingInventory.windowId) {
            this.mc.thePlayer.craftingInventory.putStacksInSlots(packet.itemStack);
        }
    }

    @Override
public void handleSignUpdate(Packet130UpdateSign packet) {
    if (this.mc.theWorld.blockExists(packet.xPosition, packet.yPosition, packet.zPosition)) {
        TileEntity tileentity = this.mc.theWorld.getBlockTileEntity(packet.xPosition, packet.yPosition, packet.zPosition);
        if (tileentity instanceof TileEntitySign) {
            TileEntitySign tileentitysign = (TileEntitySign) tileentity;
            for (int i = 0; i < 4; i++) {
                tileentitysign.signText[i] = packet.signLines[i];
            }
            tileentitysign.onInventoryChanged();
        }
    }
}

    public void handleInventoryMenuData(Packet105UpdateProgressbar packet) {
        this.registerPacket(packet);
        if (this.mc.thePlayer.craftingInventory != null && this.mc.thePlayer.craftingInventory.windowId == packet.windowId) {
            this.mc.thePlayer.craftingInventory.setData(packet.progressBar, packet.progressBarValue);
        }
    }

    public void handlePlayerInventory(Packet5PlayerInventory packet) {
        Entity entity = this.getEntityByID(packet.entityID);
        if (entity != null) {
            entity.outfitWithItem(packet.slot, packet.itemID, packet.itemDamage);
        }
    }

    public void handleCloseInventoryMenu(Packet101CloseWindow packet) {
        this.mc.thePlayer.closeScreen();
    }

    public void handleNotePlay(Packet54PlayNoteBlock packet) {
        this.mc.theWorld.playNoteAt(packet.xLocation, packet.yLocation, packet.zLocation, packet.instrumentType, packet.pitch);
    }

    public void handleGameEvent(Packet70Bed packet) {
        int n = packet.event;
        if (n >= 0 && n < Packet70Bed.EVENT_MESSAGES.length && Packet70Bed.EVENT_MESSAGES[n] != null) {
            this.mc.thePlayer.addChatMessage(Packet70Bed.EVENT_MESSAGES[n]);
        }
        if (n == 1) {
            this.worldClient.getWorldInfo().setRaining(true);
            this.worldClient.setRain(1.0f);
        } else if (n == 2) {
            this.worldClient.getWorldInfo().setRaining(false);
            this.worldClient.setRain(0.0f);
        }
    }

    public void handleMapData(Packet131MapData packet) {
        if (packet.type == Item.mapItem.shiftedIndex) {
            ItemMap.getMapData(packet.id, this.mc.theWorld).update(packet.data);
        } else {
            System.out.println("Unknown itemid: " + packet.id);
        }
    }

    public void handleWorldEvent(Packet61DoorChange packet) {
        this.mc.theWorld.doEvent(packet.event, packet.x, packet.y, packet.z, packet.data);
    }

    public void handleStat(Packet200Statistic packet) {
        ((EntityClientPlayerMP)this.mc.thePlayer).incrementRemoteStat(StatList.byKey(packet.statId), packet.value);
    }

    public boolean isServerHandler() {
        return false;
    }
}

