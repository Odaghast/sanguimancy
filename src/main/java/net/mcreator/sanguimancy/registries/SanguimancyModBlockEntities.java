package net.mcreator.sanguimancy.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.mcreator.sanguimancy.SanguimancyMod;
import net.mcreator.sanguimancy.core.blocks.entities.TestineTubeBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SanguimancyModBlockEntities {
    public static BlockEntityType<TestineTubeBlockEntity> TESTINE_TUBE;

    public static void load() {
        TESTINE_TUBE = (BlockEntityType<TestineTubeBlockEntity>) registerBlockEntity("testine_tube_entity", TestineTubeBlockEntity::new, SanguimancyModMachineBlocks.TESTINE_TUBE);
    }

    private static BlockEntityType<? extends BlockEntity> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<? extends BlockEntity> factory, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(SanguimancyMod.MODID, name),
                FabricBlockEntityTypeBuilder.create(factory, block).build());
    }
}
