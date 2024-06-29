package com.thoxia.odin.skyblock.upgrade;

import com.google.common.collect.HashBasedTable;
import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.upgrade.IUpgradeManager;
import com.thoxia.odin.skyblock.api.upgrade.Upgrade;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

@RequiredArgsConstructor
public class UpgradeManager implements IUpgradeManager {

    // not quite sure if this the best way to do something like this but this is the easiest way imo
    private final HashBasedTable<Upgrade.Type, Integer, Upgrade> upgrades = HashBasedTable.create();
    private final SkyBlockPlugin plugin;

    @Override
    public void onEnable() {
        for (Upgrade.Type type : Upgrade.Type.VALUES) {
            String s = type.name().toLowerCase(Locale.ENGLISH);
            ConfigurationSection section = plugin.getUpgradesFile().getConfigurationSection("Upgrades." + s);
            for (String key : section.getKeys(false)) {
                int level = section.getInt(key + ".level");
                int value = section.getInt(key + ".value");
                double price = section.getDouble(key + ".price");
                this.upgrades.put(type, level, new Upgrade("", type, level, value, price));
            }
        }
    }

    @Override
    public Upgrade getUpgrade(Upgrade.Type type, int level) {
        return this.upgrades.get(type, level);
    }

}
