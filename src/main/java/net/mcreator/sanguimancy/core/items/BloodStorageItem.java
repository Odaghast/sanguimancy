package net.mcreator.sanguimancy.core.items;

import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodStorageItem extends Item {

    private final BloodStorageHelper.Builder BLOOD_STORAGE;
    public BloodStorageItem(Properties properties, float maxBlood) {
        this(properties, maxBlood, true, true);
    }
    public BloodStorageItem(Properties properties, float maxBlood, boolean hasExport, boolean hasImport) {
        this(properties, new BloodStorageHelper.Builder(maxBlood));
        if (!hasExport) BLOOD_STORAGE.noExport();
        if (!hasImport) BLOOD_STORAGE.noImport();
    }
    public BloodStorageItem(Properties properties, BloodStorageHelper.Builder builder) {
        super(properties.stacksTo(1));
        BLOOD_STORAGE = builder;
    }

    //Allows outside access to BLOOD_STORAGE without allowing updating its information
    public BloodStorageHelper.Builder getBloodStorage() {
        return BLOOD_STORAGE.copy();
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level level, Player player) {
        BloodStorageHelper.setBloodStorage(itemStack, getBloodStorage());
        super.onCraftedBy(itemStack, level, player);
    }
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean holding) {
        if (!BloodStorageHelper.hasBloodStorage(itemStack)) {
            BloodStorageHelper.setBloodStorage(itemStack, getBloodStorage());
        }
        super.inventoryTick(itemStack, level, entity, i, holding);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = new ItemStack(this);
        BloodStorageHelper.setBloodStorage(itemStack, getBloodStorage());
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        float _fill = BloodStorageHelper.hasBloodStorage(itemStack) ? BloodStorageHelper.getBlood(BloodStorageHelper.getBloodStorage(itemStack)) : 0.0f;
        list.add(Component.translatable("tooltip.sanguimancy.blood_storage", String.format("%.1f", _fill)));
        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }
}
