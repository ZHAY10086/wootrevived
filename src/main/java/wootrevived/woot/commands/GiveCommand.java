package wootrevived.woot.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.events.InitServer;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.helper.SerializeEntityNBTHelper;

public class GiveCommand {
    private static final SuggestionProvider<CommandSourceStack> suggestionProvider = (commandContext, suggestionsBuilder) -> {
        return SharedSuggestionProvider.suggestResource(InitServer.mobLocations, suggestionsBuilder);
    };

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("give")
                .requires(cs -> cs.hasPermission(2))
                .then(
                    Commands.argument("target", EntityArgument.player())
                            .then(
                                    Commands.argument("entity", ResourceLocationArgument.id()).suggests(suggestionProvider)
                                            .executes(ctx -> giveItem(
                                                    ctx.getSource(),
                                                    EntityArgument.getPlayer(ctx, "target"),
                                                    ResourceLocationArgument.getId(ctx, "entity"),
                                                    new CompoundTag()
                                            ))
                                            .then(
                                                    Commands.argument("nbt", CompoundTagArgument.compoundTag())
                                                            .executes(ctx -> giveItem(
                                                                    ctx.getSource(),
                                                                    EntityArgument.getPlayer(ctx, "target"),
                                                                    ResourceLocationArgument.getId(ctx, "entity"),
                                                                    CompoundTagArgument.getCompoundTag(ctx, "nbt")
                                                            ))
                                            )
                            )
                );
    }

    private static int giveItem(CommandSourceStack source, ServerPlayer target, ResourceLocation resourceLocation, CompoundTag tag) {
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(resourceLocation);
        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(entityType);

        tag.putString("id", resourceLocation.toString());

        EntityType.create(tag, source.getLevel())
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast)
                .ifPresent(entity -> {
                    CompoundTag mobTag = mob.saveTag(SerializeEntityNBTHelper.serialize(entity), source.getLevel().registryAccess());
                    ItemStack fakeSpawner = FakeSpawnerBlockEntity.getItemStack(mobTag);
                    ItemHandlerHelper.giveItemToPlayer(target, fakeSpawner);
                });

        return 1;
    }
}
