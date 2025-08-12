/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.constellation.effect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import com.google.common.collect.Lists;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ILocatable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: CEffectEntityCollect
 * Created by HellFirePvP
 * Date: 09.11.2016 / 18:42
 */
public abstract class CEffectEntityCollect<T extends Entity> extends ConstellationEffect {

    protected final Class<T> classToSearch;
    protected final IEntitySelector searchFilter;
    protected double range;
    public static boolean enabled = true;

    private static Map<Class, AxisAlignedBB> baseBoundingBox = new HashMap<>();

    public CEffectEntityCollect(@Nullable ILocatable origin, IWeakConstellation constellation, String cfgName,
        double defaultRange, Class<T> entityClass, IEntitySelector filter) {
        super(origin, constellation, cfgName);
        this.classToSearch = entityClass;
        this.searchFilter = filter;
        this.range = defaultRange;
    }

    public List<T> collectEntities(World world, BlockPos pos) {
        if (!enabled) return Lists.newArrayList();
        return world.selectEntitiesWithinAABB(
            classToSearch,
            baseBoundingBox.get(getClass())
                .offset(pos.getX(), pos.getY(), pos.getZ())
                .expand(range, range, range),
            searchFilter);
    }

    @Override
    public void loadFromConfig(Configuration cfg) {
        range = cfg.getFloat(
            getKey() + "Range",
            getConfigurationSection(),
            (float) range,
            2,
            64,
            "Defines the range in which the ritual will try to find entities");
        enabled = cfg.getBoolean(
            getKey() + "Enabled",
            getConfigurationSection(),
            true,
            "Set to false to disable this ConstellationEffect.");

        baseBoundingBox.put(
            getClass(),
            AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1)
                .expand(range, range, range));
    }

}
