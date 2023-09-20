package net.mcreator.sanguimancy.registries;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mcreator.sanguimancy.SanguimancyMod;
import net.mcreator.sanguimancy.core.helpers.BloodStorageHelper;
import net.mcreator.sanguimancy.core.items.*;
import net.mcreator.sanguimancy.core.items.stelae.*;
import net.mcreator.sanguimancy.core.tools.DaggerItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;

public class SanguimancyModItems {

    public static Item RAW_CARMINITE, CARMINITE, PURE_GOLD, BLOOD_ROSE_PETAL, TOOTH, BLOOD_THREAD, BLOOD_SEWN_LEATHER,
            BLOOD_AMBER, BLOOD_HEART, INTESTINE, OUTESTINE, STELA_STONE, MAGNET, BLOOD_NUTRIENT, CURETTE, THE_HEART;

    public static StelaItem STELA_CORE, STELA_DEEP, STELA_EXCAVATOR, STELA_ELEMENTS, STELA_GROVE, STELA_WANDERER, STELA_UNAWARE, STELA_AWARE, STELA_SKY;

    public static BloodStorageItem BLOOD_JAR, CREATIVE_BLOOD_JAR;
    public static DaggerItem DAGGER, ATHAME;

    public static PickaxeItem CARMINITE_PICKAXE;
    public static AxeItem CARMINITE_AXE;
    public static ShovelItem CARMINITE_SHOVEL;
    public static HoeItem CARMINITE_HOE;
    public static SwordItem CARMINITE_SWORD;

    public static ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            new ResourceLocation(SanguimancyMod.MODID, "sanguimancy"));

    public static CreativeModeTab TAB;

    public static void load() {

        TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(SanguimancyMod.MODID, "sanguimancy"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(RAW_CARMINITE))
                        .title(Component.translatable("itemGroup.sanguimancy.creative_tab"))
                        .noScrollBar()
                        .build());

        DAGGER = (DaggerItem) registerItem("dagger", new DaggerItem(SanguimancyModToolTiers.SACRIFICE, 3, 0.0f, defaultProperties(), 0));
        ATHAME = (DaggerItem) registerItem("athame", new DaggerItem(SanguimancyModToolTiers.SACRIFICE, 3, 0.0f, defaultProperties(), 1));

        PURE_GOLD = registerItem("pure_gold");
        TOOTH = registerItem("tooth");

        STELA_STONE = registerItem("stela_stone");
        STELA_CORE = (StelaItem) registerItem("stela_core", new StelaFluidPlacerItem(defaultProperties(), 1f, 4, 20, Fluids.LAVA));
        STELA_DEEP = (StelaItem) registerItem("stela_deep", new StelaFluidPlacerItem(defaultProperties(), 0.1f, 16, 5, Fluids.WATER));
        STELA_GROVE = (StelaItem) registerItem("stela_grove", new StelaGroveItem(defaultProperties(), 0.1f, 16, 1));
        STELA_SKY = (StelaItem) registerItem("stela_sky", new StelaSkyItem(defaultProperties(), 0.1f, 32, 10));
        STELA_EXCAVATOR = (StelaItem) registerItem("stela_excavator", new StelaPotionApplierItem(defaultProperties(),
                1.0f, 1, 20 * 60, new MobEffect[]{MobEffects.DIG_SPEED}, 20 * 60, 1));
        STELA_ELEMENTS = (StelaItem) registerItem("stela_elements", new StelaPotionApplierItem(defaultProperties(),
                5.0f, 1, 20 * 40, new MobEffect[]{MobEffects.DAMAGE_RESISTANCE, MobEffects.FIRE_RESISTANCE, MobEffects.SLOW_FALLING, MobEffects.WATER_BREATHING}, 20 * 30, 0));
        STELA_WANDERER = (StelaItem) registerItem("stela_wanderer", new StelaPotionApplierItem(defaultProperties(),
                1.0f, 1, 20 * 30, new MobEffect[]{MobEffects.MOVEMENT_SPEED, MobEffects.JUMP, MobEffects.DOLPHINS_GRACE}, 20 * 30, 1));
        STELA_UNAWARE = (StelaItem) registerItem("stela_unaware", new StelaPotionApplierItem(defaultProperties(),
                1.0f, 1, 20 * 30, new MobEffect[]{MobEffects.INVISIBILITY}, 20 * 30, 0));
        STELA_AWARE = (StelaItem) registerItem("stela_aware", new StelaAwareItem(defaultProperties(),
                0.1f, 32, 20, new MobEffect[]{MobEffects.GLOWING, MobEffects.NIGHT_VISION}, 20 * 60, 0));


        BLOOD_ROSE_PETAL = registerItem("blood_rose_petal");
        BLOOD_THREAD = registerItem("blood_thread");
        BLOOD_SEWN_LEATHER = registerItem("blood_sewn_leather");
        BLOOD_NUTRIENT = registerItem("blood_nutrient", new NutrientItem(defaultProperties().food(new FoodProperties.Builder().nutrition(14).saturationMod(16f).meat().build())));
        BLOOD_AMBER = registerItem("blood_amber");
        BLOOD_HEART = registerItem("blood_heart");
        THE_HEART = registerItem("the_heart", new Item(defaultProperties().rarity(Rarity.EPIC)));
        INTESTINE = registerItem("intestine");
        OUTESTINE = registerItem("outestine");

        CURETTE = registerItem("curette", new CuretteItem(defaultProperties().stacksTo(1)));

        RAW_CARMINITE = registerItem("raw_carminite");
        CARMINITE = registerItem("carminite", new EnshellingItem(defaultProperties(),
                ()->SanguimancyModBlocks.BLOOD_ORGANISM, ()->SanguimancyModMachineBlocks.CARMINITE_SHELL));

        MAGNET = registerItem("magnet");

        BLOOD_JAR = (BloodStorageItem) registerItemNoEntry("blood_jar", new BloodJarItem(defaultProperties(), 25.6f));
        addGroupEntry(BLOOD_JAR.getDefaultInstance());
        addGroupEntry(BloodStorageHelper.setBloodStorage(BLOOD_JAR.getDefaultInstance(), BLOOD_JAR.getBloodStorage().startFull()));

        CREATIVE_BLOOD_JAR = (BloodStorageItem) registerItemNoEntry("creative_blood_jar", new CreativeBloodJarItem(defaultProperties()));
        addGroupEntry(CREATIVE_BLOOD_JAR.getDefaultInstance());

        CARMINITE_AXE = (AxeItem) registerItem("carminite_axe", new AxeItem(SanguimancyModToolTiers.CARMINITE, 5.0f, -3.0f, defaultProperties()));
        CARMINITE_PICKAXE = (PickaxeItem) registerItem("carminite_pickaxe", new PickaxeItem(SanguimancyModToolTiers.CARMINITE, 1, -2.8f, defaultProperties()));
        CARMINITE_SHOVEL = (ShovelItem) registerItem("carminite_shovel", new ShovelItem(SanguimancyModToolTiers.CARMINITE, 1.5f, -3.0f, defaultProperties()));
        CARMINITE_SWORD = (SwordItem) registerItem("carminite_sword", new SwordItem(SanguimancyModToolTiers.CARMINITE, 3, -2.4f, defaultProperties()));
        CARMINITE_HOE = (HoeItem) registerItem("carminite_hoe", new HoeItem(SanguimancyModToolTiers.CARMINITE, -3, 0.0f, defaultProperties()));
    }

    private static Item registerItem(String name, Item item) {
        Item _return = registerItemNoEntry(name,item);

        addGroupEntry(_return);

        return _return;
    }

    private static Item registerItem(String name) {
        return registerItem(name, new Item(defaultProperties()));
    }

    private static Item registerItemNoEntry(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(SanguimancyMod.MODID, name), item);
    }

    public static BlockItem registerBlockItem(String name, Block block) {
        return (BlockItem) registerItem(name, new BlockItem(block, defaultProperties()));
    }

    private static void addGroupEntry(ItemStack itemStack) {
        ItemGroupEvents.modifyEntriesEvent(TAB_KEY).register(content -> {
            content.accept(itemStack);
        });
    }

    private static void addGroupEntry(Item item) {
        ItemGroupEvents.modifyEntriesEvent(TAB_KEY).register(content -> {
            content.accept(item);
        });
    }

    private static Item.Properties defaultProperties() {
        return new Item.Properties().rarity(Rarity.COMMON);
    }
}
