package com.thoxia.odin.skyblock.island;

import com.google.common.base.Preconditions;
import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.event.IslandLockEvent;
import com.thoxia.odin.skyblock.api.event.IslandUnlockEvent;
import com.thoxia.odin.skyblock.api.island.IIslandManager;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.location.SLocation;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.upgrade.Upgrade;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.eventbus.EventBus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class IslandManager implements IIslandManager {

    @Getter @Setter
    private SLocation spawnLocation;

    private final SkyBlockPlugin plugin;

    /**
     * This map only contains loaded islands.
     * This means islands which are not currently located in this server are not included.
     */
    private final Map<UUID, Island> islandMap = new ConcurrentHashMap<>();

    /**
     * This map only contains loaded islands.
     * This means islands which are not currently located in this server are not included.
     */
    private final TreeMap<Integer, TreeMap<Integer, Island>> locationMap = new TreeMap<>();

    @Override
    public Collection<Island> getIslands() {
        return islandMap.values();
    }

    @Override
    public Island getIsland(UUID uuid) {
        return islandMap.get(uuid);
    }

    @Override
    public CompletableFuture<Boolean> createIsland(Island island) {
        islandMap.put(island.getUniqueId(), island);

        try {
            plugin.getWorldManager().create(island);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        putLocations(island);

        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> loadIsland(Island island) {
        islandMap.put(island.getUniqueId(), island);

        putLocations(island);

        return CompletableFuture.completedFuture(true);
    }

    private void putLocations(Island island) {
        TreeMap<Integer, Island> map = new TreeMap<>();
        int range = island.getUpgrade(Upgrade.Type.SIZE).value();
        if (range % 2 == 0)
            range += 1;
        map.put(island.getCenterLocation().getBlockZ() - range, island);
        locationMap.put(island.getCenterLocation().getBlockX() - range, map);
    }

    @Override
    public void unloadIsland(Island island) {
        if (!this.islandMap.containsKey(island.getUniqueId())) return;

        plugin.debug(String.format("Unloading island... [%s]", island.getUniqueId().toString()));

        this.islandMap.remove(island.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDatabase().saveIsland(island));
    }

    @Override
    public void deleteIsland(UUID islandId) {
        plugin.debug(String.format("Deleting island... [%s]", islandId.toString()));

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDatabase().deleteIsland(islandId));

        Island island = getIsland(islandId);

        for (SPlayer visitor : island.getVisitors()) {
            if (visitor.getPlayer() != null) {
                visitor.getPlayer().sendMessage(ChatUtils.format(
                        "<red>The island you are on has been deleted. " +
                                "Teleporting to the spawn...")
                );
                spawnLocation.teleport(visitor.getPlayer());
            }
        }

        for (SPlayer member : island.getIslandMembers()) {
            member.setRole(null);
            member.setIslandId(null);
            member.setChatEnabled(false);
            if (member.getPlayer() != null) {
                member.getPlayer().sendMessage(ChatUtils.format(
                        "<red>Your island has been deleted.")
                );
            }
        }

        island.getOwner().setRole(null);
        island.getOwner().setIslandId(null);
        island.getOwner().setChatEnabled(false);
        if (island.getOwner().getPlayer() != null) {
            island.getOwner().getPlayer().sendMessage(ChatUtils.format(
                    "<red>Your island has been deleted.")
            );
        }

        this.islandMap.remove(islandId);
    }

    @Override
    public boolean isIslandWorld(String world) {
        return world.startsWith("skyblock_");
    }

    @Override
    public boolean isInIsland(SPlayer player, Island island) {
        Preconditions.checkNotNull(island, "island");
        Preconditions.checkNotNull(player, "player");

        return island.equals(getIslandPlayerIn(player));
    }

    @Override
    public Island getIsland(Location location) {
        // idea is from BentoBox, big credit to them.
        Map.Entry<Integer, TreeMap<Integer, Island>> entry = locationMap.floorEntry(location.getBlockX());
        if (entry == null) return null;

        Map.Entry<Integer, Island> zEntry = entry.getValue().floorEntry(location.getBlockZ());
        if (zEntry == null) return null;

        Island island = zEntry.getValue();
        if (island.isInBorder(location))
            return island;

        return null;
    }

    @Override
    public Island getIslandPlayerIn(Player player) {
        if (player == null) return null;

        return getIsland(player.getLocation());
    }

    @Override
    public void teleportToIsland(Player player, UUID islandId) {
        Island island = getIsland(islandId);
        Location location = island.getLocation(World.Environment.NORMAL);
        player.teleportAsync(location);
    }

    @Override
    public boolean lockIsland(Island island) {
        Preconditions.checkArgument(island != null, "island");

        IslandLockEvent event = new IslandLockEvent(island);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        for (SPlayer visitor : island.getVisitors()) {
            if (island.getUniqueId().equals(visitor.getIslandId())) continue;
            if (island.getCoopPlayers().contains(visitor)) continue;

            Player player = visitor.getPlayer();
            if (player != null)
                SkyBlockPlugin.getInstance().getIslandManager().getSpawnLocation().teleport(player);
        }

        island.setLocked(true);

        return true;
    }

    @Override
    public boolean unlockIsland(Island island) {
        Preconditions.checkArgument(island != null, "island");

        IslandUnlockEvent event = new IslandUnlockEvent(island);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        island.setLocked(false);

        return true;
    }

    @Override
    public boolean shouldUnload(Island island) {
        return false;
    }

}
