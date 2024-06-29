package com.thoxia.odin.skyblock.api.database;

import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;

import java.util.UUID;

public interface Database {

    void onEnable();

    default void onDisable() {
        for (Island island : SkyBlock.getInstance().getIslandManager().getIslands()) {
            saveIsland(island);
        }

        for (SPlayer player : SkyBlock.getInstance().getPlayerManager().getPlayers()) {
            savePlayerData(player);
        }
    }

    Island loadIslandData(UUID islandUuid);

    default Island loadIslandData(SPlayer player) {
        return loadIslandData(player.getIslandId());
    }

    default SPlayer loadPlayerData(UUID playerUuid) {
        return loadPlayerData(playerUuid, false);
    }

    default SPlayer loadPlayerData(String playerName) {
        return loadPlayerData(playerName, false);
    }

    SPlayer loadPlayerData(UUID playerUuid, boolean loadAnyway);

    SPlayer loadPlayerData(String playerName, boolean loadAnyway);

    void deleteIsland(UUID islandUuid);

    default void deleteIsland(Island island) {
        deleteIsland(island.getUniqueId());
    }

    void saveIsland(Island island);

    void savePlayerData(SPlayer player);

    UUID getIslandId(String player);

}
