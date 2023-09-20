package net.mcreator.sanguimancy.registries;

import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;
import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.minecraft.resources.ResourceLocation;

public class SanguimancyModPredicates {

    public static void load() {
        ModelPredicateProviderRegistryAccessor.callRegister(new ResourceLocation("blood_fill"), (itemStack, clientLevel, livingEntity, i) -> {
            if (!BloodStorageHelper.hasBloodStorage(itemStack)) {
                return 0.0F;
            }

            float _val = BloodStorageHelper.getFill(BloodStorageHelper.getBloodStorage(itemStack));

            return _val > 0.0f ? Math.max(_val, 0.1f) : 0.0f;
        });
    }
}
