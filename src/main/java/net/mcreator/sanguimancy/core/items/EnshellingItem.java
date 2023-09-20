package net.mcreator.sanguimancy.core.items;

import net.minecraft.core.BlockPos;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class EnshellingItem extends Item {

    private final LazyLoadedValue<Block> INPUT;
    private final LazyLoadedValue<Block> OUTPUT;

    public EnshellingItem(Properties properties, Supplier<Block> input, Supplier<Block> output) {
        super(properties);
        this.INPUT = new LazyLoadedValue<>(input);
        this.OUTPUT = new LazyLoadedValue<>(output);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();

        if (!level.isClientSide()) {
            BlockPos blockPos = useOnContext.getClickedPos();
            if (level.getBlockState(blockPos).getBlock() == this.INPUT.get()) {
                level.destroyBlock(blockPos, false);
                level.setBlockAndUpdate(blockPos, OUTPUT.get().defaultBlockState());
                useOnContext.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(useOnContext);
    }
}
