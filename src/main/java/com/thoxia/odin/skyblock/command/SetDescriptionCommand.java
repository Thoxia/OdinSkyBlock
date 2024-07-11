package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.config.Config;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Join;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@Command(value = "is", alias = "island")
public class SetDescriptionCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("setdescription")
    @Permission("skyblock.island.setdescription")
    public void setNameCommand(Player player, @Join() String description) {
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
            player.sendMessage(ChatUtils.format("<red>Inorder to change your island description, you need to be on your island."));
            return;
        }

        if (description.length() > Config.MAX_DESCRIPTION_LENGTH.getAsInt()) {
            player.sendMessage(ChatUtils.format("<red>Description can not be longer than 32 chars."));
            return;
        }

        if (Config.BLOCKED_WORDS.getAsStringList().stream().anyMatch(description::contains)) {
            player.sendMessage(ChatUtils.format("<red>Provided description contains some blocked words."));
            return;
        }

        Component component;
        if (Config.COLORIZE_DESCRIPTION.getAsBoolean()) {
            component = ChatUtils.format("<gray>" + description);
        } else {
            component = Component.text(description, NamedTextColor.GRAY);
        }

        if (sPlayer.getIsland().setDescription(component)) {
            player.sendMessage(ChatUtils.format("<green>Successfully changed your island description."));
        }
    }

}
