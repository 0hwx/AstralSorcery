/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.data.world.WorldCacheManager;
import hellfirepvp.astralsorcery.common.data.world.data.RockCrystalBuffer;
import hellfirepvp.astralsorcery.common.item.crystal.base.ItemRockCrystalBase;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockCrystalOre
 * Created by HellFirePvP
 * Date: 07.05.2016 / 18:03
 */
public class BlockCustomOre extends Block implements BlockCustomName, BlockVariants {

    private static final Random rand = new Random();
    public IIcon[] icons = new IIcon[OreType.values().length];

//    public static PropertyEnum<OreType> ORE_TYPE = PropertyEnum.create("oretype", OreType.class);

    public BlockCustomOre() {
        super(Material.rock);
        setBlockName("BlockCustomOre");
        setHardness(3.0F);
        setResistance(25.0F);
//        setHarvestLevel("pickaxe", 3);
        setStepSound(soundTypeStone);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        for (OreType type : OreType.values()) {
            this.setHarvestLevel("pickaxe", type.getHarvestLevel(), type.getMeta());
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
       for (OreType t : OreType.values()) {
           list.add(new ItemStack(item, 1, t.getMeta()));
       }
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);

        if(blockBroken.equals(OreType.STARMETAL)) {
            BlockPos pos = new BlockPos(x, y, z);
            ((RockCrystalBuffer) WorldCacheManager.getOrLoadData(worldIn, WorldCacheManager.SaveKey.ROCK_CRYSTAL)).removeOre(pos);
        }
    }

//    @Override
//    public int getMeta(Block state) {
//        OreType type = state.getValue(ORE_TYPE);
//        return type == null ? 0 : type.getMeta();
//    }
//
//    @Override
//    public Block getStateFromMeta(int meta) {
//        return meta < OreType.values().length ? getDefaultState().withProperty(ORE_TYPE, OreType.values()[meta]) : getDefaultState();
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, ORE_TYPE);
//    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        OreType type = OreType.values()[meta];
        BlockPos pos = new BlockPos(x, y, z);
        if(type != OreType.ROCK_CRYSTAL || (securityCheck(worldIn, pos, player) && checkSafety(worldIn, pos))) {
            super.harvestBlock(worldIn, player, x, y, z, meta);
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
        OreType type = OreType.values()[metadata];
        BlockPos pos = new BlockPos(x, y, z);
        List<ItemStack> drops = new ArrayList<>();
        switch (type) {
            case ROCK_CRYSTAL:
                if(world != null && world instanceof World && checkSafety((World) world, pos) && securityCheck((World) world, pos, harvesters.get())) {
                    drops.add(ItemRockCrystalBase.createRandomBaseCrystal());
                    for (int i = 0; i < (fortune + 1); i++) {
                        if(((World) world).rand.nextBoolean()) {
                            drops.add(ItemRockCrystalBase.createRandomBaseCrystal());
                        }
                    }
                    if(((World) world).rand.nextBoolean()) {
                        drops.add(ItemRockCrystalBase.createRandomBaseCrystal());
                    }
                }
                break;
            case STARMETAL:
                drops.add(new ItemStack(this, 1, OreType.STARMETAL.ordinal()));
                break;
        }
        return (ArrayList<ItemStack>) drops;
    }

    private boolean securityCheck(World world, BlockPos pos, EntityPlayer player) {
        return !world.isRemote && player != null && !MiscUtils.isPlayerFakeMP((EntityPlayerMP) player);
    }

    private boolean checkSafety(World world, BlockPos pos) {
        EntityPlayer player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10);
        return player != null && player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 100;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean isNormalCube() {
        return true;
    }

    @Override
    public boolean func_149730_j() {
        return true;
    }

//    @Override
//    public boolean isFullyOpaque() {
//        return true;
//    }

    @Override
    public String getIdentifierForMeta(int meta) {
        OreType ot = OreType.values()[meta];
        return ot.getName();
    }

    @Override
    public List<Block> getValidStates() {
        List<Block> ret = new LinkedList<>();
        for (OreType type : OreType.values()) {
            ret.add(type.asBlock());
        }
        return ret;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public String getMetaName(int meta) {
        return OreType.values()[meta].getName();
    }

    @Override
    public int getMeta() {
        for (OreType type : OreType.values()) {
            return type.getMeta();
        }
        return 0;
    }


    @SideOnly(Side.CLIENT)
    public static void playStarmetalOreEffects(PktParticleEvent event) {
        EntityFXFacingParticle p = EffectHelper.genericFlareParticle(
                event.getVec().getX() + rand.nextFloat(),
                event.getVec().getY() + rand.nextFloat(),
                event.getVec().getZ() + rand.nextFloat());
        p.motion(0, rand.nextFloat() * 0.05, 0);
        p.scale(0.2F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg)
    {
        for (OreType type : OreType.values()) {
            icons[type.getMeta()] = reg.registerIcon("astralsorcery:ore_" + type.getName());
//            icons[0] = reg.registerIcon("astralsorcery:ore_rockcrystal");
//            icons[1] = reg.registerIcon("astralsorcery:ore_starmetal");
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta >= 0 && meta < icons.length) {
            return icons[meta];
        }
        return null;
    }



    public enum OreType {

        ROCK_CRYSTAL(0,"rock_crystal",2),
        STARMETAL(1,"starmetal",3);

        private final String name;
        private final int harvestLevel;
        private final int meta;

        private OreType(int meta ,String name, int harvestLevel) {
            this.meta = meta;
            this.name = name;
            this.harvestLevel = harvestLevel;
        }

        public Block asBlock() {
            return BlocksAS.customOre;//.getStateFromMeta(meta);
        }
        public ItemStack asStack() {
            return new ItemStack(BlocksAS.customOre, 1, meta);
        }

        public String getName() {
            return name.toLowerCase();
        }

        public int getHarvestLevel() {
            return harvestLevel;
        }

        public int getMeta() {
            return meta;
        }
    }


}
