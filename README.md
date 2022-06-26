# StonksCommand

A library for making command easily

## Example

```kotlin
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
```
