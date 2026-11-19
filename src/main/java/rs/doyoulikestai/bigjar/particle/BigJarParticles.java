package rs.doyoulikestai.bigjar.particle;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import rs.doyoulikestai.bigjar.BigJar;
import rs.doyoulikestai.bigjar.item.BigJarItem;
import rs.doyoulikestai.bigjar.item.BigJarItems;

import java.util.function.Supplier;

import static rs.doyoulikestai.bigjar.BigJar.MODID;

public class BigJarParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);

    public static final Supplier<SimpleParticleType> JAR_POOF_PARTICLE = PARTICLE_TYPES.register(
        "jar_poof",
            () -> new SimpleParticleType(false)
    );

    public static final Supplier<SimpleParticleType> LUBRICATED_DRIP = PARTICLE_TYPES.register(
            "lubricated_drip",
            () -> new SimpleParticleType(false)
    );

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
        eventBus.addListener(BigJarParticles::registerParticleProviders);
    }

    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BigJarParticles.JAR_POOF_PARTICLE.get(), JarPoofParticleProvider::new);
        event.registerSpriteSet(BigJarParticles.LUBRICATED_DRIP.get(), LubricatedDripParticle.Provider::new);
    }

}
