package com.thoxia.odin.skyblock.player;

import com.google.common.base.Objects;
import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class SSPlayer implements SPlayer {

    private final UUID uniqueId;
    private String name;
    private int islandCreation;
    private boolean chatEnabled;
    private boolean borderEnabled = true;
    @Nullable private UUID islandId;
    @Nullable private Island visitingIsland;
    @Nullable private IslandRole role;

    @Nullable
    public Island getIsland() {
        if (this.islandId == null) return null;

        return SkyBlockPlugin.getInstance().getIslandManager().getIsland(islandId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SSPlayer sPlayer = (SSPlayer) o;
        return Objects.equal(uniqueId, sPlayer.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueId);
    }

}
