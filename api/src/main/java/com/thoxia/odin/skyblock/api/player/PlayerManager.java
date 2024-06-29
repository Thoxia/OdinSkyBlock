package com.thoxia.odin.skyblock.api.player;

import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private final Map<UUID, SPlayer> playerMap = new ConcurrentHashMap<>();

    public Collection<SPlayer> getPlayers() {
        return playerMap.values();
    }

    public void addPlayer(SPlayer player) {
        playerMap.put(player.getUniqueId(), player);
    }

    public void removePlayer(SPlayer player) {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(OfflinePlayer player) {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    public SPlayer getPlayer(UUID uuid) {
        return playerMap.get(uuid);
    }

    public SPlayer getPlayer(String name) {
        return this.playerMap.values().stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public SPlayer getPlayer(OfflinePlayer player) {
        return getPlayer(player.getUniqueId());
    }

}
