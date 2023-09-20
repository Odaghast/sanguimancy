package net.mcreator.sanguimancy.core.items;

import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BloodJarItem extends BloodStorageItem{
    public BloodJarItem(Properties properties, float maxBlood) {
        super(properties, maxBlood);
    }

    public BloodJarItem(Properties properties, float maxBlood, boolean hasExport, boolean hasImport) {
        super(properties, maxBlood, hasExport, hasImport);
    }

    public BloodJarItem(Properties properties, BloodStorageHelper.Builder builder) {
        super(properties, builder);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        //check other hand for fillable item
        if (interactionHand == InteractionHand.MAIN_HAND) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            ItemStack itemStack2 = player.getItemInHand(InteractionHand.OFF_HAND);
            if (BloodStorageHelper.hasBloodStorage(itemStack) && BloodStorageHelper.hasBloodStorage(itemStack2)) {
                CompoundTag bloodStorage = BloodStorageHelper.getBloodStorage(itemStack);
                CompoundTag bloodStorage2 = BloodStorageHelper.getBloodStorage(itemStack2);
                if (BloodStorageHelper.getBlood(bloodStorage) != 0f && BloodStorageHelper.hasExport(bloodStorage) && BloodStorageHelper.hasImport(bloodStorage2)) {
                    BloodStorageHelper.insertBlood(bloodStorage2, BloodStorageHelper.extractBlood(bloodStorage, BloodStorageHelper.getEmptySpace(bloodStorage2)));
                    level.playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS);
                    return InteractionResultHolder.success(itemStack);
                }
            }
        }
        return super.use(level, player, interactionHand);
    }
}
