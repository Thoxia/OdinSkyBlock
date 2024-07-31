package com.thoxia.odin.skyblock.permission;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.permission.IIslandPermission;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.config.Messages;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerEvent;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum IslandPermission implements IIslandPermission {

    FLY("fly"),
    INVITE("invite"),
    LOCK("lock"),
    BREAK("break", PlayerArmSwingEvent.class),
    PLACE("place", PlayerArmSwingEvent.class);

    private static final Map<String, IslandPermission> ISLAND_PERMISSIONS = Arrays.stream(values())
            .collect(Collectors.toMap(IslandPermission::name, Function.identity()));

    private final String name;

    @SafeVarargs
    <T extends PlayerEvent & Cancellable> IslandPermission(String name, Class<T>... triggerEvents) {
        this.name = name;
        for (Class<T> eventClass : triggerEvents) {
            Bukkit.getPluginManager().registerEvent(
                    eventClass,
                    this,
                    EventPriority.NORMAL,
                    (listener, event) ->
                            checkAndCancel((Cancellable) event, ((PlayerEvent) event).getPlayer()),
                    SkyBlock.getInstance());
        }
    }

    @Override
    public void checkAndCancel(Cancellable event, SPlayer player) {
        Island island = SkyBlockPlugin.getInstance().getIslandManager().getIslandPlayerIn(player);
        if (island == null) return;

        if (check(island, player)) {
            event.setCancelled(true);
            Messages.ACTION_CANCELLED.sendMessage(player.getPlayer());
            player.getPlayer().playSound(player.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    private void checkAndCancel(Cancellable event, Player player) {
        SPlayer sPlayer = SkyBlockPlugin.getInstance().getPlayerManager().getPlayer(player);
        checkAndCancel(event, sPlayer);
    }

    public static IslandPermission fromName(String name) {
        return ISLAND_PERMISSIONS.get(name);
    }
}
