package com.thoxia.odin.skyblock.permission;

import com.thoxia.odin.skyblock.api.permission.IPermissionManager;

public class PermissionManager implements IPermissionManager {

    @Override
    public IslandPermission getPermission(String s) {
        return IslandPermission.fromName(s);
    }

}
