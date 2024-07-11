package com.thoxia.odin.skyblock;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thoxia.odin.skyblock.api.SkyBlock;
import com.thoxia.odin.skyblock.api.config.ConfigFile;
import com.thoxia.odin.skyblock.api.database.Database;
import com.thoxia.odin.skyblock.api.invite.IInviteManager;
import com.thoxia.odin.skyblock.api.island.IIslandManager;
import com.thoxia.odin.skyblock.api.island.chat.IChatManager;
import com.thoxia.odin.skyblock.api.module.ModuleManager;
import com.thoxia.odin.skyblock.api.permission.IPermissionManager;
import com.thoxia.odin.skyblock.api.player.PlayerManager;
import com.thoxia.odin.skyblock.api.role.IIslandRoleManager;
import com.thoxia.odin.skyblock.api.schematic.ISchematicManager;
import com.thoxia.odin.skyblock.api.server.IServerManager;
import com.thoxia.odin.skyblock.api.upgrade.IUpgradeManager;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import com.thoxia.odin.skyblock.api.world.WorldManager;
import com.thoxia.odin.skyblock.command.*;
import com.thoxia.odin.skyblock.config.Config;
import com.thoxia.odin.skyblock.config.Messages;
import com.thoxia.odin.skyblock.database.impl.yaml.YamlDatabase;
import com.thoxia.odin.skyblock.eventbus.EventBus;
import com.thoxia.odin.skyblock.invite.InviteManager;
import com.thoxia.odin.skyblock.island.IslandManager;
import com.thoxia.odin.skyblock.island.chat.ChatManager;
import com.thoxia.odin.skyblock.listener.*;
import com.thoxia.odin.skyblock.permission.PermissionManager;
import com.thoxia.odin.skyblock.role.IslandRoleManager;
import com.thoxia.odin.skyblock.schematic.SchematicManager;
import com.thoxia.odin.skyblock.server.ServerManager;
import com.thoxia.odin.skyblock.task.ScoreboardTask;
import com.thoxia.odin.skyblock.upgrade.UpgradeManager;
import com.thoxia.odin.skyblock.world.impl.DefaultWorldManager;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.exceptions.CommandRegistrationException;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.CompactNumberFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Getter
public final class SkyBlockPlugin extends SkyBlock {

    private final boolean debugMode = true;

    @Getter private static SkyBlockPlugin instance;

    private BukkitCommandManager<CommandSender> commandManager;

    private final ConfigFile config = new ConfigFile(this, "config.yml", false);
    private final ConfigFile messages = new ConfigFile(this, "messages.yml", false);
    private final ConfigFile permissions = new ConfigFile(this, "permissions.yml", true);
    private final ConfigFile schematicsFile = new ConfigFile(this, "schematics.yml", true);
    private final ConfigFile upgradesFile = new ConfigFile(this, "upgrades.yml", true);
    private final Cache<UUID, Byte> teleportMap = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build();
    private final Set<String> playerCache = ConcurrentHashMap.newKeySet();
    private final Map<UUID, FastBoard> boardMap = new ConcurrentHashMap<>();

    @Setter private Database database;

    // Managers
    @Setter private WorldManager worldManager = new DefaultWorldManager(this);
    @Setter private IIslandManager islandManager = new IslandManager(this);
    @Setter private IInviteManager inviteManager = new InviteManager(this);
    @Setter private IIslandRoleManager islandRoleManager = new IslandRoleManager(this);
    @Setter private IServerManager serverManager = new ServerManager();
    @Setter private ModuleManager moduleManager = new ModuleManager(this);
    @Setter private PlayerManager playerManager = new PlayerManager();
    @Setter private IUpgradeManager upgradeManager = new UpgradeManager(this);
    @Setter private IPermissionManager permissionManager = new PermissionManager(this);
    @Setter private ISchematicManager schematicManager = new SchematicManager(this);
    @Setter private IChatManager chatManager = new ChatManager(this);


    @Override
    public void onLoad() {
        SkyBlock.setInstance(this);
        instance = this;

        this.moduleManager.loadModules();
    }

    @Override
    public void onEnable() {

        this.commandManager = BukkitCommandManager.create(this);
        this.commandManager.registerSuggestion(SuggestionKey.of("players"), (sender, context) ->
                new ArrayList<>(this.serverManager.getAllOnlinePlayers())
        );
        this.commandManager.registerCommand(
                new CreateCommand(this),
                new GoCommand(this),
                new InviteCommand(this),
                new AcceptCommand(this),
                new DenyCommand(this),
                new DeleteCommand(this),
                new SetSpawnCommand(this),
                new SetNameCommand(this),
                new SetDescriptionCommand(this),
                new ChatCommand(this),
                new LockCommand(this),
                new SetDiscordCommand(this),
                new DiscordCommand(this),
                new RateCommand(this)
        );

        Config.loadConfig();
        Messages.loadConfig();
        schematicsFile.create();
        upgradesFile.create();

        // initialize number formatting
        ChatUtils.setCompactNumberFormat(
                CompactNumberFormat.getCompactNumberInstance(
                        Locale.forLanguageTag(Messages.NUMBER_FORMATTING_LOCALE.getAsString()),
                        NumberFormat.Style.valueOf(Messages.NUMBER_FORMATTING_STYLE.getAsString())
                )
        );

        worldManager.loadWorlds();

        upgradeManager.onEnable();
        schematicManager.onEnable();
        permissionManager.onEnable();
        islandRoleManager.onEnable();

        database = new YamlDatabase(this);

        this.getModuleManager().enableModules();

        database.onEnable();

        this.getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new TeleportListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        new ScoreboardTask(this).runTaskTimerAsynchronously(this, 10, 10);

        EventBus.setCallEvents(true);

        if (!new File(new File(this.getDataFolder(), "schematics"), "default_normal.schem").exists()) {
            saveResource("schematics/default_normal.schem", false);
            saveResource("schematics/default_nether.schem", false);
            saveResource("schematics/default_the_end.schem", false);
        }

    }

    @Override
    public void onDisable() {
        this.getModuleManager().disableModules();

        if (database != null)
            database.onDisable();

        getBukkitCommands(getCommandMap()).remove("island");
        getBukkitCommands(getCommandMap()).remove("is");

    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private CommandMap getCommandMap() {
        try {
            final Server server = Bukkit.getServer();
            final Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
            getCommandMap.setAccessible(true);

            return (CommandMap) getCommandMap.invoke(server);
        } catch (final Exception ignored) {
            throw new CommandRegistrationException("Unable get Command Map. Commands will not be registered!");
        }
    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private Map<String, Command> getBukkitCommands(@NotNull final CommandMap commandMap) {
        try {
            final Field bukkitCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            bukkitCommands.setAccessible(true);
            //noinspection unchecked
            return (Map<String, org.bukkit.command.Command>) bukkitCommands.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CommandRegistrationException("Unable get Bukkit commands. Commands might not be registered correctly!");
        }
    }

    public void debug(String message) {
        if (debugMode) log(message);
    }

    public void log(String message) {
        log(message, Level.INFO);
    }

    public void log(String message, Level level) {
        this.getLogger().log(level, message);
    }

    public void log(String message, Throwable exception) {
        log(Level.SEVERE, message, exception);
    }

    public void log(Level level, String message, Throwable exception) {
        this.getLogger().log(level, message, exception);
    }


}
