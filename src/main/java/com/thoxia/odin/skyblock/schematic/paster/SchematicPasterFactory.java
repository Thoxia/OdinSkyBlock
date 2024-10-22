package com.thoxia.odin.skyblock.schematic.paster;

import com.thoxia.odin.skyblock.api.schematic.paster.SchematicPaster;
import com.thoxia.odin.skyblock.config.Config;
import com.thoxia.odin.skyblock.schematic.paster.impl.WorldEditPaster;
import org.bukkit.Bukkit;

public class SchematicPasterFactory {

    public SchematicPaster getSchematicPaster() {
        String recommendedPaster = Config.SCHEMATIC_PASTER.getAsString();

        if(!Bukkit.getPluginManager().isPluginEnabled(recommendedPaster))
            return getDefaultSchematicPaster();

        switch (recommendedPaster) {
            case "WorldEdit":
                return new WorldEditPaster();
            default:
                return getDefaultSchematicPaster();
        }
    }

    private static SchematicPaster getDefaultSchematicPaster() {
        return new WorldEditPaster();
    }
}
