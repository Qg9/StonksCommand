package fr.stonks.command.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.reflect.Method
/*
class SimpleCommand (name: String, var annotation: Cmd, var element: Any, var function: Method) : Command(name){
	
	override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
		
		if(args.isEmpty())return false
		
		if (!sender.hasPermission(annotation.permission)){
			sender.sendMessage("§cTu n'as pas la permissions")
			return false
		}
		
		try {
			
			val o = function.invoke(element,
				if (annotation.canWithConsole) sender else (sender as Player),
				listOf(*args)) ?: return true
			
			val msg = o as String
			if (msg.isNotEmpty())sender.sendMessage(msg)
			
		} catch (e: ClassCastException) {
			e.printStackTrace()
		} catch (e: Exception){
			sender.sendMessage("§cCette argument n'existe pas !")
		}
		return true
	}
}*/