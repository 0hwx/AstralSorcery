/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.fluid;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: FluidBlockLiquidStarlight
 * Created by HellFirePvP
 * Date: 14.09.2016 / 11:38
 */
public class FluidBlockLiquidStarlight extends BlockFluidClassic {

    public FluidBlockLiquidStarlight() {
        super(BlocksAS.fluidLiquidStarlight, new MaterialLiquid(MapColor.silverColor));
        // setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random rand) {
        Integer level = 1; // stateIn.getValue(LEVEL);
        double percHeight = 1D - (((double) level + 1) / 8D);
        EntityFXFacingParticle p = EffectHelper.genericFlareParticle(x + 0.5, y, z + 0.5);
        p.offset(0, percHeight, 0);
        p.offset(
            rand.nextFloat() * 0.5 * (rand.nextBoolean() ? 1 : -1),
            0,
            rand.nextFloat() * 0.5 * (rand.nextBoolean() ? 1 : -1));
        p.scale(0.2F)
            .gravity(0.006)
            .setColor(BlockCollectorCrystalBase.CollectorCrystalType.ROCK_CRYSTAL.displayColor);
        if (rand.nextInt(3) == 0) {
            p = EffectHelper.genericFlareParticle(x + 0.5, y, z + 0.5);
            p.offset(0, percHeight, 0);
            p.offset(
                rand.nextFloat() * 0.5 * (rand.nextBoolean() ? 1 : -1),
                0,
                rand.nextFloat() * 0.5 * (rand.nextBoolean() ? 1 : -1));
            p.scale(0.2F)
                .gravity(0.006)
                .setColor(BlockCollectorCrystalBase.CollectorCrystalType.ROCK_CRYSTAL.displayColor);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        super.onNeighborBlockChange(world, x, y, z, neighborBlock);

        interactWithAdjacent(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        interactWithAdjacent(world, x, y, z);
    }

    private void interactWithAdjacent(World world, int x, int y, int z) {
        boolean shouldCreateBlock = false;
        boolean isCold = true;

        for (ForgeDirection side : ForgeDirection.values()) {
            if (side != ForgeDirection.DOWN) {
                BlockPos pos = new BlockPos(x, y, z).offset(side);
                Block offset = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
                if (offset.getMaterial()
                    .isLiquid() && !(offset instanceof FluidBlockLiquidStarlight)
                    && (offset instanceof BlockFluidBase || offset instanceof BlockLiquid)) {
                    int temp = offset instanceof BlockFluidBase
                        ? BlockFluidBase.getTemperature(world, pos.getX(), pos.getY(), pos.getZ())
                        : (offset.getMaterial() == Material.lava ? FluidRegistry.LAVA.getTemperature()
                            : offset.getMaterial() == Material.water ? FluidRegistry.WATER.getTemperature() : 100);
                    isCold = temp <= 300; // colder or equals water.
                    shouldCreateBlock = true;
                    break;
                }
            }
        }

        if (shouldCreateBlock) {
            if (isCold) {
                world.setBlock(x, y, z, Blocks.ice);
            } else {
                if (world.rand.nextInt(900) == 0) {
                    world.setBlock(x, y, z, BlocksAS.customSandOre);
                } else {
                    world.setBlock(x, y, z, Blocks.sand);
                }
            }

            world.playSound(
                x,
                y,
                z,
                world.getBlock(x, y, z).stepSound.getBreakSound(),
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F,
                false);
            for (int i = 0; i < 10; ++i) {
                world.spawnParticle(
                    "largesmoke",
                    x + Math.random(),
                    y + Math.random(),
                    z + Math.random(),
                    0.0D,
                    0.0D,
                    0.0D);
            }
        }

    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return !world.getBlock(x, y, z)
            .getMaterial()
            .isLiquid() && super.displaceIfPossible(world, x, y, z);
    }

    // @Override
    // public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, Block Block, Entity entity, double
    // yToTest, Material materialIn, boolean testingHead) {
    // AxisAlignedBB box = Block.getBoundingBox(world, blockpos).offset(blockpos);
    // AxisAlignedBB entityBox = entity.getEntityBoundingBox();//.offset(entity.posX, entity.posY, entity.posZ);
    // return box.intersectsWith(entityBox) && materialIn.isLiquid();
    // }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, x, y, z, entityIn);

        if (entityIn instanceof EntityPlayer) {
            ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 300, 0, true));
        } else if (entityIn instanceof EntityItem) {

        }
    }
}
