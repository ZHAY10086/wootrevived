package wootrevived.woot.util.common;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public enum DyeMakeup {
    BLACK(Tags.Items.DYES_BLACK, 1F / 3F, 1F / 3F, 1F / 3F, 0F),
    RED(Tags.Items.DYES_RED, 1F, 0F, 0F, 0F),
    GREEN(Tags.Items.DYES_GREEN, 0F, 1F / 2F, 1F / 2F, 0F),
    BROWN(Tags.Items.DYES_BROWN, 3F / 4F, 1F / 8F, 1F / 8F, 0F),
    BLUE(Tags.Items.DYES_BLUE, 0F, 0F, 1F, 0F),
    PURPLE(Tags.Items.DYES_PURPLE, 1F / 2F, 0F, 1F / 2F, 0F),
    CYAN(Tags.Items.DYES_CYAN, 0F, 0F, 1F / 4F, 3F / 4F),
    LIGHT_GRAY(Tags.Items.DYES_LIGHT_GRAY, 1F / 9F, 1F / 9F, 1F / 9F, 2F / 3F),
    GRAY(Tags.Items.DYES_GRAY, 1F / 6F, 1F / 6F, 1F / 6F, 1F / 2F),
    PINK(Tags.Items.DYES_PINK, 1F / 2F, 0F, 0F, 1F / 2F),
    LIME(Tags.Items.DYES_LIME, 0F, 1F / 4F, 1F / 4F, 1F / 2F),
    YELLOW(Tags.Items.DYES_YELLOW, 0F, 1F, 0F, 0F),
    LIGHT_BLUE(Tags.Items.DYES_LIGHT_BLUE, 0F, 0F, 1F / 2F, 1F / 2F),
    MAGENTA(Tags.Items.DYES_MAGENTA, 1F / 2F, 0F, 1F / 4F, 1F / 4F),
    ORANGE(Tags.Items.DYES_ORANGE, 1F / 2F, 1F / 2F, 0F, 0F),
    WHITE(Tags.Items.DYES_WHITE, 0F, 0F, 0F, 1F);

    private final TagKey<Item> tag;
    private final float red;
    private final float yellow;
    private final float blue;
    private final float white;

    DyeMakeup(TagKey<Item> tag, float red, float yellow, float blue, float white) {
        this.tag = tag;
        this.red = red;
        this.yellow = yellow;
        this.blue = blue;
        this.white = white;
    }

    public TagKey<Item> getTag() { return tag; }
    public float getRed() { return this.red; }
    public float getYellow() { return this.yellow; }
    public float getBlue() { return this.blue; }
    public float getWhite() { return this.white; }
}
