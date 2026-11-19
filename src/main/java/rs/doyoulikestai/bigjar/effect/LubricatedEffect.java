package rs.doyoulikestai.bigjar.effect;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import rs.doyoulikestai.bigjar.particle.BigJarParticles;
import rs.doyoulikestai.bigjar.sound.BigJarSounds;
import rs.doyoulikestai.bigjar.util.RNG;

public class LubricatedEffect extends MobEffect {
    protected LubricatedEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.withSoundOnAdded(BigJarSounds.BIG_JAR_USE_SOUND.value());
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        AABB bounds = livingEntity.getBoundingBox();
        Vec3 center = bounds.getCenter();
        if (livingEntity.level() instanceof ClientLevel clientLevel) {
            for (int i = RNG.getIntFromRange(1, 2); i > 0; i--) {
                clientLevel.addParticle(
                        BigJarParticles.LUBRICATED_DRIP.get(),
                        center.x,
                        center.y,
                        center.z,
                        bounds.getXsize()/4,
                        bounds.getYsize()/8,
                        bounds.getZsize()/4
                );
            }
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 5 >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }

    @Override
    public void onEffectAdded(LivingEntity entity, int amplifier) {
        super.onEffectAdded(entity, amplifier);
    }
}
