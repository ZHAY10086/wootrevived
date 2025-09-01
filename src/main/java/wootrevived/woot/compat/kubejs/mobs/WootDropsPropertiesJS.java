package wootrevived.woot.compat.kubejs.mobs;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.woot.util.helper.EnchantmentHelper;

import java.util.Arrays;
import java.util.List;

public class WootDropsPropertiesJS {
    private final WootDropsProperties properties;

    public WootDropsProperties getRawProperties(){
        return properties;
    }

    public WootDropsPropertiesJS(WootDropsProperties properties) {
        this.properties = properties;
    }

    public int getEnchantmentLevel(String enchantmentId){
        return getEnchantmentLevel(enchantmentId, "MAIN_HAND");
    }

    public int getEnchantmentLevel(String enchantmentId, String handId) {
        ResourceLocation id = ResourceLocation.tryParse(enchantmentId);
        Enchantment enchantment = BuiltInRegistries.ENCHANTMENT.get(id);
        if(enchantment == null)
            return 0;

        InteractionHand hand = InteractionHand.MAIN_HAND;
        try {
            hand = InteractionHand.valueOf(handId);
        } catch(IllegalArgumentException ignored){}

        ItemStack stack = switch(hand){
            case MAIN_HAND -> properties.getMainHandItem();
            case OFF_HAND -> properties.getOffHandItem();
        };

        if(!EnchantmentHelper.isEnchanted(stack))
            return 0;

        return stack.getEnchantmentLevel(enchantment);
    }

    public OutputItem[] getItemDrops(){
        List<ItemStack> stacks = properties.getItemDrops();
        return stacks.stream().map(OutputItem::of).toArray(OutputItem[]::new);
    }

    public void setItemDrops(OutputItem[] items){
        List<ItemStack> stacks = properties.getItemDrops();
        stacks.clear();
        stacks.addAll(Arrays.stream(items).map(o -> o.item).toList());
    }

    public FluidStackJS[] getFluidDrops(){
        List<FluidStack> stacks = properties.getFluidDrops();
        return stacks.stream()
                .map(f -> dev.architectury.fluid.FluidStack.create(f::getFluid, f.getAmount(), f.getTag()))
                .map(FluidStackJS::of)
                .toArray(FluidStackJS[]::new);
    }

    public void setFluidDrops(FluidStackJS[] items){
        List<FluidStack> stacks = properties.getFluidDrops();
        stacks.clear();
        stacks.addAll(
                Arrays.stream(items)
                .map(FluidStackJS::getFluidStack)
                .map(f -> new FluidStack(f.getFluid(), (int)f.getAmount(), f.getTag()))
                .toList()
        );
    }

    public String getFactoryTier(){
        return properties.getFactoryTier().getSerializedName();
    }

    public RandomSource getRandom() {
        return properties.getRandom();
    }

    public int getExperience(){
        return properties.getExperience();
    }

    public void setExperience(int experience){
        properties.setExperience(experience);
    }

    public float getLuck(){
        return properties.getLuck();
    }

    public boolean doSimulateChargedCreeper(){
        return properties.doSimulateChargedCreeper();
    }

    public boolean isEnderDragonAlreadyKilled(){
        return properties.isEnderDragonAlreadyKilled();
    }

    public boolean isInFire(){
        return properties.isInFire();
    }
}
