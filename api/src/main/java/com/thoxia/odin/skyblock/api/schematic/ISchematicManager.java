package com.thoxia.odin.skyblock.api.schematic;

import com.thoxia.odin.skyblock.api.schematic.paster.SchematicPaster;

public interface ISchematicManager {

    void onEnable();

    SchematicPaster getPaster();

    ISchematic getDefaultSchematic();

    ISchematic getSchematic(String name);

}
