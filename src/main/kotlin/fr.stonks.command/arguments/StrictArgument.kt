package fr.stonks.command.arguments


abstract class StrictArgument<T>(
	name: String,
	essential: Boolean = false, ) : Argument<T>(name, essential) {
	
	override fun isValid(arg: String) : Boolean = possibilities().contains(arg)
}