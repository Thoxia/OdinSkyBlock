package com.thoxia.odin.skyblock.api.schematic;

import com.thoxia.odin.skyblock.api.island.Island;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

public interface ISchematic {

    String getName();

    String getDisplayName();

    String getPermission();

    List<String> getDescription();

    Material getIcon();

    Map<World.Environment, String> getFileMap();

    void paste(Island island);

}
