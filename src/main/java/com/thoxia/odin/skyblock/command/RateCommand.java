package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(value = "is", alias = "island")
public class RateCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("rate")
    @Permission("skyblock.island.rate")
    public void rateCommand(Player player, Integer rating) {
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        if (sPlayer == null) {
            player.sendMessage(ChatUtils.format("<red>Your data is still loading, please try again."));
            return;
        }

        Island islandPlayerIn = plugin.getIslandManager().getIslandPlayerIn(player);
        if (islandPlayerIn == null) {
            player.sendMessage(ChatUtils.format("<red>You must be on an island to give a rating."));
            return;
        }

        if (islandPlayerIn.getUniqueId().equals(sPlayer.getIslandId())) {
            player.sendMessage(ChatUtils.format("<red>You can not rate your own island."));
            return;
        }

        if (islandPlayerIn.getRating(player.getUniqueId()) != -1) {
            player.sendMessage(ChatUtils.format("<gray>Since you already rated this island, your rating will be updated."));
        }

        islandPlayerIn.addRating(player.getUniqueId(), rating);
        player.sendMessage(ChatUtils.format("<green>Successfully rated the island you are currently on. You can update your rating later."));

    }

}
