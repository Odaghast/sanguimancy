package net.mcreator.sanguimancy.core.blocks;

import net.mcreator.sanguimancy.core.blocks.entities.TestineTubeBlockEntity;
import net.mcreator.sanguimancy.core.helpers.ItemTransferHelper;
import net.mcreator.sanguimancy.core.helpers.PlacementHelper;
import net.mcreator.sanguimancy.registries.SanguimancyModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class TestineTubeBlock extends BaseEntityBlock implements EntityBlock, ICuretteHandled {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public TestineTubeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return PlacementHelper.onFaceWithCrouch(blockPlaceContext, this, FACING);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(FACING, mirror.mirror(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TestineTubeBlock.createTickerHelper(blockEntityType, SanguimancyModBlockEntities.TESTINE_TUBE, TestineTubeBlockEntity::onTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TestineTubeBlockEntity(SanguimancyModBlockEntities.TESTINE_TUBE, blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        ItemTransferHelper.dropAllContents(blockState, level, blockPos, blockState2, bl);
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public InteractionResult onCurette(BlockState blockState, BlockPos blockPos, UseOnContext context) {
        PlacementHelper.rotateAll(blockState, FACING, context);
        return ICuretteHandled.super.onCurette(blockState, blockPos, context);
    }
}
