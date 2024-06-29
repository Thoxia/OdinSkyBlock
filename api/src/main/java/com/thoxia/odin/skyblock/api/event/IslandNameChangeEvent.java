package com.thoxia.odin.skyblock.api.event;

import com.thoxia.odin.skyblock.api.island.Island;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;

public class IslandNameChangeEvent extends IslandEvent implements Cancellable {

    private boolean cancelled = false;
    @Getter @Setter
    private Component newName;

    public IslandNameChangeEvent(Island island, Component newName) {
        super(island);
        this.newName = newName;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
