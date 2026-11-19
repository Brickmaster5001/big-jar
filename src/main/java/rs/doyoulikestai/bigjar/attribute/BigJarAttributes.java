package rs.doyoulikestai.bigjar.attribute;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import rs.doyoulikestai.bigjar.BigJar;

public class BigJarAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
            BuiltInRegistries.ATTRIBUTE, BigJar.MODID
    );

    public static final Holder<Attribute> FRICTION = ATTRIBUTES.register("friction", () -> new RangedAttribute(
            "attributes.bigjar.friction",
            1D,
            0D,
            1024D
    ).setSyncable(true).setSentiment(Attribute.Sentiment.NEUTRAL));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> {
            event.add(
                entityType,
                BigJarAttributes.FRICTION
            );
        });
    }
}
