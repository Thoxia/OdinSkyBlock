package com.thoxia.odin.skyblock.schematic;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.schematic.ISchematicManager;
import com.thoxia.odin.skyblock.api.schematic.paster.SchematicPaster;
import com.thoxia.odin.skyblock.schematic.paster.SchematicPasterFactory;
import com.thoxia.odin.skyblock.schematic.paster.impl.WorldEditPaster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public class SchematicManager implements ISchematicManager {

    private final Map<String, Schematic> schematicMap = new HashMap<>();
    private final SkyBlockPlugin plugin;

    private final SchematicPasterFactory schematicPasterFactory = new SchematicPasterFactory();

    @Getter @Setter
    private SchematicPaster paster;

    public void onEnable() {
        for (String key : plugin.getSchematicsFile().getConfigurationSection("Schematics").getKeys(false)) {
            ConfigurationSection section = plugin.getSchematicsFile().getConfigurationSection("Schematics." + key);
            List<String> description = section.getStringList("description");
            String displayName = section.getString("displayName");
            String permission = section.getString("permission");
            Material icon = Material.getMaterial(section.getString("icon").toUpperCase(Locale.ENGLISH));
            Map<World.Environment, String> fileMap = new HashMap<>();
            for (String s : section.getConfigurationSection("files").getKeys(false)) {
                // schematics must be located in CSkyBlock/schematics/
                fileMap.put(World.Environment.valueOf(s.toUpperCase(Locale.ENGLISH)),
                        plugin.getDataFolder() + File.separator + "schematics" + File.separator + section.getString("files." + s));
            }

            Schematic schematic = new Schematic(key, displayName, permission, description, icon, fileMap);
            this.schematicMap.put(key, schematic);
        }

            paster = schematicPasterFactory.getSchematicPaster();
    }

    public Schematic getSchematic(String name) {
        return this.schematicMap.get(name);
    }

    public Schematic getDefaultSchematic() {
        for (Schematic schematic : schematicMap.values()) {
            if (schematic.getPermission() == null)
                return schematic;
        }

        return null;
    }

}
