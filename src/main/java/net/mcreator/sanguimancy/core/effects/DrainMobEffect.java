package net.mcreator.sanguimancy.core.effects;

import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DrainMobEffect extends MobEffect {
    public DrainMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        if (livingEntity.invulnerableTime == 0) {
            livingEntity.hurt(new DamageSource(livingEntity.damageSources().starve().typeHolder()), 1.0f);
            if (livingEntity instanceof Player player) {
                ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (!itemStack.isEmpty() && BloodStorageHelper.hasBloodStorage(itemStack)) {
                    BloodStorageHelper.insertBlood(BloodStorageHelper.getBloodStorage(itemStack), 0.1f);
                } else {
                    itemStack = player.getItemInHand(InteractionHand.OFF_HAND);
                    if (!itemStack.isEmpty() && BloodStorageHelper.hasBloodStorage(itemStack)) {
                        BloodStorageHelper.insertBlood(BloodStorageHelper.getBloodStorage(itemStack), 0.1f);
                    }
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return true;
    }
}
