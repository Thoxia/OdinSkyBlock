package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(value = "island", alias = "is")
public class DeleteCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("delete")
    @Permission("island.delete")
    public void deleteCommand(Player player) {
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        if (sPlayer == null) {
            player.sendMessage(ChatUtils.format("<red>Your data is still loading, please try again."));
            return;
        }

        if (sPlayer.getIslandId() == null) {
            player.sendMessage(ChatUtils.format("<red>You don't have an island to delete."));
            return;
        }

        if (sPlayer.getRole() != IslandRole.OWNER) {
            player.sendMessage(ChatUtils.format("<red>You must be the owner of your island to delete."));
            return;
        }

        player.sendMessage(ChatUtils.format("<green>Deleting your island..."));
        plugin.getIslandManager().deleteIsland(sPlayer.getIslandId());
    }

}
