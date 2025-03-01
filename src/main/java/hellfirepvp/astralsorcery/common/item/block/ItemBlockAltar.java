/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item.block;

import hellfirepvp.astralsorcery.common.block.network.BlockAltar;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemBlockAltar
 * Created by HellFirePvP
 * Date: 10.11.2016 / 10:37
 */
public class ItemBlockAltar extends ItemBlockCustomName {

    public ItemBlockAltar(Block block) {
        super(block);
        setMaxStackSize(1);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        BlockAltar.AltarType type = BlockAltar.AltarType.values()[metadata];
        if(type != null) {
            switch (type) {
                case ALTAR_1:
                    break;
                case ALTAR_2:
                case ALTAR_3:
                    for (int xx = -1; xx <= 1; xx++) {
                        for (int zz = -1; zz <= 1; zz++) {
                            if (!world.isAirBlock(x + xx, y, z+ zz) && !world.getBlock(x + xx, y, z+ zz).isReplaceable(world, x + xx, y, z+ zz)) {
                                return false;
                            }
                        }
                    }
                    break;
                case ALTAR_4:
                    break;
                case ALTAR_5:
                    break;
            }
        }

        return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }

//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack) {
//        String name;
//
//        switch (par1ItemStack.getItemDamage()) {
//            case 0:
//                name = super.getUnlocalizedName(par1ItemStack) + "." + "Tier1";
//                break;
//            case 1:
//                name = "Tier2";
//                break;
//            case 2:
//                name = "Tier3";
//                break;
//            case 3:
//                name = "Tier4";
//                break;
//            case 4:
//                name = "hvCable";
//                break;
//            case 5:
//                name = "glassFibreCable";
//                break;
//            case 6:
//                name = "lvCable";
//                break;
//            case 13:
//                name = "meCable";
//                break;
//            case 14:
//                name = "aluminumWire";
//                break;
//            case 15:
//                name = "aluminumWireHeavy";
//                break;
//            default:
//                name = "null";
//        }
//        return name;
//    }

}
