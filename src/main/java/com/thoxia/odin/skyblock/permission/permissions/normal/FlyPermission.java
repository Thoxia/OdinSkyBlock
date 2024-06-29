package com.thoxia.odin.skyblock.permission.permissions.normal;

import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import org.bukkit.Material;

public class FlyPermission extends IslandPermission {

    public FlyPermission() {
        super("fly", IslandRole.MEMBER, Material.FEATHER);
    }

}
