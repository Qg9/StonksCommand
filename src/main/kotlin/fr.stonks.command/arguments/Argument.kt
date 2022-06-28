package fr.stonks.command.arguments

abstract class Argument<T>(
	val name: String,
	val essential: Boolean = false) {
	
	abstract fun possibilities() : List<String>
	abstract fun isValid(arg: String) : Boolean
	abstract fun convert(arg: String) : T
}
