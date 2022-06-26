package fr.stonks.command.arguments

abstract class Argument<T>(
	val name: String,
	val essential: Boolean = false,
	
	val possibility: List<String> = listOf()) {
	
	abstract fun isValid(arg: String) : Boolean
	abstract fun convert(arg: String) : T
}
