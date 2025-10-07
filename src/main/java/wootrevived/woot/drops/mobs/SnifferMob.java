package wootrevived.woot.drops.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootFactoryMobRegistration;

import java.util.List;

public class SnifferMob extends WootFactoryMob<Sniffer> {
    public SnifferMob(EntityType<Sniffer> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public void modifyDrops(Phase phase, WootDropsProperties properties){
        if(!phase.isBeforeDropCallback())
            return;

        ServerLevel level = properties.getLevel();
        LootTable lootTable = level.getServer().getLootData().getLootTable(BuiltInLootTables.SNIFFER_DIGGING);
        LootParams lootParams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, BlockPos.ZERO.getCenter())
                .withParameter(LootContextParams.THIS_ENTITY, properties.getEntity())
                .create(LootContextParamSets.GIFT);
        List<ItemStack> items = lootTable.getRandomItems(lootParams);

        properties.getItemDrops().addAll(items);
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new SnifferMob(EntityType.SNIFFER, new Properties()));
    }
}
