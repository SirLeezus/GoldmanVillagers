package lee.code.villagers.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CoreUtil {

  public static Component parseColorComponent(String text) {
    final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
    return (Component.empty().decoration(TextDecoration.ITALIC, false)).append(serializer.deserialize(text));
  }

  public static String serializeLocation(Location location) {
    if (location == null) return null;
    else if (location.getWorld() == null) return null;
    return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
  }

  public static Location parseLocation(String location) {
    if (location == null) return null;
    final String[] split = location.split(",", 6);
    return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), (float) Double.parseDouble(split[4]), (float) Double.parseDouble(split[5]));
  }

  public static String serializeColorComponentJson(String text) {
    final GsonComponentSerializer serializer = GsonComponentSerializer.gson();
    final Component component = parseColorComponent(text);
    return serializer.serialize(component);
  }

  public static String stripColorCodes(String text) {
    return PlainTextComponentSerializer.plainText().serialize(LegacyComponentSerializer.legacyAmpersand().deserialize(text));
  }

  public static String buildStringFromArgs(String[] words, int startIndex) {
    final StringBuilder sb = new StringBuilder();
    for (int i = startIndex; i < words.length; i++) {
      sb.append(words[i]);
      if (i < words.length - 1) sb.append(" ");
    }
    return sb.toString();
  }

}
