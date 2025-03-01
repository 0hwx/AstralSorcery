package hellfirepvp.astralsorcery.common.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.tile.TileFakeTree;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.struct.BlockArray;
import hellfirepvp.astralsorcery.common.util.struct.BlockDiscoverer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Map;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemChargedCrystalShovel
 * Created by HellFirePvP
 * Date: 13.03.2017 / 18:25
 */
public class ItemChargedCrystalShovel extends ItemCrystalShovel implements ChargedCrystalToolBase {


    public ItemChargedCrystalShovel() {
        setUnlocalizedName("ItemChargedCrystalShovel");
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
        World world = player.getEntityWorld();
        if (!world.isRemote) {// && !player.getCooldownTracker().hasCooldown(ItemsAS.chargedCrystalShovel)) {
            BlockPos pos = new BlockPos(x, y, z);
            Block at = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if(at.isToolEffective("shovel", at.getDamageValue(world, pos.getX(), pos.getY(), pos.getZ()))) {
                BlockArray shovelables = BlockDiscoverer.discoverBlocksWithSameStateAround(world, pos, false, 8, 100, true);
                if (shovelables != null) {
                    Map<BlockPos, BlockArray.BlockInformation> pattern = shovelables.getPattern();
                    for (Map.Entry<BlockPos, BlockArray.BlockInformation> blocks : pattern.entrySet()) {
                        world.setBlock(blocks.getKey().getX(), blocks.getKey().getY(), blocks.getKey().getZ(), BlocksAS.blockFakeTree);
                        TileFakeTree tt = MiscUtils.getTileAt(world, blocks.getKey(), TileFakeTree.class, true);
                        if(tt != null) {
                            tt.setupTile(player, itemstack, blocks.getValue().type);
                        } else {
                            world.setBlock(blocks.getKey().getX(), blocks.getKey().getY(), blocks.getKey().getZ(), blocks.getValue().type);
                        }
                    }
//                    if(!ChargedCrystalToolBase.tryRevertMainHand(player, itemstack)) {
//                        player.getCooldownTracker().setCooldown(ItemsAS.chargedCrystalShovel, 150);
//                    }
                    return true;
                }
            }
        }
        return super.onBlockStartBreak(itemstack, x, y, z, player);
    }

    @Override
    public Item getInertVariant() {
        return ItemsAS.crystalShovel;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon("astralsorcery:crystal_shovel_s");
    }
}
