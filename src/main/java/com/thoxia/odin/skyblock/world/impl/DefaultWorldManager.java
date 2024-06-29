package com.thoxia.odin.skyblock.world.impl;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.world.WorldManager;
import com.thoxia.odin.skyblock.config.Config;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.block.BlockFace;

@RequiredArgsConstructor
public class DefaultWorldManager implements WorldManager {

    private Location lastLocation;

    private final SkyBlockPlugin plugin;

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public void loadWorlds() {

        World normalWorld = Bukkit.getWorld(getNormalWorldName(null));
        World netherWorld = Bukkit.getWorld(getNetherWorldName(null));
        World endWorld = Bukkit.getWorld(getEndWorldName(null));

        if (normalWorld == null) {
            plugin.log("Creating normal island world...");
            normalWorld = WorldCreator.name(getNormalWorldName(null)).type(WorldType.FLAT)
                    .environment(World.Environment.NORMAL).generator(new VoidGenerator()).createWorld();
            registerToMultiverse(normalWorld);
            plugin.log("Successfully created normal island world...");
        }

        if (netherWorld == null) {
            plugin.log("Creating nether island world...");
            netherWorld = WorldCreator.name(getNetherWorldName(null)).type(WorldType.FLAT)
                    .environment(World.Environment.NETHER).generator(new VoidGenerator()).createWorld();
            registerToMultiverse(netherWorld);
            plugin.log("Successfully created nether island world...");
        }

        if (endWorld == null) {
            plugin.log("Creating end island world...");
            endWorld = WorldCreator.name(getEndWorldName(null)).type(WorldType.FLAT)
                    .environment(World.Environment.THE_END).generator(new VoidGenerator()).createWorld();
            registerToMultiverse(endWorld);
            plugin.log("Successfully created end island world...");
        }

        if (normalWorld != null)
            normalWorld.setDifficulty(Difficulty.NORMAL);

        if (netherWorld != null)
            netherWorld.setDifficulty(Difficulty.NORMAL);

        if (endWorld != null)
            endWorld.setDifficulty(Difficulty.NORMAL);

    }

    private void registerToMultiverse(World world) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core") == null) return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import " + world.getName() + " normal -g " + SkyBlockPlugin.getInstance().getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv modify set generator " + SkyBlockPlugin.getInstance().getName() + " " + world.getName());
    }

    @Override
    public void create(Island island) throws Exception {
        plugin.debug("creating island...");
        Location location = getNextLocation().clone().add(.5, 0, .5);

        island.setCenterLocation(location);
        island.setVisitorLocation(location);
        island.setLocation(World.Environment.NORMAL, location);
        island.setLocation(World.Environment.NETHER, new Location(Bukkit.getWorld(getNetherWorldName(island)), location.getX(), location.getY(), location.getZ()));
        island.setLocation(World.Environment.THE_END, new Location(Bukkit.getWorld(getEndWorldName(island)), location.getX(), location.getY(), location.getZ()));

        plugin.debug("pasting schematic...");
        island.getSchematic().paste(island);

        setLastLocation(location.clone().subtract(.5, 0, .5));
    }

    @Override
    public Location getNextLocation() {
        Location location = getLastLocation().clone();
        int range = Config.ISLAND_RANGE.getAsInt();
        BlockFace islandFace = getIslandFace(location);

        if (islandFace == BlockFace.NORTH) {
            location.add(range, 0, 0);
        } else if (islandFace == BlockFace.EAST) {
            if (location.getX() == -location.getZ())
                location.add(range, 0, 0);
            else if (location.getX() == location.getZ())
                location.subtract(range, 0, 0);
            else
                location.add(0, 0, range);
        } else if (islandFace == BlockFace.SOUTH) {
            if (location.getX() == -location.getZ())
                location.subtract(0, 0, range);
            else
                location.subtract(range, 0, 0);
        } else if (islandFace == BlockFace.WEST) {
            if (location.getX() == location.getZ())
                location.add(range, 0, 0);
            else
                location.subtract(0, 0, range);
        }

        return location;
    }

    // huge credit goes to bg-software-llc
    private BlockFace getIslandFace(Location location) {
        if (location.getX() >= location.getZ())
            return -location.getX() > location.getZ() ? BlockFace.NORTH : BlockFace.EAST;
        else
            return -location.getX() > location.getZ() ? BlockFace.WEST : BlockFace.SOUTH;
    }

    @Override
    public Location getLastLocation() {
        return this.lastLocation;
    }

    @Override
    public void setLastLocation(Location location) {
        this.lastLocation = location;
    }

    @Override
    public String getWorldPrefix(Island island) {
        return "skyblock";
    }

    @Override
    public String getNetherWorldName(Island island) {
        return getWorldPrefix(island) + "_nether";
    }

    @Override
    public String getNormalWorldName(Island island) {
        return getWorldPrefix(island) + "_normal";
    }

    @Override
    public String getEndWorldName(Island island) {
        return getWorldPrefix(island) + "_end";
    }

}
