package com.thoxia.odin.skyblock.api.location;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Data
public class SLocation {

    protected final String world;
    protected final double x, y, z;
    protected final float yaw, pitch;

    public static SLocation of(String worldName, double x, double y, double z, float yaw, float pitch) {
        return new SLocation(worldName, x, y, z, yaw, pitch);
    }

    public static SLocation fromLocation(Location location) {
        return of(
                location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch()
        );
    }

    public static SLocation deserialize(String s) {
        if (s == null || s.isEmpty()) return null;

        String[] sections = s.split(",");

        double x = Double.parseDouble(sections[1]);
        double y = Double.parseDouble(sections[2]);
        double z = Double.parseDouble(sections[3]);
        float yaw = sections.length > 5 ? Float.parseFloat(sections[4]) : 0;
        float pitch = sections.length > 4 ? Float.parseFloat(sections[5]) : 0;

        return new SLocation(sections[0], x, y, z, yaw, pitch);
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public String serialize() {
        return world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
    }

    public void teleport(Player player) {
        player.teleport(toLocation());
    }

}
