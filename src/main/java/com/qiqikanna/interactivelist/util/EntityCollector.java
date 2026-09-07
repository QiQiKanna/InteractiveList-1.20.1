package com.qiqikanna.interactivelist.util;

import com.qiqikanna.interactivelist.hud.HudEntry;
import com.qiqikanna.interactivelist.tag.ModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;

public class EntityCollector
{
    public static List<HudEntry> getHudEntries(MinecraftClient client,double range)
    {
        List<HudEntry> entries = new ArrayList<>();
        if (client.world == null)
            return entries;

        client.world.getEntities().forEach(entity ->
        {
            if (EntityCollector.isInteractive(client,entity,range))
            {
                HitResult hitResult = raycast(client,entity);
                if (isHit(hitResult,entity))
                    entries.add(new HudEntry(client, entity,hitResult));
            }
        });

        return entries;
    }

    public static HitResult raycast(MinecraftClient client, Entity entity)
    {
        if (client.world == null || client.player == null)
            return null;

        return client.world.raycast(new RaycastContext(
                client.player.getCameraPosVec(client.getTickDelta()),
                entity.getBoundingBox().getCenter(),
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                client.player
        ));
    }

    public static boolean isHit(HitResult hitResult,Entity entity)
    {
        if (hitResult == null || entity == null)
            return false;

        Box box = entity.getBoundingBox();
        Vec3d pos = hitResult.getPos();
        boolean inRangeX = pos.x <= box.maxX && pos.x >= box.minX;
        boolean inRangeY = pos.y <= box.maxY && pos.y >= box.minY;
        boolean inRangeZ = pos.z <= box.maxZ && pos.z >= box.minZ;
        return inRangeX && inRangeY && inRangeZ;
    }

    public static boolean isInteractive(MinecraftClient client, Entity entity,double maxDistance)
    {
        if (client.world == null || client.player == null || entity == null)
            return false;
        if (client.player.distanceTo(entity) > maxDistance)
            return false;

        EntityType<?> entityType = entity.getType();
        if (entityType.isIn(ModTags.INTERACTIVE_UNCONDITIONAL_ENTITIES))
            return true;
        if (entityType.isIn(ModTags.RIDEABLE_ENTITIES))
            return entity.hasPlayerRider();

        return false;
    }
}
