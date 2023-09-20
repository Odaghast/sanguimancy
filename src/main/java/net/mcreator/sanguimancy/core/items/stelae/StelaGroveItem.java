package net.mcreator.sanguimancy.core.items.stelae;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StelaGroveItem extends StelaItem{
    public StelaGroveItem(Properties properties, float bloodCost, int chargeMultiplier, int cooldown) {
        super(properties, bloodCost, chargeMultiplier, cooldown);

    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (tryBloodCost(useOnContext.getPlayer(), useOnContext.getItemInHand())) {
            Level level = useOnContext.getLevel();
            BlockPos blockPos = useOnContext.getClickedPos();
            BlockPos blockPos2 = blockPos.relative(useOnContext.getClickedFace());
            if (StelaGroveItem.growCrop(useOnContext.getItemInHand(), level, blockPos)) {
                if (!level.isClientSide) {
                    level.levelEvent(1505, blockPos, 0);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            BlockState blockState = level.getBlockState(blockPos);
            boolean bl = blockState.isFaceSturdy(level, blockPos, useOnContext.getClickedFace());
            if (bl && StelaGroveItem.growWaterPlant(useOnContext.getItemInHand(), level, blockPos2, useOnContext.getClickedFace())) {
                if (!level.isClientSide) {
                    level.levelEvent(1505, blockPos2, 0);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            playerStatAndCooldown(useOnContext.getPlayer());
        }
        return InteractionResult.PASS;
    }

    public static boolean growCrop(ItemStack itemStack, Level level, BlockPos blockPos) {
        BonemealableBlock bonemealableBlock;
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof BonemealableBlock && (bonemealableBlock = (BonemealableBlock)((Object)blockState.getBlock())).isValidBonemealTarget(level, blockPos, blockState, level.isClientSide)) {
            if (level instanceof ServerLevel) {
                if (bonemealableBlock.isBonemealSuccess(level, level.random, blockPos, blockState)) {
                    bonemealableBlock.performBonemeal((ServerLevel)level, level.random, blockPos, blockState);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean growWaterPlant(ItemStack itemStack, Level level, BlockPos blockPos, @Nullable Direction direction) {
        if (!level.getBlockState(blockPos).is(Blocks.WATER) || level.getFluidState(blockPos).getAmount() != 8) {
            return false;
        }
        if (!(level instanceof ServerLevel)) {
            return true;
        }
        RandomSource randomSource = level.getRandom();
        block0: for (int i = 0; i < 128; ++i) {
            BlockPos blockPos2 = blockPos;
            BlockState blockState = Blocks.SEAGRASS.defaultBlockState();
            for (int j = 0; j < i / 16; ++j) {
                if (level.getBlockState(blockPos2 = blockPos2.offset(randomSource.nextInt(3) - 1, (randomSource.nextInt(3) - 1) * randomSource.nextInt(3) / 2, randomSource.nextInt(3) - 1)).isCollisionShapeFullBlock(level, blockPos2)) continue block0;
            }
            Holder<Biome> holder2 = level.getBiome(blockPos2);
            if (holder2.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                if (i == 0 && direction != null && direction.getAxis().isHorizontal()) {
                    blockState = BuiltInRegistries.BLOCK.getTag(BlockTags.WALL_CORALS).flatMap(named -> named.getRandomElement(level.random)).map(holder -> ((Block)holder.value()).defaultBlockState()).orElse(blockState);
                    if (blockState.hasProperty(BaseCoralWallFanBlock.FACING)) {
                        blockState = (BlockState)blockState.setValue(BaseCoralWallFanBlock.FACING, direction);
                    }
                } else if (randomSource.nextInt(4) == 0) {
                    blockState = BuiltInRegistries.BLOCK.getTag(BlockTags.UNDERWATER_BONEMEALS).flatMap(named -> named.getRandomElement(level.random)).map(holder -> ((Block)holder.value()).defaultBlockState()).orElse(blockState);
                }
            }
            if (blockState.is(BlockTags.WALL_CORALS, blockStateBase -> blockStateBase.hasProperty(BaseCoralWallFanBlock.FACING))) {
                for (int k = 0; !blockState.canSurvive(level, blockPos2) && k < 4; ++k) {
                    blockState = (BlockState)blockState.setValue(BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(randomSource));
                }
            }
            if (!blockState.canSurvive(level, blockPos2)) continue;
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (blockState2.is(Blocks.WATER) && level.getFluidState(blockPos2).getAmount() == 8) {
                level.setBlock(blockPos2, blockState, 3);
                continue;
            }
            if (!blockState2.is(Blocks.SEAGRASS) || randomSource.nextInt(10) != 0) continue;
            ((BonemealableBlock)((Object)Blocks.SEAGRASS)).performBonemeal((ServerLevel)level, randomSource, blockPos2, blockState2);
        }
        return true;
    }

    public static void addGrowthParticles(LevelAccessor levelAccessor, BlockPos blockPos, int i) {
        double e;
        BlockState blockState;
        if (i == 0) {
            i = 15;
        }
        if ((blockState = levelAccessor.getBlockState(blockPos)).isAir()) {
            return;
        }
        double d = 0.5;
        if (blockState.is(Blocks.WATER)) {
            i *= 3;
            e = 1.0;
            d = 3.0;
        } else if (blockState.isSolidRender(levelAccessor, blockPos)) {
            blockPos = blockPos.above();
            i *= 3;
            d = 3.0;
            e = 1.0;
        } else {
            e = blockState.getShape(levelAccessor, blockPos).max(Direction.Axis.Y);
        }
        levelAccessor.addParticle(ParticleTypes.HAPPY_VILLAGER, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, 0.0, 0.0, 0.0);
        RandomSource randomSource = levelAccessor.getRandom();
        for (int j = 0; j < i; ++j) {
            double n;
            double m;
            double f = randomSource.nextGaussian() * 0.02;
            double g = randomSource.nextGaussian() * 0.02;
            double h = randomSource.nextGaussian() * 0.02;
            double k = 0.5 - d;
            double l = (double)blockPos.getX() + k + randomSource.nextDouble() * d * 2.0;
            if (levelAccessor.getBlockState(BlockPos.containing(l, m = (double)blockPos.getY() + randomSource.nextDouble() * e, n = (double)blockPos.getZ() + k + randomSource.nextDouble() * d * 2.0).below()).isAir()) continue;
            levelAccessor.addParticle(ParticleTypes.HAPPY_VILLAGER, l, m, n, f, g, h);
        }
    }
}
