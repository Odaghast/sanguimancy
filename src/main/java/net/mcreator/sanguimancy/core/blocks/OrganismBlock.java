package net.mcreator.sanguimancy.core.blocks;

import net.mcreator.sanguimancy.registries.SanguimancyModBlockStateProperties;
import net.mcreator.sanguimancy.registries.SanguimancyModBlocks;
import net.mcreator.sanguimancy.registries.SanguimancyModItems;
import net.mcreator.sanguimancy.registries.SanguimancyModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrganismBlock extends Block {

    private final TagKey<Block> FOOD_BLOCK_TAG;
    private final Block ORGANISM_HEART_BLOCK;
    public OrganismBlock(Properties properties, TagKey<Block> foodBlockTag, Block organismHeartBlock) {
        super(properties);
        this.FOOD_BLOCK_TAG = foodBlockTag;
        this.ORGANISM_HEART_BLOCK = organismHeartBlock;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (!level.isClientSide()) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (itemStack.getItem() == Items.GLASS_BOTTLE) {
                level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                itemStack.shrink(1);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS);
                player.addItem(new ItemStack(SanguimancyModBlocks.BLOOD_ORGANISM));

                return InteractionResult.SUCCESS;
            }

            if (player.hasEffect(SanguimancyModMobEffects.DRAIN) && !isHungry(level, blockPos, blockState)) {
                level.setBlockAndUpdate(blockPos, this.defaultBlockState());
                level.playSound(null, blockPos, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS);
                level.addFreshEntity(new ItemEntity(level,
                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        new ItemStack(SanguimancyModItems.BLOOD_NUTRIENT)));

                return InteractionResult.SUCCESS;
            }
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        List<Direction> directions = makeRandomDirections();

        if (this.isHungry(serverLevel, blockPos, blockState)) {
            BlockPos targetBlockPos;
            BlockState targetBlockState;
            for (Direction direction : directions) {
                targetBlockPos = blockPos.relative(direction);
                targetBlockState = serverLevel.getBlockState(targetBlockPos);
                if (this.eatBlock(serverLevel, blockPos, blockState, targetBlockPos, targetBlockState, randomSource)) {
                    break;
                }
            }
        } else if (randomSource.nextFloat() < this.getReproduceChance()) {
            BlockPos targetBlockPos;
            BlockState targetBlockState;
            for (Direction direction : directions) {
                targetBlockPos = blockPos.relative(direction);
                targetBlockState = serverLevel.getBlockState(targetBlockPos);
                if (!pushNutrient(serverLevel, blockPos, blockState, targetBlockPos, targetBlockState)) {
                    if (reproduce(serverLevel, blockPos, blockState, targetBlockPos, targetBlockState)) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
        List<Direction> directions = makeRandomDirections();
        int organisms = 0;
        BlockState targetBlock;
        for (Direction direction : directions) {
            targetBlock = serverLevel.getBlockState(blockPos.relative(direction));
            if (targetBlock.isAir()) {
                organisms = -5;
            } else if (targetBlock.getBlock() == blockState.getBlock()) {
                organisms = organisms + 1;
            }
        }
        if (organisms >= 5) {
            serverLevel.setBlockAndUpdate(blockPos, this.ORGANISM_HEART_BLOCK.defaultBlockState());
        } else {
            this.randomTick(blockState, serverLevel, blockPos, randomSource);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        if (block == Blocks.AIR) {
            level.scheduleTick(blockPos, this, 20 + level.random.nextInt(160), TickPriority.LOW);
        }
    }

    public boolean isHungry(Level level, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT) < 3;
    }

    public float getReproduceChance() {
        return 1.0f;
    }

    public float getSatisfyChance() {
        return 1.0f;
    }

    public boolean canEatBlock(Level level, BlockPos blockPos, BlockState blockState, BlockPos targetBlockPos, BlockState targetBlockState) {
        return targetBlockState.is(this.FOOD_BLOCK_TAG);
    }

    public boolean pushNutrient(Level level, BlockPos blockPos, BlockState blockState, BlockPos targetBlockPos, BlockState targetBlockState) {
        if (blockState.getBlock() == targetBlockState.getBlock() && targetBlockState.getValue(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT) < 3) {
            level.setBlockAndUpdate(targetBlockPos, this.defaultBlockState().setValue(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT, 3));
            level.setBlockAndUpdate(blockPos, this.defaultBlockState().setValue(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT, 0));
            level.playSound(null, blockPos, SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.scheduleTick(targetBlockPos, this, 20 + level.random.nextInt(160), TickPriority.LOW);
            return true;
        }
        return false;
    }

    public boolean reproduce(Level level, BlockPos blockPos, BlockState blockState, BlockPos targetBlockPos, BlockState targetBlockState) {
        if (targetBlockState.isAir()) {
            level.setBlockAndUpdate(blockPos, this.defaultBlockState().setValue(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT, 0));
            level.setBlockAndUpdate(targetBlockPos, this.defaultBlockState());
            level.playSound(null, blockPos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            return true;
        }
        return false;
    }

    private List<Direction> makeRandomDirections() {
        List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(directions);
        return directions;
    }

    public boolean eatBlock(Level level, BlockPos blockPos, BlockState blockState, BlockPos targetBlockPos, BlockState targetBlockState, RandomSource random) {
        if (this.canEatBlock(level, blockPos, blockState, targetBlockPos, targetBlockState)) {
            level.destroyBlock(targetBlockPos, false);
            level.playSound(null, blockPos, SoundEvents.FOX_BITE, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (random.nextFloat() < this.getSatisfyChance()) {
                level.setBlockAndUpdate(blockPos, this.defaultBlockState().setValue(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT,
                        blockState.getValue(SanguimancyModBlockStateProperties.LEVEL_NUTRIENT) + 1));
                return true;
            }
        }
        return false;
    }
}
