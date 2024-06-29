package com.thoxia.odin.skyblock.api.island;

import com.google.common.base.Preconditions;
import com.thoxia.odin.skyblock.api.location.SLocation;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IIslandManager {

    Collection<Island> getIslands();

    Island getIsland(UUID islandId);

    CompletableFuture<Boolean> createIsland(Island island);

    CompletableFuture<Boolean> loadIsland(Island island);

    void unloadIsland(Island island);

    void deleteIsland(UUID islandId);

    default boolean isIslandWorld(World world) {
        return isIslandWorld(world.getName());
    }

    boolean isIslandWorld(String world);

    default boolean isInIslandWorld(SPlayer player) {
        Preconditions.checkArgument(player != null, "player");

        return isInIslandWorld(player.getPlayer());
    }

    default boolean isInIslandWorld(Player player) {
        Preconditions.checkArgument(player != null, "player");

        return isIslandWorld(player.getWorld());
    }

    boolean isInIsland(SPlayer player, Island island);

    Island getIsland(Location location);

    default Island getIslandPlayerIn(SPlayer player) {
        return getIslandPlayerIn(player.getPlayer());
    }

    Island getIslandPlayerIn(Player player);

    void teleportToIsland(Player player, UUID islandId);

    boolean lockIsland(Island island);

    boolean unlockIsland(Island island);

    SLocation getSpawnLocation();

    void setSpawnLocation(SLocation location);

    boolean shouldUnload(Island island);

}
