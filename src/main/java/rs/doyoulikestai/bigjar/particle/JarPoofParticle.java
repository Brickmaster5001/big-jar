package rs.doyoulikestai.bigjar.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3d;
import org.joml.Vector3f;
import rs.doyoulikestai.bigjar.util.RNG;

import java.util.Random;
import java.util.random.RandomGenerator;

public class JarPoofParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final static Float[] sizes = new Float[]{20.0f, 20.0f};

    public JarPoofParticle(ClientLevel level, Vector3d location, Vector3d speed, SpriteSet spriteSet) {
        super(level, location.x, location.y, location.z, speed.x*1.5, speed.y*1.5, speed.z*1.5);
        this.spriteSet = spriteSet;

        /* Setup Initial Params */
        this.gravity = 1f;
        this.friction = 0.04f;
        this.lifetime = RNG.getIntFromRange(3, 6);
        this.xd += speed.x*0.5;
        this.yd += speed.y;
        this.zd += speed.z*0.5;
        float size = Math.random() > 0.5 ? sizes[0] : sizes[1];
        this.setSize(size, size);

        this.setSpriteFromAge(spriteSet);
        this.hasPhysics = true;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);
    }
}
