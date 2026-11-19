package rs.doyoulikestai.bigjar.sound;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import rs.doyoulikestai.bigjar.BigJar;

public class BigJarSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BigJar.MODID);

    public static final Holder<SoundEvent> BIG_JAR_USE_SOUND = SOUND_EVENTS.register(
            "item.big_jar.on_use",
            SoundEvent::createVariableRangeEvent
    );

    public static final Holder<SoundEvent> LITTLE_JAR_OF_LUBRICANT_USE = SOUND_EVENTS.register(
            "item.little_jar_lubricant.on_use",
            SoundEvent::createVariableRangeEvent
    );

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
