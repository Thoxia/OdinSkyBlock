package com.thoxia.odin.skyblock.listener;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.util.WorldBorderUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

@RequiredArgsConstructor
public class TeleportListener implements Listener {

    private final SkyBlockPlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Island from = plugin.getIslandManager().getIsland(event.getFrom());
        Island to = plugin.getIslandManager().getIsland(event.getTo());
        if (from == to) return;

        Player player = event.getPlayer();
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        if (sPlayer == null) return;

        if (from != null) {
            from.getVisitors().remove(sPlayer);
            plugin.debug("resetting border...");
            WorldBorderUtils.reset(player);
        }

        if (to != null) {
            if (to.isLocked() && (!to.getCoopPlayers().contains(sPlayer) || !to.getIslandMembers().contains(sPlayer))) {
                player.sendMessage(ChatUtils.format("<red>The island you are trying to teleport is currently locked."));
                event.setCancelled(true);
                return;
            }

            to.getVisitors().add(sPlayer);
            plugin.debug("sending border...");
            Bukkit.getScheduler().runTaskLater(plugin, () -> WorldBorderUtils.send(player, to), 5);

            if (to.getName() != null)
                player.sendTitlePart(TitlePart.TITLE, to.getName());
            if (to.getDescription() != null)
                player.sendTitlePart(TitlePart.SUBTITLE, to.getDescription());

            if (player.isFlying() && !to.hasPermission(sPlayer, plugin.getPermissionManager().getPermission("fly"))) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(ChatUtils.format("<red>You do not have permission to fly in this island."));
            }
        }
    }

}
