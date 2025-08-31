package wootrevived.woot.guide.recipes;

import guideme.compiler.PageCompiler;
import guideme.compiler.tags.BlockTagCompiler;
import guideme.document.block.LytBlockContainer;
import guideme.document.block.LytHBox;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import wootrevived.woot.config.EnchantedLiquifierConfig;

import java.util.Set;

public class EnchantedRecipeCompiler extends BlockTagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of("EnchantedRecipe");
    }

    @Override
    protected void compile(PageCompiler compiler, LytBlockContainer parent, MdxJsxElementFields el) {
        LytHBox box = new LytHBox();

        for(int enchantLevel = 1; enchantLevel <= EnchantedLiquifierConfig.MAX_ENCHANT_LVL.get(); enchantLevel++)
            box.append(new LytEnchantedLiquifierRecipe(enchantLevel));

        parent.append(box);
    }
}
