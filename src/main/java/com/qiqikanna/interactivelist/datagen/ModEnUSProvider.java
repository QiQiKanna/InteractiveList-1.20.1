package com.qiqikanna.interactivelist.datagen;

import com.qiqikanna.interactivelist.option.ModKeyBindings;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModEnUSProvider extends FabricLanguageProvider
{
    public ModEnUSProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput,"en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(ModKeyBindings.INTERACTIVE_LIST,"Interactive List Mod");
        translationBuilder.add(ModKeyBindings.SELECT_NEXT.getTranslationKey(),"Select Next");
        translationBuilder.add(ModKeyBindings.SELECT_PREVIOUS.getTranslationKey(),"Select Previous");
        translationBuilder.add(ModKeyBindings.SWITCH_SCROLL.getTranslationKey(),"Switch Scroll");
    }
}
