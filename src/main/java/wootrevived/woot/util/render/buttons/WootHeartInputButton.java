package wootrevived.woot.util.render.buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.render.WootButton;

public class WootHeartInputButton extends WootButton {
    protected final OnPress onPress;
    public final int index;
    public boolean isViewActive = false;

    public WootHeartInputButton(int index, int x, int y, OnPress onPress) {
        super(x, y, 38, 38);
        this.onPress = onPress;
        this.index = index;
    }

    public int getFakeSpawnerIndex(){
        return index;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        if(isViewActive || (active && isHovered())) {
            gui.fill(getX() + 3, getY() + 3, getX() + getWidth() - 3, getY() + getHeight() - 3, 100, 0x80FFFFFF);
        }
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(WootHeartInputButton button);
    }
}
