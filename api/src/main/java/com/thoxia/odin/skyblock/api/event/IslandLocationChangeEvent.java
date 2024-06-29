package com.thoxia.odin.skyblock.api.event;

import com.thoxia.odin.skyblock.api.island.Island;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class IslandLocationChangeEvent extends IslandEvent implements Cancellable {

    private boolean cancelled = false;
    @Getter @Setter
    private Location newLocation;

    @Getter @Setter
    private Type type;

    public IslandLocationChangeEvent(Island island, Location newLocation, Type type) {
        super(island);
        this.newLocation = newLocation;
        this.type = type;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public enum Type {
        VISITOR, OTHER
    }

}
