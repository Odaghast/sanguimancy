package net.mcreator.sanguimancy.registries;

import net.mcreator.sanguimancy.SanguimancyMod;
import net.mcreator.sanguimancy.core.blocks.TestineTubeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class SanguimancyModMachineBlocks {

    public static Block CARMINITE_SHELL;
    public static Block MUNCHER, DIGESTER;
    public static Block TESTINE_TUBE;

    public static void load() {

        CARMINITE_SHELL = registerBlock("carminite_shell", new Block(
                BlockBehaviour.Properties.of().mapColor(MapColor.FIRE)
                        .strength(2.0f, 1.0f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.METAL)
        ), true);

        MUNCHER = registerBlock("muncher", new Block(
                BlockBehaviour.Properties.copy(CARMINITE_SHELL)
        ), true);
        DIGESTER = registerBlock("digester", new Block(
                BlockBehaviour.Properties.copy(CARMINITE_SHELL)
        ), true);

        TESTINE_TUBE = registerBlock("testine_tube", new TestineTubeBlock(
                BlockBehaviour.Properties.copy(CARMINITE_SHELL)
        ), true);
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
