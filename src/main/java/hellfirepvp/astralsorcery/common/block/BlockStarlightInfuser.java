/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import hellfirepvp.astralsorcery.common.block.network.BlockStarlightNetwork;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.TileStarlightInfuser;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockStarlightInfuser
 * Created by HellFirePvP
 * Date: 11.12.2016 / 17:05
 */
public class BlockStarlightInfuser extends BlockStarlightNetwork{

    private static final AxisAlignedBB box = AxisAlignedBB.getBoundingBox(0D, 0D, 0D, 1D, 12D / 16D, 1D);

    public BlockStarlightInfuser() {
        super("BlockStarlightInfuser", Material.rock);
        setHardness(1.0F);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", 1);
setStepSound(soundTypeStone);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        setBlockTextureName("astralsorcery:starlightinfuser");
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return box;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            ItemStack heldItem = player.getHeldItem();
            BlockPos pos = new BlockPos(x, y, z);
            TileStarlightInfuser infuser = MiscUtils.getTileAt(worldIn, pos, TileStarlightInfuser.class, true);
            if(infuser != null) {
                infuser.onInteract(player, heldItem);
            }
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        if(!worldIn.isRemote) {
            BlockPos pos = new BlockPos(x, y, z);
            TileStarlightInfuser infuser = MiscUtils.getTileAt(worldIn, pos, TileStarlightInfuser.class, true);
            if(infuser != null && infuser.getInputStack() != null) {
                ItemUtils.dropItemNaturally(worldIn,
                        infuser.xCoord + 0.5,
                        infuser.yCoord + 1,
                        infuser.zCoord + 0.5,
                        infuser.getInputStack());
            }
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileStarlightInfuser();
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

//    @Override
//    public int getRenderType() {
//        return -1;
//    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileStarlightInfuser();
    }
}
