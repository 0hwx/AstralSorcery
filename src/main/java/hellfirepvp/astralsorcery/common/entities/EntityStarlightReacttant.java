/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.entities;

import hellfirepvp.astralsorcery.common.block.fluid.FluidBlockLiquidStarlight;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.ForgeDirection;


/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EntityStarlightReacttant
 * Created by HellFirePvP
 * Date: 11.12.2016 / 16:26
 */
public interface EntityStarlightReacttant {

    default public boolean isInLiquidStarlight(Entity e) {
        BlockPos at = new BlockPos(e).getPosition();//e.getPosition();
        Block state = e.worldObj.getBlock(at.getX(), at.getY(), at.getZ());
        if(!(state instanceof FluidBlockLiquidStarlight)) {
            return false;
        }
        if(!((FluidBlockLiquidStarlight) state).isSourceBlock(e.worldObj, at.getX(), at.getY(), at.getZ())) {
            return false;
        }
        BlockPos down = at.down();
        state = e.worldObj.getBlock(down.getX(), down.getY(), down.getZ());
        return state.isSideSolid(e.worldObj, down.getX(), down.getY(), down.getZ(), ForgeDirection.UP);
    }

}
