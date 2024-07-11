package com.thoxia.odin.skyblock.api.role;

import com.thoxia.odin.skyblock.api.permission.IslandPermission;

import java.util.List;
import java.util.Set;

public interface IIslandRole {

    int getId();
    String getName();
    int getWeight();
    List<IslandPermission> getDefaultPermissions();
}
