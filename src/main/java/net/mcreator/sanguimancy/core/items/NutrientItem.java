package net.mcreator.sanguimancy.core.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class NutrientItem extends Item {
    public NutrientItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return itemStack.getItem().isEdible() ? UseAnim.DRINK : UseAnim.NONE;
    }
}
