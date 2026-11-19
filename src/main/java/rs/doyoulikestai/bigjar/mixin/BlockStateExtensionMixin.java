package rs.doyoulikestai.bigjar.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockStateExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rs.doyoulikestai.bigjar.attribute.BigJarAttributes;

@Mixin(IBlockStateExtension.class)
public interface BlockStateExtensionMixin {
    @Inject(at = @At("HEAD"), method = "getFriction", cancellable = true)
    private void applyFrictionAttribute(LevelReader level, BlockPos pos, @Nullable Entity entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            var thiz = ((BlockState) this);
            cir.setReturnValue(
                    thiz.getBlock().getFriction(thiz, level, pos, entity) * (float) livingEntity.getAttributeValue(BigJarAttributes.FRICTION)
            );
        }
    }
}
