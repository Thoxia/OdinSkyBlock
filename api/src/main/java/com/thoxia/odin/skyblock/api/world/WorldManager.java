package com.thoxia.odin.skyblock.api.world;

import com.thoxia.odin.skyblock.api.island.Island;
import org.bukkit.Location;

public interface WorldManager {

    String getName();

    void loadWorlds();

    void create(Island island) throws Exception;

    Location getNextLocation();

    Location getLastLocation();

    void setLastLocation(Location location);

    String getWorldPrefix(Island island);

    String getNetherWorldName(Island island);

    String getNormalWorldName(Island island);

    String getEndWorldName(Island island);

}
