package net.mcreator.sanguimancy.core.items;

import net.mcreator.sanguimancy.core.blocks.ICuretteHandled;
import net.mcreator.sanguimancy.registries.SanguimancyModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CuretteItem extends Item {
    public CuretteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        if (level.isClientSide() || player == null || !player.mayBuild()) {
            return super.useOn(useOnContext);
        } else {
            BlockState state = level.getBlockState(useOnContext.getClickedPos());

            if (state.getBlock() instanceof ICuretteHandled actor) {
                if (player.isCrouching())
                    return actor.onSneakCurette(state, useOnContext.getClickedPos(), useOnContext);
                useOnContext.getLevel().playSound(null, useOnContext.getClickedPos(), SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS);
                return actor.onCurette(state, useOnContext.getClickedPos(), useOnContext);
            }

            if (state.is(SanguimancyModBlockTags.CURETTE_BREAKABLE) && player.isCrouching()) {
                return onSneakCuretteOther(state, useOnContext.getClickedPos(), useOnContext);
            }
        }
        return super.useOn(useOnContext);
    }

    private InteractionResult onSneakCuretteOther(BlockState blockState, BlockPos blockPos, UseOnContext context) {

        if (context.getLevel() instanceof ServerLevel level) {
            Player player = context.getPlayer();
            if (player != null && !player.isCreative()) {
                Block.getDrops(blockState, level, blockPos, level.getBlockEntity(blockPos), player, context.getItemInHand())
                        .forEach(itemStack -> {
                            player.getInventory().placeItemBackInInventory(itemStack);
                        });
                blockState.spawnAfterBreak(level, blockPos, ItemStack.EMPTY, true);
                level.destroyBlock(blockPos, false);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
