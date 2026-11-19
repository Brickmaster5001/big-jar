package rs.doyoulikestai.bigjar.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import rs.doyoulikestai.bigjar.BigJar;
import rs.doyoulikestai.bigjar.effect.BigJarEffects;
import rs.doyoulikestai.bigjar.particle.BigJarParticles;
import rs.doyoulikestai.bigjar.sound.BigJarSounds;
import rs.doyoulikestai.bigjar.util.PlayerMessenger;
import rs.doyoulikestai.bigjar.util.RNG;

import java.util.Objects;

public class BigJarItem extends Item {
    public static final TagKey<EntityType<?>> UNJARRABLE_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BigJar.MODID, "unjarrable"));
    public static final TagKey<EntityType<?>> REQUIRES_LUBRICANT_TO_JAR_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BigJar.MODID, "requires_lubricant_to_jar"));

    private static final MapCodec<EntityType<?>> ENTITY_TYPE_FIELD_CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id");

    private static final int COOLDOWN_TICKS = 10;
    private static final float POOF_PARTICLE_SPEED = 0.15f;

    public BigJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if (!(player instanceof ServerPlayer)) return InteractionResult.SUCCESS;

        if (usedHand != InteractionHand.MAIN_HAND) return InteractionResult.FAIL;
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return InteractionResult.FAIL;
        if (isUnjarrable(interactionTarget)) {
            player.playSound(SoundEvents.NOTE_BLOCK_SNARE.value(), 1.0f, RNG.RANDOM.nextFloat() * 0.4f + 1.2f);
            PlayerMessenger.broadcastActionbar(Component.translatable("context.bigjar.cannot_be_jarred").getString(), ChatFormatting.RED);
            return InteractionResult.FAIL;
        };

        if (isLubricantRequired(interactionTarget) && !interactionTarget.hasEffect(BigJarEffects.LUBRICATED)) {
            player.playSound(SoundEvents.NOTE_BLOCK_BASEDRUM.value(), 1.0f, RNG.RANDOM.nextFloat() * 0.4f + 1.2f);
            PlayerMessenger.broadcastActionbar(Component.translatable("context.bigjar.requires_lubricant_to_jar").getString(), ChatFormatting.YELLOW);
            return InteractionResult.FAIL;
        };

        if (!(stack.get(DataComponents.ENTITY_DATA) instanceof CustomData storedEntity)) return InteractionResult.FAIL;
        if (!storedEntity.isEmpty()) return InteractionResult.FAIL;

        interactionTarget.stopRiding();
        interactionTarget.ejectPassengers();

        CompoundTag targetData = new CompoundTag();

        if (!interactionTarget.saveAsPassenger(targetData)) {
            PlayerMessenger.broadcastActionbar(Component.translatable("context.bigjar.failed_save_entity_to_jar").getString(), ChatFormatting.RED);
            return InteractionResult.FAIL;
        }

        targetData.remove("Pos");
        targetData.remove("Motion");

        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        heldItem.set(DataComponents.ENTITY_DATA, CustomData.of(targetData));
        if (interactionTarget.hasCustomName()) {
            heldItem.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            heldItem.set(DataComponents.RARITY, Rarity.UNCOMMON);
        };
        player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN_TICKS);
        //interactionTarget.getBoundingBox().;
        if ((interactionTarget.level() instanceof ServerLevel serverLevel)) {
            //AABB bounds = interactionTarget.getBoundingBox();
            //Vec3 center = bounds.getCenter();
            //serverLevel.sendParticles(
            //        BigJarParticles.JAR_POOF_PARTICLE.get(),
            //        center.x,
            //        center.y,
            //        center.z,
            //        15,
            //        bounds.getXsize()/3,
            //        bounds.getYsize()/3,
            //        bounds.getZsize()/3,
            //        POOF_PARTICLE_SPEED
            //);
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), BigJarSounds.BIG_JAR_USE_SOUND.value(), SoundSource.PLAYERS, 1.0f, RNG.RANDOM.nextFloat() * 0.4f + 1.2f);
        }


        interactionTarget.discard();

        //player.displayClientMessage(Component.literal("Stored: " + interactionTarget.getEncodeId()), false);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!(context.getPlayer() instanceof ServerPlayer player) ) return InteractionResult.SUCCESS;
        if (context.getHand() != InteractionHand.MAIN_HAND || context.getLevel().isClientSide) return InteractionResult.PASS;
        ServerLevel serverLevel = Objects.requireNonNull(context.getLevel().getServer()).getLevel(context.getLevel().dimension());
        ItemStack stack = context.getItemInHand();
        if (!(stack.get(DataComponents.ENTITY_DATA) instanceof CustomData storedEntity) || storedEntity.isEmpty()) return InteractionResult.FAIL;
        assert serverLevel != null;

        EntityType<?> entityType = storedEntity.read(ENTITY_TYPE_FIELD_CODEC).result().orElse(EntityType.COD); // Because whatever atp

        Entity entity = entityType.spawn(
            serverLevel,
            stack,
            null,
            context.getClickedPos(),
            MobSpawnType.SPAWN_EGG,
            true,
            false
        );
        if (entity instanceof LivingEntity livingEntity) livingEntity.removeEffect(BigJarEffects.LUBRICATED);
        stack.set(DataComponents.ENTITY_DATA, CustomData.EMPTY);
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        stack.set(DataComponents.RARITY, Rarity.COMMON);

        AABB bounds = entity.getBoundingBox();
        Vec3 center = bounds.getCenter();
        serverLevel.sendParticles(
                BigJarParticles.JAR_POOF_PARTICLE.get(),
                center.x,
                center.y,
                center.z,
                15,
                bounds.getXsize()/3,
                bounds.getYsize()/3,
                bounds.getZsize()/3,
                POOF_PARTICLE_SPEED
        );

        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), BigJarSounds.BIG_JAR_USE_SOUND.value(), SoundSource.PLAYERS, 1.0f, RNG.RANDOM.nextFloat() * 0.4f + 1.2f);
        player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN_TICKS);
        return super.useOn(context);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        if (!(stack.get(DataComponents.ENTITY_DATA) instanceof CustomData storedEntity) || storedEntity.isEmpty()) {
            return stack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
        }
        if (storedEntity.contains("CustomName")) {
            return Component.translatable(this.getDescriptionId() + ".full", Component.literal(storedEntity.copyTag().getString("CustomName")).withStyle(ChatFormatting.ITALIC));
        } else {
            EntityType<?> entityType = storedEntity.read(ENTITY_TYPE_FIELD_CODEC).result().orElse(EntityType.COD); // Because whatever atp
            return Component.translatable(this.getDescriptionId() + ".full", Component.translatable(entityType.getDescriptionId()));
        }
    }

    public static boolean isLubricantRequired(LivingEntity entity) {
        if (entity.getType().is(REQUIRES_LUBRICANT_TO_JAR_TAG)) return true;
        if (entity instanceof Monster) return true;
        return !entity.getType().getCategory().isFriendly();
    }

    private static boolean isUnjarrable(LivingEntity entity) {
        if (!entity.getType().canSummon()) return true;
        if (SpawnEggItem.byId(entity.getType()) == null) return true;
        return entity.getType().is(UNJARRABLE_TAG);
    }
}
