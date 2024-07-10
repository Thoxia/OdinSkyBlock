package com.thoxia.odin.skyblock.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderUtils {

    public static String replacePlaceholders(Player player, String message) {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) return message;

        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
    }

}
