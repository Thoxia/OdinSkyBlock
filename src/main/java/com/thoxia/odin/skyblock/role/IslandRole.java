package com.thoxia.odin.skyblock.role;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.permission.IIslandPermission;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import java.util.Set;

public record IslandRole(int id, String name, int weight, Set<IIslandPermission> defaultPermissions) implements IIslandRole {

    public static IIslandRole defaultRole() {
        return SkyBlockPlugin.getInstance().getIslandRoleManager().getDefaultRole();
    }

    public static IIslandRole lastRole() {
        return SkyBlockPlugin.getInstance().getIslandRoleManager().getLastRole();
    }

    public static IIslandRole visitorRole() {
        return SkyBlockPlugin.getInstance().getIslandRoleManager().getVisitorRole();
    }

    public static IIslandRole coopRole() {
        return SkyBlockPlugin.getInstance().getIslandRoleManager().getCoopRole();
    }

}
