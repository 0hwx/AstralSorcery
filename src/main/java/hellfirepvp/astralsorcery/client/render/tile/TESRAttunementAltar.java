/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.models.base.ASaltarAT;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.resource.AssetLibrary;
import hellfirepvp.astralsorcery.client.util.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.util.resource.BindableResource;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRAttunementAltar
 * Created by HellFirePvP
 * Date: 06.12.2016 / 22:03
 */
public class TESRAttunementAltar extends AstralTileRenderer<TileAttunementAltar> {

    private static final ASaltarAT modelAttunementAltar = new ASaltarAT();
    private static final BindableResource texModelAttunementAltar = AssetLibrary
        .loadTexture(AssetLoader.TextureLocation.MODELS, "base/altarattunement");

    @Override
    public void renderAstralTileEntityAt(TileAttunementAltar tile, double x, double y, double z, float partialTicks) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glScaled(0.055, 0.055, 0.055);
        GL11.glRotated(180, 1, 0, 0);

        GL11.glPushMatrix();
        GL11.glRotatef(165.0F, 1.0F, 0.0F, 0.0F);
        // RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        texModelAttunementAltar.bind();
        modelAttunementAltar.renderBase();
        GL11.glPopMatrix();

        float startY = -1.5F;
        float endY = -0.5F;
        float tickPartY = (endY - startY) / ((float) TileAttunementAltar.MAX_START_ANIMATION_TICK);

        float prevPosY = endY + (tile.prevActivationTick * tickPartY);
        float posY = endY + (tile.activationTick * tickPartY);
        double framePosY = RenderingUtils.interpolate(prevPosY, posY, partialTicks);

        double generalAnimationTick = ClientScheduler.getClientTick() / 4D;
        if (tile.animate) {
            if (tile.tesrLocked) {
                tile.tesrLocked = false;
            }
        } else {
            if (tile.tesrLocked) {
                generalAnimationTick = 7.25D;
            } else {
                if (Math.abs((generalAnimationTick % TileAttunementAltar.MAX_START_ANIMATION_SPIN) - 7.25D) <= 0.3125) {
                    generalAnimationTick = 7.25D;
                    tile.tesrLocked = true;
                }
            }
        }

        float spinDur = TileAttunementAltar.MAX_START_ANIMATION_SPIN;

        for (int i = 1; i < 9; i++) {
            float incrementer = (spinDur / 8) * i;

            double aFrame = generalAnimationTick + incrementer;
            double prevAFrame = generalAnimationTick + incrementer - 1;
            double renderFrame = RenderingUtils.interpolate(prevAFrame, aFrame, 0);

            double partRenderFrame = (renderFrame % spinDur) / spinDur;
            float normalized = (float) (partRenderFrame * 2F * Math.PI);

            float xOffset = MathHelper.cos(normalized);
            float zOffset = MathHelper.sin(normalized);

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + framePosY + 0.5, z + 0.5);
            GL11.glScaled(0.055, 0.055, 0.055);
            GL11.glRotated(180, 1, 0, 0);

            renderHovering(generalAnimationTick, spinDur, 1);

            // modelAttunementAltar.renderHovering(
            // xOffset,
            // zOffset,
            // (float) RenderingUtils.interpolate(
            // ((float) tile.prevActivationTick) / TileAttunementAltar.MAX_START_ANIMATION_TICK,
            // ((float) tile.activationTick) / TileAttunementAltar.MAX_START_ANIMATION_TICK,
            // partialTicks));

            GL11.glPopMatrix();
        }

        // RenderHelper.disableStandardItemLighting();

        GL11.glPopAttrib();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();

        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glScaled(0.055, 0.055, 0.055);
        GL11.glRotated(180, 1, 0, 0);

        texModelAttunementAltar.bind();
        modelAttunementAltar.renderBase();

        // RenderHelper.enableStandardItemLighting();

        // Add animation for the hovering parts similar to tile entity rendering
        double generalAnimationTick = ClientScheduler.getClientTick() / 4D;
        float spinDur = TileAttunementAltar.MAX_START_ANIMATION_SPIN;
        GL11.glTranslated(0.0, -0.5, 0.0);
        renderHovering(generalAnimationTick, spinDur, 1);

        // RenderHelper.disableStandardItemLighting();

        GL11.glPopMatrix();

    }

    private void renderHovering(double generalAnimationTick, float spinDur, float partialTicks) {
        for (int i = 1; i < 9; i++) {
            float incrementer = (spinDur / 8) * i;

            double aFrame = generalAnimationTick + incrementer;
            double renderFrame = aFrame;
            double partRenderFrame = (renderFrame % spinDur) / spinDur;
            float normalized = (float) (partRenderFrame * 2F * Math.PI);

            float xOffset = MathHelper.cos(normalized) * 0.8F; // Scale down the movement for item rendering
            float zOffset = MathHelper.sin(normalized) * 0.8F;

            modelAttunementAltar.renderHovering(xOffset, zOffset, partialTicks);
        }
    }

}
