package wootrevived.woot.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wootrevived.woot.Woot;
import wootrevived.woot.multiblock.MultiBlockFactoryEntity;
import wootrevived.woot.multiblock.patterns.Patterns;

@Mod.EventBusSubscriber(modid = Woot.MOD_ID)
public class MultiBlockUpdate {
    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        LevelAccessor accessor = event.getLevel();
        if(accessor instanceof ServerLevel level){
            BlockState state = event.getPlacedBlock();
            if(Patterns.getValidBlocks().contains(state.getBlock())) {
                updateMultiblocksPattern(level, event.getPos());
            }
        }
    }

    @SubscribeEvent
    public static void onEntityBreak(BlockEvent.BreakEvent event) {
        LevelAccessor accessor = event.getLevel();
        if(accessor instanceof ServerLevel level){
            BlockState state = event.getState();
            if(Patterns.getValidBlocks().contains(state.getBlock())){
                level.getServer().execute(() -> updateMultiblocksPattern(level, event.getPos()));
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkWatchEvent.Watch event){
        ServerLevel level = event.getLevel();
        level.getServer().execute(() -> event.getChunk().getBlockEntities().values().stream()
                .filter(MultiBlockFactoryEntity.class::isInstance)
                .map(MultiBlockFactoryEntity.class::cast)
                .forEach(entity -> entity.updatePattern(level)));
    }

    private static void updateMultiblocksPattern(ServerLevel level, BlockPos pos){
        AABB area = Patterns.getSearchAABB(pos);

        int minSectionX = SectionPos.blockToSectionCoord(area.minX);
        int minSectionZ = SectionPos.blockToSectionCoord(area.minZ);
        int maxSectionX = SectionPos.blockToSectionCoord(area.maxX);
        int maxSectionZ = SectionPos.blockToSectionCoord(area.maxZ);

        for(int sectionX = minSectionX; sectionX <= maxSectionX; sectionX++){
            for(int sectionZ = minSectionZ; sectionZ <= maxSectionZ; sectionZ++){
                LevelChunk chunk = level.getChunk(sectionX, sectionZ);
                for(BlockPos entityPos : chunk.getBlockEntitiesPos()) {
                    if(area.minX <= entityPos.getX() && area.minY <= entityPos.getY() && area.minZ <= entityPos.getZ() &&
                        area.maxX >= entityPos.getX() && area.maxY >= entityPos.getY() && area.maxZ >= entityPos.getZ()) {
                        BlockEntity entity = chunk.getBlockEntity(entityPos);
                        if(entity instanceof MultiBlockFactoryEntity multiblock)
                            multiblock.updatePattern(level);
                    }
                }
            }
        }
    }
}
