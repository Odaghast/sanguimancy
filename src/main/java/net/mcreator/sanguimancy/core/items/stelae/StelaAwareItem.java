package net.mcreator.sanguimancy.core.items.stelae;

import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class StelaAwareItem extends StelaPotionApplierItem{
    public StelaAwareItem(Properties properties, float bloodCost, int chargeMultiplier, int cooldown, MobEffect[] mobEffects, int duration, int strength) {
        super(properties, bloodCost, chargeMultiplier, cooldown, mobEffects, duration, strength);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!player.isCrouching()) {
            return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (useOnContext.getPlayer() == null || useOnContext.getPlayer().isCrouching()) {return super.useOn(useOnContext);}
        if (canPlaceOn(useOnContext) && tryBloodCost(useOnContext.getPlayer(), useOnContext.getItemInHand())) {
            useOnContext.getLevel().setBlockAndUpdate(useOnContext.getClickedPos().relative(useOnContext.getClickedFace()), Blocks.LIGHT.defaultBlockState());
            playerStatAndCooldown(useOnContext.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return super.useOn(useOnContext);
    }

    @Override
    public boolean tryBloodCost(Player player, ItemStack itemStack) {
        if (player.isCrouching()) {
            //adjusted cost for potion secondary
            if (isCreative(player, itemStack)) return true;
            if (BloodStorageHelper.hasBloodStorage(itemStack)) {
                CompoundTag bloodStorage = BloodStorageHelper.getBloodStorage(itemStack);
                if (BloodStorageHelper.getFill(bloodStorage) >= 4.0 / CHARGE_MULTIPLIER) {
                    BloodStorageHelper.payBill(bloodStorage, BLOOD_COST * 4.0f);
                    return true;
                }
            }
        }
        return super.tryBloodCost(player, itemStack);
    }

    @Override
    public void playerStatAndCooldown(@Nullable Player player) {
        if (player != null && player.isCrouching()) {
            //adjusted cooldown for potion secondary
            player.getCooldowns().addCooldown(this, player.isCreative() ? 1 : 20 * 60);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        super.playerStatAndCooldown(player);
    }

    private boolean canPlaceOn(UseOnContext useOnContext) {
        return useOnContext.getLevel().getBlockState(useOnContext.getClickedPos().relative(useOnContext.getClickedFace())).isAir();
    }
}
