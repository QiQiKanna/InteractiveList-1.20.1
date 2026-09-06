package com.qiqikanna.interactivelist.tag;

import com.qiqikanna.interactivelist.InteractiveList;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags
{
    public static final TagKey<Block> INTERACTIVE_UNCONDITIONAL_BLOCKS =
            TagKey.of(RegistryKeys.BLOCK, InteractiveList.id("interactive_unconditional_blocks"));

    public static final TagKey<EntityType<?>> INTERACTIVE_UNCONDITIONAL_ENTITIES =
            TagKey.of(RegistryKeys.ENTITY_TYPE,InteractiveList.id("interactive_unconditional_entities"));
    public static final TagKey<EntityType<?>> RIDEABLE_ENTITIES =
            TagKey.of(RegistryKeys.ENTITY_TYPE,InteractiveList.id("rideable_entities"));
}
