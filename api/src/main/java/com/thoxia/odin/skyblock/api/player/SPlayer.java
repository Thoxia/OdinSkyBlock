package com.thoxia.odin.skyblock.api.player;

import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface SPlayer {

    UUID getUniqueId();

    String getName();

    void setName(String s);

    int getIslandCreation();

    void setIslandCreation(int i);

    boolean isChatEnabled();

    void setChatEnabled(boolean b);

    boolean isBorderEnabled();

    void setBorderEnabled(boolean b);

    @Nullable UUID getIslandId();

    void setIslandId(UUID id);

    @Nullable
    IIslandRole getRole();

    void setRole(IIslandRole role);

    @Nullable
    Island getIsland();

    @Nullable
    default Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    @NotNull
    default OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

}
