package com.thoxia.odin.skyblock.listener;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.util.WorldBorderUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class RespawnListener implements Listener {

    private final SkyBlockPlugin plugin;

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Island island = plugin.getIslandManager().getIslandPlayerIn(event.getPlayer());
        if (island != null) {
            WorldBorderUtils.send(player, island);
        } else {
            WorldBorderUtils.reset(player);
        }
    }

}
