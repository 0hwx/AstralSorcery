/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.TileIlluminator;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockWorldIlluminator
 * Created by HellFirePvP
 * Date: 01.11.2016 / 16:00
 */
public class BlockWorldIlluminator extends BlockContainer {

    public BlockWorldIlluminator() {
        super(Material.rock);
        setBlockName("BlockWorldIlluminator");
        setHardness(3.0F);
setStepSound(soundTypeStone);
        setResistance(25.0F);
        setLightLevel(0.4F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileIlluminator();
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean func_149730_j() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote && placer instanceof EntityPlayerMP && !MiscUtils.isPlayerFakeMP((EntityPlayerMP) placer)) {
            TileIlluminator ti = MiscUtils.getTileAt(worldIn, new BlockPos(x, y, z), TileIlluminator.class, true);
            if (ti != null) {
                ti.setPlayerPlaced();
            }
        }
    }

//    @Override
//    public boolean canRenderInLayer(Block state, BlockRenderLayer layer) {
//        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
//    }
//
    @Override
    public int getRenderType() {
        return 1;
    }

}
