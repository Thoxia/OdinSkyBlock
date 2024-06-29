package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.location.SLocation;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(value = "skyblock", alias = "cskyblock")
public class SetSpawnCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("setspawn")
    @Permission("skyblock.admin.setspawn")
    public void setSpawnCommand(Player player) {
        plugin.getIslandManager().setSpawnLocation(SLocation.fromLocation(player.getLocation()));
        player.sendMessage(ChatUtils.format("<green>Spawn location has been updated."));
    }

}
