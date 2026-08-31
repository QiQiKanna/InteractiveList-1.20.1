package com.qiqikanna.interactivelist.datagen;

import com.qiqikanna.interactivelist.option.ModKeyBindings;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModZhCNProvider extends FabricLanguageProvider
{
    public ModZhCNProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput,"zh_cn");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(ModKeyBindings.INTERACTIVE_LIST,"交互列表模组");
        translationBuilder.add(ModKeyBindings.SELECT_NEXT.getTranslationKey(),"选择下一项");
        translationBuilder.add(ModKeyBindings.SELECT_PREVIOUS.getTranslationKey(),"选择上一项");
        translationBuilder.add(ModKeyBindings.SWITCH_SCROLL.getTranslationKey(),"切换滚轮模式");
    }
}
