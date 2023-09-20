package net.mcreator.sanguimancy.core.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ICuretteHandled {

    default InteractionResult onSneakCurette(BlockState blockState, BlockPos blockPos, UseOnContext context) {

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
    default InteractionResult onCurette(BlockState blockState, BlockPos blockPos, UseOnContext context) {
        //WIP
        return InteractionResult.SUCCESS;
    }
}
