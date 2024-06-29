package com.thoxia.odin.skyblock.listener;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class ChatListener implements Listener {

    private final SkyBlockPlugin plugin;

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        SPlayer player = plugin.getPlayerManager().getPlayer(event.getPlayer());
        if (player.isChatEnabled()) {
            event.setCancelled(true);
            plugin.getChatManager().sendMessage(event.getPlayer(), player.getIslandId(), event.message());
        }
    }

}
