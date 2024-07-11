package com.thoxia.odin.skyblock.config;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.module.Configuration;
import java.util.Arrays;
import java.util.List;

public enum Config {

    SCHEMATIC_PASTER("Setup.schematic-paster", "WorldEdit"),

    MAX_NAME_LENGTH("Settings.max-island-name-length", 16),
    COLORIZE_NAME("Settings.colorize-island-name", true),

    MAX_DESCRIPTION_LENGTH("Settings.max-island-description-length", 32),
    COLORIZE_DESCRIPTION("Settings.colorize-island-description", true),
    BLOCKED_WORDS("Settings.blocked-words", Arrays.asList("kys", "fys", "fuck")),

    FORCE_ENABLE_BORDERS("Settings.force-enable-border", true, "Should we forcefully send border packets to the players even if they disabled border from settings?"),

    ISLAND_RANGE("Settings.island-range", 400, "How big of a space should an island have?")
    ;

    private final String path;
    private final String[] comments;
    private final Object defaultValue;

    Config(String path, Object defaultValue, String... comments) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    public static void loadConfig() {
        SkyBlockPlugin.getInstance().getConfig().create();

        for (Config value : values()) {
            if (SkyBlockPlugin.getInstance().getConfig().isSet(value.path)) continue;

            SkyBlockPlugin.getInstance().getConfig().set(value.path, value.defaultValue);

            if (value.comments.length > 0)
                SkyBlockPlugin.getInstance().getConfig().setInlineComments(value.path, List.of(value.comments));
        }

        SkyBlockPlugin.getInstance().getConfig().options().setHeader(Arrays.asList(
                "OdinSkyBlock Configuration File",
                "",
                "Developers: Thoxia, hyperion",
                "If you need help configuring the plugin, you can join our discord server.",
                "https://discord.gg/9vcAHQnZsg"
        ));
        SkyBlockPlugin.getInstance().getConfig().save();
    }

    public ConfigurationSection getAsConfigurationSection() {
        return SkyBlockPlugin.getInstance().getConfig().getConfigurationSection(this.path);
    }

    public String getAsString() {
        return SkyBlockPlugin.getInstance().getConfig().getString(this.path);
    }

    public Component getAsComponent(TagResolver... variables) {
        return ChatUtils.format(getAsString(), variables);
    }

    public List<Component> getAsComponentList(TagResolver... variables) {
        return ChatUtils.format(getAsStringList(), variables);
    }

    public boolean getAsBoolean() {
        return SkyBlockPlugin.getInstance().getConfig().getBoolean(this.path);
    }

    public List<String> getAsStringList() {
        return SkyBlockPlugin.getInstance().getConfig().getStringList(this.path);
    }

    public int getAsInt() {
        return SkyBlockPlugin.getInstance().getConfig().getInt(this.path);
    }

    public double getAsDouble() {
        return SkyBlockPlugin.getInstance().getConfig().getDouble(this.path);
    }

}
