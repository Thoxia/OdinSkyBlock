package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.config.Config;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Command(value = "is", alias = "island")
public class SetDiscordCommand extends BaseCommand {

    private static final Pattern DISCORD_PATTERN = Pattern.compile("(https?://)?(www\\.)?discord(?:.com|app.com|.gg)[/invite/]?([a-zA-Z0-9-]{2,32})");

    private final SkyBlockPlugin plugin;

    @SubCommand("setdiscord")
    @Permission("skyblock.island.setdiscord")
    public void setNameCommand(Player player, String discord) {
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
            player.sendMessage(ChatUtils.format("<red>Inorder to change your island discord, you need to be on your island."));
            return;
        }

        if (!DISCORD_PATTERN.matcher(discord).matches()) {
            player.sendMessage(ChatUtils.format("<red>This is not a valid discord url."));
            return;
        }

        if (Config.BLOCKED_WORDS.getAsStringList().stream().anyMatch(discord::contains)) {
            player.sendMessage(ChatUtils.format("<red>Provided discord url contains some blocked words."));
            return;
        }

        if (sPlayer.getIsland().setDiscord(discord)) {
            player.sendMessage(ChatUtils.format("<green>Successfully changed your island discord."));
        }
    }

}
