package wootrevived.woot.client.model.factory_upgrade;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import net.minecraftforge.registries.RegistryObject;
import wootrevived.api.WootUpgradeItem;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class FactoryUpgradeUnbakedModel implements IUnbakedGeometry<FactoryUpgradeUnbakedModel> {
    private final ResourceLocation parentLocation;

    private BlockModel blockModel;

    public FactoryUpgradeUnbakedModel(ResourceLocation parentLocation) {
        this.parentLocation = parentLocation;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation){
        TextureAtlasSprite factory = spriteGetter.apply(context.getMaterial("north"));

        FactoryUpgradeBakedModel.Builder builder = new FactoryUpgradeBakedModel.Builder(context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d(), context.getTransforms(), overrides)
                .addParticle("", factory);

        addQuads(context, builder, spriteGetter, modelState, modelLocation, factory, "");

        for(RegistryObject<? extends WootUpgradeItem> upgradeItem : UpgradeItemsRegistry.getValues()){
            String name = UpgradeItemsRegistry.getNameFromItem(upgradeItem);

            TextureAtlasSprite texture = spriteGetter.apply(new Material(
                    InventoryMenu.BLOCK_ATLAS,
                    Woot.location("block/upgrade_item_" + name)
            ));

            builder.addParticle(name, texture);

            addQuads(context, builder, spriteGetter, modelState, modelLocation, texture, name);
        }

        return builder.build();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
    {
        UnbakedModel unbakedModel = modelGetter.apply(parentLocation);
        unbakedModel.resolveParents(modelGetter);
        if(!(unbakedModel instanceof BlockModel model))
            throw new RuntimeException("Baking factory_upgrade parent not a block");
        this.blockModel = model;
    }

    @SuppressWarnings("deprecation")
    protected void addQuads(IGeometryBakingContext context, FactoryUpgradeBakedModel.Builder modelBuilder, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation, TextureAtlasSprite customSprite, String upgrade)
    {
        var postTransform = QuadTransformers.empty();
        var rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity())
            postTransform = UnbakedGeometryHelper.applyRootTransform(modelState, rootTransform);

        for (BlockElement element : blockModel.getElements())
        {
            for (Direction direction : element.faces.keySet())
            {
                var face = element.faces.get(direction);
                var sprite = direction != Direction.DOWN && direction != Direction.UP ? customSprite : spriteGetter.apply(context.getMaterial(face.texture));
                var quad = BlockModel.bakeFace(element, face, sprite, direction, modelState, modelLocation);
                postTransform.processInPlace(quad);

                modelBuilder.addFace(modelState.getRotation().rotateTransform(face.cullForDirection), upgrade, quad);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Loader implements IGeometryLoader<FactoryUpgradeUnbakedModel> {
        public static final Loader INSTANCE = new Loader();

        @Override
        public FactoryUpgradeUnbakedModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext){
            if(!jsonObject.has("parent"))
                throw new RuntimeException("Model factory_upgrade don't have parent");

            return new FactoryUpgradeUnbakedModel(ResourceLocation.tryParse(jsonObject.get("parent").getAsString()));
        }
    }
}
