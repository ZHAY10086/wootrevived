package wootrevived.woot.util.helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class RecipeHelper {
    public static class IngredientInput {
        public static @NotNull JsonElement toJson(Ingredient ingredient){
            return ingredient.toJson();
        }

        public static @NotNull Ingredient fromJson(JsonObject parent, String key) {
            return Ingredient.fromJson(parent.get(key));
        }

        public static void toNetwork(FriendlyByteBuf buffer, Ingredient ingredient){
            ingredient.toNetwork(buffer);
        }

        public static @NotNull Ingredient fromNetwork(FriendlyByteBuf buffer){
            return Ingredient.fromNetwork(buffer);
        }
    }

    public static class FluidInput {
        public static @NotNull JsonElement toJson(FluidStack fluid){
            CompoundTag tag = fluid.writeToNBT(new CompoundTag());
            return Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
        }

        public static @NotNull FluidStack fromJson(JsonObject parent, String key) {
            Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, parent.get(key));
            return FluidStack.loadFluidStackFromNBT((CompoundTag) tag);
        }

        public static void toNetwork(FriendlyByteBuf buffer, FluidStack fluid){
            buffer.writeFluidStack(fluid);
        }

        public static @NotNull FluidStack fromNetwork(FriendlyByteBuf buffer){
            return buffer.readFluidStack();
        }
    }

    public static class ItemOutput {
        public static @NotNull JsonElement toJson(ItemStack item){
            CompoundTag tag = item.save(new CompoundTag());
            return Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
        }

        public static @NotNull ItemStack fromJson(JsonObject parent, String key) {
            JsonElement elem = parent.get(key);
            Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, elem);
            return ItemStack.of((CompoundTag) tag);
        }

        public static void toNetwork(FriendlyByteBuf buffer, ItemStack item){
            buffer.writeItem(item);
        }

        public static @NotNull ItemStack fromNetwork(FriendlyByteBuf buffer){
            return buffer.readItem();
        }
    }

    public static class FluidOutput {
        public static @NotNull JsonElement toJson(FluidStack fluid){
            CompoundTag tag = fluid.writeToNBT(new CompoundTag());
            return Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
        }

        public static @NotNull FluidStack fromJson(JsonObject parent, String key) {
            JsonElement elem = parent.get(key);
            Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, elem);
            return FluidStack.loadFluidStackFromNBT((CompoundTag) tag);
        }

        public static void toNetwork(FriendlyByteBuf buffer, FluidStack fluid){
            buffer.writeFluidStack(fluid);
        }

        public static @NotNull FluidStack fromNetwork(FriendlyByteBuf buffer){
            return buffer.readFluidStack();
        }
    }
}
