package fr.stonks.command.message

@FunctionalInterface
interface MessageParser {
	
	fun parse(message: Message, placeholder: Array<out String>) : String
}

object DefaultMessageParser : MessageParser{
	
	override fun parse(message: Message, placeholder: Array<out String>): String = message.default.invoke(placeholder)
}