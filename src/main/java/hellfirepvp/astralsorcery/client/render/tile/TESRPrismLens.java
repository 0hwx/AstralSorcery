/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.models.base.ASprism_color;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.TextureHelper;
import hellfirepvp.astralsorcery.client.util.resource.AssetLibrary;
import hellfirepvp.astralsorcery.client.util.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.util.resource.BindableResource;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystal;
import hellfirepvp.astralsorcery.common.tile.network.TileCrystalPrismLens;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;


/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRPrismLens
 * Created by HellFirePvP
 * Date: 20.09.2016 / 13:08
 */
public class TESRPrismLens extends TileEntitySpecialRenderer {

    private static List<TileCrystalPrismLens> coloredPositions = new LinkedList<>();

    private static final ASprism_color modelPrismColoredFrame = new ASprism_color();
    private static final BindableResource texPrismColorFrame = AssetLibrary.loadTexture(AssetLoader.TextureLocation.MODELS, "prism/prism_color");

    public static void renderColoredPrismsLast() {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        RenderingUtils.removeStandartTranslationFromTESRMatrix(Minecraft.getMinecraft().timer.renderPartialTicks);

        GL11.glEnable(GL11.GL_BLEND);
        Blending.DEFAULT.apply();

        for (TileCrystalPrismLens prism : coloredPositions) {
            Color c = prism.getLensColor().wrappedColor;
            GL11.glPushMatrix();

            GL11.glTranslated(prism.xCoord + 0.5, prism.yCoord + 1.5, prism.zCoord + 0.5);
            GL11.glScaled(0.0625, 0.0625, 0.0625);
            GL11.glRotated(180, 1, 0, 0);

            GL11.glColor4f(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, 1F);
            renderColoredPrism();
            GL11.glColor4f(1F, 1F, 1F, 1F);

            GL11.glPopMatrix();
        }

        coloredPositions.clear();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileCrystalPrismLens te = (TileCrystalPrismLens) tile;
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        if(te.getLinkedPositions().size() > 0) {
            long sBase = 0x5911539513145924L;
            sBase ^= (long) te.xCoord;
            sBase ^= (long) te.yCoord;
            sBase ^= (long) te.zCoord;
            RenderingUtils.renderLightRayEffects(x + 0.5, y + 0.6, z + 0.5, BlockCollectorCrystal.CollectorCrystalType.ROCK_CRYSTAL.displayColor, sBase, ClientScheduler.getClientTick(), 9, 50, 25);
        }

        GL11.glTranslated(x + 0.5, y + 0.20, z + 0.5);

        GL11.glScaled(0.6, 0.6, 0.6);
        GL11.glEnable(GL11.GL_BLEND);
        Blending.DEFAULT.apply();
        TESRCollectorCrystal.renderCrystal(false, true);
        RenderHelper.disableStandardItemLighting();
        if(te.getLensColor() != null) {
            coloredPositions.add(te);
            /*GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
            GL11.glScaled(0.0625, 0.0625, 0.0625);
            GL11.glRotated(180, 1, 0, 0);
            GL11.glEnable(GL11.GL_BLEND);
            Blending.DEFAULT.applyServer();
            Color c = te.getLensColor().wrappedColor;
            GL11.glColor4f(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, 1F);
            renderColoredPrism();
            GL11.glColor4f(1F, 1F, 1F, 1F);*/
        }
        TextureHelper.refreshTextureBindState();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private static void renderColoredPrism() {
        texPrismColorFrame.bind();
        modelPrismColoredFrame.render(null, 0, 0, 0, 0, 0, 1);
    }

}
