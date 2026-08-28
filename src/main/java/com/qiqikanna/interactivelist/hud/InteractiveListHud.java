package com.qiqikanna.interactivelist.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.qiqikanna.interactivelist.InteractiveList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InteractiveListHud
{
    private static InteractiveListHud INSTANCE;
    private static final Identifier LIST_CELL_TEXTURE = InteractiveList.id("textures/ui/list_cell.png");

    private MinecraftClient client;
    private final List<String> hudTexts = new ArrayList<>();
    private int maxCellCount;
    private int selectedIndex = 0;

    private InteractiveListHud()
    {
        this.maxCellCount = 4;
    }

    public static InteractiveListHud getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new InteractiveListHud();
        return INSTANCE;
    }

    public void setClient(MinecraftClient client)
    {
        this.client = client;
    }

    public void setHudTexts(List<String> hudTexts)
    {
        this.hudTexts.clear();
        for (int i = 0; i < hudTexts.size(); i++)
        {
            if (i >= this.maxCellCount - 1)
                break;

            this.hudTexts.add(hudTexts.get(i));
        }
    }

    public void selectNext()
    {
        this.selectedIndex = (this.selectedIndex + 1) % this.hudTexts.size();
    }

    public void selectPrevious()
    {
        this.selectedIndex = (this.selectedIndex + this.hudTexts.size() -1) % this.hudTexts.size();
    }

    public void render(DrawContext drawContext)
    {
        MatrixStack matrixStack = drawContext.getMatrices();

        for (int i = 0;i < this.hudTexts.size();i++)
        {
            if (i >= this.maxCellCount - 1)
                break;

            RenderSystem.enableBlend();
            drawContext.drawTexture(
                    LIST_CELL_TEXTURE,
                    250,130 + i*15,0,0,90,12,90,12);

            String text = (i == this.selectedIndex ? "F  " : "   ") + this.hudTexts.get(i);

            matrixStack.push();
            matrixStack.scale(0.5F,0.5F,0.5F);
            drawContext.drawText(this.client.textRenderer,
                    text,
                    515,268 + i*30,
                    0xFFFFFFFF,
                    false);
            matrixStack.pop();
        }


    }
}
