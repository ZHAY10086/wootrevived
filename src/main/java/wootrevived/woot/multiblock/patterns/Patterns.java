package wootrevived.woot.multiblock.patterns;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    private static final List<Block> validBlocks = generateValidBlocks();

    public static List<Block> getValidBlocks(){
        return validBlocks;
    }

    private static List<Block> generateValidBlocks(){
        List<Block> validBlocks = new ArrayList<>();

        getValidBlocksForPattern(validBlocks, TIER_1);
        getValidBlocksForPattern(validBlocks, TIER_2);
        getValidBlocksForPattern(validBlocks, TIER_3);
        getValidBlocksForPattern(validBlocks, TIER_4);
        getValidBlocksForPattern(validBlocks, TIER_5);

        return validBlocks;
    }

    private static void getValidBlocksForPattern(List<Block> blocks, Pattern pattern){
        for(Pattern.PatternBlock patternBlock : pattern.patterns.get(Direction.NORTH)){
            for(Block block : patternBlock.blocks){
                if(block == Blocks.AIR) continue;
                if(!blocks.contains(block))
                    blocks.add(block);
            }
        }
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

    private static final int width = generateLength(BlockPos::getX);
    private static final int height = generateLength(BlockPos::getY);
    private static final int depth = generateLength(BlockPos::getZ);

    public static int getWidth(){
        return width;
    }

    public static int getHeight(){
        return height;
    }

    public static int getDepth(){
        return depth;
    }

    public static AABB getSearchAABB(BlockPos pos){
        return new AABB(
                pos.getX() - getWidth(),
                pos.getY() - getHeight(),
                pos.getZ() - getDepth(),
                pos.getX() + getWidth(),
                pos.getY() + getHeight(),
                pos.getZ() + getDepth()
        );
    }

    private static int generateLength(Function<BlockPos, Integer> function){
        int tier1 = generateLengthForTier(TIER_1, function);
        int tier2 = generateLengthForTier(TIER_2, function);
        int tier3 = generateLengthForTier(TIER_3, function);
        int tier4 = generateLengthForTier(TIER_4, function);
        int tier5 = generateLengthForTier(TIER_5, function);

        return Math.max(tier1, Math.max(tier2, Math.max(tier3, Math.max(tier4, tier5))));
    }

    private static int generateLengthForTier(Pattern tier, Function<BlockPos, Integer> function){
        List<Pattern.PatternBlock> pattern = tier.patterns.get(Direction.NORTH);

        int minLength = 0;
        int maxLength = 0;

        for(Pattern.PatternBlock block : pattern){
            if(function.apply(block.pos) < minLength) minLength = function.apply(block.pos);
            if(function.apply(block.pos) > maxLength) maxLength = function.apply(block.pos);
        }

        return maxLength - minLength;
    }
}
