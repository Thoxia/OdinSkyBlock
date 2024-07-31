package com.thoxia.odin.skyblock.api.role;

import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.permission.IIslandPermission;

import java.util.Set;

public interface IIslandRole {

    int id();
    String name();
    int weight();
    Set<IIslandPermission> defaultPermissions();

    default IIslandRole getPreviousRole() {
        return SkyBlock.getInstance().getIslandRoleManager().getIslandRole(id() - 1);
    }

    default IIslandRole getNextRole() {
        return SkyBlock.getInstance().getIslandRoleManager().getIslandRole(id() + 1);
    }
}
