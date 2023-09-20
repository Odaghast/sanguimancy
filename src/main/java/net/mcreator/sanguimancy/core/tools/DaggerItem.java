package net.mcreator.sanguimancy.core.tools;

import net.mcreator.sanguimancy.core.helpers.ItemTransferHelper;
import net.mcreator.sanguimancy.registries.SanguimancyModBlocks;
import net.mcreator.sanguimancy.registries.SanguimancyModItems;
import net.mcreator.sanguimancy.registries.SanguimancyModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class DaggerItem extends SwordItem {

    public final int DRAIN_LEVEL;
    public DaggerItem(Tier tier, int i, float f, Properties properties, int drainLevel) {

        super(tier, i, f, properties);
        this.DRAIN_LEVEL = drainLevel;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        drain(player);
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS);
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (useOnContext.getPlayer() != null) {
            drain(useOnContext.getPlayer());
            Level level = useOnContext.getLevel();
            BlockPos pos = useOnContext.getClickedPos();
            if (level.getBlockState(pos) == Blocks.POPPY.defaultBlockState()) {
                level.destroyBlock(pos, false);
                level.setBlockAndUpdate(pos, SanguimancyModBlocks.BLOOD_ROSE.defaultBlockState());
            }
        }
        return super.useOn(useOnContext);
    }

    private void drain(Player player) {
        player.addEffect(new MobEffectInstance(SanguimancyModMobEffects.DRAIN, 20*10, this.DRAIN_LEVEL));
    }
}
