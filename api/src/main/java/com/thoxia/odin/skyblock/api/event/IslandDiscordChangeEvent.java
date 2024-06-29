package com.thoxia.odin.skyblock.api.event;

import com.thoxia.odin.skyblock.api.island.Island;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class IslandDiscordChangeEvent extends IslandEvent implements Cancellable {

    private boolean cancelled = false;
    @Getter @Setter
    private String newDiscord;

    public IslandDiscordChangeEvent(Island island, String newDiscord) {
        super(island);
        this.newDiscord = newDiscord;
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
