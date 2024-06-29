package com.thoxia.odin.skyblock.api.invite;

import com.thoxia.odin.skyblock.api.player.SPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInviteManager {


    void invitePlayer(SPlayer inviter, SPlayer target);

    /**
     * @param uuid UUID of the target player
     * @return The Island UUID player got invited to
     */
    CompletableFuture<UUID> getInvite(UUID uuid);

    void rejectInvite(SPlayer player);

    void acceptInvite(SPlayer player, UUID islandId);

}
