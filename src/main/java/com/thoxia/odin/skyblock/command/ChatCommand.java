package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@Command(value = "is", alias = "island")
public class ChatCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("chat")
    @Permission("skyblock.island.chat")
    public void chatCommand(Player player) {
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

        sPlayer.setChatEnabled(!sPlayer.isChatEnabled());

        if (sPlayer.isChatEnabled()) {
            player.sendMessage(ChatUtils.format("<green>Enabled chat mode."));
        } else {
            player.sendMessage(ChatUtils.format("<red>Disabled chat mode."));
        }

    }

}
