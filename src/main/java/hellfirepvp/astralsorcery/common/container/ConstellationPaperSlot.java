/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.container;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import hellfirepvp.astralsorcery.common.item.ItemConstellationPaper;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: FilteredSlot
 * Created by HellFirePvP
 * Date: 22.11.2016 / 14:43
 */
public class ConstellationPaperSlot extends Slot {

    private final ContainerJournal listener;

    public ConstellationPaperSlot(IInventory handle, ContainerJournal containerJournal, int index, int xPosition,
        int yPosition) {
        super(handle, index, xPosition, yPosition);
        this.listener = containerJournal;
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() != null
            && stack.getItem() instanceof ItemConstellationPaper
            && ItemConstellationPaper.getConstellation(stack) != null;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();

        listener.slotChanged();
    }
}
