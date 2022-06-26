import fr.stonks.command.*
import fr.stonks.command.arguments.*
import fr.stonks.command.result.CommandResult
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
	override fun onEnable() {
		create("items")
			.withAll("give", GiveAction(), "Give an item to the player", "",
				PlayerArgument("target", true),
				EnumArgument<Material>("type", true),
				IntArgument("size", true, 0, 64)
			)
	}
}

class GiveAction : BiCommandAction {
	override fun execute(sender: CommandSender, result: CommandResult): String {
		result.get<Player>("target").inventory.addItem(ItemStack(result.get("type"), result.get("size")))
		return "§cYou give something to the player"
	}
}