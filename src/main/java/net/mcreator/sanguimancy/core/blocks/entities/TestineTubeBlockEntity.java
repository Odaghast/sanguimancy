package net.mcreator.sanguimancy.core.blocks.entities;

import net.mcreator.sanguimancy.core.blocks.TestineTubeBlock;
import net.mcreator.sanguimancy.core.helpers.ItemTransferHelper;
import net.mcreator.sanguimancy.registries.SanguimancyModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TestineTubeBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int[] SLOTS_INPUT = new int[]{0};
    private static final int[] SLOTS_NONE = new int[0];

    public static final int MOVE_ITEM_SPEED = 20;
    public int cooldown = -1;
    public static final int CONTAINER_SIZE = 1;
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    public TestineTubeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(SanguimancyModBlockEntities.TESTINE_TUBE, blockPos, blockState);
    }
    public TestineTubeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public static void onTick(Level level, BlockPos blockPos, BlockState blockState, TestineTubeBlockEntity entity) {
        --entity.cooldown;
        if (!entity.isWaiting()) {
            entity.stopCooldown();//stops cooldown from ticking into negative infinity
            if (tryDropItems(level, blockPos, blockState, entity) || tryMoveItems(level, blockPos, blockState, entity)) {
                entity.startCooldown();
                //yes i realize this always returns true, shut up
            }
        }
    }

    public static boolean tryMoveItems(Level level, BlockPos blockPos, BlockState blockState, TestineTubeBlockEntity entity) {
        Container container;
        if (level.isClientSide()) {
            return false;
        } else if (!entity.getItem(0).isEmpty() && (container = getExportContainer(level, blockPos, blockState)) != null) {
            entity.setItem(0, ItemTransferHelper.addItem(entity, container, entity.getItem(0), getFacing(blockState)));
            if (entity.getItem(0).isEmpty()) {
                if (container instanceof TestineTubeBlockEntity tube) {
                    tube.startCooldown();
                }
                entity.setChanged();
                level.playSound(null, blockPos, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS);
            }
        }
        return true;
    }

    public static boolean tryDropItems(Level level, BlockPos blockPos, BlockState blockState, TestineTubeBlockEntity entity) {
        if (level.isClientSide()) {
            return false;
        } else if (ItemTransferHelper.canDropItemInWorld(level, (blockPos = blockPos.relative(getFacing(blockState).getOpposite())), entity.getItem(0))) {
            ItemTransferHelper.dropItemInWorld(level, blockPos, entity.getItem(0));
            entity.clearContent();
            entity.setChanged();
            level.playSound(null, blockPos, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS);
            return true;
        }
        return false;
    }

    private static Container getExportContainer(Level level, BlockPos blockPos, BlockState blockState) {
        return ItemTransferHelper.getContainerAt(level, getExportBlockPos(blockPos, blockState));
    }

    private static BlockPos getExportBlockPos(BlockPos blockPos, BlockState blockState) {
        return blockPos.relative(getFacing(blockState).getOpposite());
    }

    private static Direction getFacing(BlockState blockState) {
        return blockState.getValue(TestineTubeBlock.FACING);
    }

    public boolean isWaiting() {
        return this.cooldown > 0;
    }
    public void stopCooldown() {
        this.cooldown = 0;
    }

    public void startCooldown() {
        this.cooldown = MOVE_ITEM_SPEED;
    }

    //BASECONTAINER FUNCTIONS---------------------------------------------------------------------------------

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.hopper");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = getItem(i);
        items.set(0, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = getItem(i);
        //items.set(0, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        items.set(i, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        items.set(0, ItemStack.EMPTY);
    }

    //WORLDYCONTAINER ----------------------------------------------------------------------------

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return direction == getFacing(getBlockState()) ? SLOTS_INPUT : SLOTS_NONE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return getBlockState().getValue(TestineTubeBlock.FACING) == direction;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return getBlockState().getValue(TestineTubeBlock.FACING).getOpposite() == direction;
    }
}
