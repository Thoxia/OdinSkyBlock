package com.thoxia.odin.skyblock.api.event;

import com.thoxia.odin.skyblock.api.island.Island;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Biome;
import org.bukkit.event.Cancellable;

public class IslandBiomeChangeEvent extends IslandEvent implements Cancellable {

    private boolean cancelled = false;
    @Getter @Setter
    private Biome newBiome;

    public IslandBiomeChangeEvent(Island island, Biome newBiome) {
        super(island);
        this.newBiome = newBiome;
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
