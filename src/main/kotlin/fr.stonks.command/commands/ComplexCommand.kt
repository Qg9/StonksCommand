package fr.stonks.command.commands
import fr.stonks.command.*
import fr.stonks.command.arguments.Argument
import fr.stonks.command.result.CommandResult
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

interface ComplexCommandBuilder {
	fun withPlayer(name: String, action: PlayerCommandAction, description: String, permission: String, vararg args: Argument<*>) : ComplexCommandBuilder
	fun withConsole(name: String, action: ConsoleCommandAction, description: String, permission: String, vararg args: Argument<*>) : ComplexCommandBuilder
	fun withAll(name: String, action: BiCommandAction, description: String, permission: String, vararg args: Argument<*>) : ComplexCommandBuilder
}

class ComplexCommand(name: String,
                     val subcommands: MutableList<SubCommandModel>) : Command(name), TabCompleter, ComplexCommandBuilder{
	
	data class SubCommandModel(var name: String,
	                           var action: CommandAction<*>,
	                           val permission: String,
	                           val args: List<Argument<*>>,
	                           val description: String,
	                           val endInfinite: Boolean) {
		fun buildArgs(): String {
			val builder = StringBuilder()
			for(arg in args)builder.append("${if (arg.essential) "<" else "["} ${arg.name} ${if (arg.essential) ">" else "]"}")
			return builder.toString()
		}
	}
	
	override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
		
		if(args.isEmpty()){
			sendHelpMessage(sender)
			return false
		}
		
		val subcommand = subcommands.firstOrNull { it.name.equals(args[0], true) }
		
		if(subcommand == null){
			sender.sendMessage("§cThis command does not exist")
			return false
		}
		
		if (!sender.hasPermission(subcommand.permission)){
			sender.sendMessage("§cYou don't have the permission to do that !")
			return false
		}
		
		if(args.size < subcommand.args.count { it.essential }){
			return false //"insuffisant argument size"
		}
		
		subcommand.args.withIndex().forEach {
			if(!it.value.isValid(args[it.index+1]))return false //"argument invalid error"
		}
		
		val resultData = CommandResult(subcommand.args.withIndex().associate {
			it.value.name to (it.value to args[it.index])
		})
		
		val message = when(subcommand.action){
			is PlayerCommandAction -> if (sender is Player) (subcommand.action as PlayerCommandAction).execute(sender, resultData)
				else "§cYou must use the console to execute this command !"
				
			is ConsoleCommandAction -> if (sender is ConsoleCommandSender) (subcommand.action as ConsoleCommandAction).execute(sender, resultData)
				else "§cYou need to be a player to execute this command !"
				
			else -> (subcommand.action as BiCommandAction).execute(sender, resultData)
		}
		
		if(message.isNotEmpty())sender.sendMessage(message)
		return true
	}
	
	private fun sendHelpMessage(sender: CommandSender) {
		sender.sendMessage("§6Command : $name")
		sender.sendMessage("")
		for (subcmd in subcommands){
			sender.sendMessage("§7 - §6/$name ${subcmd.name} ${subcmd.buildArgs()}§7: ${subcmd.description}")
		}
		sender.sendMessage("")
	}
	
	override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String> {
		
		if (args.isEmpty()) return subcommands.map { it.name }.toMutableList()
		
		val subcommand = subcommands.firstOrNull { it.name.equals(args[0], true) }
		
		if (subcommand == null || sender.hasPermission(subcommand.permission)) return mutableListOf()
		
		return try {
			subcommand.args[args.size].possibility.filter { it.startsWith(args.last(), ignoreCase = true) }.toMutableList()
		} catch (e: Exception) {
			mutableListOf()
		}
	}
	
	override fun withPlayer(
		name: String,
		action: PlayerCommandAction, description: String,
		permission: String, vararg args: Argument<*>
	): ComplexCommandBuilder {
		subcommands.add(SubCommandModel(name, action, permission, args.toList(), description, false))
		return this
	}
	
	override fun withConsole(
		name: String,
		action: ConsoleCommandAction, description: String,
		permission: String, vararg args: Argument<*>
	): ComplexCommandBuilder {
		subcommands.add(SubCommandModel(name, action, permission, args.toList(), description, false))
		return this
	}
	
	override fun withAll(
		name: String, action: BiCommandAction, description: String,
		permission: String, vararg args: Argument<*>
	): ComplexCommandBuilder {
		subcommands.add(SubCommandModel(name, action, permission, args.toList(), description, false))
		return this
	}
}