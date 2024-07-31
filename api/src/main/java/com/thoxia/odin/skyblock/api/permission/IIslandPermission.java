package com.thoxia.odin.skyblock.api.permission;

import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;

import java.util.List;

public interface IIslandPermission extends Listener {

    String getName();

    void checkAndCancel(Cancellable event, SPlayer player);

    default boolean check(Island island, SPlayer player) {
        return !island.hasPermission(player, this);
    }

    default IIslandRole getDefaultRole() {
        return SkyBlock.getInstance().getIslandRoleManager().getIslandRole(this);
    }

    default Component getDisplayName() {
        return ChatUtils.format(SkyBlock.getInstance().getPermissions().getString("Permissions." + getName() + ".displayName"));
    }

    default List<Component> getLore() {
        return ChatUtils.format(SkyBlock.getInstance().getPermissions().getStringList("Permissions." + getName() + ".lore"));
    }

    default Material getMaterial() {
        //TODO: will be returned from material config
        return null;
    }

}
