package dev.orhidea.commandqueue.utils;

import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.Txt;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class TxtUtil {

    // -------------------------------------------- //
    // COLORIZE & REPLACE
    // -------------------------------------------- //

    public String parse(String string) {
        return Txt.parse(string);
    }

    public String parse(String string, Object... args) {
        return Txt.parse(string, args);
    }

    public Collection<String> parse(Collection<String> list) {
        return Txt.parse(list);
    }

    public String parseAndReplace(String string, String... replacements) {
        if(replacements.length > 0) {
            Iterator<String> iterator = Arrays.asList(replacements).iterator();

            while(iterator.hasNext()) {
                String key = iterator.next();

                if(iterator.hasNext()) {
                    String value = iterator.next();

                    if(key == null || value == null) continue;

                    string = string.replace(key, value);
                }
            }
        }

        return parse(string);
    }

    public List<String> parseAndReplace(List<String> list, String... replacements) {
        return list.stream()
                .map(line -> parseAndReplace(line, replacements))
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }

    // -------------------------------------------- //
    // Material name tools
    // -------------------------------------------- //

    public String getMaterialName(Material material)
    {
        return Txt.getNicedEnum(material);
    }

    public String getItemName(ItemStack itemStack)
    {
        if (InventoryUtil.isNothing(itemStack)) return Txt.parse("<silver><em>Nothing");

        ChatColor color;

        try {
            color = (itemStack.getEnchantments().size() > 0) ? ChatColor.AQUA : ChatColor.WHITE;
        } catch (Throwable ex) {
            color = ChatColor.WHITE;
        }

        if(color == ChatColor.WHITE) {
            if(itemStack.getType() == Material.GOLDEN_APPLE) {
                short durability = itemStack.getDurability();

                switch (durability) {
                    case 1:
                        color = ChatColor.LIGHT_PURPLE;
                        break;
                    case 0:
                    default:
                        color = ChatColor.AQUA;
                        break;
                }
            }
        }

        if (itemStack.hasItemMeta())
        {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasDisplayName())
            {
                return color.toString() + ChatColor.ITALIC.toString() + itemMeta.getDisplayName();
            }
        }

        return color + Txt.getMaterialName(itemStack.getType());
    }

    // -------------------------------------------- //
    // SHUFFLE
    // -------------------------------------------- //

    public String shuffle(String input) {
        List<Character> characters = new ArrayList<>();

        for(char c : input.toCharArray()){
            characters.add(c);
        }

        StringBuilder output = new StringBuilder(input.length());

        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }

        return output.toString();
    }

    // -------------------------------------------- //
    // PRETTY LOCATIONS
    // -------------------------------------------- //

    public String getPrettyLocation(Location location) {
        return getPrettyLocation(location, true);
    }

    public String getPrettyLocation(Location location, boolean includeWorld) {
        return (includeWorld? location.getWorld().getName() + ", " : "") + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }

}
