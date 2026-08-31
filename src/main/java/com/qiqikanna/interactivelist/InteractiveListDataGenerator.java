package com.qiqikanna.interactivelist;

import com.qiqikanna.interactivelist.datagen.ModBlockTagsProvider;
import com.qiqikanna.interactivelist.datagen.ModEnUSProvider;
import com.qiqikanna.interactivelist.datagen.ModZhCNProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class InteractiveListDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModEnUSProvider::new);
		pack.addProvider(ModZhCNProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
	}
}
