package rs.doyoulikestai.bigjar.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import rs.doyoulikestai.bigjar.effect.BigJarEffects;

import static rs.doyoulikestai.bigjar.item.BigJarItem.UNJARRABLE_TAG;

public class LittleJarOfLubricantItem extends Item {
    public LittleJarOfLubricantItem(Properties properties) {
        super(properties);
    }
    public static final Integer LUBRICATED_EFFECT_DURATION = 15 * 20;

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand usedHand) {
        if (!(player instanceof ServerPlayer)) return InteractionResult.SUCCESS;
        if (usedHand != InteractionHand.MAIN_HAND) return InteractionResult.FAIL;
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return InteractionResult.FAIL;
        if (isUnjarrable(entity)) return InteractionResult.FAIL;

        entity.addEffect(new MobEffectInstance(BigJarEffects.LUBRICATED, LUBRICATED_EFFECT_DURATION, 0, false, false, true));
        stack.consume(1, player);

        return InteractionResult.SUCCESS;
    }

    private static boolean isUnjarrable(LivingEntity entity) {
        return entity.getType().is(UNJARRABLE_TAG);
    }
}
