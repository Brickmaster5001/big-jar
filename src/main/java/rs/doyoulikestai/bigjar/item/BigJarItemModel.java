package rs.doyoulikestai.bigjar.item;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.atlas.sources.Unstitcher;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import org.joml.Vector3f;
import org.slf4j.Logger;
import rs.doyoulikestai.bigjar.BigJar;
import rs.doyoulikestai.bigjar.datagen.BigJarRecipeProvider;
import rs.doyoulikestai.bigjar.util.PlayerMessenger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class BigJarItemModel implements ItemModel {
    private final List<BakedQuad> quads;
    private final Supplier<Vector3f[]> extents;
    private final ModelRenderProperties properties;

    private static final HashMap<EntityType<?>, JarItemSpawnEggTexture> spawnEggTextures = new HashMap<EntityType<?>, JarItemSpawnEggTexture>();

    private static Integer generateCount = 0;

    public BigJarItemModel(List<BakedQuad> quads, ModelRenderProperties properties) {
        this.quads = quads;
        this.properties = properties;
        extents = Suppliers.memoize(() -> computeExtents(this.quads));
    }

    public static Vector3f[] computeExtents(List<BakedQuad> quads) {
        Set<Vector3f> vertices = new HashSet<>();
        for (BakedQuad quad : quads) {
            FaceBakery.extractPositions(quad.vertices(), vertices::add);
        }
        return vertices.toArray(Vector3f[]::new);
    }

    public static JarItemSpawnEggTexture generateModelFromSpawnEgg(EntityType<?> entityType) {
        SpawnEggItem spawnEggItem = SpawnEggItem.byId(entityType);
        ResourceLocation itemModelId = spawnEggItem.components().get(DataComponents.ITEM_MODEL);
        ResourceLocation texture = itemModelId.withPath(s -> "item/" + s);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);

        Vector3f fromVecN = new Vector3f(4.0f, 4.0f, 7.501f);
        Vector3f toVecN = new Vector3f(12.0f, 12.0f, 7.501f);

        QuadCollection.Builder quads = new QuadCollection.Builder();
        quads.addUnculledFace(FaceBakery.bakeQuad( // North Face
                fromVecN.sub(0.0f, 2.0f, 0.0f),
                toVecN.sub(0.0f, 2.0f, 0.0f),
                new BlockElementFace(null, 0, "", new BlockElementFace.UVs(
                        fromVecN.x, fromVecN.y+3, toVecN.x, toVecN.y+3
                ), Quadrant.R0),
                sprite,
                Direction.NORTH,
                BlockModelRotation.X0_Y0,
                null,
                true,
                0
        ));

        Vector3f fromVecS = new Vector3f(4.0f, 4.0f, 8.499f);
        Vector3f toVecS = new Vector3f(12.0f, 12.0f, 8.499f);

        quads.addUnculledFace(FaceBakery.bakeQuad( // South Face
                fromVecS.sub(0.0f, 2.0f, 0.0f),
                toVecS.sub(0.0f, 2.0f, 0.0f),
                new BlockElementFace(null, 0, "", new BlockElementFace.UVs(
                        fromVecS.x, fromVecS.y+3, toVecS.x, toVecS.y+3
                ), Quadrant.R0),
                sprite,
                Direction.SOUTH,
                BlockModelRotation.X0_Y0,
                null,
                true,
                0
        ));

        generateCount++;
        //PlayerMessenger.broadcastActionbar(generateCount.toString() + " Generated new item model for entity type " + entityType.getDescriptionId());

        List<BakedQuad> bakedQuadList = quads.build().getAll();
        return new JarItemSpawnEggTexture(bakedQuadList);
    }

    public static JarItemSpawnEggTexture getOrGenerate(EntityType<?> entityType) {
        if (!spawnEggTextures.containsKey(entityType)) {
            spawnEggTextures.put(entityType, generateModelFromSpawnEgg(entityType));
        }
        return spawnEggTextures.get(entityType);
    }

    @Override
    public void update(
            ItemStackRenderState itemStackRenderState,
            ItemStack itemStack,
            ItemModelResolver resolver,
            ItemDisplayContext context,
            @Nullable ClientLevel level,
            @Nullable LivingEntity entity,
            int p_388300_
    ) {
        itemStackRenderState.appendModelIdentityElement(this);
        ItemStackRenderState.LayerRenderState layer = itemStackRenderState.newLayer();
        if (itemStack.hasFoil()) {
            layer.setFoilType(ItemStackRenderState.FoilType.STANDARD);
            itemStackRenderState.setAnimated();
            itemStackRenderState.appendModelIdentityElement(ItemStackRenderState.FoilType.STANDARD);
        }

        layer.setExtents(extents);
        layer.setRenderType(ItemBlockRenderTypes.getRenderType(itemStack));
        properties.applyToLayer(layer, context);

        List<BakedQuad> quadList = layer.prepareQuadList();
        if (!(itemStack.get(DataComponents.ENTITY_DATA) instanceof CustomData storedEntity) || storedEntity.isEmpty()) {
            // Do empty texture
            itemStackRenderState.appendModelIdentityElement("empty_jar");
            quadList.addAll(this.quads);
        } else {
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(storedEntity.parseEntityId());
            itemStackRenderState.appendModelIdentityElement(entityType.getDescriptionId());
            List<BakedQuad> bakedQuadList = BigJarItemModel.getOrGenerate(entityType).bakedQuads;
            quadList.addAll(this.quads);
            quadList.addAll(bakedQuadList);
        }
    }

    public record Unbaked(ResourceLocation model) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                ResourceLocation.CODEC.fieldOf("model").forGetter(Unbaked::model)
        ).apply(i, Unbaked::new));

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.markDependency(model);
        }

        @Override
        public ItemModel bake(BakingContext context) {
            ModelBaker baker = context.blockModelBaker();
            ResolvedModel model = baker.getModel(this.model);
            TextureSlots slots = model.getTopTextureSlots();
            List<BakedQuad> baseQuads = model.bakeTopGeometry(slots, baker, BlockModelRotation.X0_Y0).getAll();
            ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(baker, model, slots);
            return new BigJarItemModel(baseQuads, properties);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }

    public record JarItemSpawnEggTexture(List<BakedQuad> bakedQuads) {}
}
