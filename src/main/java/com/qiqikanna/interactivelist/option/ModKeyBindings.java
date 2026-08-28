package com.qiqikanna.interactivelist.option;

import com.qiqikanna.interactivelist.InteractiveList;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings
{
    public static final String INTERACTIVE_LIST = "key.categories.%s.interactive_list".formatted(InteractiveList.MOD_ID);

    public static final KeyBinding SELECT_NEXT = register("select_next",
            InputUtil.Type.MOUSE ,GLFW.GLFW_MOUSE_BUTTON_4,INTERACTIVE_LIST);
    public static final KeyBinding SELECT_PREVIOUS = register("select_previous",
            InputUtil.Type.MOUSE,GLFW.GLFW_MOUSE_BUTTON_5,INTERACTIVE_LIST);
    public static final KeyBinding SWITCH_SCROLL = register("switch_scroll",
            GLFW.GLFW_KEY_LEFT_CONTROL,INTERACTIVE_LIST);


    public static KeyBinding register(String name,int code,String category)
    {
        return register(name,InputUtil.Type.KEYSYM,code,category);
    }

    public static KeyBinding register(String name, InputUtil.Type type, int code, String category)
    {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.%s.%s".formatted(InteractiveList.MOD_ID,name),
                type,
                code,
                category
        ));
    }

    public static void register()
    {}
}
