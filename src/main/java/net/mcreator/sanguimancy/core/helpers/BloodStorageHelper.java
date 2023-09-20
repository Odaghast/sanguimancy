package net.mcreator.sanguimancy.core.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class BloodStorageHelper {

    public static final String BLOOD_STORAGE_TAG = "sgmc_blood_storage";
    public static final String BLOOD_TAG = "blood";
    public static final String BLOOD_MAX_TAG = "blood_max";
    public static final String HAS_IMPORT_TAG = "has_import";
    public static final String HAS_EXPORT_TAG = "has_export";

    public static ItemStack setBloodStorage(ItemStack itemStack, float maxBlood) {
        return setBloodStorage(itemStack, new Builder(maxBlood));
    }

    public static ItemStack setBloodStorage(ItemStack itemStack, BloodStorageHelper.Builder bloodStorage) {
        itemStack.getOrCreateTag().put(BLOOD_STORAGE_TAG, bloodStorage.build());
        return itemStack;
    }

    public static class Builder {
        private final CompoundTag storage;
        public Builder(float maxBlood) {
            storage = new CompoundTag();
            storage.putFloat(BLOOD_TAG, 0.0f);
            storage.putFloat(BLOOD_MAX_TAG, maxBlood);
            storage.putBoolean(HAS_IMPORT_TAG, true);
            storage.putBoolean(HAS_EXPORT_TAG, true);
        }

        public Builder noExport() {
            storage.putBoolean(HAS_EXPORT_TAG, false);
            return this;
        }

        public Builder noImport() {
            storage.putBoolean(HAS_IMPORT_TAG, false);
            return this;
        }

        public Builder startFull() {
            storage.putFloat(BLOOD_TAG, storage.getFloat(BLOOD_MAX_TAG));
            return this;
        }

        public Builder setStored(float stored) {
            storage.putFloat(BLOOD_TAG, stored);
            return this;
        }
        //copy() is needed in case events for duplication of a builder, usually in the case of simplifying BloodStorageItem's
        public Builder copy() {
            Builder _builder = new Builder(storage.getFloat(BLOOD_MAX_TAG)).setStored(storage.getFloat(BLOOD_TAG));
            if (!storage.getBoolean(HAS_IMPORT_TAG)) _builder.noImport();
            if (!storage.getBoolean(HAS_EXPORT_TAG)) _builder.noExport();
            return _builder;
        }

        public CompoundTag build() {
            return storage;
        }
    }

    public static boolean hasBloodStorage(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.getTag() != null && itemStack.getTag().contains(BLOOD_STORAGE_TAG);
    }

    public static CompoundTag getBloodStorage(ItemStack itemStack) {
        return itemStack.getTag().getCompound(BLOOD_STORAGE_TAG);
    }

    public static boolean hasImport(CompoundTag bloodStorage) {
        return bloodStorage.getBoolean(HAS_IMPORT_TAG);
    }

    public static boolean hasExport(CompoundTag bloodStorage) {
        return bloodStorage.getBoolean(HAS_EXPORT_TAG);
    }

    public static float getBlood(CompoundTag bloodStorage) {
        return bloodStorage.getFloat(BLOOD_TAG);
    }

    public static float getMaxBlood(CompoundTag bloodStorage) {
        return bloodStorage.getFloat(BLOOD_MAX_TAG);
    }

    public static float getEmptySpace(CompoundTag bloodStorage) {
        return getMaxBlood(bloodStorage) - getBlood(bloodStorage);
    }

    public static float getFill(CompoundTag bloodStorage) {
        return getBlood(bloodStorage) / getMaxBlood(bloodStorage);
    }

    public static boolean payBill(CompoundTag bloodStorage, float amount) {
        if (getBlood(bloodStorage) >= amount) {
            bloodStorage.putFloat(BLOOD_TAG, getBlood(bloodStorage) - amount);
            return true;
        }
        return false;
    }

    public static float insertBlood(CompoundTag bloodStorage, float amount) {
        if (!hasImport(bloodStorage)) {return amount;}

        float _fill = getBlood(bloodStorage);
        float _added = Math.min(amount, getMaxBlood(bloodStorage) - _fill);

        bloodStorage.putFloat(BLOOD_TAG, _fill + _added);

        //amount unused
        return amount - _added;
    }

    public static float extractBlood(CompoundTag bloodStorage, float amount) {
        if (!hasExport(bloodStorage)) {return 0.0f;}

        float _fill = getBlood(bloodStorage);
        float _removed = _fill == -1 ? amount : Math.min(amount, _fill);

        bloodStorage.putFloat(BLOOD_TAG, Math.max(-1, Math.min(_fill, _fill-_removed)));

        return _removed;
    }
}
