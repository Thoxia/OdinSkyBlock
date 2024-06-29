package com.thoxia.odin.skyblock.api.event;

import com.thoxia.odin.skyblock.api.island.Island;
import org.bukkit.event.Cancellable;

public class IslandUnlockEvent extends IslandEvent implements Cancellable {

    private boolean cancelled = false;

    public IslandUnlockEvent(Island island) {
        super(island);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
