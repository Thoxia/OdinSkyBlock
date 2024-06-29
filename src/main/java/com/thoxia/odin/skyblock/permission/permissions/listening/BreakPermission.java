package com.thoxia.odin.skyblock.permission.permissions.listening;

import com.thoxia.odin.skyblock.api.role.IslandRole;
import com.thoxia.odin.skyblock.permission.ListeningPermission;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakPermission extends ListeningPermission {

    public BreakPermission() {
        super("break", IslandRole.COOP, Material.IRON_PICKAXE);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

}
