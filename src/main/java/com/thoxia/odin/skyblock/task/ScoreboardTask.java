package com.thoxia.odin.skyblock.task;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.upgrade.Upgrade;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.config.Messages;
import com.thoxia.odin.skyblock.util.PlaceholderUtils;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@RequiredArgsConstructor
public class ScoreboardTask extends BukkitRunnable {

    private final SkyBlockPlugin plugin;

    @Override
    public void run() {
        for (FastBoard board : plugin.getBoardMap().values()) {
            Player player = board.getPlayer();
            Island island = plugin.getIslandManager().getIsland(player.getLocation());
            board.updateTitle(Messages.BOARD_TITLE.getAsComponent());
            List<String> list = Messages.BOARD_LINES.getAsStringList().stream().map(s -> PlaceholderUtils.replacePlaceholders(player, s)).toList();
            board.updateLines(
                    ChatUtils.format(
                            list,
                            Placeholder.unparsed("current-island-level", (island == null ? "0" : ChatUtils.formatNumber(island.getLevel()))),
                            Placeholder.unparsed("current-island-owner", (island == null ? "none" : island.getOwner().getName())),
                            Placeholder.unparsed("current-island-size", (island == null ? "0" : String.valueOf(island.getUpgrade(Upgrade.Type.SIZE).value())))
                    )
            );
        }
    }
}
