/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.network;

import java.awt.*;
import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.data.research.EnumGatedKnowledge;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.item.ISpecialStackDescriptor;
import hellfirepvp.astralsorcery.common.item.block.ItemCollectorCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.CrystalProperties;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.network.TileCollectorCrystal;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockCollectorCrystalBase
 * Created by HellFirePvP
 * Date: 15.09.2016 / 19:03
 */
public abstract class BlockCollectorCrystalBase extends BlockStarlightNetwork implements ISpecialStackDescriptor {

    private static AxisAlignedBB boxCrystal = AxisAlignedBB.getBoundingBox(0.3, 0, 0.3, 0.7, 1, 0.7);

    public BlockCollectorCrystalBase(Material material) {
        super("BlockCollectorCrystal", material);
        setBlockUnbreakable();
        setResistance(200000F);
        setHarvestLevel("pickaxe", 2);
        // setSoundType(SoundType.GLASS);
        setLightLevel(0.7F);
        setCreativeTab(RegistryItems.creativeTabAstralSorceryTunedCrystals);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }

    // @Override
    // public boolean causesSuffocation() {
    // return false;
    // }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return boxCrystal;
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // CrystalProperties prop = CrystalProperties.getCrystalProperties(stack);
    // BlockCollectorCrystalBase.CollectorCrystalType type = ItemCollectorCrystal.getType(stack);
    // Optional<Boolean> missing = CrystalProperties.addPropertyTooltip(prop, tooltip, type ==
    // CollectorCrystalType.CELESTIAL_CRYSTAL ? CrystalProperties.MAX_SIZE_CELESTIAL : CrystalProperties.MAX_SIZE_ROCK);
    //
    // if(missing.isPresent()) {
    // ProgressionTier tier = ResearchManager.clientProgress.getTierReached();
    // IWeakConstellation c = ItemCollectorCrystal.getConstellation(stack);
    // if(c != null) {
    // if(EnumGatedKnowledge.COLLECTOR_TYPE.canSee(tier) &&
    // ResearchManager.clientProgress.hasConstellationDiscovered(c.getUnlocalizedName())) {
    // tooltip.add(ChatFormatting.GRAY + I18n.format("crystal.collect.type") + " " + ChatFormatting.BLUE +
    // I18n.format(c.getUnlocalizedName()));
    // } else if(!missing.get()) {
    // tooltip.add(ChatFormatting.GRAY + I18n.format("progress.missing.knowledge"));
    // }
    // }
    // }
    // }

    /*
     * @Override
     * public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX,
     * float hitY, float hitZ) {
     * if(!worldIn.isRemote) {
     * TileCollectorCrystal te = MiscUtils.getTileAt(worldIn, pos, TileCollectorCrystal.class);
     * if(te != null) {
     * playerIn.addChatMessage(new TextComponentString("PlayerMade: " + te.isPlayerMade()));
     * playerIn.addChatMessage(new TextComponentString("Constellation: " + te.getTransmittingType().getName()));
     * playerIn.addChatMessage(new TextComponentString("Can charge: " + te.canCharge()));
     * playerIn.addChatMessage(new TextComponentString("Charge: " + te.getCharge()));
     * }
     * }
     * return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
     * }
     */

    @Override
    public float getBlockHardness(World worldIn, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileCollectorCrystal te = MiscUtils.getTileAt(worldIn, pos, TileCollectorCrystal.class, true);
        if (te != null) {
            if (te.isPlayerMade()) {
                return 4.0F;
            }
        }
        return super.getBlockHardness(worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        if (placer == null || !(placer instanceof EntityPlayer)) return;
        BlockPos pos = new BlockPos(x, y, z);
        TileCollectorCrystal te = MiscUtils.getTileAt(worldIn, pos, TileCollectorCrystal.class, true);
        if (te == null) return;

        IWeakConstellation c = ItemCollectorCrystal.getConstellation(stack);
        if (c != null) {
            te.onPlace(c, CrystalProperties.getCrystalProperties(stack), true, ItemCollectorCrystal.getType(stack));
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return Lists.newArrayList();
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileCollectorCrystal();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        BlockPos pos = new BlockPos(x, y, z);
        TileCollectorCrystal te = MiscUtils.getTileAt(world, pos, TileCollectorCrystal.class, true);
        if (te != null) {
            if (te.getCrystalProperties() == null || te.getConstellation() == null || te.getType() == null) {
                return null;
            }
            ItemStack stack = new ItemStack(this);
            CrystalProperties.applyCrystalProperties(stack, te.getCrystalProperties());
            ItemCollectorCrystal.setConstellation(stack, te.getConstellation());
            ItemCollectorCrystal.setType(stack, te.getType());
            return stack;
        }
        return null;
    }

    @Override
    public String getUnlocalizedName() {
        PlayerProgress client = ResearchManager.clientProgress;
        if (EnumGatedKnowledge.COLLECTOR_CRYSTAL.canSee(client.getTierReached())) {
            return super.getUnlocalizedName();
        }
        return "tile.BlockCollectorCrystal.obf";
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int damageDropped(int state) {
        return state;
    }

    @Override
    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player) {
        BlockPos pos = new BlockPos(x, y, z);
        TileCollectorCrystal te = MiscUtils.getTileAt(worldIn, pos, TileCollectorCrystal.class, true);
        if (te != null && !worldIn.isRemote) {
            PktParticleEvent event = new PktParticleEvent(
                PktParticleEvent.ParticleEventType.COLLECTOR_BURST,
                pos.getX(),
                pos.getY(),
                pos.getZ());
            PacketChannel.CHANNEL.sendToAllAround(event, PacketChannel.pointFromPos(worldIn, pos, 32));
            TileCollectorCrystal.breakDamage(worldIn, pos);

            if (te.isPlayerMade() && !player.capabilities.isCreativeMode) {
                ItemStack drop = new ItemStack(
                    te.getType() == CollectorCrystalType.CELESTIAL_CRYSTAL ? BlocksAS.celestialCollectorCrystal
                        : BlocksAS.collectorCrystal);
                if (te.getCrystalProperties() != null && te.getConstellation() != null) {
                    CrystalProperties.applyCrystalProperties(drop, te.getCrystalProperties());
                    ItemCollectorCrystal
                        .setType(drop, te.getType() != null ? te.getType() : CollectorCrystalType.ROCK_CRYSTAL);
                    ItemCollectorCrystal.setConstellation(drop, te.getConstellation());
                    ItemUtils.dropItemNaturally(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
                }
            }
        }
        super.onBlockHarvested(worldIn, x, y, z, meta, player);
    }

    public static enum CollectorCrystalType {

        ROCK_CRYSTAL(new Color(0xDD, 0xDD, 0xFF)),
        CELESTIAL_CRYSTAL(new Color(0x0, 0x88, 0xFF));

        public final Color displayColor;

        private CollectorCrystalType(Color c) {
            this.displayColor = c;
        }

    }

}
