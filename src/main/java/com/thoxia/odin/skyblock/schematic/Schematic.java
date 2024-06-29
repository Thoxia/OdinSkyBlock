package com.thoxia.odin.skyblock.schematic;

import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.schematic.ISchematic;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
public class Schematic implements ISchematic {

    private final String name, displayName, permission;
    private final List<String> description;
    private final Material icon;
    private final Map<World.Environment, String> fileMap;

    @Override
    public void paste(Island island) {
        for (Map.Entry<World.Environment, Location> entry : island.getTeleportLocations().entrySet()) {
            SkyBlock.getInstance().getSchematicManager().getPaster().paste(entry.getValue(), new File(fileMap.get(entry.getKey())));
        }
    }

}
