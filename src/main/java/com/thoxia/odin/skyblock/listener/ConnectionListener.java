package com.thoxia.odin.skyblock.listener;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.util.WorldBorderUtils;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {

    private final SkyBlockPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            SPlayer sPlayer = plugin.getDatabase().loadPlayerData(player.getUniqueId());
            plugin.getPlayerManager().addPlayer(sPlayer);
            sPlayer.setName(player.getName());

            Bukkit.getScheduler().runTask(plugin, () -> {
                FastBoard board = new FastBoard(player);
                plugin.getBoardMap().put(player.getUniqueId(), board);

                Island island = plugin.getIslandManager().getIsland(player.getLocation());
                if (island == null) return;

                island.getVisitors().add(sPlayer);
                WorldBorderUtils.send(player, island);
            });
        }, 10);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (sPlayer != null)
                plugin.getDatabase().savePlayerData(sPlayer);
        });
        plugin.getPlayerManager().removePlayer(player.getUniqueId());
        plugin.getBoardMap().remove(player.getUniqueId());

        plugin.getTeleportMap().invalidate(player.getUniqueId());

        Island island = plugin.getIslandManager().getIslandPlayerIn(player);
        if (island == null) return;

        island.getVisitors().remove(sPlayer);
        if (plugin.getIslandManager().shouldUnload(island)) {
            // if no one left on the island, we can unload and save the data. it is no longer needed
            plugin.getIslandManager().unloadIsland(island);
        }
    }

}
