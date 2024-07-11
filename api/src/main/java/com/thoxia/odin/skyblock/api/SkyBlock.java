package com.thoxia.odin.skyblock.api;

import com.thoxia.odin.skyblock.api.config.ConfigFile;
import com.thoxia.odin.skyblock.api.database.Database;
import com.thoxia.odin.skyblock.api.invite.IInviteManager;
import com.thoxia.odin.skyblock.api.island.IIslandManager;
import com.thoxia.odin.skyblock.api.island.chat.IChatManager;
import com.thoxia.odin.skyblock.api.module.ModuleManager;
import com.thoxia.odin.skyblock.api.permission.IPermissionManager;
import com.thoxia.odin.skyblock.api.player.PlayerManager;
import com.thoxia.odin.skyblock.api.role.IIslandRole;
import com.thoxia.odin.skyblock.api.role.IIslandRoleManager;
import com.thoxia.odin.skyblock.api.schematic.ISchematicManager;
import com.thoxia.odin.skyblock.api.server.IServerManager;
import com.thoxia.odin.skyblock.api.upgrade.IUpgradeManager;
import com.thoxia.odin.skyblock.api.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SkyBlock extends JavaPlugin {

    private static SkyBlock api;

    public static void setInstance(SkyBlock api) {
        if (api == null)
            throw new IllegalStateException("Plugin instance is already set!");

        SkyBlock.api = api;
    }

    public static SkyBlock getInstance() {
        return api;
    }

    public abstract ConfigFile getPermissions();

    public abstract PlayerManager getPlayerManager();

    public abstract IIslandManager getIslandManager();

    public abstract IIslandRoleManager getIslandRoleManager();

    public abstract ISchematicManager getSchematicManager();

    public abstract ModuleManager getModuleManager();

    public abstract WorldManager getWorldManager();

    public abstract IInviteManager getInviteManager();

    public abstract IUpgradeManager getUpgradeManager();

    public abstract IPermissionManager getPermissionManager();

    public abstract IServerManager getServerManager();

    public abstract IChatManager getChatManager();

    public abstract Database getDatabase();

    public abstract void setDatabase(Database database);

    public abstract void setWorldManager(WorldManager worldManager);

    public abstract void setIslandManager(IIslandManager islandManager);

    public abstract void setInviteManager(IInviteManager inviteManager);

    public abstract void setModuleManager(ModuleManager manager);

    public abstract void setPlayerManager(PlayerManager manager);

    public abstract void setUpgradeManager(IUpgradeManager manager);

    public abstract void setSchematicManager(ISchematicManager manager);

    public abstract void setServerManager(IServerManager manager);

    public abstract void setChatManager(IChatManager manager);

}
