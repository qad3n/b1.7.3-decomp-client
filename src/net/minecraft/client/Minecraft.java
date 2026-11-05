package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderLoadOrGenerate;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.ColorizerWater;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConflictWarning;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiErrorScreen;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiUnused;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IStatStringFormat;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.PanelCrashReport;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.SaveConverterMcRegion;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ScreenShotHelper;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.StatFileWriter;
import net.minecraft.src.StatList;
import net.minecraft.src.Teleporter;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.Timer;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldRenderer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public abstract class Minecraft
implements Runnable {
    public static byte[] MEMORY_RESERVED_FOR_CRASH = new byte[0xA00000];
    private static Minecraft theMinecraft;
    public PlayerController playerController;
    private boolean fullscreen = false;
    private boolean hasCrashed = false;
    public int displayWidth;
    public int displayHeight;
    private OpenGlCapsChecker glCapabilities;
    private Timer timer = new Timer(20.0f);
    public World theWorld;
    public RenderGlobal renderGlobal;
    public EntityPlayerSP thePlayer;
    public EntityLiving renderViewEntity;
    public EffectRenderer effectRenderer;
    public Session session = null;
    public String minecraftUri;
    public Canvas mcCanvas;
    public boolean hideQuitButton = true;
    public volatile boolean isGamePaused = false;
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    public GuiScreen currentScreen = null;
    public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
    public EntityRenderer entityRenderer;
    private ThreadDownloadResources downloadResourcesThread;
    private int ticksRan = 0;
    private int leftClickCounter = 0;
    private int tempDisplayWidth;
    private int tempDisplayHeight;
    public GuiAchievement guiAchievement = new GuiAchievement(this);
    public GuiIngame ingameGUI;
    public boolean skipRenderWorld = false;
    public ModelBiped humanoidModel = new ModelBiped(0.0f);
    public MovingObjectPosition objectMouseOver = null;
    public GameSettings gameSettings;
    protected MinecraftApplet mcApplet;
    public SoundManager sndManager = new SoundManager();
    public MouseHelper mouseHelper;
    public TexturePackList texturePackList;
    private File mcDataDir;
    private ISaveFormat saveLoader;
    public static long[] frameTimes;
    public static long[] tickTimes;
    public static int numRecordedFrameTimes;
    public static long hasPaidCheckTime;
    public StatFileWriter statFileWriter;
    private String serverName;
    private int serverPort;
    private TextureWaterFX textureWaterFX = new TextureWaterFX();
    private TextureLavaFX textureLavaFX = new TextureLavaFX();
    private static File minecraftDir;
    public volatile boolean running = true;
    public String debug = "";
    boolean isTakingScreenshot = false;
    long prevFrameTime = -1L;
    public boolean inGameHasFocus = false;
    private int mouseTicksRan = 0;
    public boolean isRaining = false;
    long systemTime = System.currentTimeMillis();
    private int joinPlayerCounter = 0;

    public Minecraft(Component component, Canvas canvas, MinecraftApplet applet, int width, int height, boolean fullscreen) {
        StatList.init();
        this.tempDisplayHeight = height;
        this.fullscreen = fullscreen;
        this.mcApplet = applet;
        new Thread("Timer hack thread"){
            {
                this.setDaemon(true);
                this.start();
            }

            public void run() {
                while (Minecraft.this.running) {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    }
                    catch (InterruptedException interruptedException) {}
                }
            }
        };
        this.mcCanvas = canvas;
        this.displayWidth = width;
        this.displayHeight = height;
        this.fullscreen = fullscreen;
        if (applet == null || "true".equals(applet.getParameter("stand-alone"))) {
            this.hideQuitButton = false;
        }
        theMinecraft = this;
    }

    public void onMinecraftCrash(UnexpectedThrowable report) {
        this.hasCrashed = true;
        this.displayUnexpectedThrowable(report);
    }

    public abstract void displayUnexpectedThrowable(UnexpectedThrowable var1);

    public void setServer(String address, int port) {
        this.serverName = address;
        this.serverPort = port;
    }

    public void startGame() throws LWJGLException {
        if (this.mcCanvas != null) {
            Graphics graphics = this.mcCanvas.getGraphics();
            if (graphics != null) {
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, this.displayWidth, this.displayHeight);
                graphics.dispose();
            }
            Display.setParent(this.mcCanvas);
        } else if (this.fullscreen) {
            Display.setFullscreen(true);
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
            if (this.displayWidth <= 0) {
                this.displayWidth = 1;
            }
            if (this.displayHeight <= 0) {
                this.displayHeight = 1;
            }
        } else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
        Display.setTitle("Minecraft Minecraft Beta 1.7.3");
        try {
            Display.create();
        }
        catch (LWJGLException lWJGLException) {
            lWJGLException.printStackTrace();
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            Display.create();
        }
        this.mcDataDir = Minecraft.getMinecraftDir();
        this.saveLoader = new SaveConverterMcRegion(new File(this.mcDataDir, "saves"));
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.texturePackList = new TexturePackList(this, this.mcDataDir);
        this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
        this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);
        ColorizerWater.set(this.renderEngine.getColors("/misc/watercolor.png"));
        ColorizerGrass.set(this.renderEngine.getColors("/misc/grasscolor.png"));
        ColorizerFoliage.set(this.renderEngine.getColors("/misc/foliagecolor.png"));
        this.entityRenderer = new EntityRenderer(this);
        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat(){

            public String formatString(String value) {
                return String.format(value, Keyboard.getKeyName(Minecraft.this.gameSettings.keyBindInventory.keyCode));
            }
        });
        this.loadScreen();
        Keyboard.create();
        Mouse.create();
        this.mouseHelper = new MouseHelper(this.mcCanvas);
        try {
            Controllers.create();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.checkGLError("Pre startup");
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glClearDepth(1.0);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1f);
        GL11.glCullFace(1029);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        this.checkGLError("Startup");
        this.glCapabilities = new OpenGlCapsChecker();
        this.sndManager.loadSoundSettings(this.gameSettings);
        this.renderEngine.registerTextureFX(this.textureLavaFX);
        this.renderEngine.registerTextureFX(this.textureWaterFX);
        this.renderEngine.registerTextureFX(new TexturePortalFX());
        this.renderEngine.registerTextureFX(new TextureCompassFX(this));
        this.renderEngine.registerTextureFX(new TextureWatchFX(this));
        this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
        this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
        this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
        this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
        this.renderGlobal = new RenderGlobal(this, this.renderEngine);
        GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        try {
            this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
            this.downloadResourcesThread.start();
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
        if (this.serverName != null) {
            this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
        } else {
            this.displayGuiScreen(new GuiMainMenu());
        }
    }

    private void loadScreen() throws LWJGLException {
        ScaledResolution scaledResolution = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
        GL11.glClear(16640);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, scaledResolution.scaledWidthD, scaledResolution.scaledHeightD, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
        GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2896);
        GL11.glEnable(3553);
        GL11.glDisable(2912);
        GL11.glBindTexture(3553, this.renderEngine.getTexture("/title/mojang.png"));
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xFFFFFF);
        tessellator.addVertexWithUV(0.0, this.displayHeight, 0.0, 0.0, 0.0);
        tessellator.addVertexWithUV(this.displayWidth, this.displayHeight, 0.0, 0.0, 0.0);
        tessellator.addVertexWithUV(this.displayWidth, 0.0, 0.0, 0.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        tessellator.draw();
        int n = 256;
        int n2 = 256;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        tessellator.setColorOpaque_I(0xFFFFFF);
        this.draw((scaledResolution.getScaledWidth() - n) / 2, (scaledResolution.getScaledHeight() - n2) / 2, 0, 0, n, n2);
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1f);
        Display.swapBuffers();
    }

    public void draw(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625f;
        float f2 = 0.00390625f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, 0.0, (float)(u + 0) * f, (float)(v + height) * f2);
        tessellator.addVertexWithUV(x + width, y + height, 0.0, (float)(u + width) * f, (float)(v + height) * f2);
        tessellator.addVertexWithUV(x + width, y + 0, 0.0, (float)(u + width) * f, (float)(v + 0) * f2);
        tessellator.addVertexWithUV(x + 0, y + 0, 0.0, (float)(u + 0) * f, (float)(v + 0) * f2);
        tessellator.draw();
    }

    public static File getMinecraftDir() {
        if (minecraftDir == null) {
            minecraftDir = Minecraft.getAppDir("minecraft");
        }
        return minecraftDir;
    }

    public static File getAppDir(String name) {
        File file;
        String string = System.getProperty("user.home", ".");
        switch (Minecraft.getOs()) {
            case linux: 
            case solaris: {
                file = new File(string, '.' + name + '/');
                break;
            }
            case windows: {
                String string2 = System.getenv("APPDATA");
                if (string2 != null) {
                    file = new File(string2, "." + name + '/');
                    break;
                }
                file = new File(string, '.' + name + '/');
                break;
            }
            case macos: {
                file = new File(string, "Library/Application Support/" + name);
                break;
            }
            default: {
                file = new File(string, name + '/');
            }
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + file);
        }
        return file;
    }

    private static EnumOS2 getOs() {
        String string = System.getProperty("os.name").toLowerCase();
        if (string.contains("win")) {
            return EnumOS2.windows;
        }
        if (string.contains("mac")) {
            return EnumOS2.macos;
        }
        if (string.contains("solaris")) {
            return EnumOS2.solaris;
        }
        if (string.contains("sunos")) {
            return EnumOS2.solaris;
        }
        if (string.contains("linux")) {
            return EnumOS2.linux;
        }
        if (string.contains("unix")) {
            return EnumOS2.linux;
        }
        return EnumOS2.unknown;
    }

    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }

    public void displayGuiScreen(GuiScreen screen) {
        if (this.currentScreen instanceof GuiUnused) {
            return;
        }
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
        if (screen instanceof GuiMainMenu) {
            this.statFileWriter.func_27175_b();
        }
        this.statFileWriter.syncStats();
        if (screen == null && this.theWorld == null) {
            screen = new GuiMainMenu();
        } else if (screen == null && this.thePlayer.health <= 0) {
            screen = new GuiGameOver();
        }
        if (screen instanceof GuiMainMenu) {
            this.ingameGUI.clearChatMessages();
        }
        this.currentScreen = screen;
        if (screen != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledResolution = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
            int n = scaledResolution.getScaledWidth();
            int n2 = scaledResolution.getScaledHeight();
            screen.setWorldAndResolution(this, n, n2);
            this.skipRenderWorld = false;
        } else {
            this.setIngameFocus();
        }
    }

    private void checkGLError(String message) {
        int n = GL11.glGetError();
        if (n != 0) {
            String string = GLU.gluErrorString(n);
            System.out.println("########## GL ERROR ##########");
            System.out.println("@ " + message);
            System.out.println(n + ": " + string);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void shutdownMinecraftApplet() {
        try {
            this.statFileWriter.func_27175_b();
            this.statFileWriter.syncStats();
            if (this.mcApplet != null) {
                this.mcApplet.clearApplet();
            }
            try {
                if (this.downloadResourcesThread != null) {
                    this.downloadResourcesThread.closeMinecraft();
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            System.out.println("Stopping!");
            try {
                this.changeWorld1(null);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            try {
                GLAllocation.deleteTexturesAndDisplayLists();
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            this.sndManager.closeMinecraft();
            Mouse.destroy();
            Keyboard.destroy();
        }
        finally {
            Display.destroy();
            if (!this.hasCrashed) {
                System.exit(0);
            }
        }
        System.gc();
    }

    
    @Override
public void run() {
    this.running = true;
    try {
        this.startGame();
    } catch (Exception exception) {
        exception.printStackTrace();
        this.onMinecraftCrash(new UnexpectedThrowable("Failed to start game", exception));
        return;
    }
    try {
        long i = System.currentTimeMillis();
        int j = 0;
        while (this.running) {
            try {
                if (this.mcApplet != null && !this.mcApplet.isActive()) {
                    break;
                }
                AxisAlignedBB.clearBoundingBoxPool();
                Vec3D.initialize();
                if (this.mcCanvas == null && Display.isCloseRequested()) {
                    this.shutdown();
                }
                if (this.isGamePaused && this.theWorld != null) {
                    float f = this.timer.renderPartialTicks;
                    this.timer.updateTimer();
                    this.timer.renderPartialTicks = f;
                } else {
                    this.timer.updateTimer();
                }
                long l = System.nanoTime();
                for (int k = 0; k < this.timer.elapsedTicks; k++) {
                    this.ticksRan++;
                    try {
                        this.runTick();
                    } catch (MinecraftException minecraftexception) {
                        this.theWorld = null;
                        this.changeWorld1(null);
                        this.displayGuiScreen(new GuiConflictWarning());
                    }
                }
                long i1 = System.nanoTime() - l;
                this.checkGLError("Pre render");
                RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics;
                this.sndManager.update(this.thePlayer, this.timer.renderPartialTicks);
                GL11.glEnable(3553);
                if (this.theWorld != null) {
                    this.theWorld.updatingLighting();
                }
                if (!Keyboard.isKeyDown(65)) {
                    Display.update();
                }
                if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
                    this.gameSettings.thirdPersonView = false;
                }
                if (!this.skipRenderWorld) {
                    if (this.playerController != null) {
                        this.playerController.setPartialTime(this.timer.renderPartialTicks);
                    }
                    this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
                }
                if (!Display.isActive()) {
                    if (this.fullscreen) {
                        this.toggleFullscreen();
                    }
                    Thread.sleep(10L);
                }
                if (this.gameSettings.showDebugInfo) {
                    this.displayDebugInfo(i1);
                } else {
                    this.prevFrameTime = System.nanoTime();
                }
                this.guiAchievement.updateAchievementWindow();
                Thread.yield();
                if (Keyboard.isKeyDown(65)) {
                    Display.update();
                }
                this.screenshotListener();
                if (this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
                    this.displayWidth = this.mcCanvas.getWidth();
                    this.displayHeight = this.mcCanvas.getHeight();
                    if (this.displayWidth <= 0) {
                        this.displayWidth = 1;
                    }
                    if (this.displayHeight <= 0) {
                        this.displayHeight = 1;
                    }
                    this.resize(this.displayWidth, this.displayHeight);
                }
                this.checkGLError("Post render");
                j++;
                for (this.isGamePaused = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= i + 1000L; j = 0) {
                    this.debug = j + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
                    WorldRenderer.chunksUpdated = 0;
                    i += 1000L;
                }
            } catch (MinecraftException minecraftexception1) {
                this.theWorld = null;
                this.changeWorld1(null);
                this.displayGuiScreen(new GuiConflictWarning());
            } catch (OutOfMemoryError outofmemoryerror) {
                this.cleanHeap();
                this.displayGuiScreen(new GuiErrorScreen());
                System.gc();
            }
        }
    } catch (MinecraftError minecrafterror) {
    } catch (Throwable throwable) {
        this.cleanHeap();
        throwable.printStackTrace();
        this.onMinecraftCrash(new UnexpectedThrowable("Unexpected error", throwable));
    } finally {
        this.shutdownMinecraftApplet();
    }
}

    public void cleanHeap() {
        try {
            MEMORY_RESERVED_FOR_CRASH = new byte[0];
            this.renderGlobal.releaseGlLists();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            System.gc();
            AxisAlignedBB.clearCache();
            Vec3D.clearPool();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            System.gc();
            this.changeWorld1(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        System.gc();
    }

    private void screenshotListener() {
        if (Keyboard.isKeyDown(60)) {
            if (!this.isTakingScreenshot) {
                this.isTakingScreenshot = true;
                this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight));
            }
        } else {
            this.isTakingScreenshot = false;
        }
    }

    private void displayDebugInfo(long tickTime) {
        int n;
        long l = 16666666L;
        if (this.prevFrameTime == -1L) {
            this.prevFrameTime = System.nanoTime();
        }
        long l2 = System.nanoTime();
        Minecraft.tickTimes[Minecraft.numRecordedFrameTimes & Minecraft.frameTimes.length - 1] = tickTime;
        Minecraft.frameTimes[Minecraft.numRecordedFrameTimes++ & Minecraft.frameTimes.length - 1] = l2 - this.prevFrameTime;
        this.prevFrameTime = l2;
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(7);
        int n2 = (int)(l / 200000L);
        tessellator.setColorOpaque_I(0x20000000);
        tessellator.addVertex(0.0, this.displayHeight - n2, 0.0);
        tessellator.addVertex(0.0, this.displayHeight, 0.0);
        tessellator.addVertex(frameTimes.length, this.displayHeight, 0.0);
        tessellator.addVertex(frameTimes.length, this.displayHeight - n2, 0.0);
        tessellator.setColorOpaque_I(0x20200000);
        tessellator.addVertex(0.0, this.displayHeight - n2 * 2, 0.0);
        tessellator.addVertex(0.0, this.displayHeight - n2, 0.0);
        tessellator.addVertex(frameTimes.length, this.displayHeight - n2, 0.0);
        tessellator.addVertex(frameTimes.length, this.displayHeight - n2 * 2, 0.0);
        tessellator.draw();
        long l3 = 0L;
        for (n = 0; n < frameTimes.length; ++n) {
            l3 += frameTimes[n];
        }
        n = (int)(l3 / 200000L / (long)frameTimes.length);
        tessellator.startDrawing(7);
        tessellator.setColorOpaque_I(0x20400000);
        tessellator.addVertex(0.0, this.displayHeight - n, 0.0);
        tessellator.addVertex(0.0, this.displayHeight, 0.0);
        tessellator.addVertex(frameTimes.length, this.displayHeight, 0.0);
        tessellator.addVertex(frameTimes.length, this.displayHeight - n, 0.0);
        tessellator.draw();
        tessellator.startDrawing(1);
        for (int i = 0; i < frameTimes.length; ++i) {
            int n3 = (i - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
            int n4 = n3 * n3 / 255;
            n4 = n4 * n4 / 255;
            int n5 = n4 * n4 / 255;
            n5 = n5 * n5 / 255;
            if (frameTimes[i] > l) {
                tessellator.setColorOpaque_I(-16777216 + n4 * 65536);
            } else {
                tessellator.setColorOpaque_I(-16777216 + n4 * 256);
            }
            long l4 = frameTimes[i] / 200000L;
            long l5 = tickTimes[i] / 200000L;
            tessellator.addVertex((float)i + 0.5f, (float)((long)this.displayHeight - l4) + 0.5f, 0.0);
            tessellator.addVertex((float)i + 0.5f, (float)this.displayHeight + 0.5f, 0.0);
            tessellator.setColorOpaque_I(-16777216 + n4 * 65536 + n4 * 256 + n4 * 1);
            tessellator.addVertex((float)i + 0.5f, (float)((long)this.displayHeight - l4) + 0.5f, 0.0);
            tessellator.addVertex((float)i + 0.5f, (float)((long)this.displayHeight - (l4 - l5)) + 0.5f, 0.0);
        }
        tessellator.draw();
        GL11.glEnable(3553);
    }

    public void shutdown() {
        this.running = false;
    }

    public void setIngameFocus() {
        if (!Display.isActive()) {
            return;
        }
        if (this.inGameHasFocus) {
            return;
        }
        this.inGameHasFocus = true;
        this.mouseHelper.grabMouseCursor();
        this.displayGuiScreen(null);
        this.leftClickCounter = 10000;
        this.mouseTicksRan = this.ticksRan + 10000;
    }

    public void setIngameNotInFocus() {
        if (!this.inGameHasFocus) {
            return;
        }
        if (this.thePlayer != null) {
            this.thePlayer.resetPlayerKeyState();
        }
        this.inGameHasFocus = false;
        this.mouseHelper.ungrabMouseCursor();
    }

    public void displayInGameMenu() {
        if (this.currentScreen != null) {
            return;
        }
        this.displayGuiScreen(new GuiIngameMenu());
    }

    private void handleMouseDown(int button, boolean holdingAttack) {
        if (this.playerController.creative) {
            return;
        }
        if (!holdingAttack) {
            this.leftClickCounter = 0;
        }
        if (button == 0 && this.leftClickCounter > 0) {
            return;
        }
        if (holdingAttack && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.EnumMovingObjectType.TILE && button == 0) {
            int n = this.objectMouseOver.blockX;
            int n2 = this.objectMouseOver.blockY;
            int n3 = this.objectMouseOver.blockZ;
            this.playerController.sendBlockRemoving(n, n2, n3, this.objectMouseOver.sideHit);
            this.effectRenderer.addBlockHitEffects(n, n2, n3, this.objectMouseOver.sideHit);
        } else {
            this.playerController.resetBlockRemoving();
        }
    }

    private void clickMouse(int button) {
        ItemStack itemStack;
        if (button == 0 && this.leftClickCounter > 0) {
            return;
        }
        if (button == 0) {
            this.thePlayer.swingItem();
        }
        boolean bl = true;
        if (this.objectMouseOver == null) {
            if (button == 0 && !(this.playerController instanceof PlayerControllerTest)) {
                this.leftClickCounter = 10;
            }
        } else if (this.objectMouseOver.typeOfHit == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
            if (button == 0) {
                this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
            }
            if (button == 1) {
                this.playerController.interactWithEntity(this.thePlayer, this.objectMouseOver.entityHit);
            }
        } else if (this.objectMouseOver.typeOfHit == MovingObjectPosition.EnumMovingObjectType.TILE) {
            int n = this.objectMouseOver.blockX;
            int n2 = this.objectMouseOver.blockY;
            int n3 = this.objectMouseOver.blockZ;
            int n4 = this.objectMouseOver.sideHit;
            if (button == 0) {
                this.playerController.clickBlock(n, n2, n3, this.objectMouseOver.sideHit);
            } else {
                int n5;
                ItemStack itemStack2 = this.thePlayer.inventory.getCurrentItem();
                int n6 = n5 = itemStack2 != null ? itemStack2.stackSize : 0;
                if (this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, itemStack2, n, n2, n3, n4)) {
                    bl = false;
                    this.thePlayer.swingItem();
                }
                if (itemStack2 == null) {
                    return;
                }
                if (itemStack2.stackSize == 0) {
                    this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                } else if (itemStack2.stackSize != n5) {
                    this.entityRenderer.itemRenderer.onBlockUsed();
                }
            }
        }
        if (bl && button == 1 && (itemStack = this.thePlayer.inventory.getCurrentItem()) != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemStack)) {
            this.entityRenderer.itemRenderer.onItemUsed();
        }
    }

    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            if (this.fullscreen) {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            } else {
                if (this.mcCanvas != null) {
                    this.displayWidth = this.mcCanvas.getWidth();
                    this.displayHeight = this.mcCanvas.getHeight();
                } else {
                    this.displayWidth = this.tempDisplayWidth;
                    this.displayHeight = this.tempDisplayHeight;
                }
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            }
            if (this.currentScreen != null) {
                this.resize(this.displayWidth, this.displayHeight);
            }
            Display.setFullscreen(this.fullscreen);
            Display.update();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void resize(int width, int height) {
        if (width <= 0) {
            width = 1;
        }
        if (height <= 0) {
            height = 1;
        }
        this.displayWidth = width;
        this.displayHeight = height;
        if (this.currentScreen != null) {
            ScaledResolution scaledResolution = new ScaledResolution(this.gameSettings, width, height);
            int n = scaledResolution.getScaledWidth();
            int n2 = scaledResolution.getScaledHeight();
            this.currentScreen.setWorldAndResolution(this, n, n2);
        }
    }

    private void clickMiddleMouseButton() {
        if (this.objectMouseOver != null) {
            int n = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
            if (n == Block.grass.blockID) {
                n = Block.dirt.blockID;
            }
            if (n == Block.stairDouble.blockID) {
                n = Block.stairSingle.blockID;
            }
            if (n == Block.bedrock.blockID) {
                n = Block.stone.blockID;
            }
            this.thePlayer.inventory.setCurrentItem(n, this.playerController instanceof PlayerControllerTest);
        }
    }

    private void initLicenseCheckThread() {
        new Thread(){

            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("https://login.minecraft.net/session?name=" + Minecraft.this.session.username + "&session=" + Minecraft.this.session.sessionId).openConnection();
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 400) {
                        hasPaidCheckTime = System.currentTimeMillis();
                    }
                    httpURLConnection.disconnect();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.start();
    }

    public void runTick() {
        int n;
        if (this.ticksRan == 6000) {
            this.initLicenseCheckThread();
        }
        this.statFileWriter.tick();
        this.ingameGUI.updateTick();
        this.entityRenderer.getMouseOver(1.0f);
        if (this.thePlayer != null) {
            IChunkProvider iChunkProvider = this.theWorld.getIChunkProvider();
            if (iChunkProvider instanceof ChunkProviderLoadOrGenerate) {
                ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate = (ChunkProviderLoadOrGenerate)iChunkProvider;
                n = MathHelper.floor_float((int)this.thePlayer.posX) >> 4;
                int n2 = MathHelper.floor_float((int)this.thePlayer.posZ) >> 4;
                chunkProviderLoadOrGenerate.setCurrentChunkOver(n, n2);
            }
        }
        if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }
        GL11.glBindTexture(3553, this.renderEngine.getTexture("/terrain.png"));
        if (!this.isGamePaused) {
            this.renderEngine.updateDynamicTextures();
        }
        if (this.currentScreen == null && this.thePlayer != null) {
            if (this.thePlayer.health <= 0) {
                this.displayGuiScreen(null);
            } else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null && this.theWorld.multiplayerWorld) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }
        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
            this.mouseTicksRan = this.ticksRan + 10000;
        }
        if (this.currentScreen != null) {
            this.currentScreen.handleInput();
            if (this.currentScreen != null) {
                this.currentScreen.particlesGui.tick();
                this.currentScreen.updateScreen();
            }
        }
        if (this.currentScreen == null || this.currentScreen.passEvents) {
            while (Mouse.next()) {
                long l = System.currentTimeMillis() - this.systemTime;
                if (l > 200L) continue;
                n = Mouse.getEventDWheel();
                if (n != 0) {
                    this.thePlayer.inventory.changeCurrentItem(n);
                    if (this.gameSettings.discreteScroll) {
                        if (n > 0) {
                            n = 1;
                        }
                        if (n < 0) {
                            n = -1;
                        }
                        this.gameSettings.totalDiscreteScroll += (float)n * 0.25f;
                    }
                }
                if (this.currentScreen == null) {
                    if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                        this.setIngameFocus();
                        continue;
                    }
                    if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                        this.clickMouse(0);
                        this.mouseTicksRan = this.ticksRan;
                    }
                    if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                        this.clickMouse(1);
                        this.mouseTicksRan = this.ticksRan;
                    }
                    if (Mouse.getEventButton() != 2 || !Mouse.getEventButtonState()) continue;
                    this.clickMiddleMouseButton();
                    continue;
                }
                if (this.currentScreen == null) continue;
                this.currentScreen.handleMouseInput();
            }
            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }
            while (Keyboard.next()) {
                this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if (!Keyboard.getEventKeyState()) continue;
                if (Keyboard.getEventKey() == 87) {
                    this.toggleFullscreen();
                    continue;
                }
                if (this.currentScreen != null) {
                    this.currentScreen.handleKeyboardInput();
                } else {
                    if (Keyboard.getEventKey() == 1) {
                        this.displayInGameMenu();
                    }
                    if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
                        this.forceReload();
                    }
                    if (Keyboard.getEventKey() == 59) {
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                    }
                    if (Keyboard.getEventKey() == 61) {
                        this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                    }
                    if (Keyboard.getEventKey() == 63) {
                        this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
                    }
                    if (Keyboard.getEventKey() == 66) {
                        this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                    }
                    if (Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
                        this.displayGuiScreen(new GuiInventory(this.thePlayer));
                    }
                    if (Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
                        this.thePlayer.dropCurrentItem();
                    }
                    if (this.isMultiplayerWorld() && Keyboard.getEventKey() == this.gameSettings.keyBindChat.keyCode) {
                        this.displayGuiScreen(new GuiChat());
                    }
                }
                for (int i = 0; i < 9; ++i) {
                    if (Keyboard.getEventKey() != 2 + i) continue;
                    this.thePlayer.inventory.currentItem = i;
                }
                if (Keyboard.getEventKey() != this.gameSettings.keyBindToggleFog.keyCode) continue;
                this.gameSettings.setOptionValue(GameSettings.EnumOptions.RENDER_DISTANCE, Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54) ? -1 : 1);
            }
            if (this.currentScreen == null) {
                if (Mouse.isButtonDown(0) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0f && this.inGameHasFocus) {
                    this.clickMouse(0);
                    this.mouseTicksRan = this.ticksRan;
                }
                if (Mouse.isButtonDown(1) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0f && this.inGameHasFocus) {
                    this.clickMouse(1);
                    this.mouseTicksRan = this.ticksRan;
                }
            }
            this.handleMouseDown(0, this.currentScreen == null && Mouse.isButtonDown(0) && this.inGameHasFocus);
        }
        if (this.theWorld != null) {
            if (this.thePlayer != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
                }
            }
            this.theWorld.difficultySetting = this.gameSettings.difficulty;
            if (this.theWorld.multiplayerWorld) {
                this.theWorld.difficultySetting = 3;
            }
            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }
            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }
            if (!this.isGamePaused) {
                if (this.theWorld.lightningCooldown > 0) {
                    --this.theWorld.lightningCooldown;
                }
                this.theWorld.updateEntities();
            }
            if (!this.isGamePaused || this.isMultiplayerWorld()) {
                this.theWorld.setAllowedMobSpawns(this.gameSettings.difficulty > 0, true);
                this.theWorld.tick();
            }
            if (!this.isGamePaused && this.theWorld != null) {
                this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
            }
            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }
        this.systemTime = System.currentTimeMillis();
    }

    private void forceReload() {
        System.out.println("FORCING RELOAD!");
        this.sndManager = new SoundManager();
        this.sndManager.loadSoundSettings(this.gameSettings);
        this.downloadResourcesThread.reloadResources();
    }

    public boolean isMultiplayerWorld() {
        return this.theWorld != null && this.theWorld.multiplayerWorld;
    }

    public void startWorld(String saveName, String name, long seed) {
        this.changeWorld1(null);
        System.gc();
        if (this.saveLoader.isOldMapFormat(saveName)) {
            this.convertMapFormat(saveName, name);
        } else {
            ISaveHandler iSaveHandler = this.saveLoader.getSaveLoader(saveName, false);
            World world = null;
            world = new World(iSaveHandler, name, seed);
            if (world.isNewWorld) {
                this.statFileWriter.readStat(StatList.createWorldStat, 1);
                this.statFileWriter.readStat(StatList.startGameStat, 1);
                this.changeWorld2(world, "Generating level");
            } else {
                this.statFileWriter.readStat(StatList.loadWorldStat, 1);
                this.statFileWriter.readStat(StatList.startGameStat, 1);
                this.changeWorld2(world, "Loading level");
            }
        }
    }

    public void usePortal() {
        System.out.println("Toggling dimension!!");
        if (this.thePlayer.dimension == -1) {
            this.thePlayer.dimension = 0;
        } else {
            this.thePlayer.dimension = -1;
        }
        this.theWorld.setEntityDead(this.thePlayer);
        this.thePlayer.isDead = false;
        double d0 = this.thePlayer.posX;
        double d1 = this.thePlayer.posZ;
        double d2 = 8.0;
        if (this.thePlayer.dimension == -1) {
            d0 /= d2;
            d1 /= d2;
            this.thePlayer.setLocationAndAngles(d0, this.thePlayer.posY, d1, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
            if (this.thePlayer.isEntityAlive()) {
                this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
            }
            World world = null;
            world = new World(this.theWorld, WorldProvider.getProviderForDimension(-1));
            this.changeWorld(world, "Entering the Nether", this.thePlayer);
        } else {
            d0 *= d2;
            d1 *= d2;
            this.thePlayer.setLocationAndAngles(d0, this.thePlayer.posY, d1, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
            if (this.thePlayer.isEntityAlive()) {
                this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
            }
            World world1 = null;
            world1 = new World(this.theWorld, WorldProvider.getProviderForDimension(0));
            this.changeWorld(world1, "Leaving the Nether", this.thePlayer);
        }
        this.thePlayer.worldObj = this.theWorld;
        if (this.thePlayer.isEntityAlive()) {
            this.thePlayer.setLocationAndAngles(d0, this.thePlayer.posY, d1, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
            this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
            new Teleporter().onDimensionChanged(this.theWorld, this.thePlayer);
        }
    }

    public void changeWorld1(World world) {
        this.changeWorld2(world, "");
    }

    public void changeWorld2(World world, String message) {
        this.changeWorld(world, message, null);
    }

    public void changeWorld(World world, String message, EntityPlayer player) {
        this.statFileWriter.func_27175_b();
        this.statFileWriter.syncStats();
        this.renderViewEntity = null;
        this.loadingScreen.printText(message);
        this.loadingScreen.displayLoadingString("");
        this.sndManager.playStreaming(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        if (this.theWorld != null) {
            this.theWorld.saveWorldIndirectly(this.loadingScreen);
        }
        this.theWorld = world;
        if (world != null) {
            this.playerController.initWorld(world);
            if (!this.isMultiplayerWorld()) {
                if (player == null) {
                    this.thePlayer = world.getEntityOfSubtype(EntityPlayerSP.class);
                }
            } else if (this.thePlayer != null) {
                this.thePlayer.preparePlayerToSpawn();
                if (world != null) {
                    world.entityJoinedWorld(this.thePlayer);
                }
            }
            if (!world.multiplayerWorld) {
                this.prepareWorld(message);
            }
            if (this.thePlayer == null) {
                this.thePlayer = (EntityPlayerSP) this.playerController.createPlayer(world);
                this.thePlayer.preparePlayerToSpawn();
                this.playerController.flipPlayer(this.thePlayer);
            }
            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            if (this.renderGlobal != null) {
                this.renderGlobal.changeWorld(world);
            }
            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects(world);
            }
            this.playerController.adjustPlayer(this.thePlayer);
            if (player != null) {
                world.emptyMethod1();
            }
            IChunkProvider ichunkprovider = world.getIChunkProvider();
            if (ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate) ichunkprovider;
                int i = MathHelper.floor_float((int) this.thePlayer.posX) >> 4;
                int j = MathHelper.floor_float((int) this.thePlayer.posZ) >> 4;
                chunkproviderloadorgenerate.setCurrentChunkOver(i, j);
            }
            world.spawnPlayerWithLoadedChunks(this.thePlayer);
            if (world.isNewWorld) {
                world.saveWorldIndirectly(this.loadingScreen);
            }
            this.renderViewEntity = this.thePlayer;
        } else {
            this.thePlayer = null;
        }
        System.gc();
        this.systemTime = 0L;
    }

    private void convertMapFormat(String saveName, String name) {
        this.loadingScreen.printText("Converting World to " + this.saveLoader.getName());
        this.loadingScreen.displayLoadingString("This may take a while :)");
        this.saveLoader.convertMapFormat(saveName, this.loadingScreen);
        this.startWorld(saveName, name, 0L);
    }

    private void prepareWorld(String saveName) {
        this.loadingScreen.printText(saveName);
        this.loadingScreen.displayLoadingString("Building terrain");
        int n = 128;
        int n2 = 0;
        int n3 = n * 2 / 16 + 1;
        n3 *= n3;
        IChunkProvider iChunkProvider = this.theWorld.getIChunkProvider();
        ChunkCoordinates chunkCoordinates = this.theWorld.getSpawnPoint();
        if (this.thePlayer != null) {
            chunkCoordinates.x = (int)this.thePlayer.posX;
            chunkCoordinates.z = (int)this.thePlayer.posZ;
        }
        if (iChunkProvider instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate = (ChunkProviderLoadOrGenerate)iChunkProvider;
            chunkProviderLoadOrGenerate.setCurrentChunkOver(chunkCoordinates.x >> 4, chunkCoordinates.z >> 4);
        }
        for (int i = -n; i <= n; i += 16) {
            for (int j = -n; j <= n; j += 16) {
                this.loadingScreen.setLoadingProgress(n2++ * 100 / n3);
                this.theWorld.getBlockId(chunkCoordinates.x + i, 64, chunkCoordinates.z + j);
                while (this.theWorld.updatingLighting()) {
                }
            }
        }
        this.loadingScreen.displayLoadingString("Simulating world for a bit");
        n3 = 2000;
        this.theWorld.prepare();
    }

    public void installResource(String path, File file) {
        int n = path.indexOf("/");
        String string = path.substring(0, n);
        path = path.substring(n + 1);
        if (string.equalsIgnoreCase("sound")) {
            this.sndManager.addSound(path, file);
        } else if (string.equalsIgnoreCase("newsound")) {
            this.sndManager.addSound(path, file);
        } else if (string.equalsIgnoreCase("streaming")) {
            this.sndManager.addStreaming(path, file);
        } else if (string.equalsIgnoreCase("music")) {
            this.sndManager.addMusic(path, file);
        } else if (string.equalsIgnoreCase("newmusic")) {
            this.sndManager.addMusic(path, file);
        }
    }

    public OpenGlCapsChecker getOpenGlCapsChecker() {
        return this.glCapabilities;
    }

    public String getRenderChunkDebugInfo() {
        return this.renderGlobal.getDebugInfoRenders();
    }

    public String getRenderEntityDebugInfo() {
        return this.renderGlobal.getDebugInfoEntities();
    }

    public String getChunkSourceDebugInfo() {
        return this.theWorld.getChunkSourceDebugInfo();
    }

    public String getWorldDebugInfo() {
        return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugInfo();
    }

    public void respawn(boolean worldSpawn, int dimension) {
        if (!this.theWorld.multiplayerWorld && !this.theWorld.worldProvider.canRespawnHere()) {
            this.usePortal();
        }
        ChunkCoordinates chunkcoordinates = null;
        ChunkCoordinates chunkcoordinates1 = null;
        boolean flag = true;
        if (this.thePlayer != null && !worldSpawn) {
            chunkcoordinates = this.thePlayer.getPlayerSpawnCoordinate();
            if (chunkcoordinates != null) {
                chunkcoordinates1 = EntityPlayer.loadSpawnPoint(this.theWorld, chunkcoordinates);
                if (chunkcoordinates1 == null) {
                    this.thePlayer.addChatMessage("tile.bed.notValid");
                }
            }
        }
        if (chunkcoordinates1 == null) {
            chunkcoordinates1 = this.theWorld.getSpawnPoint();
            flag = false;
        }
        IChunkProvider ichunkprovider = this.theWorld.getIChunkProvider();
        if (ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate) ichunkprovider;
            chunkproviderloadorgenerate.setCurrentChunkOver(chunkcoordinates1.x >> 4, chunkcoordinates1.z >> 4);
        }
        this.theWorld.setSpawnLocation();
        this.theWorld.updateEntityList();
        int i = 0;
        if (this.thePlayer != null) {
            i = this.thePlayer.entityId;
            this.theWorld.setEntityDead(this.thePlayer);
        }
        this.renderViewEntity = null;
        this.thePlayer = (EntityPlayerSP) this.playerController.createPlayer(this.theWorld);
        this.thePlayer.dimension = dimension;
        this.renderViewEntity = this.thePlayer;
        this.thePlayer.preparePlayerToSpawn();
        if (flag) {
            this.thePlayer.setPlayerSpawnCoordinate(chunkcoordinates);
            this.thePlayer.setLocationAndAngles(chunkcoordinates1.x + 0.5F, chunkcoordinates1.y + 0.1F, chunkcoordinates1.z + 0.5F, 0.0F, 0.0F);
        }
        this.playerController.flipPlayer(this.thePlayer);
        this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.thePlayer.entityId = i;
        this.thePlayer.addRespawnParticles();
        this.playerController.adjustPlayer(this.thePlayer);
        this.prepareWorld("Respawning");
        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }

    public static void start(String username, String sessionId) {
        Minecraft.startMainThread(username, sessionId, null);
    }

    public static void startMainThread(String username, String sessionId, String server) {
        boolean flag = false;
        String s = username;
        final Frame frame = new Frame("Minecraft");
        Canvas canvas = new Canvas();
        frame.setLayout(new BorderLayout());
        frame.add(canvas, "Center");
        canvas.setPreferredSize(new Dimension(854, 480));
        frame.pack();
        frame.setLocationRelativeTo(null);
        final Minecraft minecraft = new Minecraft(frame, canvas, null, 854, 480, flag) {
            
            @Override
public void displayUnexpectedThrowable(UnexpectedThrowable report) {
    frame.removeAll();
    frame.add(new PanelCrashReport(report), "Center");
    frame.validate();
}
        };
        final Thread thread = new Thread(minecraft, "Minecraft main thread");
        thread.setPriority(10);
        minecraft.minecraftUri = "www.minecraft.net";
        if (s != null && sessionId != null) {
            minecraft.session = new Session(s, sessionId);
        } else {
            minecraft.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");
        }
        if (server != null) {
            String[] astring = server.split(":");
            minecraft.setServer(astring[0], Integer.parseInt(astring[1]));
        }
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            
            @Override
public void windowClosing(WindowEvent var1) {
    minecraft.shutdown();
    try {
        thread.join();
    } catch (InterruptedException interruptedexception) {
        interruptedexception.printStackTrace();
    }
    System.exit(0);
}
        });
        thread.start();
    }

    public NetClientHandler getSendQueue() {
        if (this.thePlayer instanceof EntityClientPlayerMP) {
            return ((EntityClientPlayerMP)this.thePlayer).sendQueue;
        }
        return null;
    }

    public static void main(String[] args) {
        String string = null;
        String string2 = null;
        string = "Player" + System.currentTimeMillis() % 1000L;
        if (args.length > 0) {
            string = args[0];
        }
        string2 = "-";
        if (args.length > 1) {
            string2 = args[1];
        }
        Minecraft.start(string, string2);
    }

    public static boolean isGuiEnabled() {
        return theMinecraft == null || !Minecraft.theMinecraft.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled() {
        return theMinecraft != null && Minecraft.theMinecraft.gameSettings.fancyGraphics;
    }

    public static boolean isAmbientOcclusionEnabled() {
        return theMinecraft != null && Minecraft.theMinecraft.gameSettings.ambientOcclusion;
    }

    public static boolean isDebugInfoEnabled() {
        return theMinecraft != null && Minecraft.theMinecraft.gameSettings.showDebugInfo;
    }

    public boolean lineIsCommand(String message) {
        if (message.startsWith("/")) {
            // empty if block
        }
        return false;
    }

    static {
        frameTimes = new long[512];
        tickTimes = new long[512];
        numRecordedFrameTimes = 0;
        hasPaidCheckTime = 0L;
        minecraftDir = null;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum EnumOS2 {
        linux,
        solaris,
        windows,
        macos,
        unknown;

    }
}

