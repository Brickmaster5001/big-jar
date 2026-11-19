package rs.doyoulikestai.bigjar;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import rs.doyoulikestai.bigjar.attribute.BigJarAttributes;
import rs.doyoulikestai.bigjar.effect.BigJarEffects;
import rs.doyoulikestai.bigjar.item.BigJarItems;
import rs.doyoulikestai.bigjar.datagen.BigJarRecipeProvider;
import rs.doyoulikestai.bigjar.particle.BigJarParticles;
import rs.doyoulikestai.bigjar.sound.BigJarSounds;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BigJar.MODID)
public class BigJar {
    public static final String MODID = "bigjar";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BIG_JAR_TAB = CREATIVE_MODE_TABS.register("big_jar_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bigjar")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> BigJarItems.BIG_JAR_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BigJarItems.BIG_JAR_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public BigJar(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(BigJar::onGatherData);
        modEventBus.addListener(BigJar::onGatherServerData);
        modEventBus.addListener(BigJarAttributes::modifyDefaultAttributes);

        CREATIVE_MODE_TABS.register(modEventBus);

        BigJarItems.register(modEventBus);
        BigJarParticles.register(modEventBus);
        BigJarSounds.register(modEventBus);
        BigJarEffects.register(modEventBus);
        BigJarAttributes.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == BIG_JAR_TAB.getKey()) {
            //event.accept(BigJarItems.BIG_JAR_ITEM);
            event.accept(BigJarItems.LITTLE_JAR);
            event.accept(BigJarItems.LITTLE_JAR_OF_LUBRICANT);
        }
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        event.createProvider(BigJarRecipeProvider.Runner::new);
    }

    @SubscribeEvent
    public static void onGatherServerData(GatherDataEvent.Server event) {
        event.createProvider(BigJarRecipeProvider.Runner::new);
    }
}
