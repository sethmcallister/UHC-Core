package center.hardcore.uhccore.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtils
{
    public static boolean isBoots(ItemStack item)
    {
        return isBoots(item.getType());
    }

    private static boolean isBoots(Material material)
    {
        return material.name().endsWith("BOOTS");
    }

    public static boolean isLegs(ItemStack item)
    {
        return isLegs(item.getType());
    }

    private static boolean isLegs(Material material)
    {
        return material.name().endsWith("LEGGINGS");
    }

    public static boolean isChest(ItemStack item)
    {
        return isChest(item.getType());
    }

    private static boolean isChest(Material material)
    {
        return material.name().endsWith("CHESTPLATE");
    }

    public static boolean isHelmet(ItemStack item)
    {
        return isHelmet(item.getType());
    }

    private static boolean isHelmet(Material material)
    {
        return material.name().endsWith("HELMET");
    }

    public static boolean isArmor(ItemStack item)
    {
        return isArmor(item.getType());
    }

    private static boolean isArmor(Material type)
    {
        return isBoots(type) || isLegs(type) || isChest(type) || isHelmet(type);
    }

    public static boolean isShield(ItemStack item)
    {
        return isShield(item.getType());
    }

    private static boolean isShield(Material material)
    {
        return material.name().endsWith("SHIELD");
    }
}
