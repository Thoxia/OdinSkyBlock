package com.thoxia.odin.skyblock.api.schematic.paster;

import org.bukkit.Location;

import java.io.File;

public interface SchematicPaster {

    void paste(Location location, File schematic);

}
