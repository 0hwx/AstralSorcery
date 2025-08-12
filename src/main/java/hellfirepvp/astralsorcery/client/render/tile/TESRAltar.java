/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import java.awt.*;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.tile.TileAltar;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRAltar
 * Created by HellFirePvP
 * Date: 11.05.2016 / 18:21
 */
public class TESRAltar extends AstralTileRenderer<TileAltar> {

    private IModelCustom model = AdvancedModelLoader
        .loadModel(new ResourceLocation(AstralSorcery.MODID, "models/block/BlockAltar1.obj"));
    private ResourceLocation texture = new ResourceLocation(
        AstralSorcery.MODID,
        "textures/models/altar/altar_1_top.png");

    @Override
    public void renderAstralTileEntityAt(TileAltar tile, double x, double y, double z, float tick) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        this.bindTexture(texture);
        model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        TileAltar dummyTile = new TileAltar();
        // Render the tile entity at origin
        this.renderAstralTileEntityAt(dummyTile, 0, 0, 0, 0.0f);
        GL11.glPopMatrix();

    }
    // private static final Random rand = new Random();
    // private static final ASaltarT2 modelAltar2 = new ASaltarT2();
    // private static final BindableResource texAltar2 = AssetLibrary.loadTexture(AssetLoader.TextureLocation.MODELS,
    // "base/altarT2");
    //
    // private static final ASaltarT3 modelAltar3 = new ASaltarT3();
    // private static final BindableResource texAltar3 = AssetLibrary.loadTexture(AssetLoader.TextureLocation.MODELS,
    // "base/altarT3");
    //
    // @Override
    // public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
    // TileAltar te = (TileAltar) tile;
    // switch (te.getAltarLevel()) {
    // case DISCOVERY:
    // this.bindTexture(new ResourceLocation("textures/entity/chest/normal.png"));
    // break;
    // case TRAIT_CRAFT:
    // if (te.getMultiblockState()) {
    // IConstellation c = te.getFocusedConstellation();
    // if (c != null) {
    // GL11.glPushMatrix();
    // float alphaDaytime = ConstellationSkyHandler.getInstance().getCurrentDaytimeDistribution(te.getWorldObj());
    // alphaDaytime *= 0.8F;
    //
    // int max = 5000;
    // int t = (int) (ClientScheduler.getClientTick() % max);
    // float halfAge = max / 2F;
    // float tr = 1F - (Math.abs(halfAge - t) / halfAge);
    // tr *= 2;
    //
    // RenderingUtils.removeStandartTranslationFromTESRMatrix(partialTicks);
    //
    // float br = 0.9F * alphaDaytime;
    //
    // RenderConstellation.renderConstellationIntoWorldFlat(c, c.getConstellationColor(), new Vector3(te).add(0.5, 0.03,
    // 0.5), 5 + tr, 2, 0.1F + br);
    // GL11.glPopMatrix();
    // }
    // GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
    // GL11.glPushMatrix();
    // GL11.glTranslated(x + 0.5, y + 4, z + 0.5);
    // ActiveCraftingTask act = te.getActiveCraftingTask();
    // if (act != null && act.getRecipeToCraft() instanceof TraitRecipe) {
    // Collection<ItemHandle> requiredHandles = ((TraitRecipe) act.getRecipeToCraft()).getTraitItemHandles();
    // if (!requiredHandles.isEmpty()) {
    // int amt = 60 / requiredHandles.size();
    // for (ItemHandle outer : requiredHandles) {
    // List<ItemStack> stacksApplicable = outer.getApplicableItemsForRender();
    // int mod = (int) (ClientScheduler.getClientTick() % (stacksApplicable.size() * 60));
    // ItemStack element = stacksApplicable.get(MathHelper.floor_double(
    // MathHelper.clamp_double(stacksApplicable.size() * (mod / (stacksApplicable.size() * 60)), 0,
    // stacksApplicable.size() - 1)));
    // Color col = ItemColorizationHelper.getDominantColorFromItemStack(element);
    // if (col == null) {
    // col = BlockCollectorCrystal.CollectorCrystalType.CELESTIAL_CRYSTAL.displayColor;
    // }
    // RenderingUtils.renderLightRayEffects(0, 0.5, 0, col, 0x12315L | outer.hashCode(),
    // ClientScheduler.getClientTick(), 20, 2F, amt, amt / 2);
    // }
    // }
    // RenderingUtils.renderLightRayEffects(0, 0.5, 0, Color.WHITE, 0, ClientScheduler.getClientTick(), 15, 2F, 40, 25);
    // } else {
    // RenderingUtils.renderLightRayEffects(0, 0.5, 0, Color.WHITE, 0x12315661L, ClientScheduler.getClientTick(), 20,
    // 2F, 50, 25);
    // RenderingUtils.renderLightRayEffects(0, 0.5, 0, Color.BLUE, 0, ClientScheduler.getClientTick(), 10, 1F, 40, 25);
    // }
    // GL11.glTranslated(0, 0.15, 0);
    // GL11.glScaled(0.7, 0.7, 0.7);
    // TESRCollectorCrystal.renderCrystal(true, true);
    // GL11.glPopMatrix();
    // TextureHelper.refreshTextureBindState();
    // GL11.glPopAttrib();
    // }
    // break;
    // }
    //
    // ActiveCraftingTask task = te.getActiveCraftingTask();
    // if (task != null) {
    // task.getRecipeToCraft().onCraftTESRRender(te, x, y, z, partialTicks);
    // }
    // }
    // private void renderT3Additions(TileAltar te, double x, double y, double z, double jump) {
    // GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
    // GL11.glPushMatrix();
    // GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
    // GL11.glRotated(180, 1, 0, 0);
    // GL11.glScaled(0.0625, 0.0625, 0.0625);
    // RenderHelper.disableStandardItemLighting();
    //
    // GL11.glPushMatrix();
    // //GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
    // GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
    // RenderHelper.enableStandardItemLighting();
    // GL11.glPopMatrix();
    //
    // texAltar3.bind();
    // modelAltar3.render(null, (float) jump, 0, 0, 0, 0, 1F);
    // RenderHelper.disableStandardItemLighting();
    // GL11.glPopMatrix();
    // GL11.glPopAttrib();
    // }
    //
    // private void renderT2Additions(TileAltar te, double x, double y, double z, double jump) {
    // GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
    // GL11.glPushMatrix();
    // GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
    // GL11.glRotated(180, 1, 0, 0);
    // GL11.glScaled(0.0625, 0.0625, 0.0625);
    // RenderHelper.disableStandardItemLighting();
    //
    // GL11.glPushMatrix();
    // //GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
    // GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
    // RenderHelper.enableStandardItemLighting();
    // GL11.glPopMatrix();
    //
    // texAltar2.bind();
    // modelAltar2.render(null, (float) jump, 0, 0, 0, 0, 1F);
    // RenderHelper.disableStandardItemLighting();
    // GL11.glPopMatrix();
    // GL11.glPopAttrib();
    // }

}
