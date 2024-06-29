package com.thoxia.odin.skyblock.island.chat;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.island.chat.IChatManager;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class ChatManager implements IChatManager {

    private final SkyBlockPlugin plugin;

    @Override
    public void sendMessage(Player player, UUID islandId, Component message) {
        Island island = plugin.getIslandManager().getIsland(islandId);
        if (island == null) return;

        Component component = ChatUtils.format(
                "<gold>[ISLAND CHAT] <yellow><player>: <white><message>",
                Placeholder.unparsed("player", player.getName()),
                Placeholder.component("message", message)
        );

        for (SPlayer member : island.getIslandMembers()) {
            if (member.getPlayer() == null) continue;

            member.getPlayer().sendMessage(component);
        }

        if (island.getOwner().getPlayer() != null)
            island.getOwner().getPlayer().sendMessage(component);

    }

}
