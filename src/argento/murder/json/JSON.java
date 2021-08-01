package argento.murder.json;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public interface JSON {

	public void json(Player p, Location loc, FileConfiguration lang);
	public void json2(Player p, FileConfiguration lang);
	
}