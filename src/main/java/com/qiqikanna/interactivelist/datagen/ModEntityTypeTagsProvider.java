package com.qiqikanna.interactivelist.datagen;

import com.qiqikanna.interactivelist.tag.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends FabricTagProvider.EntityTypeTagProvider
{

    public ModEntityTypeTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
    {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup)
    {
        getOrCreateTagBuilder(ModTags.INTERACTIVE_UNCONDITIONAL_ENTITIES)
                .add(EntityType.VILLAGER);

        getOrCreateTagBuilder(ModTags.RIDEABLE_ENTITIES)
                .add(EntityType.MINECART)
                .add(EntityType.CHEST_MINECART)
                .add(EntityType.BOAT)
                .add(EntityType.CHEST_BOAT)
                .add(EntityType.HORSE)
                .add(EntityType.SKELETON_HORSE)
                .add(EntityType.ZOMBIE_HORSE)
                .add(EntityType.DONKEY)
                .add(EntityType.MULE);
    }
}
