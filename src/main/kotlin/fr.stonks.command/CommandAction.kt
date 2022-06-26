package fr.stonks.command

import fr.stonks.command.result.CommandResult
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

interface CommandAction<T : CommandSender> {
	fun execute(sender: T, result: CommandResult) : String
}

interface PlayerCommandAction : CommandAction<Player>
interface ConsoleCommandAction : CommandAction<ConsoleCommandSender>
interface BiCommandAction : CommandAction<CommandSender>