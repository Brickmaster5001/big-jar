package rs.doyoulikestai.bigjar.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import rs.doyoulikestai.bigjar.effect.BigJarEffects;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static rs.doyoulikestai.bigjar.BigJar.MODID;

public class BigJarItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> BIG_JAR_ITEM = ITEMS.registerItem(
        "big_jar",
        BigJarItem::new,
        new Item.Properties().stacksTo(1).component(DataComponents.ENTITY_DATA, CustomData.EMPTY)
    );

    public static final DeferredItem<Item> LITTLE_JAR = ITEMS.registerItem(
        "little_jar",
        Item::new,
        new Item.Properties().stacksTo(16)
    );

    public static final DeferredItem<Item> LITTLE_JAR_OF_LUBRICANT = ITEMS.registerItem(
        "little_jar_of_lubricant",
        LittleJarOfLubricantItem::new,
        new Item.Properties().stacksTo(16).component(DataComponents.FOOD,
                new FoodProperties(0, 0.0f, true, 1.0f, Optional.empty(), List.of(
                        new FoodProperties.PossibleEffect(new Supplier<MobEffectInstance>() {
                            @Override
                            public MobEffectInstance get() {
                                return new MobEffectInstance(
                                        BigJarEffects.LUBRICATED,
                                        LittleJarOfLubricantItem.LUBRICATED_EFFECT_DURATION,
                                        0, false, false, true
                                );
                            }
                        }, 1.0f)
                ))
        )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        eventBus.addListener(BigJarItems::registerItemModels);
    }

    public static void registerItemModels(ModelEvent event) {
        event.

        event.register(
            ResourceLocation.fromNamespaceAndPath(MODID, "big_jar_composited"),
            BigJarItemModel.Unbaked.MAP_CODEC
        );
    }
}
