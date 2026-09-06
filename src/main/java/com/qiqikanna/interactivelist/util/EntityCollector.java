package com.qiqikanna.interactivelist.util;

import com.qiqikanna.interactivelist.tag.ModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class EntityCollector
{
    public static HitResult raycast(MinecraftClient client, Entity entity)
    {
        if (client.world == null || client.player == null)
            return null;

        return client.world.raycast(new RaycastContext(
                client.player.getCameraPosVec(client.getTickDelta()),
                entity.getBoundingBox().getCenter(),
                RaycastContext.ShapeType.OUTLINE,
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
        boolean inRangeX = pos.x < box.maxX + 0.1 && pos.x > box.minX - 0.1;
        boolean inRangeY = pos.y < box.maxY + 0.1 && pos.y > box.minY - 0.1;
        boolean inRangeZ = pos.z < box.maxZ + 0.1 && pos.z > box.minZ - 0.1;
        return inRangeX && inRangeY && inRangeZ;
    }

    public static boolean isInteractive(MinecraftClient client, Entity entity)
    {
        if (client.world == null || client.player == null || entity == null)
            return false;
        if (client.player.distanceTo(entity) > 5.0)
            return false;

        EntityType<?> entityType = entity.getType();
        if (entityType.isIn(ModTags.INTERACTIVE_UNCONDITIONAL_ENTITIES))
            return true;
        if (entityType.isIn(ModTags.RIDEABLE_ENTITIES))
            return entity.hasPlayerRider();

        return false;
    }
}
