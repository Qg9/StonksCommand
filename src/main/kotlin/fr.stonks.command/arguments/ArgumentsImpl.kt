package fr.stonks.command.arguments

import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class PlayerArgument(name: String = "player", essential: Boolean = false) : StrictArgument<Player>(
	name, essential) {
	override fun possibilities(): List<String> = Bukkit.getOnlinePlayers().map { it.name }
	
	override fun convert(arg: String): Player = Bukkit.getPlayer(arg)!!
}

class IntArgument(name: String = "size", essential: Boolean = false,
                  private val min: Int = Int.MIN_VALUE,
                  private val max: Int = Int.MAX_VALUE) : Argument<Int> (
	name, essential) {
	override fun possibilities(): List<String> = if(max-min < 100) (min..max).map { it.toString() } else listOf()
	
	override fun isValid(arg: String): Boolean = try { arg.toInt() in min..max }catch (e: java.lang.NumberFormatException) { false }
	
	override fun convert(arg: String): Int = arg.toInt()
}

class PotionArgument(name: String = "potion", essential: Boolean = false) :
	StrictArgument<PotionEffectType>(name, essential) {
	override fun possibilities(): List<String> = PotionEffectType.values().map { it.name }
	
	override fun convert(arg: String): PotionEffectType = PotionEffectType.getByName(arg)!!
}

class EnchantArgument(name: String = "enchant", essential: Boolean = false) :
	StrictArgument<Enchantment>(name, essential) {
	override fun possibilities(): List<String> = Enchantment.values().map { it.name }
	
	override fun convert(arg: String): Enchantment = Enchantment.getByName(arg)!!
}

inline fun <reified E: Enum<E>> EnumArgument(name: String, essential: Boolean = false) : Argument<E> =
	object : StrictArgument<E>(name, essential) {
		override fun possibilities(): List<String> = enumValues<E>().map { it.name }
		override fun convert(arg: String): E = enumValueOf(arg)
	}