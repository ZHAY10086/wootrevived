package wootrevived.woot.compat.kubejs.components;

import com.google.gson.JsonElement;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentWithParent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

public class WootComponents {
    public static RecipeComponent<InputFluid> INPUT_FLUID = new RecipeComponentWithParent<>() {
        @Override
        public RecipeComponent<InputFluid> parentComponent() {
            return FluidComponents.INPUT;
        }

        @Override
        public JsonElement write(RecipeJS recipe, InputFluid value){
            FluidStackJS stackJS = (FluidStackJS)value;
            CompoundTag tag = stackJS.getFluidStack().write(new CompoundTag());
            return Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
        }
    };

    public static RecipeComponent<OutputFluid> OUTPUT_FLUID = new RecipeComponentWithParent<>() {
        @Override
        public RecipeComponent<OutputFluid> parentComponent() {
            return FluidComponents.OUTPUT;
        }

        @Override
        public JsonElement write(RecipeJS recipe, OutputFluid value){
            FluidStackJS stackJS = (FluidStackJS)value;
            CompoundTag tag = stackJS.getFluidStack().write(new CompoundTag());
            return Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
        }
    };

    public static RecipeComponent<OutputItem> OUTPUT_ITEM = new RecipeComponentWithParent<>() {
        @Override
        public RecipeComponent<OutputItem> parentComponent() {
            return ItemComponents.OUTPUT;
        }

        @Override
        public JsonElement write(RecipeJS recipe, OutputItem value){
            CompoundTag tag = value.item.save(new CompoundTag());
            return Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
        }
    };
}
