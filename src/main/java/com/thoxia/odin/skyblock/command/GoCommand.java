package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command(value = "island", alias = "is")
@RequiredArgsConstructor
public class GoCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("go")
    public void goCommand(Player player) {
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        if (sPlayer == null) {
            player.sendMessage(ChatUtils.format("<red>Your data is still loading, please try again."));
            return;
        }

        UUID islandId = sPlayer.getIslandId();
        if (islandId == null) {
            player.sendMessage(ChatUtils.format("<red>You don't have an island. Create a new one first."));
            return;
        }

        if (plugin.getTeleportMap().getIfPresent(player.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.format("<red>Please wait for your other teleport action to end."));
            return;
        }

        player.sendMessage(ChatUtils.format("<green>Teleporting you to your island. This may take a while."));
        plugin.getIslandManager().teleportToIsland(player, islandId);
    }

}
