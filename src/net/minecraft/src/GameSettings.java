package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GameSettings {
    private static final String[] RENDER_DISTANCES = new String[]{
        "options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"
    };
    private static final String[] DIFFICULTIES = new String[]{
        "options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"
    };
    private static final String[] GUISCALES = new String[]{
        "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"
    };
    private static final String[] LIMIT_FRAMERATES = new String[]{"performance.max", "performance.balanced", "performance.powersaver"};
    public float musicVolume = 1.0F;
    public float soundVolume = 1.0F;
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse = false;
    public int renderDistance = 0;
    public boolean viewBobbing = true;
    public boolean anaglyph = false;
    public boolean advancedOpengl = false;
    public int limitFramerate = 1;
    public boolean fancyGraphics = true;
    public boolean ambientOcclusion = true;
    public String skin = "Default";
    public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
    public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
    public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
    public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
    public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
    public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
    public KeyBinding keyBindToggleFog = new KeyBinding("key.fog", 33);
    public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
    public KeyBinding[] keyBindings = new KeyBinding[]{
        this.keyBindForward,
        this.keyBindLeft,
        this.keyBindBack,
        this.keyBindRight,
        this.keyBindJump,
        this.keyBindSneak,
        this.keyBindDrop,
        this.keyBindInventory,
        this.keyBindChat,
        this.keyBindToggleFog
    };
    protected Minecraft mc;
    private File optionsFile;
    public int difficulty = 2;
    public boolean hideGUI = false;
    public boolean thirdPersonView = false;
    public boolean showDebugInfo = false;
    public String lastServer = "";
    public boolean discreteScroll = false;
    public boolean smoothCamera = false;
    public boolean debugCamera = false;
    public float totalDiscreteScroll = 1.0F;
    public float field_22271_G = 1.0F;
    public int guiScale = 0;

    public GameSettings(Minecraft minecraft, File dir) {
        this.mc = minecraft;
        this.optionsFile = new File(dir, "options.txt");
        this.loadOptions();
    }

    public GameSettings() {
    }

    public String getKeyBindingDescription(int keyBinding) {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        return stringtranslate.translateKey(this.keyBindings[keyBinding].keyDescription);
    }

    public String getOptionDisplayString(int keyBinding) {
        return Keyboard.getKeyName(this.keyBindings[keyBinding].keyCode);
    }

    public void setKeyBinding(int keyBinding, int keyCode) {
        this.keyBindings[keyBinding].keyCode = keyCode;
        this.saveOptions();
    }

    public void setOptionFloatValue(GameSettings.EnumOptions option, float value) {
        if (option == GameSettings.EnumOptions.MUSIC) {
            this.musicVolume = value;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if (option == GameSettings.EnumOptions.SOUND) {
            this.soundVolume = value;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if (option == GameSettings.EnumOptions.SENSITIVITY) {
            this.mouseSensitivity = value;
        }
    }

    public void setOptionValue(GameSettings.EnumOptions option, int value) {
        if (option == GameSettings.EnumOptions.INVERT_MOUSE) {
            this.invertMouse = !this.invertMouse;
        }

        if (option == GameSettings.EnumOptions.RENDER_DISTANCE) {
            this.renderDistance = this.renderDistance + value & 3;
        }

        if (option == GameSettings.EnumOptions.GUI_SCALE) {
            this.guiScale = this.guiScale + value & 3;
        }

        if (option == GameSettings.EnumOptions.VIEW_BOBBING) {
            this.viewBobbing = !this.viewBobbing;
        }

        if (option == GameSettings.EnumOptions.ADVANCED_OPENGL) {
            this.advancedOpengl = !this.advancedOpengl;
            this.mc.renderGlobal.loadRenderers();
        }

        if (option == GameSettings.EnumOptions.ANAGLYPH) {
            this.anaglyph = !this.anaglyph;
            this.mc.renderEngine.refreshTextures();
        }

        if (option == GameSettings.EnumOptions.FRAMERATE_LIMIT) {
            this.limitFramerate = (this.limitFramerate + value + 3) % 3;
        }

        if (option == GameSettings.EnumOptions.DIFFICULTY) {
            this.difficulty = this.difficulty + value & 3;
        }

        if (option == GameSettings.EnumOptions.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.loadRenderers();
        }

        if (option == GameSettings.EnumOptions.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = !this.ambientOcclusion;
            this.mc.renderGlobal.loadRenderers();
        }

        this.saveOptions();
    }

    public float getOptionFloatValue(EnumOptions option) {
        if (option == EnumOptions.MUSIC) {
            return this.musicVolume;
        }
        if (option == EnumOptions.SOUND) {
            return this.soundVolume;
        }
        if (option == EnumOptions.SENSITIVITY) {
            return this.mouseSensitivity;
        }
        return 0.0f;
    }

    public boolean getOptionOrdinalValue(GameSettings.EnumOptions option) {
        switch (option) {
            case INVERT_MOUSE:
                return this.invertMouse;
            case VIEW_BOBBING:
                return this.viewBobbing;
            case ANAGLYPH:
                return this.anaglyph;
            case ADVANCED_OPENGL:
                return this.advancedOpengl;
            case AMBIENT_OCCLUSION:
                return this.ambientOcclusion;
            default:
                return false;
        }
    }

    public String getKeyBinding(EnumOptions option) {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        String string = stringTranslate.translateKey(option.getEnumString()) + ": ";
        if (option.getEnumFloat()) {
            float f = this.getOptionFloatValue(option);
            if (option == EnumOptions.SENSITIVITY) {
                if (f == 0.0f) {
                    return string + stringTranslate.translateKey("options.sensitivity.min");
                }
                if (f == 1.0f) {
                    return string + stringTranslate.translateKey("options.sensitivity.max");
                }
                return string + (int) (f * 200.0f) + "%";
            }
            if (f == 0.0f) {
                return string + stringTranslate.translateKey("options.off");
            }
            return string + (int) (f * 100.0f) + "%";
        }
        if (option.getEnumBoolean()) {
            boolean bl = this.getOptionOrdinalValue(option);
            if (bl) {
                return string + stringTranslate.translateKey("options.on");
            }
            return string + stringTranslate.translateKey("options.off");
        }
        if (option == EnumOptions.RENDER_DISTANCE) {
            return string + stringTranslate.translateKey(RENDER_DISTANCES[this.renderDistance]);
        }
        if (option == EnumOptions.DIFFICULTY) {
            return string + stringTranslate.translateKey(DIFFICULTIES[this.difficulty]);
        }
        if (option == EnumOptions.GUI_SCALE) {
            return string + stringTranslate.translateKey(GUISCALES[this.guiScale]);
        }
        if (option == EnumOptions.FRAMERATE_LIMIT) {
            return string + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]);
        }
        if (option == EnumOptions.GRAPHICS) {
            if (this.fancyGraphics) {
                return string + stringTranslate.translateKey("options.graphics.fancy");
            }
            return string + stringTranslate.translateKey("options.graphics.fast");
        }
        return string;
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }

            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                try {
                    String[] astring = s.split(":");
                    if (astring[0].equals("music")) {
                        this.musicVolume = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("sound")) {
                        this.soundVolume = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("mouseSensitivity")) {
                        this.mouseSensitivity = this.parseFloat(astring[1]);
                    }

                    if (astring[0].equals("invertYMouse")) {
                        this.invertMouse = astring[1].equals("true");
                    }

                    if (astring[0].equals("viewDistance")) {
                        this.renderDistance = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("guiScale")) {
                        this.guiScale = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("bobView")) {
                        this.viewBobbing = astring[1].equals("true");
                    }

                    if (astring[0].equals("anaglyph3d")) {
                        this.anaglyph = astring[1].equals("true");
                    }

                    if (astring[0].equals("advancedOpengl")) {
                        this.advancedOpengl = astring[1].equals("true");
                    }

                    if (astring[0].equals("fpsLimit")) {
                        this.limitFramerate = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("difficulty")) {
                        this.difficulty = Integer.parseInt(astring[1]);
                    }

                    if (astring[0].equals("fancyGraphics")) {
                        this.fancyGraphics = astring[1].equals("true");
                    }

                    if (astring[0].equals("ao")) {
                        this.ambientOcclusion = astring[1].equals("true");
                    }

                    if (astring[0].equals("skin")) {
                        this.skin = astring[1];
                    }

                    if (astring[0].equals("lastServer") && astring.length >= 2) {
                        this.lastServer = astring[1];
                    }

                    for (int i = 0; i < this.keyBindings.length; i++) {
                        if (astring[0].equals("key_" + this.keyBindings[i].keyDescription)) {
                            this.keyBindings[i].keyCode = Integer.parseInt(astring[1]);
                        }
                    }
                } catch (Exception exception) {
                    System.out.println("Skipping bad option: " + s);
                }
            }

            bufferedreader.close();
        } catch (Exception exception1) {
            System.out.println("Failed to load options");
            exception1.printStackTrace();
        }
    }

    private float parseFloat(String s) {
        if (s.equals("true")) {
            return 1.0f;
        }
        if (s.equals("false")) {
            return 0.0f;
        }
        return Float.parseFloat(s);
    }

    public void saveOptions() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
            printwriter.println("music:" + this.musicVolume);
            printwriter.println("sound:" + this.soundVolume);
            printwriter.println("invertYMouse:" + this.invertMouse);
            printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printwriter.println("viewDistance:" + this.renderDistance);
            printwriter.println("guiScale:" + this.guiScale);
            printwriter.println("bobView:" + this.viewBobbing);
            printwriter.println("anaglyph3d:" + this.anaglyph);
            printwriter.println("advancedOpengl:" + this.advancedOpengl);
            printwriter.println("fpsLimit:" + this.limitFramerate);
            printwriter.println("difficulty:" + this.difficulty);
            printwriter.println("fancyGraphics:" + this.fancyGraphics);
            printwriter.println("ao:" + this.ambientOcclusion);
            printwriter.println("skin:" + this.skin);
            printwriter.println("lastServer:" + this.lastServer);

            for (int i = 0; i < this.keyBindings.length; i++) {
                printwriter.println("key_" + this.keyBindings[i].keyDescription + ":" + this.keyBindings[i].keyCode);
            }

            printwriter.close();
        } catch (Exception exception) {
            System.out.println("Failed to save options");
            exception.printStackTrace();
        }
    }

    public enum EnumOptions {
        MUSIC("options.music", true, false),
        SOUND("options.sound", true, false),
        INVERT_MOUSE("options.invertMouse", false, true),
        SENSITIVITY("options.sensitivity", true, false),
        RENDER_DISTANCE("options.renderDistance", false, false),
        VIEW_BOBBING("options.viewBobbing", false, true),
        ANAGLYPH("options.anaglyph", false, true),
        ADVANCED_OPENGL("options.advancedOpengl", false, true),
        FRAMERATE_LIMIT("options.framerateLimit", false, false),
        DIFFICULTY("options.difficulty", false, false),
        GRAPHICS("options.graphics", false, false),
        AMBIENT_OCCLUSION("options.ao", false, true),
        GUI_SCALE("options.guiScale", false, false);

        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;

        public static GameSettings.EnumOptions getEnumOptions(int id) {
            for (GameSettings.EnumOptions gamesettings$enumoptions : values()) {
                if (gamesettings$enumoptions.returnEnumOrdinal() == id) {
                    return gamesettings$enumoptions;
                }
            }

            return null;
        }

        EnumOptions(String name, boolean isFloat, boolean isBoolean) {
            this.enumString = name;
            this.enumFloat = isFloat;
            this.enumBoolean = isBoolean;
        }

        public boolean getEnumFloat() {
            return this.enumFloat;
        }

        public boolean getEnumBoolean() {
            return this.enumBoolean;
        }

        public int returnEnumOrdinal() {
            return this.ordinal();
        }

        public String getEnumString() {
            return this.enumString;
        }
    }
}
