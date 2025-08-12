/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item.wand;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.event.ClientRenderEventHandler;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.TextureHelper;
import hellfirepvp.astralsorcery.common.base.Mods;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.integrations.ModIntegrationBotania;
import hellfirepvp.astralsorcery.common.item.ItemAlignmentChargeConsumer;
import hellfirepvp.astralsorcery.common.item.ItemBlockStorage;
import hellfirepvp.astralsorcery.common.item.ItemHandRender;
import hellfirepvp.astralsorcery.common.item.ItemHudRender;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.struct.BlockArray;
import hellfirepvp.astralsorcery.common.util.struct.BlockDiscoverer;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemExchangeWand
 * Created by HellFirePvP
 * Date: 07.02.2017 / 01:03
 */
public class ItemExchangeWand extends ItemBlockStorage
    implements ItemHandRender, ItemHudRender, ItemAlignmentChargeConsumer {

    private static final int searchDepth = 5;

    public ItemExchangeWand() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setUnlocalizedName("ItemExchangeWand");
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) { // getStrVsBlock
        return 0;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack itemStack) {
        return true;
    }

    // @Override
    // public boolean canHarvestBlock(Block state, ItemStack stack) {
    // return true;
    // }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRenderInHandHUD(ItemStack lastCacheInstance, float fadeAlpha, float pTicks) {
        ItemStack blockStackStored = getStoredStateAsStack(lastCacheInstance);
        if (blockStackStored == null) return;

        int amtFound = 0;
        if (Mods.BOTANIA.isPresent()) {
            amtFound = ModIntegrationBotania.getItemCount(
                Minecraft.getMinecraft().thePlayer,
                lastCacheInstance,
                ItemUtils.createBlockState(blockStackStored));
        } else {
            Collection<ItemStack> stacks = ItemUtils
                .scanInventoryForMatching(Minecraft.getMinecraft().thePlayer.inventory, blockStackStored, false);
            for (ItemStack stack : stacks) {
                amtFound += stack.stackSize;
            }
        }

        int height = 26;
        int width = 26;
        int offsetX = 30;
        int offsetY = 15;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        Blending.DEFAULT.apply();
        ClientRenderEventHandler.texHUDItemFrame.bind();

        GL11.glColor4f(1F, 1F, 1F, fadeAlpha * 0.9F);
        Tessellator tess = Tessellator.instance;
        // VertexBuffer vb = tes.getBuffer();
        tess.startDrawingQuads();
        tess.addVertexWithUV(offsetX, offsetY + height, 10, 0, 1);
        tess.addVertexWithUV(offsetX + width, offsetY + height, 10, 1, 1);
        tess.addVertexWithUV(offsetX + width, offsetY, 10, 1, 0);
        tess.addVertexWithUV(offsetX, offsetY, 10, 0, 0);
        tess.draw();
        // vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        // vb.pos(offsetX, offsetY + height, 10).tex(0, 1).endVertex();
        // vb.pos(offsetX + width, offsetY + height, 10).tex(1, 1).endVertex();
        // vb.pos(offsetX + width, offsetY, 10).tex(1, 0).endVertex();
        // vb.pos(offsetX, offsetY, 10).tex(0, 0).endVertex();
        // tes.draw();

        TextureHelper.refreshTextureBindState();
        TextureHelper.setActiveTextureToAtlasSprite();

        RenderHelper.enableGUIStandardItemLighting();
        RenderItem ri = new RenderItem();
        Minecraft mc = Minecraft.getMinecraft();
        ri.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, blockStackStored, offsetX + 5, offsetY + 5);
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(GL11.GL_ALPHA_TEST); // Because Mc item rendering..

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslated(offsetX + 14, offsetY + 16, 0);
        String amtString = String.valueOf(amtFound);
        if (amtFound == -1) {
            amtString = "∞";
        }
        GL11.glTranslated(-Minecraft.getMinecraft().fontRenderer.getStringWidth(amtString) / 3, 0, 0);
        GL11.glScaled(0.7, 0.7, 0.7);
        if (amtString.length() > 3) {
            GL11.glScaled(0.9, 0.9, 0.9);
        }
        int c = 0x00DDDDDD;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(amtString, 0, 0, c);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        TextureHelper.refreshTextureBindState();

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRenderWhileInHand(ItemStack stack, float pTicks) {
        Block stored = getStoredState(stack, stack.getItemDamage());
        ItemStack matchStack = getStoredStateAsStack(stack);
        if (stored == null || stored.equals(Blocks.air) || matchStack == null) return;

        EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
        PlayerControllerMP ctrl = Minecraft.getMinecraft().playerController;
        if (ctrl == null || pl == null) return;
        MovingObjectPosition rtr = getLookBlock(pl, false, true, ctrl.getBlockReachDistance());
        if (rtr == null) return;

        // IBlockAccess airWorld = new AirBlockRenderWorld(BiomeGenBase.plains,
        // Minecraft.getMinecraft().theWorld.getWorldInfo().getTerrainType());
        BlockPos origin = new BlockPos(rtr.blockX, rtr.blockY, rtr.blockZ);
        Block atOrigin = Minecraft.getMinecraft().theWorld.getBlock(origin.getX(), origin.getY(), origin.getZ());
        int storedMeta = stored.damageDropped(stack.getItemDamage());
        int atOriginMeta = atOrigin.damageDropped(stack.getItemDamage());
        if (stored.equals(atOrigin) && storedMeta == atOriginMeta) {
            return;
        }
        int amt = 0;
        if (pl.capabilities.isCreativeMode) {
            amt = -1;
        } else {
            if (Mods.BOTANIA.isPresent()) {
                amt = ModIntegrationBotania.getItemCount(Minecraft.getMinecraft().thePlayer, stack, stored);
            } else {
                for (ItemStack st : ItemUtils.findItemsInPlayerInventory(pl, matchStack, false)) {
                    amt += st.stackSize;
                }
            }
        }
        BlockArray found = BlockDiscoverer.discoverBlocksWithSameStateAround(
            Minecraft.getMinecraft().theWorld,
            origin,
            true,
            searchDepth,
            amt,
            false);
        if (found.isEmpty()) return;
        if (atOrigin.getBlockHardness(Minecraft.getMinecraft().theWorld, origin.getX(), origin.getY(), origin.getZ())
            == -1) {
            return;
        }

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_BLEND);
        Blending.ADDITIVEDARK.apply();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderingUtils.removeStandartTranslationFromTESRMatrix(pTicks);

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        // VertexBuffer vb = tes.getBuffer();
        // vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        for (BlockPos pos : found.getPattern()
            .keySet()) {
            // RenderBlocks.getInstance().renderBlock(stored, pos, airWorld, vb);
        }
        // tess.getVertexState((float) TileEntityRendererDispatcher.staticPlayerX, (float)
        // TileEntityRendererDispatcher.staticPlayerY, (float) TileEntityRendererDispatcher.staticPlayerZ);
        tess.draw();

        Blending.DEFAULT.apply();
        if (!blend) {
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (world.isRemote) return false;
        BlockPos origin = new BlockPos(x, y, z);
        if (playerIn.isSneaking()) {
            tryStoreBlock(stack, world, origin);
            return false;
        }

        Block stored = getStoredState(stack, stack.getItemDamage());
        int storedMeta = stack.getItemDamage();
        ItemStack consumeStack = getStoredStateAsStack(stack);
        if (stored == null || stored.equals(Blocks.air) || consumeStack == null) return false;
        Block atOrigin = world.getBlock(origin.getX(), origin.getY(), origin.getZ());
        int atOriginMeta = atOrigin.damageDropped(stack.getItemDamage());
        if (stored.equals(atOrigin) && storedMeta == atOriginMeta) {
            return false;
        }
        Block atState = world.getBlock(origin.getX(), origin.getY(), origin.getZ());
        if (atState.getBlockHardness(world, origin.getX(), origin.getY(), origin.getZ()) == -1) {
            return false;
        }

        int amt = 0;
        if (playerIn.capabilities.isCreativeMode) {
            amt = -1;
        } else {
            if (Mods.BOTANIA.isPresent()) {
                amt = ModIntegrationBotania.getItemCount(playerIn, consumeStack, stored);
            } else {
                for (ItemStack st : ItemUtils.findItemsInPlayerInventory(playerIn, consumeStack, false)) {
                    amt += st.stackSize;
                }
            }
        }
        BlockArray found = BlockDiscoverer
            .discoverBlocksWithSameStateAround(world, origin, true, searchDepth, amt, false);
        if (found.isEmpty()) return false;

        for (BlockPos placePos : found.getPattern()
            .keySet()) {
            if (drainTempCharge(playerIn, Config.exchangeWandUseCost, true)
                && (playerIn.capabilities.isCreativeMode || ItemUtils
                    .consumeFromPlayerInventory(playerIn, stack, ItemUtils.copyStackWithSize(consumeStack, 1), true))) {
                if (((EntityPlayerMP) playerIn).theItemInWorldManager
                    .tryHarvestBlock(placePos.getX(), placePos.getY(), placePos.getZ())) {
                    drainTempCharge(playerIn, Config.exchangeWandUseCost, false);
                    gainPermCharge(playerIn, Config.exchangeWandUseCost / 4);
                    if (!playerIn.capabilities.isCreativeMode) {
                        ItemUtils.consumeFromPlayerInventory(
                            playerIn,
                            stack,
                            ItemUtils.copyStackWithSize(consumeStack, 1),
                            false);
                    }
                    world.setBlock(placePos.getX(), placePos.getY(), placePos.getZ(), stored);
                    PktParticleEvent ev = new PktParticleEvent(
                        PktParticleEvent.ParticleEventType.ARCHITECT_PLACE,
                        placePos);
                    ev.setAdditionalData(Block.getIdFromBlock(atOrigin));
                    PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(world, placePos, 40));
                }
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("astralsorcery:exchangeWand");
    }
}
