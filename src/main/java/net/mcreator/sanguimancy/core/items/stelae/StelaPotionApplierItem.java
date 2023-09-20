package net.mcreator.sanguimancy.core.items.stelae;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StelaPotionApplierItem extends StelaItem{

    private final MobEffect[] MOB_EFFECTS;
    private final int DURATION;
    private final int STRENGTH;
    public StelaPotionApplierItem(Properties properties, float bloodCost, int chargeMultiplier, int cooldown, MobEffect[] mobEffects, int duration, int strength) {
        super(properties, bloodCost, chargeMultiplier, cooldown);
        MOB_EFFECTS = mobEffects;
        DURATION = duration;
        STRENGTH = strength;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (tryBloodCost(player, player.getItemInHand(interactionHand))) {
            applyEffects(level, player);
            playerStatAndCooldown(player);
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }
        return super.use(level, player, interactionHand);
    }

    public void applyEffects(Level level, Player player) {
        if (player != null) {
            for (MobEffect effect : MOB_EFFECTS) {
                player.addEffect(new MobEffectInstance(effect, DURATION, STRENGTH));
            }
            level.playSound(null, player.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS);
        }
    }
}
