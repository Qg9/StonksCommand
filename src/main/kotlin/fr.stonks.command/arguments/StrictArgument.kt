package fr.stonks.command.arguments


abstract class StrictArgument<T>(
	name: String,
	possibility: List<String>,
	essential: Boolean = false, ) : Argument<T>(name, essential, possibility) {
	
	override fun isValid(arg: String) : Boolean = possibility.contains(arg)
}