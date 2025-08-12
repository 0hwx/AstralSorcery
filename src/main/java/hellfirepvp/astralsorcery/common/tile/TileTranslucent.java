/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.tile;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileTranslucent
 * Created by HellFirePvP
 * Date: 17.01.2017 / 03:45
 */
public class TileTranslucent extends TileEntitySynchronized {

    private Block fakedState = Blocks.air;
    private int metadata = 0;

    public Block getFakedState() {
        return fakedState;
    }

    public void setFakedState(Block fakedState) {
        this.fakedState = fakedState;
        markForUpdate();
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        if (compound.hasKey("Block") && compound.hasKey("Data")) {
            int data = compound.getInteger("Data");
            Block b = Block.getBlockFromName(compound.getString("Block"));
            if (b != null) {
                metadata = b.damageDropped(data);
            }
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        if (fakedState != null) {
            compound.setString(
                "Block",
                Block.blockRegistry.getNameForObject(fakedState)
                    .toString());
            compound.setInteger("Data", metadata);
        }
    }

}
