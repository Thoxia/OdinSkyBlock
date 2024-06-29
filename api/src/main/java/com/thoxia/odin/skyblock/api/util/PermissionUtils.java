package com.thoxia.odin.skyblock.api.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Locale;

public class PermissionUtils {

    public static int getMax(Player player, String s, int def) {
        int max = -1;

        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            String perm = permission.getPermission().toLowerCase(Locale.ENGLISH);
            if (perm.startsWith(s)) {
                int index = perm.lastIndexOf('.');
                int i = Integer.parseInt(perm.substring(index));

                if (i >= max) max = i;
            }
        }

        return max == -1 ? def : max;
    }

}
