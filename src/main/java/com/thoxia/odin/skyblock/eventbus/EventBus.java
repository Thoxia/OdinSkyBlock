package com.thoxia.odin.skyblock.eventbus;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class EventBus {

    @Getter @Setter
    private static boolean callEvents;

    public static void callEvent(Event event) {
        if (!callEvents) return;

        Bukkit.getPluginManager().callEvent(event);
    }

}
