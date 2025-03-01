/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.tile;

import hellfirepvp.astralsorcery.common.tile.TileStarlightInfuser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TESRStarlightInfuser
 * Created by HellFirePvP
 * Date: 13.01.2017 / 12:17
 */
public class TESRStarlightInfuser extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileStarlightInfuser te = (TileStarlightInfuser) tile;
        ItemStack in = te.getInputStack();
        if(in == null) return;
        EntityItem ei = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, in);
        ei.age = te.getTicksExisted();
        ei.hoverStart = 0;
        RenderManager.instance.func_147939_a(ei, x + 0.5, y + 0.6, z + 0.5, 0, partialTicks, true);
    }
}
