package center.hardcore.uhccore.dto.encoder;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackEncoder
{
    public static ItemStack decode(String encoded)
    {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encoded)); BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream))
        {
            return (ItemStack) dataInput.readObject();
        } catch (ClassNotFoundException | IOException e)
        {
            Bukkit.getLogger().severe(e.getMessage());
        }

        return null;
    }

    public static String encode(ItemStack stack)
    {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos))
        {
            boos.writeObject(stack);

            return Base64Coder.encodeLines(baos.toByteArray());
        }
        catch (IOException e)
        {
            Bukkit.getLogger().severe(e.getMessage());
        }

        return null;
    }
}
