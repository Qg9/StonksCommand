package fr.stonks.command.result

import fr.stonks.command.arguments.Argument

data class CommandResult(private val arguments: Map<String, Pair<Argument<*>, String>>) {
	
	init {
		println(arguments.map { it.key + " : " + it.value }.toString())
		
	}
	
	fun <T> get(argument: String) : T {
		val result = arguments[argument] ?:
			throw NullPointerException("The argument $argument isn't present or doesn't exist !")
		return result.first.convert(result.second) as? T ?:
			throw ClassCastException("The argument $argument can't be casted to the specified type !")
	}
	
	fun getSimple(argument: String) : String = arguments[argument]?.second ?:
			throw NullPointerException("The argument $argument isn't present or doesn't exist !")
	
	fun exist(argument: String) : Boolean = arguments.containsKey(argument)
}