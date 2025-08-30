package wootrevived.woot.guide.recipes;

import guideme.document.LytRect;
import guideme.document.block.LytBox;
import guideme.document.block.LytSlot;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import wootrevived.woot.client.render.fluid_infuser.FluidInfuserContainerScreen;
import wootrevived.woot.config.FluidInfuserConfig;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.util.render.guide.LytEnergy;
import wootrevived.woot.util.render.guide.LytFluid;

public class LytFluidInfuserRecipe extends LytBox {
    private static final int GUI_WIDTH = 139;
    private static final int GUI_HEIGHT = 66;

    private static final int ENERGY_X = 5;
    private static final int ENERGY_Y = 5;

    private static final int INPUT_SLOT_X = 71;
    private static final int INPUT_SLOT_Y = 10;

    private static final int INPUT_FLUID_X = 26;
    private static final int INPUT_FLUID_Y = 5;

    private static final int OUTPUT_FLUID_X = 116;
    private static final int OUTPUT_FLUID_Y = 5;

    private static final int PROGRESS_X = 48;
    private static final int PROGRESS_Y = 29;

    private final LytEnergy energy;
    private final LytFluid inputFluid;
    private final LytFluid outputFluid;
    private final LytSlot inputSlot;

    public LytFluidInfuserRecipe(FluidInfuserRecipe recipe){
        append(energy = new LytEnergy(recipe.getEnergy(), FluidInfuserConfig.ENERGY_CAPACITY.get()));
        append(inputFluid = new LytFluid(recipe.getInputFluid(), FluidInfuserConfig.INPUT_TANK_CAPACITY.get()));
        append(outputFluid = new LytFluid(recipe.getOutputFluid(), FluidInfuserConfig.OUTPUT_TANK_CAPACITY.get()));
        append(inputSlot = new LytSlot(recipe.getInputIngredient()));
    }

    @Override
    protected LytRect computeBoxLayout(LayoutContext context, int x, int y, int availableWidth) {
        energy.layout(context, x + ENERGY_X, y + ENERGY_Y, availableWidth);
        inputSlot.layout(context, x + INPUT_SLOT_X, y + INPUT_SLOT_Y, availableWidth);
        inputFluid.layout(context, x + INPUT_FLUID_X, y + INPUT_FLUID_Y, availableWidth);
        outputFluid.layout(context, x + OUTPUT_FLUID_X, y + OUTPUT_FLUID_Y, availableWidth);

        return new LytRect(x, y, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    public void render(RenderContext context) {
        context.renderPanel(bounds);

        FluidInfuserContainerScreen.renderProgressBg(context.guiGraphics(), bounds.x() + PROGRESS_X, bounds.y() + PROGRESS_Y);

        super.render(context);
    }
}
