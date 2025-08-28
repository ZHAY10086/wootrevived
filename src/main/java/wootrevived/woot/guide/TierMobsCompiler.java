package wootrevived.woot.guide;

import guideme.compiler.PageCompiler;
import guideme.compiler.tags.BlockTagCompiler;
import guideme.compiler.tags.MdxAttrs;
import guideme.document.block.LytBlockContainer;
import guideme.document.block.LytHBox;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import guideme.scene.level.GuidebookLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.woot.events.client.GuideCacheLivingEntities;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.render.guide.LytEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TierMobsCompiler extends BlockTagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of("TierMobs");
    }

    @Override
    protected void compile(PageCompiler compiler, LytBlockContainer parent, MdxJsxElementFields el) {
        Tier tier = MdxAttrs.getEnum(compiler, parent, el, "tier", Tier.TIER_1);

        Map<EntityType<?>, LivingEntity> cachedLivingEntities = GuideCacheLivingEntities.getLivingEntities();

        List<WootFactoryMob<?>> mobs = new ArrayList<>();
        List<LivingEntity> entities = new ArrayList<>();

        for(WootFactoryMob<?> mob : WootFactoryMobsRegistry.getFactoryMobValues()){
            if(mob.getTier() != tier) continue;
            EntityType<?> entityType = mob.getEntityType();
            if(!cachedLivingEntities.containsKey(entityType)) continue;
            mobs.add(mob);
            entities.add(cachedLivingEntities.get(mob.getEntityType()));
        }

        GuidebookLevel level = new GuidebookLevel();

        LytHBox box = new LytHBox();

        for(int i = 0; i < mobs.size(); i++)
            box.append(new LytEntity(mobs.get(i), entities.get(i), level));

        parent.append(box);
    }
}
