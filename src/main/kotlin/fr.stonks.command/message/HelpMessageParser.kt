package fr.stonks.command.message

import fr.stonks.command.commands.ComplexCommand
import org.bukkit.command.CommandSender

@FunctionalInterface
interface HelpMessageParser  {
	
	fun sendHelpMessage(sender: CommandSender, cmd: ComplexCommand)
}

object DefaultHelpMessageParser : HelpMessageParser{
	override fun sendHelpMessage(sender: CommandSender, cmd: ComplexCommand) {
		sender.sendMessage("§6Command : ${cmd.name}")
		sender.sendMessage("")
		for (subcmd in cmd.subcommands){
			sender.sendMessage("§7 - §6/${cmd.name} ${subcmd.name} ${subcmd.buildArgs()} §7: ${subcmd.description}")
		}
		sender.sendMessage("")
	}
}