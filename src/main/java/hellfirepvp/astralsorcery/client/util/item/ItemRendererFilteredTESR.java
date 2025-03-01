/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemRendererFilteredTESR
 * Created by HellFirePvP
 * Date: 31.07.2016 / 10:20
 */
public class ItemRendererFilteredTESR implements IItemRenderer {

    private Map<Integer, TEISRProperties> renderMap = new HashMap<>();

    public void addRender(int stackMeta, TileEntitySpecialRenderer tesr, TileEntity renderTile) {
        renderMap.put(stackMeta, new TEISRProperties(tesr, renderTile));
    }

//    @Override
//    public void render(ItemStack stack) {
//
//    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if(renderMap.containsKey(item.getItemDamage())) {
            TEISRProperties prop = renderMap.get(item.getItemDamage());
            prop.tesr.renderTileEntityAt(prop.renderTile, 0, 0, 0, Minecraft.getMinecraft().timer.renderPartialTicks);
        }
    }

    private static class TEISRProperties {

        private final TileEntitySpecialRenderer tesr;
        private final TileEntity renderTile;

        private TEISRProperties(TileEntitySpecialRenderer tesr, TileEntity renderTile) {
            this.tesr = tesr;
            this.renderTile = renderTile;
        }
    }

}
