package com.qiqikanna.interactivelist.util;

import com.qiqikanna.interactivelist.tag.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class BlockCollector
{
    private static final List<BlockPos> OFFSETS = List.of(
            new BlockPos(1,0,0),
            new BlockPos(-1,0,0),
            new BlockPos(0,0,1),
            new BlockPos(0,0,-1),
            new BlockPos(0,1,0),
            new BlockPos(0,-1,0)
    );

    public static List<BlockPos> collectBlocksBFS(MinecraftClient client, int range)
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

            for (BlockPos offset : OFFSETS)
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
        return blockState.isIn(ModTags.INTERACTIVE_BLOCKS);
    }

    private static void disposeBlock(Set<BlockPos> visited,MinecraftClient client ,BlockPos blockPos)
    {
        if (client.world == null)
            return;
        BlockState blockState = client.world.getBlockState(blockPos);

        if (blockState.isOf(Blocks.CHEST))
            disposeChest(visited,client,blockPos);
    }

    private static void disposeChest(Set<BlockPos> visited,MinecraftClient client ,BlockPos blockPos)
    {
        if (client.world == null)
            return;
        BlockState blockState = client.world.getBlockState(blockPos);
        if (!blockState.isOf(Blocks.CHEST))
            return;

        ChestType type = blockState.get(ChestBlock.CHEST_TYPE);
        int offset = 1;
        if (type.equals(ChestType.RIGHT))
        {
            offset = -1;
        }
        else if (type.equals(ChestType.SINGLE))
        {
            return;
        }

        Direction facing = blockState.get(ChestBlock.FACING);
        BlockPos neighborPos = switch (facing)
        {
            case NORTH -> blockPos.offset(Direction.EAST,offset);
            case EAST -> blockPos.offset(Direction.SOUTH,offset);
            case SOUTH -> blockPos.offset(Direction.WEST,offset);
            case WEST -> blockPos.offset(Direction.NORTH,offset);
            default -> blockPos;
        };

        if (client.world.getBlockState(neighborPos).isOf(Blocks.CHEST))
            visited.add(new BlockPos(neighborPos));
    }
}
