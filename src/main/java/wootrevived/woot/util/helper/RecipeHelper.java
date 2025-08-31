package wootrevived.woot.util.helper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class RecipeHelper {
    public static class IngredientInput {
        public static void toNetwork(FriendlyByteBuf buffer, Ingredient ingredient){
            ingredient.toNetwork(buffer);
        }

        public static @NotNull Ingredient fromNetwork(FriendlyByteBuf buffer){
            return Ingredient.fromNetwork(buffer);
        }
    }

    public static class FluidInput {
        public static void toNetwork(FriendlyByteBuf buffer, FluidStack fluid){
            buffer.writeFluidStack(fluid);
        }

        public static @NotNull FluidStack fromNetwork(FriendlyByteBuf buffer){
            return buffer.readFluidStack();
        }
    }

    public static class ItemOutput {
        public static void toNetwork(FriendlyByteBuf buffer, ItemStack item){
            buffer.writeItem(item);
        }

        public static @NotNull ItemStack fromNetwork(FriendlyByteBuf buffer){
            return buffer.readItem();
        }
    }

    public static class FluidOutput {
        public static void toNetwork(FriendlyByteBuf buffer, FluidStack fluid){
            buffer.writeFluidStack(fluid);
        }

        public static @NotNull FluidStack fromNetwork(FriendlyByteBuf buffer){
            return buffer.readFluidStack();
        }
    }
}
