/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.models.base.ASstarmapper;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.SpriteLibrary;
import hellfirepvp.astralsorcery.client.util.TextureHelper;
import hellfirepvp.astralsorcery.client.util.resource.AssetLibrary;
import hellfirepvp.astralsorcery.client.util.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.util.resource.BindableResource;
import hellfirepvp.astralsorcery.client.util.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.common.tile.TileMapDrawingTable;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.data.Tuple;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRMapDrawingTable
 * Created by HellFirePvP
 * Date: 23.04.2017 / 22:39
 */
public class TESRMapDrawingTable extends TileEntitySpecialRenderer {

    private static List<BlockPos> requiredGlasses = new LinkedList<>();

    public static void renderRemainingGlasses(float pTicks) {
        TextureHelper.refreshTextureBindState();
        TextureHelper.setActiveTextureToAtlasSprite();

         GL11.glPushMatrix();
        GL11.glRotatef(240.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glColor4f(1F, 1F, 1F,1F);
        GL11.glEnable(GL_BLEND);
        Blending.DEFAULT.apply();
         GL11.glPushMatrix();
        RenderingUtils.removeStandartTranslationFromTESRMatrix(pTicks);

        texDrawingTable.bind();
        for (BlockPos p : requiredGlasses) {
             GL11.glPushMatrix();
            GL11.glTranslated(p.getX() + 0.5, p.getY() + 1.5, p.getZ() + 0.5);
            GL11.glRotatef(180, 1, 0, 0);
            GL11.glScaled(0.0625, 0.0625, 0.0625);

            modelDrawingTable.treated_glass.render(1F);

            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
        requiredGlasses.clear();
        RenderHelper.disableStandardItemLighting();

        TextureHelper.refreshTextureBindState();
    }

    private static final ASstarmapper modelDrawingTable = new ASstarmapper();
    private static final BindableResource texDrawingTable = AssetLibrary.loadTexture(AssetLoader.TextureLocation.MODELS, "starmapper/astralsorcery_starmapper");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileMapDrawingTable te = (TileMapDrawingTable) tile;
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glEnable(GL_BLEND);
        Blending.DEFAULT.apply();

        if(!te.hasParchment() && te.getSlotIn() != null && te.getSlotIn().getItem() != null) {
            ItemStack in = te.getSlotIn();
            RenderHelper.enableStandardItemLighting();
             GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 1.02, z + 0.75);
            GL11.glRotatef(270, 1, 0, 0);
            GL11.glScalef(2F, 2F, 2F);

//            Minecraft.getMinecraft().getRenderItem().renderItem(in, ItemCameraTransforms.TransformType.GROUND);
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
        }

         GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glScaled(0.0625, 0.0625, 0.0625);
        GL11.glEnable(GL_BLEND);

        RenderHelper.disableStandardItemLighting();

         GL11.glPushMatrix();
        GL11.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(165.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        texDrawingTable.bind();
        modelDrawingTable.render(null, te.hasParchment() ? 1 : 0, 0, 0, 0, 0, 1F);
        RenderHelper.disableStandardItemLighting();
        TextureHelper.refreshTextureBindState();
        GL11.glPopMatrix();

        if(te.getPercRunning() > 1E-4) {
            GL11.glColor4f(1F, 1F, 1F, te.getPercRunning());

            SpriteSheetResource halo = SpriteLibrary.spriteHalo2;
            halo.getResource().bind();
            Tuple<Double, Double> uvFrame = halo.getUVOffset(ClientScheduler.getClientTick());
            float rot = 360F - ((float) (ClientScheduler.getClientTick()     % 2000) / 2000F * 360F);

//            GlStateManager.disableLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
             GL11.glPushMatrix();
//            GlStateManager.disableCull();
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_ALPHA_TEST);

            RenderingUtils.renderAngleRotatedTexturedRect(
                    new Vector3(0.5, 1.01, 0.5).add(te.xCoord, te.yCoord, te.zCoord),
                    Vector3.RotAxis.Y_AXIS, Math.toRadians(rot), 1F,
                    uvFrame.key, uvFrame.value, halo.getULength(), halo.getVLength(), partialTicks);

//            GlStateManager.enableCull();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glEnable(GL_LIGHTING);
//            GlStateManager.enableLighting();
        }

        if(te.getSlotGlassLens() != null && te.getSlotGlassLens().getItem() != null) {
           BlockPos tePos = new BlockPos(te.xCoord, te.yCoord, te.zCoord);
            requiredGlasses.add(tePos);
        }

        TextureHelper.refreshTextureBindState();
    }

}
