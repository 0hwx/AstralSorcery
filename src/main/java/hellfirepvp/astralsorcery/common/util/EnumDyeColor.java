package hellfirepvp.astralsorcery.common.util;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum EnumDyeColor {

    WHITE(0, 15, "white", "white", 16383998, ChatFormatting.WHITE),
    ORANGE(1, 14, "orange", "orange", 16351261, ChatFormatting.GOLD),
    MAGENTA(2, 13, "magenta", "magenta", 13061821, ChatFormatting.AQUA),
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, ChatFormatting.BLUE),
    YELLOW(4, 11, "yellow", "yellow", 16701501, ChatFormatting.YELLOW),
    LIME(5, 10, "lime", "lime", 8439583, ChatFormatting.GREEN),
    PINK(6, 9, "pink", "pink", 15961002, ChatFormatting.LIGHT_PURPLE),
    GRAY(7, 8, "gray", "gray", 4673362, ChatFormatting.DARK_GRAY),
    SILVER(8, 7, "silver", "silver", 10329495, ChatFormatting.GRAY),
    CYAN(9, 6, "cyan", "cyan", 1481884, ChatFormatting.DARK_AQUA),
    PURPLE(10, 5, "purple", "purple", 8991416, ChatFormatting.DARK_PURPLE),
    BLUE(11, 4, "blue", "blue", 3949738, ChatFormatting.DARK_BLUE),
    BROWN(12, 3, "brown", "brown", 8606770, ChatFormatting.GOLD),
    GREEN(13, 2, "green", "green", 6192150, ChatFormatting.DARK_GREEN),
    RED(14, 1, "red", "red", 11546150, ChatFormatting.DARK_RED),
    BLACK(15, 0, "black", "black", 1908001, ChatFormatting.BLACK);

    private static final EnumDyeColor[] META_LOOKUP = new EnumDyeColor[values().length];
    private static final EnumDyeColor[] DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String translationKey;
    /** An int containing the corresponding RGB color for this dye color. */
    public final int colorValue;
    /**
     * An array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the corresponding
     * color.
     */
    private final float[] colorComponentValues;
    private final ChatFormatting chatColor;

    private EnumDyeColor(int metaIn, int dyeDamageIn, String nameIn, String unlocalizedNameIn, int colorValueIn,
        ChatFormatting chatColorIn) {
        this.meta = metaIn;
        this.dyeDamage = dyeDamageIn;
        this.name = nameIn;
        this.translationKey = unlocalizedNameIn;
        this.colorValue = colorValueIn;
        this.chatColor = chatColorIn;
        int i = (colorValueIn & 16711680) >> 16;
        int j = (colorValueIn & 65280) >> 8;
        int k = (colorValueIn & 255) >> 0;
        this.colorComponentValues = new float[] { (float) i / 255.0F, (float) j / 255.0F, (float) k / 255.0F };
    }

    public int getMetadata() {
        return this.meta;
    }

    public int getDyeDamage() {
        return this.dyeDamage;
    }

    @SideOnly(Side.CLIENT)
    public String getDyeColorName() {
        return this.name;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    /**
     * Gets the RGB color corresponding to this dye color.
     */
    @SideOnly(Side.CLIENT)
    public int getColorValue() {
        return this.colorValue;
    }

    /**
     * Gets an array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the
     * corresponding color.
     */
    public float[] getColorComponentValues() {
        return this.colorComponentValues;
    }

    public static EnumDyeColor byDyeDamage(int damage) {
        if (damage < 0 || damage >= DYE_DMG_LOOKUP.length) {
            damage = 0;
        }

        return DYE_DMG_LOOKUP[damage];
    }

    public static EnumDyeColor byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    public String toString() {
        return this.translationKey;
    }

    public String getName() {
        return this.name;
    }

    static {
        for (EnumDyeColor enumdyecolor : values()) {
            META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
        }
    }
}
