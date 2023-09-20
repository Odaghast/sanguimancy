package net.mcreator.sanguimancy.core.items.stelae;

import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.mcreator.sanguimancy.core.items.BloodStorageItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StelaItem extends BloodStorageItem {

    public final float BLOOD_COST;
    public final float CHARGE_MULTIPLIER;
    public final int COOLDOWN;
    public StelaItem(Properties properties, float bloodCost, int chargeMultiplier, int cooldown) {
        super(properties, bloodCost*chargeMultiplier, false, true);
        BLOOD_COST = bloodCost;
        CHARGE_MULTIPLIER = (float) Math.max(1, chargeMultiplier);
        COOLDOWN = cooldown;
    }

    public boolean tryBloodCost(Player player, ItemStack itemStack) {
        if (isCreative(player, itemStack)) return true;
        if (BloodStorageHelper.hasBloodStorage(itemStack)) {
            CompoundTag bloodStorage = BloodStorageHelper.getBloodStorage(itemStack);
            if (BloodStorageHelper.getFill(bloodStorage) >= 1.0 / CHARGE_MULTIPLIER) {
                BloodStorageHelper.payBill(bloodStorage, BLOOD_COST);
                return true;
            }
        }
        return false;
    }

    public boolean isCreative(Player player, ItemStack itemStack) {
        return (player != null && player.isCreative()) || (BloodStorageHelper.hasBloodStorage(itemStack) && BloodStorageHelper.getBlood(BloodStorageHelper.getBloodStorage(itemStack)) == -1);
    }

    public void playerStatAndCooldown(@Nullable Player player) {
        if (player != null) {
            player.getCooldowns().addCooldown(this, player.isCreative() ? 1 : COOLDOWN);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        //this is just here as a reminder
        return super.use(level, player, interactionHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        //this is just here as a reminder
        return super.useOn(useOnContext);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        float _fill = BloodStorageHelper.hasBloodStorage(itemStack) ? BloodStorageHelper.getFill(BloodStorageHelper.getBloodStorage(itemStack)) : 0.0f;
        list.add(Component.translatable("tooltip.sanguimancy.stela_charges", Math.floor(_fill * CHARGE_MULTIPLIER), Math.floor(CHARGE_MULTIPLIER)));
    }
}
