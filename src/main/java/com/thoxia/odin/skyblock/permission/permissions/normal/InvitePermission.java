package com.thoxia.odin.skyblock.permission.permissions.normal;

import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import org.bukkit.Material;

public class InvitePermission extends IslandPermission {

    public InvitePermission() {
        super("invite", IslandRole.ADMIN, Material.PLAYER_HEAD);
    }

}
