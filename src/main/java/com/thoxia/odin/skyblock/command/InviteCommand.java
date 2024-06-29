package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.permission.IslandPermission;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

@Command(value = "island", alias = "is")
@RequiredArgsConstructor
public class InviteCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("invite")
    public void inviteCommand(Player player, @Suggestion("players") String target) {
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

        assert sPlayer.getRole() != null;
        IslandPermission permission = plugin.getPermissionManager().getPermission("invite");
        if (island.hasPermission(sPlayer, permission)) {
            player.sendMessage(ChatUtils.format("<red>You have to be an admin or owner of the island to invite someone."));
            return;
        }

        if (target.equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatUtils.format("<red>You cannot invite yourself."));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Collection<String> players = plugin.getServerManager().getAllOnlinePlayers();
            if (!players.contains(target)) {
                player.sendMessage(ChatUtils.format("<red><player> is not online.", Placeholder.unparsed("player", target)));
                return;
            }

            SPlayer targetPlayer = plugin.getDatabase().loadPlayerData(target);
            plugin.getInviteManager().getInvite(targetPlayer.getUniqueId()).whenComplete((invite, ex) -> {
                if (ex != null) {
                    plugin.log("An exception was found!", ex);
                    return;
                }

                if (invite != null) {
                    player.sendMessage(ChatUtils.format("<red>Target player already has an invite, please wait for 3 minutes and try again."));
                    return;
                }

                plugin.getInviteManager().invitePlayer(sPlayer, targetPlayer);

                player.sendMessage(ChatUtils.format("<green>Player invited successfully."));
            });
        });
    }

}
