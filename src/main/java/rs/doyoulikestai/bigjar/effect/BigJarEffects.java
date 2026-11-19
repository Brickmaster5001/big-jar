package rs.doyoulikestai.bigjar.effect;

import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import rs.doyoulikestai.bigjar.BigJar;
import rs.doyoulikestai.bigjar.attribute.BigJarAttributes;

import static rs.doyoulikestai.bigjar.BigJar.MODID;

public class BigJarEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(
            BuiltInRegistries.MOB_EFFECT,
            BigJar.MODID
    );

    public static final Holder<MobEffect> LUBRICATED = MOB_EFFECTS.register("lubricated", () -> new LubricatedEffect(
            MobEffectCategory.NEUTRAL,
            0xEDFF16
    ).addAttributeModifier(BigJarAttributes.FRICTION,
            ResourceLocation.fromNamespaceAndPath(MODID, "lubricated"),
            0.7,
            AttributeModifier.Operation.ADD_VALUE
    ));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
