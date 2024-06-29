package com.thoxia.odin.skyblock.api.permission;

public interface IPermissionManager {

    void onEnable();

    IslandPermission getPermission(String s);

    void register(IslandPermission... permissions);

}
