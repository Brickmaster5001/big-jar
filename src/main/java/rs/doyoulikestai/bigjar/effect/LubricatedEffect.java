package rs.doyoulikestai.bigjar.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rs.doyoulikestai.bigjar.particle.BigJarParticles;
import rs.doyoulikestai.bigjar.sound.BigJarSounds;
import rs.doyoulikestai.bigjar.util.RNG;

public class LubricatedEffect extends MobEffect {
    protected LubricatedEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.withSoundOnAdded(BigJarSounds.BIG_JAR_USE_SOUND.value());
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        AABB bounds = entity.getBoundingBox();
        Vec3 center = bounds.getCenter();
        level.sendParticles(
                BigJarParticles.LUBRICATED_DRIP.get(),
                center.x,
                center.y,
                center.z,
                3,
                bounds.getXsize()/4,
                bounds.getYsize()/8,
                bounds.getZsize()/4,
                0
        );
        //level.playSound(null, player.getX(), player.getY(), player.getZ(), BigJarSounds.BIG_JAR_USE_SOUND.value(), SoundSource.PLAYERS, 1.0f, RNG.RANDOM.nextFloat() * 0.4f + 1.2f);
        return super.applyEffectTick(level, entity, amplifier);
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
