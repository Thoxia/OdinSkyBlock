package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.Constants;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.api.util.PermissionUtils;
import com.thoxia.odin.skyblock.island.Island;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "island", alias = {"is"})
@RequiredArgsConstructor
public class CreateCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("create")
    public void createCommand(Player player) {
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        if (sPlayer == null) {
            player.sendMessage(ChatUtils.format("<red>Your data is still loading, please try again."));
            return;
        }

        if (plugin.getIslandManager().getSpawnLocation() == null) {
            player.sendMessage(ChatUtils.format("<red>Spawn location is not set! A server admin must set the spawn location first."));
            return;
        }

        if (sPlayer.getIslandId() != null) {
            player.sendMessage(ChatUtils.format("<red>You already have an island!"));
            return;
        }

        if (sPlayer.getIslandCreation() >= PermissionUtils.getMax(player, Constants.CREATE_LIMIT_PERMISSION, 3)) {
            player.sendMessage(ChatUtils.format("<red>You have reached to the island creation limit."));
            return;
        }

        if (plugin.getTeleportMap().getIfPresent(player.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.format("<red>Please wait for your other teleport action to end."));
            return;
        }

        plugin.getTeleportMap().put(player.getUniqueId(), (byte) 0);
        player.sendMessage(ChatUtils.format("<green>Creating a new island for you. This may take a while."));
        Island island = new Island(sPlayer);
        plugin.getIslandManager().createIsland(island).whenComplete((bool, ex) -> {
            plugin.debug("island created. teleporting player...");
            plugin.getIslandManager().teleportToIsland(player, island.getUniqueId());
        });

        sPlayer.setIslandCreation(sPlayer.getIslandCreation() + 1);
        sPlayer.setIslandId(island.getUniqueId());
        sPlayer.setRole(IslandRole.OWNER);
    }

}
