package com.qiqikanna.interactivelist;

import com.qiqikanna.interactivelist.hud.InteractiveListHud;
import com.qiqikanna.interactivelist.option.ModKeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import java.util.List;

public class InteractiveListClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ModKeyBindings.register();

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            InteractiveListHud hud = InteractiveListHud.getInstance();
            hud.setClient(client);
            hud.setHudTexts(List.of(
                    "Haha",
                    "Hello",
                    "hello world"
            ));

            while (ModKeyBindings.SELECT_NEXT.wasPressed())
            {
                hud.selectNext();
            }

            while (ModKeyBindings.SELECT_PREVIOUS.wasPressed())
            {
                hud.selectPrevious();
            }

        });

        HudRenderCallback.EVENT.register(((drawContext, v) -> {
            InteractiveListHud hud = InteractiveListHud.getInstance();
            hud.render(drawContext);
        }));
    }
}
