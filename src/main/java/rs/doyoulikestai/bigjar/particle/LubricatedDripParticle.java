package rs.doyoulikestai.bigjar.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3d;
import rs.doyoulikestai.bigjar.util.RNG;

public class LubricatedDripParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private static final int BASE_LIFETIME = 8;

    public LubricatedDripParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed,
            float sizeMultiplier,
            SpriteSet sprites
    ) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.sprites = sprites;
        this.friction = 0.67F;
        this.gravity = 1.05F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd *= 0.0;
        this.yd *= Math.random() * -0.20000000298023224 - 0.10000000149011612;;
        this.zd *= 0.0;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        this.quadSize *= 0.75F * sizeMultiplier;
        this.lifetime = (int)(8.0F / Mth.randomBetween(this.random, 1.0F, 7.0F) * sizeMultiplier);
        this.lifetime = Math.max(this.lifetime, 1);
        this.setSpriteFromAge(sprites);
        this.hasPhysics = true;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
        return SingleQuadParticle.FacingCameraMode.LOOKAT_Y;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp((this.age + scaleFactor) / this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new LubricatedDripParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1.5F, this.sprites);
        }
    }
}
