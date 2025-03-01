/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item.tool;

import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.block.BlockCustomOre;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler;
import hellfirepvp.astralsorcery.common.data.research.EnumGatedKnowledge;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.data.world.WorldCacheManager;
import hellfirepvp.astralsorcery.common.data.world.data.RockCrystalBuffer;
import hellfirepvp.astralsorcery.common.item.base.ISpecialInteractItem;
import hellfirepvp.astralsorcery.common.item.base.IWandInteract;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemWand
 * Created by HellFirePvP
 * Date: 23.09.2016 / 12:57
 */
public class ItemWand extends Item implements ISpecialInteractItem {

    private static final Random rand = new Random();

    public ItemWand() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setUnlocalizedName("ItemWand");
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    /*@Override
   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block state = worldIn.getBlockState(pos);
        Block b = state.getBlock();
        if(b instanceof IWandInteract) {
            if(((IWandInteract) b).onInteract(worldIn, pos, playerIn, facing, playerIn.isSneaking())) {
                return EnumActionResult.SUCCESS;
            }
        }
        IWandInteract wandTe = MiscUtils.getTileAt(worldIn, pos, IWandInteract.class);
        if(wandTe != null) {
            if(wandTe.onInteract(worldIn, pos, playerIn, facing, playerIn.isSneaking())) {
                return EnumActionResult.SUCCESS;
            }
        }
        playerIn.swingArm(hand);
        return EnumActionResult.PASS;
    }*/

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!isSelected) isSelected = (entityIn instanceof EntityPlayer) && ((EntityPlayer) entityIn).getHeldItem() == stack;
        if(!worldIn.isRemote && isSelected && worldIn.getTotalWorldTime() % 20 == 0 && entityIn instanceof EntityPlayerMP) {
            //PlayerProgress progress = ResearchManager.getProgress((EntityPlayer) entityIn);
            //if(progress == null || !EnumGatedKnowledge.WAND_TYPE.canSee(progress.getViewCapability())) return;

            RockCrystalBuffer buf = WorldCacheManager.getOrLoadData(worldIn, WorldCacheManager.SaveKey.ROCK_CRYSTAL);
            BlockPos getPosition = new BlockPos(entityIn).getPosition();;
            ChunkCoordIntPair pos = new ChunkCoordIntPair(getPosition.chunkX(), getPosition.chunkZ());
            List<BlockPos> posList = buf.collectPositions(pos, 4);
            for (BlockPos rPos : posList) {
                Block state = worldIn.getBlock(rPos.getX(), rPos.getY(), rPos.getZ());
                if(!(state instanceof BlockCustomOre)) {// || state.getValue(BlockCustomOre.ORE_TYPE) != BlockCustomOre.OreType.ROCK_CRYSTAL) {
                    buf.removeOre(rPos);
                    continue;
                }
                int top = worldIn.getTopSolidOrLiquidBlock(rPos.getX(),rPos.getZ());
                double dstr = ConstellationSkyHandler.getInstance().getCurrentDaytimeDistribution(worldIn);
                if(dstr > 1E-4) {
                    PktParticleEvent pkt = new PktParticleEvent(PktParticleEvent.ParticleEventType.WAND_CRYSTAL_HIGHLIGHT, rPos.getX(), top, rPos.getZ());
                    PacketChannel.CHANNEL.sendTo(pkt, (EntityPlayerMP) entityIn);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void highlightEffects(PktParticleEvent event) {
        BlockPos pos = event.getVec().toBlockPos();
        double x = pos.getX() + rand.nextFloat() * (rand.nextBoolean() ? 2 : -2);
        double y = pos.getY() + rand.nextFloat() * (rand.nextBoolean() ? 2 : -2);
        double z = pos.getZ() + rand.nextFloat() * (rand.nextBoolean() ? 2 : -2);
        double velX = rand.nextFloat() * 0.01F * (rand.nextBoolean() ? 1 : -1);
        double velY = rand.nextFloat() * 0.3F;
        double velZ = rand.nextFloat() * 0.01F * (rand.nextBoolean() ? 1 : -1);
        double dstr = ConstellationSkyHandler.getInstance().getCurrentDaytimeDistribution(Minecraft.getMinecraft().theWorld);
        for (int i = 0; i < 10; i++) {
            EntityFXFacingParticle particle = EffectHelper.genericFlareParticle(x, y, z);
            particle.setColor(BlockCollectorCrystalBase.CollectorCrystalType.ROCK_CRYSTAL.displayColor);
            particle.motion(velX * (0.2 + 0.8 * rand.nextFloat()), velY * (0.4 + 0.6 * rand.nextFloat()), velZ * (0.2 + 0.8 * rand.nextFloat()));
            particle.scale(0.7F).setMaxAge(70);
            particle.enableAlphaFade(EntityComplexFX.AlphaFunction.PYRAMID).setAlphaMultiplier((float) ((150 * dstr) / 255F));
        }

    }

    @Override
    public boolean needsSpecialHandling(World world, BlockPos at, EntityPlayer player, ItemStack stack) {
        return true;
    }

    @Override
    public void onRightClick(World world, BlockPos pos, EntityPlayer entityPlayer, int side, ItemStack stack) {
        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
        if(block instanceof IWandInteract) {
            ((IWandInteract) block).onInteract(world, pos, entityPlayer, side, entityPlayer.isSneaking());
            return;
        }
        IWandInteract wandTe = MiscUtils.getTileAt(world, pos, IWandInteract.class, true);
        if(wandTe != null) {
            wandTe.onInteract(world, pos, entityPlayer, side, entityPlayer.isSneaking());
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon("astralsorcery:wand");
    }
}
