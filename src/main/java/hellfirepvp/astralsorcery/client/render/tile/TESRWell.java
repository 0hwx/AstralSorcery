/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.common.tile.TileWell;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRWell
 * Created by HellFirePvP
 * Date: 18.10.2016 / 16:25
 */
public class TESRWell extends AstralTileRenderer<TileWell> {

    private IModelCustom model = AdvancedModelLoader
        .loadModel(new ResourceLocation(AstralSorcery.MODID, "models/block/BlockWell.obj"));
    private ResourceLocation texture = new ResourceLocation("astralsorcery", "textures/models/lightwell/lightwell.png");

    @Override
    public void renderAstralTileEntityAt(TileWell tile, double x, double y, double z, float partialTicks) {
        if (tile != null) {
            ItemStack catalyst = tile.getInventoryHandler()
                .getStackInSlot(0);
            if (catalyst != null) {
                renderItemInWorld(catalyst, x + 0.5, y + 0.8, z + 0.5, tile.getTicksExisted());
            }

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y, z + 0.5);
            this.bindTexture(texture);
            this.model.renderAll();

            if (tile.getFluidAmount() > 0 && tile.getHeldFluid() != null) {
                GL11.glEnable(GL11.GL_BLEND);
                Blending.DEFAULT.apply();
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                this.bindTexture(TextureMap.locationBlocksTexture);
                RenderHelper.disableStandardItemLighting();
                renderFluid(tile, 0.75, 0.32 + tile.getPercFilled() * 0.6); // Adjust yoffset based on fluid level
                RenderHelper.enableStandardItemLighting();
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();

        // // Scale and position the item depending on render type
        switch (type) {
            case INVENTORY:
                GL11.glTranslatef(0.0f, -0.1f, 0.0f);
                break;
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                GL11.glScalef(1.5f, 1.5f, 1.5f);
                GL11.glTranslatef(0.5f, -0.5f, -0.2f);
                break;
            case ENTITY:
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            default:
                break;
        }

        // Create a dummy tile entity for rendering
        TileWell dummyTile = new TileWell();

        // Render the tile entity at origin
        this.renderAstralTileEntityAt(dummyTile, 0, 0, 0, 0.0f);

        GL11.glPopMatrix();
    }

    private void renderFluid(TileWell tile, double size, double yoffset) {
        String iconName = tile.getHeldFluid()
            .getBlock()
            .getIcon(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, 0)
            .getIconName();
        TextureMap textureMap = Minecraft.getMinecraft()
            .getTextureMapBlocks();
        TextureAtlasSprite tas = textureMap.getTextureExtry(iconName) != null ? textureMap.getTextureExtry(iconName)
            : textureMap.missingImage;
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(-size / 2, yoffset, size / 2, tas.getMinU(), tas.getMaxV());
        tess.addVertexWithUV(size / 2, yoffset, size / 2, tas.getMaxU(), tas.getMaxV());
        tess.addVertexWithUV(size / 2, yoffset, -size / 2, tas.getMaxU(), tas.getMinV());
        tess.addVertexWithUV(-size / 2, yoffset, -size / 2, tas.getMinU(), tas.getMinV());
        tess.draw();
    }

}
