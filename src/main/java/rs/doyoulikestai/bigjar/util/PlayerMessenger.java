package rs.doyoulikestai.bigjar.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;

public class PlayerMessenger {

    public static void broadcast(String string) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        player.displayClientMessage(Component.literal(string), false);
    }

    public static void broadcastActionbar(String string, @Nullable ChatFormatting style) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (style != null) {
            player.displayClientMessage(Component.literal(string).withStyle(style), true);
        } else {
            player.displayClientMessage(Component.literal(string), true);
        }
    }

}
