package com.thoxia.odin.skyblock.database.impl.yaml;

import com.google.common.base.Preconditions;
import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.database.Database;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.island.bank.log.BankAction;
import com.thoxia.odin.skyblock.api.island.bank.log.BankLog;
import com.thoxia.odin.skyblock.api.location.SLocation;
import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import com.thoxia.odin.skyblock.api.schematic.ISchematic;
import com.thoxia.odin.skyblock.api.upgrade.Upgrade;
import com.thoxia.odin.skyblock.api.util.LocationUtils;
import com.thoxia.odin.skyblock.player.SSPlayer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@RequiredArgsConstructor
public class YamlDatabase implements Database {

    private static final File ISLANDS_FOLDER = new File(SkyBlockPlugin.getInstance().getDataFolder(), "island-data");
    private static final File PLAYERS_FOLDER = new File(SkyBlockPlugin.getInstance().getDataFolder(), "player-data");
    private static final YamlData OTHER_DATA = new YamlData(SkyBlockPlugin.getInstance(), "data.yml");

    private final SkyBlockPlugin plugin;

    @Override
    public void onEnable() {
        if ((!ISLANDS_FOLDER.exists() && !ISLANDS_FOLDER.mkdirs())
                || (!PLAYERS_FOLDER.exists() && !PLAYERS_FOLDER.mkdirs())) {
            plugin.log("Could not create data folder!", Level.SEVERE);
            return;
        }

        OTHER_DATA.create();
        if (OTHER_DATA.isSet("last-location")) {
            plugin.getWorldManager().setLastLocation(LocationUtils.getLocation(OTHER_DATA.getString("last-location")));
        } else {
            World world = Bukkit.getWorld(plugin.getWorldManager().getNormalWorldName(null));
            plugin.getWorldManager().setLastLocation(new Location(world, 0, 99, 0));
        }

        if (OTHER_DATA.isSet("spawn-location")) {
            plugin.getIslandManager().setSpawnLocation(SLocation.deserialize(OTHER_DATA.getString("spawn-location")));
        }

        File[] files = ISLANDS_FOLDER.listFiles();
        if (files != null) {
            for (File file : files) {
                UUID islandId = UUID.fromString(file.getName().replace(".yml", ""));
                Island island = loadIslandData(islandId);
                plugin.getIslandManager().loadIsland(island);
            }
        }
    }

    @Override
    public void onDisable() {
        Database.super.onDisable();

        if (plugin.getIslandManager().getSpawnLocation() != null) {
            OTHER_DATA.set("spawn-location", plugin.getIslandManager().getSpawnLocation().serialize());
        }

        OTHER_DATA.set("last-location", LocationUtils.getLocation(plugin.getWorldManager().getLastLocation()));
        OTHER_DATA.save();
    }

    @Override
    public Island loadIslandData(UUID islandUuid) {
        YamlData data = new YamlData(ISLANDS_FOLDER.getPath(), islandUuid.toString());
        data.create();

        // stuff related to island
        UUID ownerId = UUID.fromString(data.getString("owner"));
        SPlayer owner = loadPlayerData(ownerId, false);
        long creationTime = data.getLong("creationTime");
        ISchematic schematic = plugin.getSchematicManager().getSchematic(data.getString("schematic"));
        com.thoxia.odin.skyblock.island.Island island = new com.thoxia.odin.skyblock.island.Island(owner, islandUuid, creationTime, schematic);
        Location normalLocation = LocationUtils.getLocation(data.getString("normalLocation"));
        Location netherLocation = LocationUtils.getLocation(data.getString("netherLocation"));
        Location endLocation = LocationUtils.getLocation(data.getString("endLocation"));
        Location visitorLocation = LocationUtils.getLocation(data.getString("visitorLocation"));
        Location centerLocation = LocationUtils.getLocation(data.getString("centerLocation"));
        String name = data.getString("name");
        String description = data.getString("description");
        String discord = data.getString("discord");
        Biome biome = Biome.valueOf(data.getString("biome"));
        boolean locked = data.getBoolean("locked");
        long level = data.getLong("level");
        long worth = data.getLong("worth");

        island.setLocation(World.Environment.NORMAL, normalLocation);
        island.setLocation(World.Environment.NETHER, netherLocation);
        island.setLocation(World.Environment.THE_END, endLocation);
        island.setVisitorLocation(visitorLocation);
        island.setCenterLocation(centerLocation);
        if (name != null)
            island.setName(GsonComponentSerializer.gson().deserialize(name));
        if (description != null)
            island.setDescription(GsonComponentSerializer.gson().deserialize(description));
        island.setDiscord(discord);
        island.setBiome(biome);
        island.setLocked(locked);
        island.setLevel(level);
        island.setWorth(worth);

        // members
        if (data.isSet("Members")) {
            for (String s : data.getConfigurationSection("Members").getKeys(false)) {
                IslandRole role = IslandRole.valueOf(data.getString("Members." + s));
                SPlayer player = loadPlayerData(UUID.fromString(s));
                switch (role) {
                    case COOP -> island.getCoopPlayers().add(player);
                    case MEMBER, ADMIN, OWNER -> island.getIslandMembers().add(player);
                }
            }
        }

        // island bank
        island.getIslandBank().setBalance(BigDecimal.valueOf(data.getDouble("Bank.balance")));
        List<String> logs = data.getStringList("Bank.logs");
        for (String s : logs) {
            String[] log = s.split(",");
            SPlayer player = loadPlayerData(UUID.fromString(log[0]));
            BankAction action = BankAction.valueOf(log[1]);
            BigDecimal amount = new BigDecimal(log[2]);
            long time = Long.parseLong(log[3]);
            BankLog bankLog = new BankLog(player, action, amount, time);
            island.getIslandBank().addLog(bankLog);
        }

        // upgrades.yml
        if (data.isSet("Upgrades")) {
            for (String s : data.getConfigurationSection("Upgrades").getKeys(false)) {
                Upgrade.Type type = Upgrade.Type.valueOf(s);
                int l = data.getInt("Upgrades." + s);
                Upgrade upgrade = plugin.getUpgradeManager().getUpgrade(type, l);
                island.setUpgrade(upgrade);
            }
        }

        // permissions
        if (data.isSet("Permissions")) {
            for (String s : data.getConfigurationSection("Permissions").getKeys(false)) {
                IslandRole role = IslandRole.valueOf(data.getString("Permissions." + s));
                IslandPermission permission = plugin.getPermissionManager().getPermission(s);
                island.getPermissionMap().put(permission, role);
            }
        }

        // ratings
        if (data.isSet("Ratings")) {
            for (String s : data.getConfigurationSection("Ratings").getKeys(false)) {
                int rating = data.getInt("Ratings." + s, -1);
                island.addRating(UUID.fromString(s), rating);
            }
        }

        return island;
    }

    @Override
    public SPlayer loadPlayerData(UUID playerUuid, boolean loadAnyway) {
        SPlayer player = SkyBlock.getInstance().getPlayerManager().getPlayer(playerUuid);
        if (player != null && !loadAnyway) {
            return player;
        }

        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), playerUuid.toString());
        data.create();

        player = new SSPlayer(playerUuid);

        String name = data.getString("name");

        if (data.getString("island") != null) {
            UUID islandId = UUID.fromString(data.getString("island"));
            player.setIslandId(islandId);
        }

        if (data.getString("role") != null) {
            IslandRole role = IslandRole.valueOf(data.getString("role"));
            player.setRole(role);
        }

        boolean chat = data.getBoolean("chat");
        int islandCreation = data.getInt("islandCreation");

        player.setName(name);
        player.setChatEnabled(chat);
        player.setIslandCreation(islandCreation);

        plugin.getPlayerManager().addPlayer(player);

        return player;
    }

    @Override
    public SPlayer loadPlayerData(String playerName, boolean loadAnyway) {
        SPlayer player = SkyBlock.getInstance().getPlayerManager().getPlayer(playerName);
        if (player != null && !loadAnyway) {
            return player;
        }

        File[] files = PLAYERS_FOLDER.listFiles();
        if (files == null) return null;

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;

            YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), file.getName());
            data.create();

            if (!playerName.equals(data.getString("name"))) continue;

            player = new SSPlayer(UUID.fromString(file.getName().replace(".yml", "")));
            if (data.getString("island") != null) {
                UUID islandId = UUID.fromString(data.getString("island"));
                player.setIslandId(islandId);
            }

            if (data.getString("role") != null) {
                IslandRole role = IslandRole.valueOf(data.getString("role"));
                player.setRole(role);
            }

            boolean chat = data.getBoolean("chat");
            int islandCreation = data.getInt("islandCreation");

            player.setName(playerName);
            player.setChatEnabled(chat);
            player.setIslandCreation(islandCreation);

            plugin.getPlayerManager().addPlayer(player);

            return player;
        }

        return null;
    }

    @Override
    public void deleteIsland(UUID islandUuid) {
        File file = new File(ISLANDS_FOLDER.getPath(), islandUuid.toString() + ".yml");
        file.delete();
    }

    @Override
    public void saveIsland(Island island) {
        YamlData data = new YamlData(ISLANDS_FOLDER.getPath(), island.getUniqueId().toString());
        data.create();

        // stuff related to island
        data.set("owner", island.getOwner().getUniqueId().toString());
        data.set("creationTime", island.getCreationTime());
        data.set("schematic", island.getSchematic().getName());
        data.set("normalLocation", LocationUtils.getLocation(island.getLocation(World.Environment.NORMAL)));
        data.set("netherLocation", LocationUtils.getLocation(island.getLocation(World.Environment.NETHER)));
        data.set("endLocation", LocationUtils.getLocation(island.getLocation(World.Environment.THE_END)));
        data.set("visitorLocation", LocationUtils.getLocation(island.getVisitorLocation()));
        data.set("centerLocation", LocationUtils.getLocation(island.getCenterLocation()));
        Component name = island.getName();
        if (name == null) name = Component.empty();
        Component description = island.getDescription();
        if (description == null) description = Component.empty();
        data.set("name", GsonComponentSerializer.gson().serialize(name));
        data.set("description", GsonComponentSerializer.gson().serialize(description));
        data.set("discord", island.getDiscord());
        data.set("biome", island.getBiome().name());
        data.set("locked", island.isLocked());
        data.set("level", island.getLevel());
        data.set("worth", island.getWorth());

        // members
        for (SPlayer member : island.getIslandMembers()) {
            data.set("Members." + member.getUniqueId(), member.getRole().name());
        }

        for (SPlayer member : island.getCoopPlayers()) {
            data.set("Members." + member.getUniqueId(), IslandRole.COOP.name());
        }

        // island bank
        data.set("Bank.balance", island.getIslandBank().getBalance().doubleValue());
        List<String> logs = new ArrayList<>();
        for (BankLog log : island.getIslandBank().getLogs()) {
            logs.add(log.player().getUniqueId() + "," + log.action().name() + "," + log.amount() + "," + log.timestamp());
        }
        data.set("Bank.logs", logs);

        // upgrades
        for (Upgrade.Type type : Upgrade.Type.VALUES) {
            Upgrade upgrade = island.getUpgrade(type);
            if (upgrade == null) continue;
            data.set("Upgrades." + type.name(), upgrade.level());
        }

        // permissions
        for (Map.Entry<IslandPermission, IslandRole> entry : island.getPermissionMap().entrySet()) {
            data.set("Permissions." + entry.getKey().getName(), entry.getValue().name());
        }

        // ratings
        for (Map.Entry<UUID, Integer> entry : island.getRatings().entrySet()) {
            data.set("Ratings." + entry.getKey(), entry.getValue());
        }

        data.save();
    }

    @Override
    public void savePlayerData(SPlayer player) {
        Preconditions.checkArgument(player != null, "player");

        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), player.getUniqueId().toString());
        data.create();

        data.set("name", player.getName());

        if (player.getIslandId() != null)
            data.set("island", player.getIslandId().toString());
        else
            data.set("island", null);

        if (player.getRole() != null)
            data.set("role", player.getRole().name());
        else
            data.set("role", null);

        data.set("chat", player.isChatEnabled());
        data.set("islandCreation", player.getIslandCreation());

        data.save();
    }

    @Override
    public UUID getIslandId(String player) {
        SPlayer sPlayer = loadPlayerData(player);
        if (sPlayer == null) return null;

        return sPlayer.getIslandId();
    }
}
