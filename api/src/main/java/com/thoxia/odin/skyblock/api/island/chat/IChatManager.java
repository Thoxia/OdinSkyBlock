package com.thoxia.odin.skyblock.api.island.chat;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IChatManager {

    void sendMessage(Player player, UUID islandId, Component message);

}
