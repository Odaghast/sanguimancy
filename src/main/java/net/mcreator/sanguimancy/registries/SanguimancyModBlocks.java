package net.mcreator.sanguimancy.registries;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.mcreator.sanguimancy.SanguimancyMod;
import net.mcreator.sanguimancy.core.blocks.BloodRoseBlock;
import net.mcreator.sanguimancy.core.blocks.OrganismBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class SanguimancyModBlocks {

    public static Block CARMINITE_ORE, DEEPSLATE_CARMINITE_ORE, RAW_CARMINITE_BLOCK, CARMINITE_BLOCK, BLOOD_ORGANISM_HEART;
    public static Block BLOOD_ROSE;
    public static Block POTTED_BLOOD_ROSE;
    public static Block BLOOD_ORGANISM;

    public static void load() {

        BLOOD_ROSE = registerBlock("blood_rose", new BloodRoseBlock(flowerBlockBehavior()), true);
        POTTED_BLOOD_ROSE = registerBlock("potted_blood_rose", flowerPot(BLOOD_ROSE, new FeatureFlag[0]), false);

        BLOOD_ORGANISM_HEART = registerBlock("blood_organism_heart", new Block(
                BlockBehaviour.Properties.of().mapColor(MapColor.CRIMSON_HYPHAE)
                        .strength(3.0f,3.0f)
                        .instrument(NoteBlockInstrument.XYLOPHONE)
                        .sound(SoundType.SLIME_BLOCK)
        ), false);
        BLOOD_ORGANISM = registerBlock("blood_organism", new OrganismBlock(
                BlockBehaviour.Properties.copy(BLOOD_ORGANISM_HEART),
                SanguimancyModBlockTags.BLOOD_ORGANISM_FOOD,
                BLOOD_ORGANISM_HEART
        ), true);

        CARMINITE_ORE = registerBlock("carminite_ore", new DropExperienceBlock(
                BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .requiresCorrectToolForDrops()
                        .strength(3.0f, 3.0f),
                UniformInt.of(3, 7)
        ), true);

        DEEPSLATE_CARMINITE_ORE = registerBlock("deepslate_carminite_ore", new DropExperienceBlock(
                BlockBehaviour.Properties.copy(CARMINITE_ORE).mapColor(MapColor.DEEPSLATE)
                        .sound(SoundType.DEEPSLATE)
                        .strength(4.5f, 3.0f),
                UniformInt.of(3, 7)
        ), true);

        RAW_CARMINITE_BLOCK = registerBlock("raw_carminite_block", new Block(
                BlockBehaviour.Properties.of().mapColor(MapColor.FIRE)
                        .strength(2.0f, 1.0f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.METAL)
        ), true);

        CARMINITE_BLOCK = registerBlock("carminite_block", new Block(
                BlockBehaviour.Properties.copy(RAW_CARMINITE_BLOCK)
        ), true);

    }

    public static void loadRenderLayer() {
        BlockRenderLayerMap.INSTANCE.putBlock(BLOOD_ROSE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(POTTED_BLOOD_ROSE, RenderType.cutout());
    }

    private static BlockBehaviour.Properties flowerBlockBehavior() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY);
    }

    private static FlowerPotBlock flowerPot(Block block, FeatureFlag... featureFlags) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
        if (featureFlags.length > 0) {
            properties = properties.requiredFeatures(featureFlags);
        }
        return new FlowerPotBlock(block, properties);
    }

    private static Block registerBlock(String name, Block block, Boolean item) {
        Block _return = Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(SanguimancyMod.MODID, name), block);

        if (item) {
            SanguimancyModItems.registerBlockItem(name, _return);
        }

        return _return;
    }

    private static Boolean always(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> entityType) {
        return true;
    }
}
