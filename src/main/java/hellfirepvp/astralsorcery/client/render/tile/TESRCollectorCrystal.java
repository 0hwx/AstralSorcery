/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.item.block.ItemCollectorCrystal;
import hellfirepvp.astralsorcery.common.tile.network.TileCollectorCrystal;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRCollectorCrystal
 * Created by HellFirePvP
 * Date: 01.08.2016 / 13:42
 */
public class TESRCollectorCrystal extends AstralTileRenderer<TileCollectorCrystal> {

    private static final Random rand = new Random();
    private static IModelCustom crystalModel = AdvancedModelLoader
        .loadModel(new ResourceLocation(AstralSorcery.MODID, "models/obj/crystal_big.obj"));

    private static ResourceLocation texWhite = new ResourceLocation(
        AstralSorcery.MODID,
        "textures/models/crystal_big_white.png");
    private static ResourceLocation texBlue = new ResourceLocation(
        AstralSorcery.MODID,
        "textures/models/crystal_big_blue.png");

    @Override
    public void renderAstralTileEntityAt(TileCollectorCrystal tile, double x, double y, double z, float partialTicks) {
        BlockCollectorCrystalBase.CollectorCrystalType type = tile.getType();
        long sBase = 1553015L;
        sBase ^= (long) tile.xCoord;
        sBase ^= (long) tile.yCoord;
        sBase ^= (long) tile.zCoord;
        Color c = type == null ? BlockCollectorCrystalBase.CollectorCrystalType.ROCK_CRYSTAL.displayColor
            : type.displayColor;
        if (tile.isEnhanced()) {
            RenderingUtils.renderLightRayEffects(
                x + 0.5,
                y + 0.5,
                z + 0.5,
                c,
                sBase,
                ClientScheduler.getClientTick(),
                20,
                1.4F,
                50,
                25);
            RenderingUtils.renderLightRayEffects(
                x + 0.5,
                y + 0.5,
                z + 0.5,
                Color.WHITE,
                sBase,
                ClientScheduler.getClientTick(),
                40,
                2,
                15,
                15);
        } else {
            RenderingUtils.renderLightRayEffects(
                x + 0.5,
                y + 0.5,
                z + 0.5,
                c,
                sBase,
                ClientScheduler.getClientTick(),
                20,
                50,
                25);
        }

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        renderCrystal(type == BlockCollectorCrystalBase.CollectorCrystalType.CELESTIAL_CRYSTAL, true);
        GL11.glPopMatrix();
    }

    public static void renderCrystal(boolean isCelestial, boolean bounce) {
        GL11.glPushMatrix();
        if (bounce) {
            int t = (int) (Minecraft.getMinecraft().theWorld.getTotalWorldTime() & 255);
            float perc = (256 - t) / 256F;
            perc = MathHelper.cos((float) (perc * 2 * Math.PI));
            GL11.glTranslated(0, 0.03 * perc, 0);
        }
        RenderHelper.disableStandardItemLighting();
        if (isCelestial) {
            renderTile(texBlue);
        } else {
            renderTile(texWhite);
        }
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    private static void renderTile(ResourceLocation tex) {
        GL11.glPushMatrix();
        GL11.glScalef(0.13F, 0.13F, 0.13F);
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(tex);
        crystalModel.renderAll();
        GL11.glPopMatrix();
    }

    // @Override
    // public void render(ItemStack stack) {
    // GL11.glPushMatrix();
    // GL11.glTranslated(0.5, 0, 0.5);
    // RenderHelper.disableStandardItemLighting();
    // BlockCollectorCrystalBase.CollectorCrystalType type = ItemCollectorCrystal.getType(stack);
    // switch (type) {
    // case ROCK_CRYSTAL:
    // renderTile(texWhite);
    // break;
    // case CELESTIAL_CRYSTAL:
    // renderTile(texBlue);
    // break;
    // }
    // GL11.glPopMatrix();
    // RenderHelper.enableStandardItemLighting();
    // }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0, 0.5);
        RenderHelper.disableStandardItemLighting();
        BlockCollectorCrystalBase.CollectorCrystalType myType = ItemCollectorCrystal.getType(item);
        switch (myType) {
            case ROCK_CRYSTAL:
                renderTile(texWhite);
                break;
            case CELESTIAL_CRYSTAL:
                renderTile(texBlue);
                break;
        }
        GL11.glPopMatrix();
        RenderHelper.enableStandardItemLighting();
    }
}
