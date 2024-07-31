package com.thoxia.odin.skyblock.api.role;


import com.thoxia.odin.skyblock.api.permission.IIslandPermission;

public interface IIslandRoleManager {

    void onEnable();

    IIslandRole getIslandRole(int id);
    IIslandRole getIslandRole(IIslandPermission IIslandPermission);

    IIslandRole getDefaultRole();
    IIslandRole getLastRole();
    IIslandRole getVisitorRole();
    IIslandRole getCoopRole();
}
