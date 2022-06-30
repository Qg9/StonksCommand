package fr.stonks.command

import fr.stonks.command.commands.ComplexCommand
import fr.stonks.command.message.*
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

private val commandMap: CommandMap = createMap()
private var messageParser: MessageParser = DefaultMessageParser
private var helpMessageParser: HelpMessageParser = DefaultHelpMessageParser

private fun createMap() : CommandMap{
	val bukkitCmdMap = Bukkit.getServer()::class.java.getDeclaredField("commandMap")
	bukkitCmdMap.isAccessible = true
	return bukkitCmdMap.get(Bukkit.getServer()) as CommandMap
}

fun JavaPlugin.create(name: String) : ComplexCommand {
	val cmd = ComplexCommand(name, mutableListOf())
	commandMap.register(name, cmd)
	
	return cmd
}

fun JavaPlugin.useParser(parser:MessageParser = DefaultMessageParser){
	messageParser = parser
}

fun JavaPlugin.useParser(parser: HelpMessageParser = DefaultHelpMessageParser){
	helpMessageParser = parser
}

fun ComplexCommand.getParser() : MessageParser = messageParser
fun ComplexCommand.sendHelpMessage(sender: CommandSender) = helpMessageParser.sendHelpMessage(sender, this)
