package com.thoxia.odin.skyblock.api.role;

import java.util.List;

public interface IIslandRoleManager {

    void onEnable();

    IIslandRole getIslandRole(int id);
    IIslandRole getDefaultRole();
    IIslandRole getLastRole();
    IIslandRole getVisitorRole();
    IIslandRole getCoopRole();
    List<IIslandRole> getIslandRoles();
}
