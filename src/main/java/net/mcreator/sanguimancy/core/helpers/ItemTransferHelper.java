package net.mcreator.sanguimancy.core.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTransferHelper {


    public static boolean canPlaceItemInContainer(Container container, ItemStack itemStack, int i, @Nullable Direction direction) {
        WorldlyContainer worldlyContainer;
        if (!container.canPlaceItem(i, itemStack)) {
            return false;
        }
        return !(container instanceof WorldlyContainer) || (worldlyContainer = (WorldlyContainer)container).canPlaceItemThroughFace(i, itemStack, direction);
    }

    public static boolean canTakeItemFromContainer(Container container, Container container2, ItemStack itemStack, int i, Direction direction) {
        WorldlyContainer worldlyContainer;
        if (!container2.canTakeItem(container, i, itemStack)) {
            return false;
        }
        return !(container2 instanceof WorldlyContainer) || (worldlyContainer = (WorldlyContainer)container2).canTakeItemThroughFace(i, itemStack, direction);
    }

    //RUN THIS METHOD FOR ADDING ITEMS TO A CONTAINER
    public static ItemStack addItem(@Nullable Container container, Container container2, ItemStack itemStack, @Nullable Direction direction) {
        if (container2 instanceof WorldlyContainer worldlyContainer) {
            if (direction != null) {
                int[] is = worldlyContainer.getSlotsForFace(direction);
                int i = 0;
                while (i < is.length) {
                    if (itemStack.isEmpty()) return itemStack;
                    itemStack = tryMoveInItem(container, container2, itemStack, is[i], direction);
                    ++i;
                }
                return itemStack;
            }
        }
        int j = container2.getContainerSize();
        int i = 0;
        while (i < j) {
            if (itemStack.isEmpty()) return itemStack;
            itemStack = tryMoveInItem(container, container2, itemStack, i, direction);
            ++i;
        }
        return itemStack;
    }

    private static ItemStack tryMoveInItem(@Nullable Container container, Container container2, ItemStack itemStack, int i, @Nullable Direction direction) {
        ItemStack itemStack2 = container2.getItem(i);
        if (canPlaceItemInContainer(container2, itemStack, i, direction)) {
            int k;
            boolean bl = false;
            boolean bl2 = container2.isEmpty();
            if (itemStack2.isEmpty()) {
                container2.setItem(i, itemStack);
                itemStack = ItemStack.EMPTY;
                bl = true;
            } else if (canMergeItems(itemStack2, itemStack)) {
                int j = itemStack.getMaxStackSize() - itemStack2.getCount();
                k = Math.min(itemStack.getCount(), j);
                itemStack.shrink(k);
                itemStack2.grow(k);
                boolean bl3 = bl = k > 0;
            }
            if (bl) {
                container2.setChanged();
            }
        }
        return itemStack;
    }

    private static boolean canMergeItems(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack.getCount() <= itemStack.getMaxStackSize() && ItemStack.isSameItemSameTags(itemStack, itemStack2);
    }

    public static Container getContainerAt(Level level, BlockPos blockPos) {
        return getContainerAt(level, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
    }

    @Nullable
    private static Container getContainerAt(Level level, double d, double e, double f) {
        List<Entity> list;
        BlockEntity blockEntity;
        Container container = null;
        BlockPos blockPos = BlockPos.containing(d, e, f);
        BlockState blockState = level.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof WorldlyContainerHolder) {
            container = ((WorldlyContainerHolder)((Object)block)).getContainer(blockState, level, blockPos);
        } else if (blockState.hasBlockEntity() && (blockEntity = level.getBlockEntity(blockPos)) instanceof Container && (container = (Container)((Object)blockEntity)) instanceof ChestBlockEntity && block instanceof ChestBlock) {
            container = ChestBlock.getContainer((ChestBlock)block, blockState, level, blockPos, true);
        }
        if (container == null && !(list = level.getEntities((Entity)null, new AABB(d - 0.5, e - 0.5, f - 0.5, d + 0.5, e + 0.5, f + 0.5), EntitySelector.CONTAINER_ENTITY_SELECTOR)).isEmpty()) {
            container = (Container)((Object)list.get(level.random.nextInt(list.size())));
        }
        return container;
    }

    public static boolean canDropItemInWorld(Level level, BlockPos blockPos, ItemStack itemStack) {
        return level.getBlockState(blockPos).isAir() && !itemStack.isEmpty();
    }

    public static void dropItemInWorld(Level level, BlockPos blockPos, ItemStack itemStack) {
        level.addFreshEntity(new ItemEntity(level, blockPos.getX()+0.5f, blockPos.getY()+0.5f, blockPos.getZ()+0.5f, itemStack));
    }

    public static void dropAllContents(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.is(blockState2.getBlock())) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof Container container) {
            Containers.dropContents(level, blockPos, container);
        }
    }
}
