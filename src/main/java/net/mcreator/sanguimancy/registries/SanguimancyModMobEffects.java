package net.mcreator.sanguimancy.registries;

import net.mcreator.sanguimancy.SanguimancyMod;
import net.mcreator.sanguimancy.core.effects.DrainMobEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SanguimancyModMobEffects {

    public static final MobEffect DRAIN = new DrainMobEffect(MobEffectCategory.HARMFUL, 11101546);

    public static void load() {
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(SanguimancyMod.MODID, "drain"), DRAIN);
    }
}
