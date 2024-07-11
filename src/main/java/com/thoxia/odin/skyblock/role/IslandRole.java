package com.thoxia.odin.skyblock.role;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class IslandRole implements IIslandRole {

    private final int id;
    private final String name;
    private final int weight;
    private final IslandRole previousRole;
    private final List<String> defaultPermissions;

    @Override
    public List<IslandPermission> getDefaultPermissions() {
        return defaultPermissions.stream()
                .map(permissionName -> SkyBlockPlugin.getInstance().getPermissionManager().getPermission(permissionName))
                .toList();
    }

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
