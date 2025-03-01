package hellfirepvp.astralsorcery.common.base;

import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: MeltInteraction
 * Created by HellFirePvP
 * Date: 22.04.2017 / 16:40
 */
public interface MeltInteraction {

    public boolean isMeltable(World world, BlockPos pos, Block state);

    default public int getMeltTickDuration() {
        return 100; //Def. Furance duration halved
    }

    //Result can be a blockstate and/or an itemstack.
    //The BlockState result, if present, takes precedence.
    @Nullable
    public Block getMeltResultState();

    @Nullable
    public ItemStack getMeltResultStack();

    default public void placeResultAt(World world, BlockPos pos) {
        Block result = getMeltResultState();
        if(result != null) {
            world.setBlock(pos.getX(), pos.getY(), pos.getZ(), result, 3,1);// todo check this
        } else {
            world.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
            ItemStack resultStack = getMeltResultStack();
            if(resultStack != null && resultStack.getItem() != null) {
                ItemUtils.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, resultStack);
            }
        }
    }

}
