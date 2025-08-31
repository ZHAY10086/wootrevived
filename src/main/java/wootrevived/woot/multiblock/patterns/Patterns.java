package wootrevived.woot.multiblock.patterns;

import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Patterns {
    public static Pattern TIER_1 = new Tier1();
    public static Pattern TIER_2 = new Tier2();
    public static Pattern TIER_3 = new Tier3();
    public static Pattern TIER_4 = new Tier4();
    public static Pattern TIER_5 = new Tier5();

    private static final Map<Direction, List<Pattern.PatternBlock>> fakeSpawnersBlocks = generateDirectionalBlocks(BlocksRegistry.FAKE_SPAWNER_BLOCK.get());
    private static final Map<Direction, List<Pattern.PatternBlock>> upgradeBlocks = generateDirectionalBlocks(BlocksRegistry.FACTORY_UPGRADE_BLOCK.get());
    private static final Map<Direction, List<Pattern.PatternBlock>> importBlocks = generateDirectionalBlocks(BlocksRegistry.IMPORT_BLOCK.get());
    private static final Map<Direction, List<Pattern.PatternBlock>> exportBlocks = generateDirectionalBlocks(BlocksRegistry.EXPORT_BLOCK.get());
    private static final Map<Direction, List<Pattern.PatternBlock>> cellBlocks = generateDirectionalBlocks(BlocksRegistry.COPPER_CELL_BLOCK.get());

    public static List<Pattern.PatternBlock> getFakeSpawnersBlocks(Direction facing) {
        return fakeSpawnersBlocks.get(facing);
    }

    public static List<Pattern.PatternBlock> getUpgradeBlocks(Direction facing) {
        return upgradeBlocks.get(facing);
    }

    public static Pattern.PatternBlock getImportBlock(Direction facing) {
        return importBlocks.get(facing).get(0);
    }

    public static Pattern.PatternBlock getExportBlock(Direction facing) {
        return exportBlocks.get(facing).get(0);
    }

    public static Pattern.PatternBlock getCellBlock(Direction facing) {
        return cellBlocks.get(facing).get(0);
    }

    private static Map<Direction, List<Pattern.PatternBlock>> generateDirectionalBlocks(Block block){
        Map<Direction, List<Pattern.PatternBlock>> fakeSpawners = Maps.newEnumMap(Direction.class);
        for(Direction direction : Direction.values()){
            if(direction == Direction.UP || direction == Direction.DOWN) continue;
            fakeSpawners.put(direction, getBlocks(direction, block));
        }
        return fakeSpawners;
    }

    private static List<Pattern.PatternBlock> getBlocks(Direction direction, Block block){
        List<Pattern.PatternBlock> blocks = new ArrayList<>();

        getBlocksForPattern(blocks, TIER_1, direction, block);
        getBlocksForPattern(blocks, TIER_2, direction, block);
        getBlocksForPattern(blocks, TIER_3, direction, block);
        getBlocksForPattern(blocks, TIER_4, direction, block);
        getBlocksForPattern(blocks, TIER_5, direction, block);

        return blocks;
    }

    private static void getBlocksForPattern(List<Pattern.PatternBlock> blocks, Pattern pattern, Direction direction, Block block){
        main:
        for(Pattern.PatternBlock patternBlock : pattern.patterns.get(direction)){
            for(Block b : patternBlock.blocks){
                if(b == block){
                    blocks.add(patternBlock);
                    continue main;
                }
            }
        }
    }

    private static final int height = generateHeight();

    public static int getHeight(){
        return height;
    }

    private static int generateHeight(){
        int heightTier1 = generateHeightForTier(TIER_1);
        int heightTier2 = generateHeightForTier(TIER_2);
        int heightTier3 = generateHeightForTier(TIER_3);
        int heightTier4 = generateHeightForTier(TIER_4);
        int heightTier5 = generateHeightForTier(TIER_5);

        return Math.max(heightTier1, Math.max(heightTier2, Math.max(heightTier3, Math.max(heightTier4, heightTier5))));
    }

    private static int generateHeightForTier(Pattern tier){
        List<Pattern.PatternBlock> pattern = tier.patterns.get(Direction.NORTH);

        int minHeight = 0;
        int maxHeight = 0;

        for(Pattern.PatternBlock block : pattern){
            if(block.pos.getY() < minHeight) minHeight = block.pos.getY();
            if(block.pos.getY() > maxHeight) maxHeight = block.pos.getY();
        }

        return maxHeight - minHeight;
    }
}
