package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wootrevived.woot.Woot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Woot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = generator.getPackOutput();
        if(event.includeServer()) {
            generator.addProvider(true, new Recipes(packOutput));
            BlockTagsProvider blockTagsProvider = new BlockTagsGen(packOutput, lookupProvider, existingFileHelper);
            generator.addProvider(true, blockTagsProvider);
            generator.addProvider(true, new ItemTagsGen(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
            generator.addProvider(true, new DropSimulatorDim(packOutput, lookupProvider));
            generator.addProvider(true, new Advancements(packOutput, lookupProvider, existingFileHelper));
            generator.addProvider(true, new LootTableProvider(packOutput, Set.of(), List.of(
                    new LootTableProvider.SubProviderEntry(LootTables::new, LootContextParamSets.BLOCK)
            )));
        }
        if(event.includeClient()) {
            generator.addProvider(true, new Blocks(packOutput, existingFileHelper));
            generator.addProvider(true, new Items(packOutput, existingFileHelper));
            generator.addProvider(true, new Atlas(packOutput, existingFileHelper));
            generator.addProvider(true, new Languages(packOutput));
        }
    }
}
