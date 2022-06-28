package fr.stonks.command.commands
import fr.stonks.command.*
import fr.stonks.command.arguments.Argument
import fr.stonks.command.message.Message.*
import fr.stonks.command.result.CommandResult
import org.bukkit.Location
import org.bukkit.command.*
import org.bukkit.entity.Player

interface ComplexCommandBuilder {
	fun withPlayer(name: String, action: PlayerCommandAction, description: String, permission: String, vararg args: Argument<*>) : ComplexCommandBuilder
	fun withConsole(name: String, action: ConsoleCommandAction, description: String, permission: String, vararg args: Argument<*>) : ComplexCommandBuilder
	fun withAll(name: String, action: BiCommandAction, description: String, permission: String, vararg args: Argument<*>) : ComplexCommandBuilder
}

class ComplexCommand(name: String,
                     val subcommands: MutableList<SubCommandModel>) : Command(name), ComplexCommandBuilder{
	
	data class SubCommandModel(var name: String,
	                           var action: CommandAction<*>,
	                           val permission: String,
	                           val args: List<Argument<*>>,
	                           val description: String,
	                           val endInfinite: Boolean) {
		fun buildArgs(): String {
			val builder = StringBuilder()
			for(arg in args)builder.append("${if (arg.essential) "<" else "["}${arg.name}${if (arg.essential) ">" else "]"}")
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
			sender.sendMessage(getParser().parse(NO_COMMAND, arrayOf()))
			return false
		}
		
		if (!sender.hasPermission(subcommand.permission)){
			sender.sendMessage(getParser().parse(NO_PERMISSION, arrayOf()))
			return false
		}
		
		if(args.size < subcommand.args.count { it.essential }){
			sender.sendMessage(getParser().parse(WRONG_ARG_SIZE, arrayOf()))
			return false
		}
		
		subcommand.args.withIndex().forEach {
			if(!it.value.isValid(args[it.index+1])){
				sender.sendMessage(getParser().parse(WRONG_ARG_TYPE, arrayOf()))
				return false
			}
		}
		var i = 0
		val resultData = CommandResult(subcommand.args.associate {
			i+=1
			it.name to (it to args[i])
		})
		
		val message = when(subcommand.action){
			is PlayerCommandAction -> if (sender is Player) (subcommand.action as PlayerCommandAction).execute(sender, resultData)
				else getParser().parse(MUST_BE_A_PLAYER, arrayOf())
				
			is ConsoleCommandAction -> if (sender is ConsoleCommandSender) (subcommand.action as ConsoleCommandAction).execute(sender, resultData)
				else getParser().parse(MUST_BE_A_CONSOLE, arrayOf())
				
			else -> (subcommand.action as BiCommandAction).execute(sender, resultData)
		}
		
		if(message.isNotEmpty())sender.sendMessage(message)
		return true
	}
	
	override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String>  {
		if (args.size == 1) {
			return subcommands.filter { it.name.startsWith(args[0], ignoreCase = true) && sender.hasPermission(it.permission) }
				.map{ it.name }.toMutableList()
		}
		
		val subcommand = subcommands.firstOrNull { it.name.equals(args[0], true) }
		
		if (subcommand == null || !sender.hasPermission(subcommand.permission)) return mutableListOf()
		
		return try {
			subcommand.args[args.size-2].possibilities().filter { it.startsWith(args.last(), ignoreCase = true) }.toMutableList()
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