package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.config.Messages;
import com.thoxia.odin.skyblock.permission.IslandPermission;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(value = "is", alias = "island")
public class LockCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("lock")
    @Permission("skyblock.island.lock")
    public void lockCommand(Player player) {
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        if (sPlayer == null) {
            player.sendMessage(ChatUtils.format("<red>Your data is still loading, please try again."));
            return;
        }

        if (sPlayer.getIslandId() == null) {
            player.sendMessage(ChatUtils.format("<red>You don't have an island."));
            return;
        }

        Island island = sPlayer.getIsland();
        if (island == null) {
            player.sendMessage(ChatUtils.format("<red>You must be on your island."));
            return;
        }

        if (!island.hasPermission(sPlayer, IslandPermission.LOCK)) {
            Messages.ACTION_CANCELLED.sendMessage(player);
            return;
        }

        if (island.isLocked()) {
            if (plugin.getIslandManager().unlockIsland(island)) {
                player.sendMessage(ChatUtils.format("<green>Unlocked your island."));
            }
        } else {
            if (plugin.getIslandManager().lockIsland(island)) {
                player.sendMessage(ChatUtils.format("<red>Locked your island."));
            }
        }
    }

}
