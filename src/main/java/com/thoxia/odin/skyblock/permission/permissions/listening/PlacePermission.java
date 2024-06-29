package com.thoxia.odin.skyblock.permission.permissions.listening;

import com.thoxia.odin.skyblock.api.role.IslandRole;
import com.thoxia.odin.skyblock.permission.ListeningPermission;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class PlacePermission extends ListeningPermission {

    public PlacePermission() {
        super("place", IslandRole.COOP, Material.DIRT);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onMultiPlace(BlockMultiPlaceEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

}
