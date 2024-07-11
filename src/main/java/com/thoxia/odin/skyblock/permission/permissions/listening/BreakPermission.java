package com.thoxia.odin.skyblock.permission.permissions.listening;

import com.thoxia.odin.skyblock.permission.ListeningPermission;
import com.thoxia.odin.skyblock.role.IslandRole;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakPermission extends ListeningPermission {

    public BreakPermission() {
        super("break");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        checkAndCancel(event, event.getPlayer());
    }

}
