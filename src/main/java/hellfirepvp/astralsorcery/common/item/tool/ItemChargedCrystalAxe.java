package hellfirepvp.astralsorcery.common.item.tool;

import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.network.packet.server.PktDualParticleEvent;
import hellfirepvp.astralsorcery.common.tile.TileFakeTree;
import hellfirepvp.astralsorcery.common.tile.TileTranslucent;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.struct.BlockArray;
import hellfirepvp.astralsorcery.common.util.struct.TreeDiscoverer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Map;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemChargedCrystalAxe
 * Created by HellFirePvP
 * Date: 11.03.2017 / 11:59
 */
public class ItemChargedCrystalAxe extends ItemCrystalAxe implements ChargedCrystalToolBase {


    public ItemChargedCrystalAxe() {
        setUnlocalizedName("ItemChargedCrystalAxe");
    }
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
        World world = player.getEntityWorld();
        if (!world.isRemote) {// && !player.getCooldownTracker().hasCooldown(ItemsAS.chargedCrystalAxe)) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockArray tree = TreeDiscoverer.tryCaptureTreeAt(world, pos, 9, true);
            if (tree != null) {
                Map<BlockPos, BlockArray.BlockInformation> pattern = tree.getPattern();
                for (Map.Entry<BlockPos, BlockArray.BlockInformation> blocks : pattern.entrySet()) {
                    world.setBlock(blocks.getKey().getX(), blocks.getKey().getY(), blocks.getKey().getZ(), BlocksAS.blockFakeTree);
                    TileFakeTree tt = MiscUtils.getTileAt(world, blocks.getKey(), TileFakeTree.class, true);
                    if(tt != null) {
                        tt.setupTile(player, itemstack, blocks.getValue().type);
                    } else {
                        world.setBlock(blocks.getKey().getX(), blocks.getKey().getY(), blocks.getKey().getZ(),blocks.getValue().type, blocks.getValue().metadata, 3);
                    }
                }
                if(!ChargedCrystalToolBase.tryRevertMainHand(player, itemstack)) {
//                    player.getCooldownTracker().setCooldown(ItemsAS.chargedCrystalAxe, 150);
                }
                return true;
            }
        }
        return super.onBlockStartBreak(itemstack, x, y, z, player);
    }

    @SideOnly(Side.CLIENT)
    public static void playDrainParticles(PktDualParticleEvent pktDualParticleEvent) {
        Vector3 to = pktDualParticleEvent.getTargetVec();
        int colorHex = MathHelper.floor_double(pktDualParticleEvent.getAdditionalData());
        Color c = new Color(colorHex);
        for (int i = 0; i < 10; i++) {
            Vector3 from = pktDualParticleEvent.getOriginVec().add(itemRand.nextFloat(), itemRand.nextFloat(), itemRand.nextFloat());
            Vector3 mov = to.clone().subtract(from).normalize().multiply(0.1 + 0.1 * itemRand.nextFloat());
            EntityFXFacingParticle p = EffectHelper.genericFlareParticle(from.getX(), from.getY(), from.getZ());
            p.motion(mov.getX(), mov.getY(), mov.getZ()).setMaxAge(30 + itemRand.nextInt(25));
            p.gravity(0.004).scale(0.25F).setColor(c);
        }
    }

    @Override
    public Item getInertVariant() {
        return ItemsAS.crystalAxe;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon("astralsorcery:crystal_axe_s");
    }
}
