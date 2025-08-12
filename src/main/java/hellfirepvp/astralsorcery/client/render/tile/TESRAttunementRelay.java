/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import hellfirepvp.astralsorcery.common.tile.TileAttunementRelay;
import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRAttunementRelay
 * Created by HellFirePvP
 * Date: 27.03.2017 / 18:07
 */
public class TESRAttunementRelay extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileAttunementRelay te = (TileAttunementRelay) tile;
        TileInventoryBase.ItemHandlerTile iht = te.getInventoryHandler();
        if (iht == null) return;
        ItemStack in = iht.getStackInSlot(0);
        if (in == null) return;
        EntityItem ei = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, in);
        ei.age = te.getTicksExisted();
        ei.hoverStart = 0;
        RenderItem.getInstance()
            .doRender(ei, x + 0.5, y, z + 0.5, 0, partialTicks);
    }

}
