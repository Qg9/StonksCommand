package fr.stonks.command

import fr.stonks.command.commands.ComplexCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin

private val commandMap: CommandMap = createMap()

private fun createMap() : CommandMap{
	val bukkitCmdMap = Bukkit.getServer()::class.java.getDeclaredField("commandMap")
	bukkitCmdMap.isAccessible = true
	return bukkitCmdMap.get(Bukkit.getServer()) as CommandMap
}

fun JavaPlugin.create(name: String) : ComplexCommand {
	val cmd = ComplexCommand(name, mutableListOf())
	if (commandMap.knownCommands.containsKey(name)) commandMap.knownCommands.remove(name)
	commandMap.register(name, cmd)
	return cmd
}
