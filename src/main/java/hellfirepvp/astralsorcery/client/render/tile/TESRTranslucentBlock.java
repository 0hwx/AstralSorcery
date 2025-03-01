/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import hellfirepvp.astralsorcery.client.util.AirBlockRenderWorld;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.TextureHelper;
import hellfirepvp.astralsorcery.common.tile.TileTranslucent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRTranslucentBlock
 * Created by HellFirePvP
 * Date: 17.01.2017 / 03:57
 */
public class TESRTranslucentBlock extends TileEntitySpecialRenderer {

    protected static final List<TranslucentBlockState> blocks = new LinkedList<>();
    private static int hash = -1;
    private static int batchDList = -1;

    public static void renderTranslucentBlocks() {
        TextureHelper.refreshTextureBindState();
        TextureHelper.setActiveTextureToAtlasSprite();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        RenderingUtils.removeStandartTranslationFromTESRMatrix(Minecraft.getMinecraft().timer.renderPartialTicks);
        GL11.glColor4f(1F, 1F, 1F,1F);

        if(batchDList == -1) {
            batchBlocks();
            hash = hashBlocks();
            blocks.clear();
        } else {
            int currentHash = hashBlocks();
            if(hash != currentHash) {
                GLAllocation.deleteDisplayLists(batchDList);
                batchBlocks();
                hash = currentHash;
                blocks.clear();
            }
        }
        GL11.glEnable(GL11.GL_BLEND);
        Blending.ADDITIVEDARK.apply();
        GL11.glCallList(batchDList);
        blocks.clear();
        Blending.DEFAULT.apply();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private static void batchBlocks() {
        IBlockAccess iba = new AirBlockRenderWorld(BiomeGenBase.plains, Minecraft.getMinecraft().theWorld.getWorldInfo().getTerrainType());
        batchDList = GLAllocation.generateDisplayLists(1);
        GL11.glEnable(GL11.GL_BLEND);
        Blending.ADDITIVEDARK.apply();
        GL11.glNewList(batchDList, GL11.GL_COMPILE);
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
//        VertexBuffer vb = tes.getBuffer();
//        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        RenderBlocks rb = new RenderBlocks(iba);
        for (TranslucentBlockState tbs : blocks) {
            rb.renderBlockAllFaces(tbs.block, tbs.posX, tbs.posY, tbs.posZ);
//            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(tbs.state, tbs.pos, iba, vb);
        }
        tess.draw();
        GL11.glEndList();
        Blending.DEFAULT.apply();
    }

    private static int hashBlocks() {
        int hash = 80238287;
        for (TranslucentBlockState tbs : blocks) {
            hash = (hash << 4) ^ (hash >> 28) ^ (tbs.posX * 5449 % 130651);
            hash = (hash << 4) ^ (hash >> 28) ^ (tbs.posY * 5449 % 130651);
            hash = (hash << 4) ^ (hash >> 28) ^ (tbs.posZ * 5449 % 130651);
            hash = (hash << 4) ^ (hash >> 28) ^ (tbs.hashCode() * 5449 % 130651);
        }
        return hash % 75327403;
    }

    public static void cleanUp() {
        hash = -1;
        if(batchDList != -1) {
            GLAllocation.deleteDisplayLists(batchDList);
            batchDList = -1;
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileTranslucent te = (TileTranslucent) tile;
        if(te.getFakedState() == null) return;
        Block renderState = te.getFakedState();
        TESRTranslucentBlock.blocks.add(new TESRTranslucentBlock.TranslucentBlockState(renderState, te.xCoord, te.yCoord, te.zCoord));
    }

    public static class TranslucentBlockState {

        public final Block block;
        public final int posX;
        public final int posY;
        public final int posZ;

        public TranslucentBlockState(Block block, int x, int y, int z) {
            this.block = block;
            this.posX = x;
            this.posY = y;
            this.posZ = z;
        }

    }

}
