package com.thoxia.odin.skyblock.api.event;

import com.thoxia.odin.skyblock.api.island.Island;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public abstract class IslandEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Island island;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
