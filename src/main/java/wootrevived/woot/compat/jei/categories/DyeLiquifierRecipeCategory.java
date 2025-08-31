package wootrevived.woot.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.client.render.dye_liquifier.DyeLiquifierContainerScreen;
import wootrevived.woot.compat.jei.WootJeiPluginTypes;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.events.client.GlobalClientTicker;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.render.WootContainerScreen;

public class DyeLiquifierRecipeCategory implements IRecipeCategory<DyeLiquifierRecipe> {
    private static IDrawable icon;

    private static final int GUI_WIDTH = 119;
    private static final int GUI_HEIGHT = 56;

    private static final int ENERGY_X = 0;
    private static final int ENERGY_Y = 0;

    private static final int INPUT_SLOT_X = 21;
    private static final int INPUT_SLOT_Y = 19;

    private static final int COLOR_BAR_X = 63;

    private static final int RED_COLOR_BAR_Y = 5;
    private static final int YELLOW_COLOR_BAR_Y = 17;
    private static final int BLUE_COLOR_BAR_Y = 29;
    private static final int WHITE_COLOR_BAR_Y = 41;

    private static final int PROGRESS_X = 42;
    private static final int PROGRESS_Y = 7;

    public DyeLiquifierRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemLike(BlocksRegistry.DYE_LIQUIFIER_BLOCK.get());
    }

    @Override
    public void draw(DyeLiquifierRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics gui, double mouseX, double mouseY) {
        int totalProgressTick = recipe.getEnergy() / DyeLiquifierConfig.ENERGY_PROCESS_TRANSFER.get();
        int progress = (GlobalClientTicker.tickCounter % totalProgressTick) * 100 / totalProgressTick;

        WootContainerScreen.renderVanillaSlot(gui, INPUT_SLOT_X, INPUT_SLOT_Y);
        WootContainerScreen.renderColorBarBg(gui, COLOR_BAR_X, RED_COLOR_BAR_Y, DyeColor.RED.getTextureDiffuseColors());
        WootContainerScreen.renderColorBarBg(gui, COLOR_BAR_X, YELLOW_COLOR_BAR_Y, DyeColor.YELLOW.getTextureDiffuseColors());
        WootContainerScreen.renderColorBarBg(gui, COLOR_BAR_X, BLUE_COLOR_BAR_Y, DyeColor.BLUE.getTextureDiffuseColors());
        WootContainerScreen.renderColorBarBg(gui, COLOR_BAR_X, WHITE_COLOR_BAR_Y, DyeColor.WHITE.getTextureDiffuseColors());
        WootContainerScreen.renderEnergyBg(gui, ENERGY_X, ENERGY_Y);
        DyeLiquifierContainerScreen.renderProgressBg(gui, PROGRESS_X, PROGRESS_Y);

        WootContainerScreen.renderEnergy(gui, ENERGY_X, ENERGY_Y, recipe.getEnergy(), DyeLiquifierConfig.ENERGY_CAPACITY.get());
        WootContainerScreen.renderColorBar(gui, COLOR_BAR_X, RED_COLOR_BAR_Y, recipe.getRed(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.RED.getTextureDiffuseColors());
        WootContainerScreen.renderColorBar(gui, COLOR_BAR_X, YELLOW_COLOR_BAR_Y, recipe.getYellow(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.YELLOW.getTextureDiffuseColors());
        WootContainerScreen.renderColorBar(gui, COLOR_BAR_X, BLUE_COLOR_BAR_Y, recipe.getBlue(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.BLUE.getTextureDiffuseColors());
        WootContainerScreen.renderColorBar(gui, COLOR_BAR_X, WHITE_COLOR_BAR_Y, recipe.getWhite(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.WHITE.getTextureDiffuseColors());
        DyeLiquifierContainerScreen.renderProgress(gui, PROGRESS_X, PROGRESS_Y, progress);

        WootContainerScreen._renderEnergyTooltip(gui, (int)mouseX, (int)mouseY, ENERGY_X, ENERGY_Y, recipe.getEnergy(), DyeLiquifierConfig.ENERGY_CAPACITY.get(), false, false);
        WootContainerScreen._renderColorBarTooltip(gui, (int)mouseX, (int)mouseY, COLOR_BAR_X, RED_COLOR_BAR_Y, recipe.getRed(), 0, Component.translatable("info.woot_revived.dye.red"), false, false);
        WootContainerScreen._renderColorBarTooltip(gui, (int)mouseX, (int)mouseY, COLOR_BAR_X, YELLOW_COLOR_BAR_Y, recipe.getYellow(), 0, Component.translatable("info.woot_revived.dye.yellow"), false, false);
        WootContainerScreen._renderColorBarTooltip(gui, (int)mouseX, (int)mouseY, COLOR_BAR_X, BLUE_COLOR_BAR_Y, recipe.getBlue(), 0, Component.translatable("info.woot_revived.dye.blue"), false, false);
        WootContainerScreen._renderColorBarTooltip(gui, (int)mouseX, (int)mouseY, COLOR_BAR_X, WHITE_COLOR_BAR_Y, recipe.getWhite(), 0, Component.translatable("info.woot_revived.dye.white"), false, false);
        DyeLiquifierContainerScreen._renderProgressTooltip(gui, (int)mouseX, (int)mouseY, PROGRESS_X, PROGRESS_Y, progress, Math.max(0F, totalProgressTick / 20F), DyeLiquifierConfig.ENERGY_PROCESS_TRANSFER.get(), false);
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
    public @NotNull RecipeType<DyeLiquifierRecipe> getRecipeType() {
        return WootJeiPluginTypes.DYE_LIQUIFIER_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("gui.woot_revived.dye_liquifier.name");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DyeLiquifierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(INPUT_SLOT_X + 1, INPUT_SLOT_Y + 1)
                .addIngredients(recipe.getInputIngredient());

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addFluidStack(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get())
                .addItemLike(FluidsRegistry.PURE_DYE_FLUID_BUCKET.get());
    }
}
