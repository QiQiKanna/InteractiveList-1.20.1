package com.qiqikanna.interactivelist;

import com.qiqikanna.interactivelist.hud.InteractiveListHud;
import com.qiqikanna.interactivelist.option.ModKeyBindings;
import com.qiqikanna.interactivelist.util.BlockCollector;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class InteractiveListClient implements ClientModInitializer
{
    private static int interactCooldown = 4;


    @Override
    public void onInitializeClient()
    {
        ModKeyBindings.register();

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            if (interactCooldown > 0)
                interactCooldown--;

            List<BlockPos> blockPosList = BlockCollector.collectBlocksBFS(client,5);
            if (client.player == null)
                return;
            BlockPos playerBlockPos = client.player.getBlockPos();
            blockPosList.sort((pos1,pos2) ->
            {
                double distance1 = playerBlockPos.getSquaredDistance(pos1);
                double distance2 = playerBlockPos.getSquaredDistance(pos2);
                return  (int)(distance1 - distance2);
            });
            List<String> blockNameList = new ArrayList<>();
            blockPosList.forEach(blockPos ->
            {
                if (client.world == null)
                    return;
                blockNameList.add(
                        Text.translatable(client.world.getBlockState(blockPos).getBlock().getTranslationKey())
                                .getString()
                );
            });

            InteractiveListHud hud = InteractiveListHud.getInstance();
            hud.setClient(client);
            hud.setHudTexts(blockNameList);

            while (ModKeyBindings.SELECT_NEXT.wasPressed())
            {
                hud.selectNext();
            }

            while (ModKeyBindings.SELECT_PREVIOUS.wasPressed())
            {
                hud.selectPrevious();
            }

            while (ModKeyBindings.INTERACT.wasPressed())
            {
                BlockPos blockPos = blockPosList.get(hud.getSelectIndex());
                if (client.interactionManager != null && interactCooldown == 0)
                {
                    client.interactionManager.interactBlock(
                            client.player,
                            client.player.getActiveHand(),
                            new BlockHitResult(blockPos.toCenterPos(), Direction.UP,blockPos,false));
                    client.player.swingHand(client.player.getActiveHand());
                    interactCooldown = 4;
                }
            }

        });

        HudRenderCallback.EVENT.register(((drawContext, v) -> {
            InteractiveListHud hud = InteractiveListHud.getInstance();
            hud.render(drawContext);
        }));
    }
}
