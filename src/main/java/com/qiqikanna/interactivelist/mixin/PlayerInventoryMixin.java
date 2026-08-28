package com.qiqikanna.interactivelist.mixin;

import com.qiqikanna.interactivelist.hud.InteractiveListHud;
import com.qiqikanna.interactivelist.option.ModKeyBindings;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin
{
    @Inject(at = @At("HEAD"), method = "scrollInHotbar",cancellable = true)
    private void switchScroll(double scrollAmount, CallbackInfo ci)
    {
        if (ModKeyBindings.SWITCH_SCROLL.isPressed())
        {
            ci.cancel();
            InteractiveListHud hud = InteractiveListHud.getInstance();
            if (scrollAmount > 0)
            {
                hud.selectPrevious();
            }
            else
            {
                hud.selectNext();
            }
        }
    }
}
