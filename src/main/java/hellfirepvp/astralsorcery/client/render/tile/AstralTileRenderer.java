package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public abstract class AstralTileRenderer<T extends TileEntity> extends TileEntitySpecialRenderer
    implements IItemRenderer {

    private RenderItem itemRenderer = new RenderItem();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        renderAstralTileEntityAt((T) tile, x, y, z, tick);
    }

    public abstract void renderAstralTileEntityAt(T tile, double x, double y, double z, float tick);

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {}

    public void renderItemInWorld(ItemStack item, double x, double y, double z, float partialTicks) {
        EntityItem ei = new EntityItem(null, x, y, z, item);
        ei.age = (int) partialTicks;
        ei.hoverStart = 0;
        this.itemRenderer.setRenderManager(RenderManager.instance);
        this.itemRenderer.doRender(ei, x, y, z, 0.0F, 0.0F);
    }
}
