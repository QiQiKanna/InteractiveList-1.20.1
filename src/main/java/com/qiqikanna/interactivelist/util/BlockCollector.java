package com.qiqikanna.interactivelist.util;

import com.qiqikanna.interactivelist.hud.HudEntry;
import com.qiqikanna.interactivelist.tag.ModTags;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.ChestType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.*;

public class BlockCollector
{
    private static final List<BlockPos> BFS_OFFSETS = List.of(
            new BlockPos(1,0,0),
            new BlockPos(-1,0,0),
            new BlockPos(0,0,1),
            new BlockPos(0,0,-1),
            new BlockPos(0,1,0),
            new BlockPos(0,-1,0)
    );

    private static final List<Vec3d> RAYCAST_OFFSETS = List.of(
            new Vec3d(0.0,0.0,0.0),
            new Vec3d(0.0,0.5,0.0),
            new Vec3d(0.5,0.0,0.0),
            new Vec3d(-0.5,0.0,0.0),
            new Vec3d(0.0,0.0,0.5),
            new Vec3d(0.0,0.0,-0.5),
            new Vec3d(0.0,-0.5,0.0)
    );

    public static List<HudEntry> getHudEntries(MinecraftClient client, double range)
    {
        List<HudEntry> entries = new ArrayList<>();
        List<BlockPos> blockPosList = collectBlockPosBFS(client,range + 2.0);
        blockPosList.forEach(blockPos ->
        {
            BlockHitResult hitResult = raycast(client,blockPos,range);
            if (isHit(hitResult,blockPos))
                entries.add(new HudEntry(client,blockPos,hitResult));
        });
        return entries;
    }

    public static List<BlockPos> collectBlockPosBFS(MinecraftClient client, double range)
    {
        List<BlockPos> result = new ArrayList<>();

        if (client.world == null || client.player == null)
            return result;
        World world = client.world;
        BlockPos origin = client.player.getBlockPos();

        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        BlockPos.Mutable cursor = new BlockPos.Mutable();
        queue.add(origin);
        visited.add(new BlockPos(origin));
        disposeBlock(visited,client,origin);
        while (!queue.isEmpty())
        {
            BlockPos currentPos = queue.poll();
            BlockState currentBlockState = world.getBlockState(currentPos);
            FluidState currentFluidState = world.getFluidState(currentPos);
            if (isInteractiveBlock(currentBlockState))
            {
                result.add(new BlockPos(currentPos));
            }

            for (BlockPos offset : BFS_OFFSETS)
            {
                cursor.set(currentPos.getX() + offset.getX(),
                        currentPos.getY() + offset.getY(),
                        currentPos.getZ() + offset.getZ());
                if (!cursor.isWithinDistance(origin,range) || visited.contains(cursor))
                {
                    continue;
                }

                if
                (       currentFluidState.isIn(FluidTags.WATER) ||
                        currentBlockState.isOf(Blocks.AIR) ||
                        currentPos.equals(origin)
                )
                {
                    visited.add(new BlockPos(cursor));
                    queue.add(cursor.toImmutable());
                }

                if (isInteractiveBlock(client.world.getBlockState(cursor)))
                {
                    disposeBlock(visited,client,cursor);
                }
            }
        }

        return result;
    }

    public static boolean isInteractiveBlock(BlockState blockState)
    {
        return blockState.isIn(ModTags.INTERACTIVE_UNCONDITIONAL_BLOCKS);
    }

    public static BlockHitResult raycast(MinecraftClient client, BlockPos blockPos, double maxDistance)
    {
        if (client.world == null || client.player == null)
            return null;

        Vec3d centerPos = blockPos.toCenterPos();

        for (Vec3d offset : RAYCAST_OFFSETS)
        {
            Vec3d pos = centerPos.add(offset);
            Vec3d direction = pos.subtract(client.player.getCameraPosVec(client.getTickDelta())).normalize();

            BlockHitResult blockHitResult = client.world.raycast(new RaycastContext(
                    client.player.getCameraPosVec(client.getTickDelta()),
                    client.player.getCameraPosVec(client.getTickDelta()).add(direction.multiply(maxDistance)),
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    client.player
            ));

            if (isHit(blockHitResult, blockPos))
                return blockHitResult;
        }

        return null;

    }

    public static boolean isHit(BlockHitResult hitResult, BlockPos blockPos)
    {
        if (hitResult == null || blockPos == null || !hitResult.getType().equals(HitResult.Type.BLOCK))
            return false;

        return hitResult.getBlockPos().equals(blockPos);
    }

    public static void disposeBlock(Set<BlockPos> visited,MinecraftClient client ,BlockPos blockPos)
    {
        if (client.world == null)
            return;

        BlockState blockState = client.world.getBlockState(blockPos);
        BlockPos neighborBlockPos = null;

        if (blockState.isOf(Blocks.CHEST))
            neighborBlockPos = getNeighborChestPos(blockState,blockPos);
        else if (blockState.isIn(BlockTags.BEDS))
            neighborBlockPos = getNeighborBedPos(blockState,blockPos);
        else if (blockState.isIn(BlockTags.DOORS))
            neighborBlockPos = getNeighborDoorPos(blockState,blockPos);

        if (neighborBlockPos != null && client.world.getBlockState(blockPos).isOf(blockState.getBlock()))
            visited.add(neighborBlockPos);
    }

    public static BlockPos getNeighborChestPos(BlockState blockState , BlockPos blockPos)
    {
        ChestType type = blockState.get(ChestBlock.CHEST_TYPE);
        Direction facing = blockState.get(ChestBlock.FACING);
        BlockPos neighborPos = null;
        if (type.equals(ChestType.LEFT))
        {
            neighborPos = blockPos.offset(BlockRotation.CLOCKWISE_90.rotate(facing));
        }
        else if (type.equals(ChestType.RIGHT))
        {
            neighborPos = blockPos.offset(BlockRotation.COUNTERCLOCKWISE_90.rotate(facing));
        }

        return neighborPos;
    }

    public static BlockPos getNeighborBedPos(BlockState blockState , BlockPos blockPos)
    {
        BedPart part = blockState.get(BedBlock.PART);
        Direction facing = blockState.get(BedBlock.FACING);

        return (part == BedPart.FOOT) ? blockPos.offset(facing) : blockPos.offset(facing.getOpposite());
    }

    public static BlockPos getNeighborDoorPos(BlockState blockState ,BlockPos blockPos)
    {
        DoubleBlockHalf half = blockState.get(DoorBlock.HALF);

        return (half == DoubleBlockHalf.LOWER) ? blockPos.offset(Direction.UP) : blockPos.offset(Direction.DOWN);
    }
}
