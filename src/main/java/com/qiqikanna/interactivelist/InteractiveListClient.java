package com.qiqikanna.interactivelist;

import com.qiqikanna.interactivelist.hud.HudEntry;
import com.qiqikanna.interactivelist.hud.InteractiveListHud;
import com.qiqikanna.interactivelist.option.ModKeyBindings;
import com.qiqikanna.interactivelist.util.BlockCollector;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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

            if (client.player == null || client.world == null || client.interactionManager == null)
                return;

            List<HudEntry> entries = new ArrayList<>();
            BlockCollector.collectBlocksBFS(client,client.interactionManager.getReachDistance())
                    .forEach(blockPos ->
                        entries.add(new HudEntry(client,blockPos))
                    );
            client.world.getEntities().forEach(entity ->
            {
                if (entity.distanceTo(client.player) <= client.interactionManager.getReachDistance() && !(entity instanceof PlayerEntity))
                    entries.add(new HudEntry(client,entity));
            });
            entries.sort((e1,e2)-> (int) (e1.distance - e2.distance));

            InteractiveListHud hud = InteractiveListHud.getInstance();
            hud.setClient(client);
            hud.setHudEntries(entries);


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
                if (interactCooldown != 0)
                    continue;

                HudEntry hudEntry = hud.getSelectedEntry();
                if (hudEntry.obj instanceof BlockPos blockPos)
                {
                    client.interactionManager.interactBlock(
                            client.player,
                            client.player.getActiveHand(),
                            new BlockHitResult(blockPos.toCenterPos(), Direction.NORTH,blockPos,false));
                    client.player.swingHand(client.player.getActiveHand());
                    interactCooldown = 4;
                }
                else if (hudEntry.obj instanceof Entity entity)
                {
                    client.interactionManager.interactEntity(
                            client.player,
                            entity,
                            client.player.getActiveHand()
                    );
                }
            }

        });

        HudRenderCallback.EVENT.register(((drawContext, v) -> {
            InteractiveListHud hud = InteractiveListHud.getInstance();
            hud.render(drawContext);
        }));
    }
}
