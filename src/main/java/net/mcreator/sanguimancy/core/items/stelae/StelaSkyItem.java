package net.mcreator.sanguimancy.core.items.stelae;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StelaSkyItem extends StelaItem{

    public static final Vec3 VELOCITY = new Vec3(3,3,3);
    public StelaSkyItem(Properties properties, float bloodCost, int chargeMultiplier, int cooldown) {
        super(properties, bloodCost, chargeMultiplier, cooldown);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (tryBloodCost(player, player.getItemInHand(interactionHand))) {
            player.addDeltaMovement(player.getLookAngle().multiply(VELOCITY));
            playerStatAndCooldown(player);
            return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
        }
        return super.use(level, player, interactionHand);
    }
}
