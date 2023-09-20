package net.mcreator.sanguimancy.core.items;

import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreativeBloodJarItem extends BloodJarItem {
    public CreativeBloodJarItem(Properties properties) {
        super(properties, new BloodStorageHelper.Builder(-1).setStored(-1));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.sanguimancy.blood_storage_creative"));
        //super.appendHoverText(itemStack, level, list, tooltipFlag);
    }
}
