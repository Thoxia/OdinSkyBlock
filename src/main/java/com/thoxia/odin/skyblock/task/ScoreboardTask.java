package com.thoxia.odin.skyblock.task;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import fr.mrmicky.fastboard.FastBoard;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class ScoreboardTask extends BukkitRunnable {

    private final SkyBlockPlugin plugin;

    @Override
    public void run() {
        for (FastBoard board : plugin.getBoardMap().values()) {
            Island island = plugin.getIslandManager().getIsland(board.getPlayer().getLocation());
            board.updateLines(
                    "",
                    "Current Island: " + (island == null ? "none" : island.getOwner().getName()),
                    ""
            );
        }
    }
}
