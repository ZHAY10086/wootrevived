package wootrevived.woot.util.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WootRecipeSerializer<T extends WootRecipe> implements RecipeSerializer<T> {
    protected final IFactory<T> factory;

    public WootRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull T fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        int energy = GsonHelper.getAsInt(json, "energy", 0);

        List<Ingredient> inputItems = readInputIngredientsJson(json);
        List<FluidStack> inputFluids = readInputFluidsJson(json);
        ItemStack outputItem = readOutputItemJson(json);
        FluidStack outputFluid = readOutputFluidJson(json);

        return factory.create(recipeId, energy, inputItems, inputFluids, outputItem, outputFluid);
    }

    public static List<Ingredient> readInputIngredientsJson(JsonObject json) {
        if(!json.has("inputIngredients"))
            return List.of();

        List<Ingredient> inputItems = new ArrayList<>();

        for(JsonElement ingredient : GsonHelper.getAsJsonArray(json, "inputIngredients"))
            inputItems.add(Ingredient.fromJson(ingredient));

        return inputItems;
    }

    public static List<FluidStack> readInputFluidsJson(JsonObject json) {
        if(!json.has("inputFluids"))
            return List.of();

        List<FluidStack> inputFluids = new ArrayList<>();

        for(JsonElement fluidElem : GsonHelper.getAsJsonArray(json, "inputFluids")){
            Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, fluidElem);
            inputFluids.add(FluidStack.loadFluidStackFromNBT((CompoundTag) tag));
        }

        return inputFluids;
    }

    public static ItemStack readOutputItemJson(JsonObject json) {
        if(!json.has("outputItem"))
            return null;

        JsonElement itemElem = json.get("outputItem");
        Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, itemElem);
        return ItemStack.of((CompoundTag) tag);
    }

    public static FluidStack readOutputFluidJson(JsonObject json) {
        if(!json.has("outputFluid"))
            return null;

        JsonElement fluidElem = json.get("outputFluid");
        Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, fluidElem);
        return FluidStack.loadFluidStackFromNBT((CompoundTag) tag);
    }

    @Override
    public @Nullable T fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();

        int lenInputItems = buffer.readVarInt();
        ArrayList<Ingredient> inputItems = new ArrayList<>(lenInputItems);
        for(int i = 0; i < lenInputItems; i++){
            inputItems.add(Ingredient.fromNetwork(buffer));
        }

        int lenInputFluids = buffer.readVarInt();
        ArrayList<FluidStack> inputFluids = new ArrayList<>(lenInputFluids);
        for(int i = 0; i < lenInputFluids; i++){
            inputFluids.add(buffer.readFluidStack());
        }

        ItemStack outputItem = null;
        if(buffer.readBoolean()){
            outputItem = buffer.readItem();
        }

        FluidStack outputFluid = null;
        if(buffer.readBoolean()){
            outputFluid = buffer.readFluidStack();
        }

        return factory.create(recipeId, energy, inputItems, inputFluids, outputItem, outputFluid);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeVarInt(recipe.energy);

        int lenInputItems = recipe.inputItems.size();
        buffer.writeVarInt(lenInputItems);
        for(int i = 0; i < lenInputItems; ++i){
            recipe.inputItems.get(i).toNetwork(buffer);
        }

        int lenInputFluids = recipe.inputFluids.size();
        buffer.writeVarInt(lenInputFluids);
        for(int i = 0; i < lenInputFluids; ++i){
            buffer.writeFluidStack(recipe.inputFluids.get(i));
        }

        buffer.writeBoolean(recipe.outputItem != null);
        if(recipe.outputItem != null){
            buffer.writeItem(recipe.outputItem);
        }

        buffer.writeBoolean(recipe.outputFluid != null);
        if(recipe.outputFluid != null){
            buffer.writeFluidStack(recipe.outputFluid);
        }
    }

    public interface IFactory<T extends WootRecipe> {
        T create(ResourceLocation recipeId, int energy, List<Ingredient> inputItems, List<FluidStack> inputFluids, ItemStack outputItem, FluidStack outputFluid);
    }
}
