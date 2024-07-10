package com.thoxia.odin.skyblock.api.util;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.text.NumberFormat;
import java.util.List;

public class ChatUtils {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(component -> component.decoration(TextDecoration.ITALIC, false)).build();

    @Setter
    private static NumberFormat compactNumberFormat;

    public static Component format(String string, TagResolver... placeholders) {
        return MINI_MESSAGE.deserialize(string, placeholders);
    }

    public static List<Component> format(List<String> list, TagResolver... placeholders) {
        return list.stream().map(s -> format(s, placeholders)).toList();
    }

    public static String formatNumber(Number number) {
        return compactNumberFormat.format(number);
    }

}
