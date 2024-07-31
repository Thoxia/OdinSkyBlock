package com.thoxia.odin.skyblock.role;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.permission.IIslandPermission;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.api.role.IIslandRoleManager;
import com.thoxia.odin.skyblock.permission.IslandPermission;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class IslandRoleManager implements IIslandRoleManager {

    private static final ConfigurationSection VISITOR_SECTION = SkyBlockPlugin.getInstance().getRoles().getConfigurationSection("visitor");
    private static final ConfigurationSection COOP_SECTION = SkyBlockPlugin.getInstance().getRoles().getConfigurationSection("coop");

    private final SkyBlockPlugin plugin;

    private final LinkedHashMap<Integer, IIslandRole> islandRoleMap = new LinkedHashMap<>();
    private final Map<IIslandPermission, IIslandRole> defaultRolesOfPermissions = new HashMap<>();

    @Override
    public void onEnable() {
        IIslandRole visitorRole = loadRole(VISITOR_SECTION, null);
        IIslandRole coopRole = loadRole(COOP_SECTION, visitorRole);

        List<ConfigurationSection> rolesByWeight = new ArrayList<>();
        for (String key : plugin.getConfig().getConfigurationSection("Island-Roles").getKeys(false)) {
            ConfigurationSection islandRoleSection = plugin.getConfig().getConfigurationSection("Island-Roles." + key);
            rolesByWeight.add(islandRoleSection);
        }

        rolesByWeight.sort(Comparator.comparingInt(section -> section.getInt("weight")));
        IIslandRole previousRole = coopRole;
        for (ConfigurationSection roleSection : rolesByWeight) {
            previousRole = loadRole(roleSection, previousRole);
        }
    }

    @Override
    public IIslandRole getIslandRole(int id) {
        return islandRoleMap.get(id);
    }

    @Override
    public IIslandRole getIslandRole(IIslandPermission IIslandPermission) {
        return defaultRolesOfPermissions.get(IIslandPermission);
    }

    @Override
    public IIslandRole getDefaultRole() {
        return islandRoleMap.get(COOP_SECTION.getInt("id") + 1);
    }

    @Override
    public IIslandRole getLastRole() {
        return islandRoleMap.lastEntry().getValue();
    }

    @Override
    public IIslandRole getVisitorRole() {
        return islandRoleMap.firstEntry().getValue();
    }

    @Override
    public IIslandRole getCoopRole() {
        return islandRoleMap.get(COOP_SECTION.getInt("id"));
    }


    private IslandRole loadRole(ConfigurationSection roleSection, IIslandRole previousRole) {
        int id = roleSection.getInt("id");
        String name = roleSection.getString("name");
        int weight = roleSection.getInt("weight");
        Set<IIslandPermission> IIslandPermissions = roleSection.getStringList("permissions").stream()
                .map(IslandPermission::fromName)
                .collect(Collectors.toSet());

        if (previousRole != null)
            IIslandPermissions.addAll(previousRole.defaultPermissions());

        IslandRole islandRole = new IslandRole(id, name, weight, IIslandPermissions);
        islandRoleMap.put(id, islandRole);

        IIslandPermissions.forEach(permission -> defaultRolesOfPermissions.putIfAbsent(permission, islandRole));

        return islandRole;
    }
}
