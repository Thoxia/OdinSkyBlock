package com.thoxia.odin.skyblock.api.permission;

import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public abstract class IslandPermission {

    private final String name;

    @Setter
    private IIslandRole defaultRole;

    public Component getDisplayName() {
        return ChatUtils.format(SkyBlock.getInstance().getPermissions().getString("Permissions." + name + ".displayName"));
    }

    public List<Component> getLore() {
        return ChatUtils.format(SkyBlock.getInstance().getPermissions().getStringList("Permissions." + name + ".lore"));
    }

    public Material getMaterial() {
        //TODO: will be returned from material config
        return null;
    }

    public boolean check(Island island, SPlayer player) {
        return !island.hasPermission(player, this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof IslandPermission islandPermission)) return false;
        return name.equals(islandPermission.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
