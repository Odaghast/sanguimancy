package net.mcreator.sanguimancy.core.blocks;

import net.mcreator.sanguimancy.registries.SanguimancyModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BloodRoseBlock extends FlowerBlock {
    public BloodRoseBlock(Properties properties) {
        super(SanguimancyModMobEffects.DRAIN, 8, properties);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        LivingEntity livingEntity;
        if (level.isClientSide || level.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (entity instanceof LivingEntity && !(livingEntity = (LivingEntity)entity).isInvulnerableTo(level.damageSources().starve())) {
            livingEntity.addEffect(new MobEffectInstance(SanguimancyModMobEffects.DRAIN, 10));
        }
    }
}
