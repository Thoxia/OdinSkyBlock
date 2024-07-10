package com.thoxia.odin.skyblock.config;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum Messages {

    ACTION_CANCELLED("Messages.action-cancelled", "<red>You do not have the permission to do this."),

    NUMBER_FORMATTING_LOCALE("NumberFormat.locale", "en-US", "Full list of locales can be found at https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html"),
    NUMBER_FORMATTING_STYLE("NumberFormat.style", "SHORT"),

    BOARD_TITLE("Board.title", "<#00ffff>OdinSkyBlock"),
    BOARD_LINES(
            "Board.lines",
            Arrays.asList(
                    "",
                    "<#cfe2f3>Island owner: <#00ffff><current-island-owner>",
                    "<#cfe2f3>Island size: <#00ffff><current-island-size>",
                    "<#cfe2f3>Island level: <#00ffff><current-island-level>",
                    ""
            ),
            "You can use PlaceholderAPI placeholders as well!"
    ),
    ;

    private final String path;
    private final String[] comments;
    private final Object defaultValue;

    Messages(String path, Object defaultValue, String... comments) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    public static void loadConfig() {
        SkyBlockPlugin.getInstance().getMessages().create();

        for (Messages value : values()) {
            if (SkyBlockPlugin.getInstance().getMessages().isSet(value.path)) continue;

            SkyBlockPlugin.getInstance().getMessages().set(value.path, value.defaultValue);

            if (value.comments.length > 0)
                SkyBlockPlugin.getInstance().getMessages().setInlineComments(value.path, List.of(value.comments));
        }

        SkyBlockPlugin.getInstance().getMessages().options().setHeader(Arrays.asList(
                "OdinSkyBlock Configuration File",
                "",
                "Developers: Thoxia, hyperion",
                "If you need help configuring the plugin, you can join our discord server.",
                "https://discord.gg/9vcAHQnZsg"
        ));
        SkyBlockPlugin.getInstance().getMessages().save();
    }

    public void sendMessage(Player player, TagResolver... variables) {
        player.sendMessage(getAsComponent(variables));
    }

    public String getAsString() {
        return SkyBlockPlugin.getInstance().getMessages().getString(this.path);
    }

    public Component getAsComponent(TagResolver... variables) {
        return ChatUtils.format(getAsString(), variables);
    }

    public List<Component> getAsComponentList(TagResolver... variables) {
        return ChatUtils.format(getAsStringList(), variables);
    }

    public boolean getAsBoolean() {
        return SkyBlockPlugin.getInstance().getMessages().getBoolean(this.path);
    }

    public List<String> getAsStringList() {
        return SkyBlockPlugin.getInstance().getMessages().getStringList(this.path);
    }

    public int getAsInt() {
        return SkyBlockPlugin.getInstance().getMessages().getInt(this.path);
    }

    public double getAsDouble() {
        return SkyBlockPlugin.getInstance().getMessages().getDouble(this.path);
    }

}
