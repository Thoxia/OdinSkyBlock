package com.thoxia.odin.skyblock.invite;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.invite.IInviteManager;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.role.IslandRole;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class InviteManager implements IInviteManager {

    private final SkyBlockPlugin plugin;

    private final Cache<UUID, UUID> inviteCache = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();

    @Override
    public void invitePlayer(SPlayer inviter, SPlayer target) {
        inviteCache.put(target.getUniqueId(), inviter.getIslandId());

        Component component = ChatUtils.format("<yellow>You just got an invite from <player>. ", Placeholder.unparsed("player", inviter.getName()));
        component = component.append(ChatUtils.format("<green>ACCEPT").clickEvent(ClickEvent.runCommand("/is accept " + inviter.getName())));
        component = component.append(ChatUtils.format("<dark_red>DENY").clickEvent(ClickEvent.runCommand("/is deny " + inviter.getName())));

        if (target.getPlayer() != null) {
            target.getPlayer().sendMessage(component);
        }
    }

    @Override
    public CompletableFuture<UUID> getInvite(UUID uuid) {
        return CompletableFuture.completedFuture(inviteCache.getIfPresent(uuid));
    }

    @Override
    public void rejectInvite(SPlayer player) {
        inviteCache.invalidate(player.getUniqueId());
    }

    @Override
    public void acceptInvite(SPlayer player, UUID islandId) {
        player.setRole(IslandRole.MEMBER);
        player.setIslandId(islandId);
        plugin.getIslandManager().teleportToIsland(player.getPlayer(), islandId);
    }

}
