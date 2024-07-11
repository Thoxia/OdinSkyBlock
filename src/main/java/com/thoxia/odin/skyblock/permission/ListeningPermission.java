package com.thoxia.odin.skyblock.permission;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.config.Messages;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;

public abstract class ListeningPermission extends IslandPermission implements Listener {

    public ListeningPermission(String name) {
        super(name);

        SkyBlockPlugin.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlockPlugin.getInstance());
    }

    protected void checkAndCancel(Cancellable event, Player player) {
        SPlayer sPlayer = SkyBlockPlugin.getInstance().getPlayerManager().getPlayer(player);
        checkAndCancel(event, sPlayer);
    }

    protected void checkAndCancel(Cancellable event, SPlayer player) {
        Island island = SkyBlockPlugin.getInstance().getIslandManager().getIslandPlayerIn(player);
        if (island == null) return;

        if (check(island, player)) {
            event.setCancelled(true);
            Messages.ACTION_CANCELLED.sendMessage(player.getPlayer());
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

}
