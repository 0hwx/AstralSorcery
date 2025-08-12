package hellfirepvp.astralsorcery.common.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockMachineMaterial extends Material {

    public BlockMachineMaterial(MapColor mapColor) {
        super(mapColor);
        setRequiresTool();
        setImmovableMobility();
    }
}
