package argento.murder.json;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
public class JSON_1_16_R3 implements JSON {
	
	@Override
	public void json(Player p, Location loc, FileConfiguration lang) {
        IChatBaseComponent comp = ChatSerializer.a("{\"text\":\""+lang.getString("json.1")+"\",\"extra\":[{\"text\":\"§3"+String.valueOf(loc.getBlockX())+"§7, §3"+String.valueOf(loc.getBlockY())+"§7, §3"+String.valueOf(loc.getBlockZ())+lang.getString("json.2")+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+lang.getString("json.3")+"\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/dis "+String.valueOf(loc.getBlockX())+" "+String.valueOf(loc.getBlockY())+" "+String.valueOf(loc.getBlockZ())+"\"}}]}");
        PacketPlayOutChat packet = new PacketPlayOutChat(comp, ChatMessageType.a((byte) 300), p.getUniqueId());
        ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
	
	@Override
	public void json2(Player p, FileConfiguration lang) {
        IChatBaseComponent comp = ChatSerializer.a("{\"text\":\""+lang.getString("json.4")+"\",\"extra\":[{\"text\":\""+lang.getString("json.5")+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+lang.getString("json.5")+"\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/bedtp \"}}]}");
        PacketPlayOutChat packet = new PacketPlayOutChat(comp, ChatMessageType.a((byte) 300), p.getUniqueId());
        ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
