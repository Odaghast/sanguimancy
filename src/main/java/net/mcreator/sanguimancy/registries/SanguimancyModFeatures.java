package net.mcreator.sanguimancy.registries;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.mcreator.sanguimancy.SanguimancyMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SanguimancyModFeatures {

    public static final ResourceKey<PlacedFeature> ORE_CARMINITE_PLACED_KEY = placedFeatureKey("ore_carminite");
    public static final ResourceKey<PlacedFeature> ORE_CARMINITE_LARGE_PLACED_KEY = placedFeatureKey("ore_carminite_large");

    public static void load() {
        addUndergroundOreFeature(ORE_CARMINITE_PLACED_KEY);
        addUndergroundOreFeature(ORE_CARMINITE_LARGE_PLACED_KEY);
    }

    private static void addUndergroundOreFeature(ResourceKey<PlacedFeature> placeKey) {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, placeKey);
    }

    private static ResourceKey<PlacedFeature> placedFeatureKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(SanguimancyMod.MODID, name));
    }

}
