package com.thoxia.odin.skyblock.role;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.api.role.IIslandRoleManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class IslandRoleManager implements IIslandRoleManager {

    private static final int VISITOR_ROLE_ID = -2;
    private static final int COOP_ROLE_ID = -1;

    private final SkyBlockPlugin plugin;

    private final Map<Integer, IslandRole> islandRoleMap = new HashMap<>();
    private final List<IIslandRole> islandRolesByWeight = new ArrayList<>();

    @Override
    public void onEnable() {
        ConfigurationSection islandRolesSection = plugin.getConfig().getConfigurationSection("Island-Roles");
        IslandRole visitorRole = loadRole(islandRolesSection.getConfigurationSection("visitor"), null, -200);
        IslandRole coopRole = loadRole(islandRolesSection.getConfigurationSection("coop"), visitorRole, -100);

        List<ConfigurationSection> rolesByWeight = new ArrayList<>();
        for (String key : plugin.getConfig().getConfigurationSection("Member-Ranks").getKeys(false)) {
            ConfigurationSection islandRankSection = plugin.getConfig().getConfigurationSection("Member-Ranks." + key);
            rolesByWeight.add(islandRankSection);
        }
        rolesByWeight.sort(Comparator.comparingInt(section -> section.getInt("weight")));

        IslandRole previousRole = coopRole;
        for (ConfigurationSection roleSection : rolesByWeight) {
            previousRole = loadRole(roleSection, previousRole);
        }
    }

    @Override
    public IIslandRole getIslandRole(int id) {
        return islandRoleMap.get(id);
    }

    @Override
    public IIslandRole getDefaultRole() {
        return islandRolesByWeight.get(2);
    }

    @Override
    public IIslandRole getLastRole() {
        return islandRolesByWeight.getLast();
    }

    @Override
    public IIslandRole getVisitorRole() {
        return islandRoleMap.get(VISITOR_ROLE_ID);
    }

    @Override
    public IIslandRole getCoopRole() {
        return islandRoleMap.get(COOP_ROLE_ID);
    }

    @Override
    public List<IIslandRole> getIslandRoles() {
        return Collections.unmodifiableList(islandRolesByWeight);
    }

    private IslandRole loadRole(ConfigurationSection roleSection, IslandRole previousRole, int weight) {
        int id = roleSection.getInt("id");
        String name = roleSection.getString("name");
        List<String> permissions = roleSection.getStringList("permissions");

        IslandRole islandRole = new IslandRole(id, name, weight, previousRole, permissions);

        islandRolesByWeight.add(islandRole);
        islandRoleMap.put(id, islandRole);
        return islandRole;
    }

    private IslandRole loadRole(ConfigurationSection roleSection, IslandRole previousRole) {
        int weight = roleSection.getInt("weight");
        return loadRole(roleSection, previousRole, weight);
    }
}
