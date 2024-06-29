package com.thoxia.odin.skyblock.util;

import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.upgrade.Upgrade;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class WorldBorderUtils {

    public static void send(Player player, Island island) {
        WorldBorder border = Bukkit.createWorldBorder();
        border.setCenter(island.getCenterLocation());
        double size = island.getUpgrade(Upgrade.Type.SIZE).value();
        double decrement = size % 2 == 0 ? 1 : 0;
        border.setSize(size - decrement);
        border.setDamageAmount(0);
        border.setDamageBuffer(0);
        border.setWarningDistance(0);
        border.setWarningTime(0);
        player.setWorldBorder(border);
    }

    public static void reset(Player player) {
        player.setWorldBorder(null);
    }

}
