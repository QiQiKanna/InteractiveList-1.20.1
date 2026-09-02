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
    private final List<HudEntry> hudEntries = new ArrayList<>();
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

    public void setHudEntries(List<HudEntry> hudEntries)
    {
        this.hudEntries.clear();
        for (int i = 0; i < hudEntries.size(); i++)
        {
            if (i >= this.maxCellCount)
                break;

            this.hudEntries.add(hudEntries.get(i));
        }
    }

    public HudEntry getSelectedEntry()
    {
        return this.hudEntries.get(this.selectedIndex);
    }

    public void selectNext()
    {
        if (this.selectedIndex < this.hudEntries.size() - 1)
            this.selectedIndex++;
    }

    public void selectPrevious()
    {
        if (this.selectedIndex > 0)
            this.selectedIndex--;
    }

    public void render(DrawContext drawContext)
    {
        if (this.client == null)
            return;

        if (this.selectedIndex >= this.hudEntries.size())
            this.selectedIndex = 0;

        MatrixStack matrixStack = drawContext.getMatrices();

        for (int i = 0; i < this.hudEntries.size(); i++)
        {
            matrixStack.push();
            matrixStack.translate(250.0,130.0,0.0);

            if (i > this.maxCellCount - 1)
                break;

            //绘制单元格背景纹理
            RenderSystem.enableBlend();
            drawContext.drawTexture(
                    LIST_CELL_TEXTURE,
                    0, i * 15,0,0,90,12,90,12);

            String text = (i == this.selectedIndex ? ">  " : "   ") + this.hudEntries.get(i).content;

            //绘制文本
            matrixStack.push();
            matrixStack.translate(6.0,4.5,0.0);
            matrixStack.scale(0.5F,0.5F,0.5F);
            drawContext.drawText(this.client.textRenderer,
                    text,
                    0, i*30,
                    0xFFFFFFFF,
                    false);
            matrixStack.pop();

            matrixStack.pop();
        }

    }
}
