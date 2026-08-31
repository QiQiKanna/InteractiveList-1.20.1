package com.qiqikanna.interactivelist.tag;

import com.qiqikanna.interactivelist.InteractiveList;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags
{
    public static final TagKey<Block> INTERACTIVE_BLOCKS =
            TagKey.of(RegistryKeys.BLOCK, InteractiveList.id("interactive_block"));
}
