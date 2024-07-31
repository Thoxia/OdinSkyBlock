package com.thoxia.odin.skyblock.island;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.event.*;
import com.thoxia.odin.skyblock.api.island.bank.IslandBank;
import com.thoxia.odin.skyblock.api.permission.IIslandPermission;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.api.schematic.ISchematic;
import com.thoxia.odin.skyblock.api.upgrade.Upgrade;
import com.thoxia.odin.skyblock.eventbus.EventBus;
import com.thoxia.odin.skyblock.player.SSPlayer;
import com.thoxia.odin.skyblock.role.IslandRole;
import com.thoxia.odin.skyblock.schematic.Schematic;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.*;

public class Island implements com.thoxia.odin.skyblock.api.island.Island {

    @Getter private final UUID uniqueId;
    @Getter private final long creationTime;
    @Getter private final ISchematic schematic;
    @Getter private final Map<IIslandPermission, IIslandRole> permissionMap = new HashMap<>();
    @Getter private final Set<SPlayer> islandMembers = new HashSet<>();
    @Getter private final Set<SPlayer> bannedMembers = new HashSet<>();
    @Getter private final Set<SPlayer> coopPlayers = new HashSet<>();
    @Getter private final Map<World.Environment, Location> teleportLocations = new HashMap<>();

    /**
     * includes the island members who are on the island currently
     */
    @Getter private final Set<SPlayer> visitors = new HashSet<>();

    @Getter private Location visitorLocation;
    @Getter @Setter private Location centerLocation;

    @Getter private SPlayer owner;
    @Getter private Component name;
    @Getter private Component description;
    @Getter private String discord;

    @Getter private org.bukkit.block.Biome biome = Biome.PLAINS;

    @Getter @Setter private boolean locked;

    @Getter private final IslandBank islandBank = new IslandBank(this);

    @Getter @Setter private BigDecimal worth = BigDecimal.ZERO;
    @Getter @Setter private BigDecimal level = BigDecimal.ZERO;

    private final Map<Upgrade.Type, Upgrade> upgrades = new HashMap<>();

    @Getter private final Map<UUID, Integer> ratings = new HashMap<>();

    public Island(SSPlayer owner, Schematic schematic) {
        this.owner = owner;
        this.uniqueId = UUID.randomUUID();
        this.creationTime = System.currentTimeMillis();
        this.schematic = schematic;
    }

    public Island(SPlayer owner) {
        this.owner = owner;
        this.uniqueId = UUID.randomUUID();
        this.creationTime = System.currentTimeMillis();
        this.schematic = SkyBlockPlugin.getInstance().getSchematicManager().getDefaultSchematic();
    }

    public Island(SPlayer owner, UUID uuid, long creationTime, ISchematic schematic) {
        this.owner = owner;
        this.uniqueId = uuid;
        this.creationTime = creationTime;
        this.schematic = schematic;
    }

    public Upgrade getUpgrade(Upgrade.Type type) {
        return upgrades.getOrDefault(type, SkyBlockPlugin.getInstance().getUpgradeManager().getUpgrade(type, 0));
    }

    public void setUpgrade(Upgrade upgrade) {
        upgrades.put(upgrade.type(), upgrade);
    }

    public Location getLocation(World.Environment environment) {
        Location location = this.teleportLocations.get(environment);

        if (location == null) {
            String name = switch (environment) {
                case NETHER -> SkyBlockPlugin.getInstance().getWorldManager().getNetherWorldName(this);
                case NORMAL, CUSTOM -> SkyBlockPlugin.getInstance().getWorldManager().getNormalWorldName(this);
                case THE_END ->  SkyBlockPlugin.getInstance().getWorldManager().getEndWorldName(this);
            };

            this.teleportLocations.put(environment, new Location(Bukkit.getWorld(name), 0, 100, 0));

            return this.teleportLocations.get(environment);
        }

        return location;
    }

    public boolean setLocation(World.Environment environment, Location location) {
        IslandLocationChangeEvent event = new IslandLocationChangeEvent(this, location, IslandLocationChangeEvent.Type.OTHER);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        this.teleportLocations.put(environment, event.getNewLocation());

        return true;
    }

    @Override
    public boolean isInBorder(Location location) {
        double size = this.getUpgrade(Upgrade.Type.SIZE).value() / 2d;
        if (size % 2 == 0)
            size += 1;
        double x = Math.abs(location.getX() - centerLocation.getX());
        double z = Math.abs(location.getZ() - centerLocation.getZ());
        return x <= size && z <= size;
    }

    public boolean setDiscord(String discord) {
        IslandDiscordChangeEvent event = new IslandDiscordChangeEvent(this, discord);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        this.discord = event.getNewDiscord();

        return true;
    }

    public boolean setBiome(org.bukkit.block.Biome biome) {
        IslandBiomeChangeEvent event = new IslandBiomeChangeEvent(this, biome);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        this.biome = event.getNewBiome();

        return true;
    }

    public boolean setDescription(Component description) {
        IslandDescriptionChangeEvent event = new IslandDescriptionChangeEvent(this, description);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        this.description = event.getNewDescription();

        return true;
    }

    public boolean setName(Component name) {
        IslandNameChangeEvent event = new IslandNameChangeEvent(this, name);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        this.name = event.getNewName();

        return true;
    }

    public boolean setOwner(SPlayer player) {
        IslandOwnerChangeEvent event = new IslandOwnerChangeEvent(this, player);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        this.owner = event.getNewOwner();

        return true;
    }

    public boolean setVisitorLocation(Location visitorLocation) {
        IslandLocationChangeEvent event = new IslandLocationChangeEvent(this, visitorLocation, IslandLocationChangeEvent.Type.VISITOR);
        EventBus.callEvent(event);
        if (event.isCancelled()) return false;

        this.visitorLocation = event.getNewLocation();

        return true;
    }

    @Override
    public int getRating(UUID player) {
        return ratings.getOrDefault(player, -1);
    }

    @Override
    public void addRating(UUID player, int rating) {
        this.ratings.put(player, rating);
    }

    public boolean hasPermission(SPlayer player, IIslandPermission IIslandPermission) {
        if (player == null) return false;

        IIslandRole role = this.uniqueId.equals(player.getIslandId()) ? player.getRole() :
                this.coopPlayers.contains(player) ? IslandRole.coopRole() : IslandRole.visitorRole();
        IIslandRole minRole = this.permissionMap.getOrDefault(IIslandPermission, IIslandPermission.getDefaultRole());

        return role.weight() >= minRole.weight();
    }

    public boolean hasPermission(Player player, IIslandPermission IIslandPermission) {
        SPlayer sPlayer = SkyBlockPlugin.getInstance().getPlayerManager().getPlayer(player);
        if (sPlayer == null) return false;

        return hasPermission(sPlayer, IIslandPermission);
    }
}
