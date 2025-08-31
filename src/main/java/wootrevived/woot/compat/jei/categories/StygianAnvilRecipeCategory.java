package wootrevived.woot.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.compat.jei.WootJeiPluginTypes;
import wootrevived.woot.items.mob_shard.MobShardItem;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.util.render.WootContainerScreen;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class StygianAnvilRecipeCategory implements IRecipeCategory<StygianAnvilRecipe>, IRecipeSlotRichTooltipCallback {
    private static IDrawable icon;
    private static IDrawable stygian_hammer;

    private static final int GUI_WIDTH = 113;
    private static final int GUI_HEIGHT = 38;

    private static final int BASE_X = 1;
    private static final int BASE_Y = 10;

    private static final int INGREDIENT_0_X = 38;
    private static final int INGREDIENT_0_Y = 1;

    private static final int INGREDIENT_1_X = 56;
    private static final int INGREDIENT_1_Y = 1;

    private static final int INGREDIENT_2_X = 38;
    private static final int INGREDIENT_2_Y = 19;

    private static final int INGREDIENT_3_X = 56;
    private static final int INGREDIENT_3_Y = 19;

    private static final int OUTPUT_X = 94;
    private static final int OUTPUT_Y = 10;

    private static final int PLUS_X = 22;
    private static final int PLUS_Y = 12;

    private static final int HAMMER_X = 75;
    private static final int HAMMER_Y = 11;

    public StygianAnvilRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemLike(BlocksRegistry.STYGIAN_ANVIL_BLOCK.get());
        stygian_hammer = guiHelper.createDrawableItemLike(ItemsRegistry.STYGIAN_HAMMER_ITEM.get());
    }

    @Override
    public void draw(@NotNull StygianAnvilRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics gui, double mouseX, double mouseY) {
        WootContainerScreen.renderVanillaSlot(gui, BASE_X, BASE_Y);
        WootContainerScreen.renderVanillaSlot(gui, INGREDIENT_0_X, INGREDIENT_0_Y);
        WootContainerScreen.renderVanillaSlot(gui, INGREDIENT_1_X, INGREDIENT_1_Y);
        WootContainerScreen.renderVanillaSlot(gui, INGREDIENT_2_X, INGREDIENT_2_Y);
        WootContainerScreen.renderVanillaSlot(gui, INGREDIENT_3_X, INGREDIENT_3_Y);
        WootContainerScreen.renderVanillaSlot(gui, OUTPUT_X, OUTPUT_Y);

        WootContainerScreen.renderPlus(gui, PLUS_X, PLUS_Y);
        stygian_hammer.draw(gui, HAMMER_X, HAMMER_Y);
    }

    @Override
    public int getWidth() {
        return GUI_WIDTH;
    }

    @Override
    public int getHeight() {
        return GUI_HEIGHT;
    }

    @Override
    public @NotNull RecipeType<StygianAnvilRecipe> getRecipeType() {
        return WootJeiPluginTypes.STYGIAN_ANVIL_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("gui.woot_revived.anvil.name");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StygianAnvilRecipe recipe, @NotNull IFocusGroup focuses) {
        IRecipeSlotBuilder baseSlot = builder.addInputSlot(BASE_X + 1, BASE_Y + 1)
                .addRichTooltipCallback(this);

        if(recipe.getBase().getItems()[0].getItem() == ItemsRegistry.MOB_SHARD_ITEM.get()){
            ItemStack itemStack = ItemsRegistry.MOB_SHARD_ITEM.get().getDefaultInstance();
            MobShardItem.setJEIShard(itemStack);
            baseSlot.addItemStack(itemStack);
        } else {
            baseSlot.addIngredients(recipe.getBase());
        }

        IRecipeSlotBuilder[] slots = {
                builder.addInputSlot(INGREDIENT_0_X + 1, INGREDIENT_0_Y + 1),
                builder.addInputSlot(INGREDIENT_1_X + 1, INGREDIENT_1_Y + 1),
                builder.addInputSlot(INGREDIENT_2_X + 1, INGREDIENT_2_Y + 1),
                builder.addInputSlot(INGREDIENT_3_X + 1, INGREDIENT_3_Y + 1),
        };

        recipe.getFirstComplementary().ifPresent(slots[0]::addIngredients);
        recipe.getSecondComplementary().ifPresent(slots[1]::addIngredients);
        recipe.getThirdComplementary().ifPresent(slots[2]::addIngredients);
        recipe.getFourthComplementary().ifPresent(slots[3]::addIngredients);

        builder.addOutputSlot(OUTPUT_X + 1, OUTPUT_Y + 1)
               .addItemStack(recipe.getOutput());
    }

    @Override
    public void onRichTooltip(@NotNull IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
        tooltip.add(Component.translatable("info.woot_revived.base_item").setStyle(DESCRIPTION_STYLE));
    }
}
