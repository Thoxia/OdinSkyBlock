package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@Command(value = "is", alias = "island")
public class DiscordCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("discord")
    @Permission("skyblock.island.discord")
    public void setNameCommand(Player player) {
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

        if (sPlayer.getIsland() == null) {
            player.sendMessage(ChatUtils.format("<red>You must be on your island."));
            return;
        }

        if (sPlayer.getIsland().getDiscord() == null) {
            player.sendMessage(ChatUtils.format("<red>Your island does not have an discord yet."));
            return;
        }

        String discord = sPlayer.getIsland().getDiscord();
        Component component = ChatUtils.format(
                "<green>Your island's discord: <click:open_url:<discord>><discord></click>",
                Placeholder.parsed("discord", discord)
        );

        player.sendMessage(component);

    }

}
