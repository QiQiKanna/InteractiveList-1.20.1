package com.qiqikanna.interactivelist;

import com.qiqikanna.interactivelist.hud.HudEntry;
import com.qiqikanna.interactivelist.hud.InteractiveListHud;
import com.qiqikanna.interactivelist.option.ModKeyBindings;
import com.qiqikanna.interactivelist.tag.ModTags;
import com.qiqikanna.interactivelist.util.BlockCollector;
import com.qiqikanna.interactivelist.util.EntityCollector;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

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
            BlockCollector.collectBlocksBFS(client,5.0).forEach(
                    blockPos ->
                    {
                        BlockHitResult hitResult = BlockCollector.raycast(client,blockPos,5.0);
                        if (BlockCollector.isHIt(hitResult,blockPos))
                            entries.add(new HudEntry(client, blockPos, hitResult));
                    });
            client.world.getEntities().forEach(entity ->
            {
                if (EntityCollector.isInteractive(client,entity))
                {
                    HitResult hitResult = EntityCollector.raycast(client,entity);
                    if (EntityCollector.isHit(hitResult,entity))
                        entries.add(new HudEntry(client, entity,hitResult));
                }
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
                HudEntry hudEntry = hud.getSelectedEntry();
                if (interactCooldown != 0 || hudEntry == null)
                    continue;

                if (hudEntry.obj instanceof BlockPos)
                {
                    BlockHitResult hitResult = (BlockHitResult) hudEntry.hitResult;
                    ActionResult actionResult = client.interactionManager.interactBlock(
                            client.player,
                            client.player.getActiveHand(),
                            hitResult);
                    if (actionResult.shouldSwingHand())
                        client.player.swingHand(client.player.getActiveHand());
                }
                else if (hudEntry.obj instanceof Entity entity)
                {
                    ActionResult actionResult = client.interactionManager.interactEntity(
                            client.player,
                            entity,
                            client.player.getActiveHand());
                    if (actionResult.shouldSwingHand())
                        client.player.swingHand(client.player.getActiveHand());
                }

                interactCooldown = 4;
            }

        });

        HudRenderCallback.EVENT.register(((drawContext, v) -> {
            InteractiveListHud hud = InteractiveListHud.getInstance();
            hud.render(drawContext);
        }));
    }
}
