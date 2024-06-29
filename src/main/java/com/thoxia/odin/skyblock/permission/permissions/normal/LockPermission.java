package com.thoxia.odin.skyblock.permission.permissions.normal;

import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import org.bukkit.Material;

public class LockPermission extends IslandPermission {

    public LockPermission() {
        super("lock", IslandRole.MEMBER, Material.DARK_OAK_DOOR);
    }

}
