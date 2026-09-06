package com.qiqikanna.interactivelist.hud;

import com.qiqikanna.interactivelist.util.BlockCollector;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class HudEntry
{
    public final Object obj;
    public final String content;
    public final double distance;
    public final HitResult hitResult;

    public HudEntry(MinecraftClient client, Object obj,HitResult hitResult)
    {
        String content = "";
        double distance = 0.0;

        if (client.world != null && client.player !=null)
        {
            if (obj instanceof BlockPos blockPos)
            {
                String key = client.world.getBlockState(blockPos).getBlock().getTranslationKey();
                String fixedKey = key + ".interactive_list_content";
                content = Text.translatable(fixedKey).getString();
                if (content.equals(fixedKey))
                    content = Text.translatable(key).getString();
                distance = blockPos.getSquaredDistance(client.player.getPos());
            }
            else if (obj instanceof Entity entity)
            {
                String key = entity.getType().getTranslationKey();
                String fixedKey = key + ".interactive_list_content";
                content = Text.translatable(fixedKey).getString();
                if (content.equals(fixedKey))
                    content =Text.translatable(key).getString();
                distance = entity.distanceTo(client.player);

            }
        }

        this.obj = obj;
        this.content = content;
        this.distance = distance;
        this.hitResult = hitResult;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        HudEntry hudEntry = (HudEntry) obj;
        return this.obj.equals(hudEntry.obj) &&
                    this.content.equals(hudEntry.content)&&
                    this.distance == hudEntry.distance;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.obj,this.content,this.distance);
    }
}
