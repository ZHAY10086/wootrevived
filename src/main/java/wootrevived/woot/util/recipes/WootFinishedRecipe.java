package wootrevived.woot.util.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class WootFinishedRecipe implements FinishedRecipe {
    protected final ResourceLocation recipeId;

    protected final ArrayList<Ingredient> inputItems = new ArrayList<>();
    protected final ArrayList<FluidStack> inputFluids = new ArrayList<>();

    protected ItemStack outputItem = null;
    protected FluidStack outputFluid = null;

    protected int energy;

    protected WootFinishedRecipe(ResourceLocation recipeId, int energy, @Nullable List<Ingredient> inputItems, @Nullable List<FluidStack> inputFluids, @Nullable ItemStack outputItem, @Nullable FluidStack outputFluid) {
        this.recipeId = recipeId;
        this.energy = energy;

        if(inputItems != null) this.inputItems.addAll(inputItems);
        if(inputFluids != null) this.inputFluids.addAll(inputFluids);
        if(outputItem != null) this.outputItem = outputItem;
        if(outputFluid != null) this.outputFluid = outputFluid;

        this.inputItems.trimToSize();
        this.inputFluids.trimToSize();
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.addProperty("energy", energy);

        JsonArray inputs = new JsonArray();

        if(!inputItems.isEmpty()){
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "ingredient");
            JsonArray ingredients = new JsonArray();
            for(Ingredient ingredient : inputItems){
                ingredients.add(ingredient.toJson());
            }
            obj.add("ingredients", ingredients);
            inputs.add(obj);
        }

        if(!inputFluids.isEmpty()){
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "fluid");
            JsonArray fluids = new JsonArray();
            for(FluidStack stack : inputFluids){
                CompoundTag tag = stack.writeToNBT(new CompoundTag());
                JsonElement fluid = Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
                fluids.add(fluid);
            }
            obj.add("fluids", fluids);
            inputs.add(obj);
        }

        json.add("inputs", inputs);

        JsonArray outputs = new JsonArray();

        if(outputItem != null){
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "item");
            CompoundTag tag = outputItem.save(new CompoundTag());
            JsonElement item = Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
            obj.add("item", item);
            outputs.add(obj);
        }

        if(outputFluid != null){
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "fluid");
            CompoundTag tag = outputFluid.writeToNBT(new CompoundTag());
            JsonElement fluid = Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
            obj.add("fluid", fluid);
            outputs.add(obj);
        }

        json.add("outputs", outputs);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public @Nullable JsonObject serializeAdvancement() {
        return null;
    }

    @Override
    public @Nullable ResourceLocation getAdvancementId() {
        return null;
    }

    @Override
    public abstract @NotNull RecipeSerializer<?> getType();
}
