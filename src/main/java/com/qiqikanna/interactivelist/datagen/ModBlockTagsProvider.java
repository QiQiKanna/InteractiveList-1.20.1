package com.qiqikanna.interactivelist.datagen;

import com.qiqikanna.interactivelist.tag.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider
{
    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup)
    {
        getOrCreateTagBuilder(ModTags.INTERACTIVE_BLOCKS)
                .add(Blocks.CHEST)
                .add(Blocks.STONECUTTER)
                .add(Blocks.CRAFTING_TABLE)
                .add(Blocks.BEACON)
                .add(Blocks.CARTOGRAPHY_TABLE)
                .add(Blocks.SMITHING_TABLE)
                .add(Blocks.ENCHANTING_TABLE)
                .add(Blocks.FURNACE)
                .add(Blocks.BLAST_FURNACE)
                .forceAddTag(BlockTags.ANVIL)
                .forceAddTag(BlockTags.FENCE_GATES)
                .forceAddTag(BlockTags.BEDS)
                .forceAddTag(BlockTags.WOODEN_BUTTONS)
                .forceAddTag(BlockTags.WOODEN_TRAPDOORS)
                .forceAddTag(BlockTags.WOODEN_DOORS)
                .forceAddTag(BlockTags.BEDS)
                .forceAddTag(BlockTags.ALL_SIGNS)
                .forceAddTag(BlockTags.ALL_HANGING_SIGNS);
    }
}
