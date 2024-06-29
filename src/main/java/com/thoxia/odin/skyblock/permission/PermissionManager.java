package com.thoxia.odin.skyblock.permission;

import com.thoxia.odin.skyblock.api.permission.IPermissionManager;
import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.permission.permissions.listening.PlacePermission;
import com.thoxia.odin.skyblock.permission.permissions.normal.FlyPermission;
import com.thoxia.odin.skyblock.permission.permissions.normal.InvitePermission;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PermissionManager implements IPermissionManager {

    private final Map<String, IslandPermission> permissionMap = new HashMap<>();

    public void onEnable() {
        register(
                new InvitePermission(),
                new PlacePermission(),
                new FlyPermission()
        );
    }

    public void register(IslandPermission permission) {
        this.permissionMap.put(permission.getName(), permission);
    }

    public void register(IslandPermission... permissions) {
        Arrays.stream(permissions).forEach(this::register);
    }

    public IslandPermission getPermission(String s) {
        return this.permissionMap.get(s);
    }

}
