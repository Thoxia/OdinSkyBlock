package com.thoxia.odin.skyblock.api.permission;

import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class IslandPermission {

    private final String name;
    private final IslandRole defaultRole;
    private final Material icon;

    public Component getDisplayName() {
        return ChatUtils.format(SkyBlock.getInstance().getPermissions().getString("Permissions." + name + ".displayName"));
    }

    public List<Component> getLore() {
        return ChatUtils.format(SkyBlock.getInstance().getPermissions().getStringList("Permissions." + name + ".lore"));
    }

    public boolean check(Island island, SPlayer player) {
        return !island.hasPermission(player, this);
    }

}
