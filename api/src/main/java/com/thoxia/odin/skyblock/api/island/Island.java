package com.thoxia.odin.skyblock.api.island;

import com.thoxia.odin.skyblock.api.island.bank.IslandBank;
import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.api.schematic.ISchematic;
import com.thoxia.odin.skyblock.api.upgrade.Upgrade;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Island {

    UUID getUniqueId();

    long getCreationTime();

    ISchematic getSchematic();

    Map<IslandPermission, IIslandRole> getPermissionMap();

    Set<SPlayer> getIslandMembers();

    Set<SPlayer> getBannedMembers();

    Set<SPlayer> getCoopPlayers();

    Set<SPlayer> getVisitors();

    Map<World.Environment, Location> getTeleportLocations();

    Location getVisitorLocation();

    SPlayer getOwner();

    Component getName();

    Component getDescription();

    String getDiscord();

    Biome getBiome();

    boolean isLocked();

    void setLocked(boolean locked);

    IslandBank getIslandBank();

    long getWorth();

    void setWorth(long decimal);

    long getLevel();

    void setLevel(long decimal);

    Upgrade getUpgrade(Upgrade.Type type);

    void setUpgrade(Upgrade upgrade);

    Location getLocation(World.Environment environment);

    Location getCenterLocation();

    void setCenterLocation(Location location);

    boolean setLocation(World.Environment environment, Location location);

    boolean isInBorder(Location location);

    boolean setDiscord(String discord);

    boolean setBiome(Biome biome);

    boolean setDescription(Component description);

    boolean setName(Component name);

    boolean setOwner(SPlayer player);

    boolean setVisitorLocation(Location visitorLocation);

    /**
     * @param player Player
     * @return The rating. -1 if player did not rate the island.
     */
    int getRating(UUID player);

    void addRating(UUID player, int rating);

    Map<UUID, Integer> getRatings();

    boolean hasPermission(SPlayer player, IslandPermission permission);

}
