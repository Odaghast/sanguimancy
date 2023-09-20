package net.mcreator.sanguimancy.core.items.stelae;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class StelaFluidPlacerItem extends StelaItem{

    private final Fluid FLUID;
    public StelaFluidPlacerItem(Properties properties, float bloodCost, int chargeMultiplier, int cooldown, Fluid fluid) {
        super(properties, bloodCost, chargeMultiplier, cooldown);
        FLUID = fluid;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos().relative(useOnContext.getClickedFace());
        BlockState blockState = level.getBlockState(blockPos);
        boolean canReplace = blockState.isAir() || blockState.canBeReplaced(FLUID);

        if (canReplace && tryBloodCost(useOnContext.getPlayer(), useOnContext.getItemInHand())) {
            Player player = useOnContext.getPlayer();
            playerStatAndCooldown(player);
            if (level.dimensionType().ultraWarm() && FLUID == Fluids.WATER) {
                //haha you want water in the NETHER?! not gonna happen
                level.playSound(player, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (level.random.nextFloat() - level.random.nextFloat()) * 0.8f);
                for (int l = 0; l < 8; ++l) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, blockPos.getX() + Math.random(), blockPos.getY() + Math.random(), blockPos.getZ() + Math.random(), 0.0, 0.0, 0.0);
                }
            } else {
                level.setBlock(blockPos, FLUID.defaultFluidState().createLegacyBlock(), 11);
                level.playSound(player, blockPos, FLUID == Fluids.LAVA ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.gameEvent(player, GameEvent.FLUID_PLACE, blockPos);
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(useOnContext);
    }
}
