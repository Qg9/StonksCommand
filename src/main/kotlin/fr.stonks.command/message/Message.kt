package fr.stonks.command.message

enum class Message(var default: (Array<out String>) -> String) {
	
	NO_COMMAND({ "§cThis Command don't exist !" }),
	NO_PERMISSION({ "§cYou don't have the permission to do that !" }),
	WRONG_ARG_TYPE({ "§cThe Argument ${it[0]} is not consistent with what is required (${it[1]})" }),
	WRONG_ARG_SIZE({ "§cThe command you entered is incorrect! Arguments are missing!"}),
	MUST_BE_A_PLAYER({ "§cYou must be a player to do that !" }),
	MUST_BE_A_CONSOLE({ "§cYou must be on the console to do that !" }),
}