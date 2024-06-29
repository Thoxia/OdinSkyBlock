package com.thoxia.odin.skyblock.api.event;

import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class IslandOwnerChangeEvent extends IslandEvent implements Cancellable {

    private boolean cancelled = false;
    @Getter @Setter
    private SPlayer newOwner;

    public IslandOwnerChangeEvent(Island island, SPlayer newOwner) {
        super(island);
        this.newOwner = newOwner;
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
