/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.network;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.base.TileNetwork;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockNetwork
 * Created by HellFirePvP
 * Date: 03.08.2016 / 21:01
 */
public abstract class BlockStarlightNetwork extends BlockContainer {

    // public BlockStarlightNetwork(Material blockMaterialIn, MapColor blockMapColorIn) {
    // super(blockMaterialIn);
    // }
    String blockName;

    public BlockStarlightNetwork(String blockName, Material materialIn) {
        super(materialIn);
        this.blockName = blockName;
        this.setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        this.setBlockName(blockName);
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        BlockPos pos = new BlockPos(x, y, z);
        TileNetwork teN = MiscUtils.getTileAt(worldIn, pos, TileNetwork.class, true);
        if (teN != null) {
            teN.onBreak();
        }

        // TileEntity inv = MiscUtils.getTileAt(worldIn, pos, TileEntity.class, true);
        // if(inv != null && !worldIn.isRemote) {
        // for (ForgeDirection face : ForgeDirection.values()) {
        // IInventory handle = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
        // if (handle != null) {
        // ItemUtils.dropInventory(handle, worldIn, pos);
        // break;
        // }
        // }
        // }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

}
