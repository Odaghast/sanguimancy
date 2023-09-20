package net.mcreator.sanguimancy.registries;

import net.mcreator.sanguimancy.SanguimancyMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SanguimancyModBlockTags {
    public static final TagKey<Block> BLOOD_ORGANISM_FOOD = makeKey("blood_organism_food");
    public static final TagKey<Block> CURETTE_BREAKABLE = makeKey("curette_breakable");
    private static TagKey<Block> makeKey(String id) {
        return TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation(SanguimancyMod.MODID, id));
    }
}
