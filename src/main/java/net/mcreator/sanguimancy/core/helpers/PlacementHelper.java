package net.mcreator.sanguimancy.core.helpers;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class PlacementHelper {

    public static BlockState onFaceWithCrouch(BlockPlaceContext blockPlaceContext, Block block, DirectionProperty facing) {
        Direction direction = blockPlaceContext.getClickedFace();
        if (blockPlaceContext.getPlayer() != null && blockPlaceContext.getPlayer().isCrouching()) {
            return block.defaultBlockState().setValue(facing, direction.getOpposite());
        }
        return block.defaultBlockState().setValue(facing, direction);
    }

    public static Direction getRotateAll(Direction facing) {
        int pos = 0;
        for (Direction i : Direction.values()) {
            pos++;
            if (i == facing) {
                return Direction.values()[pos == 6 ? 0 : pos];
            }
        }
        return facing;
    }

    public static void rotateAll(BlockState blockState, DirectionProperty facing, UseOnContext context) {
        context.getLevel().setBlockAndUpdate(context.getClickedPos(), blockState.setValue(facing, getRotateAll(blockState.getValue(facing))));
    }
}
