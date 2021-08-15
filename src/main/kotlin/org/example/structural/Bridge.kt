package org.example.structural

interface Weapon {
    fun wield()
    fun swing()
    fun unwield()
    val enchantment: Enchantment?
}

class Sword(enchantment: Enchantment) : Weapon {
    override val enchantment: Enchantment = enchantment
    override fun wield() {
        println("The sword is wielded.")
        enchantment.onActivate()
    }

    override fun swing() {
        println("The sword is swinged.")
        enchantment.apply()
    }

    override fun unwield() {
        println("The sword is unwielded.")
        enchantment.onDeactivate()
    }
}


class Hammer(enchantment: Enchantment) : Weapon {
    override val enchantment: Enchantment = enchantment
    override fun wield() {
        println("The hammer is wielded.")
        enchantment.onActivate()
    }

    override fun swing() {
        println("The hammer is swinged.")
        enchantment.apply()
    }

    override fun unwield() {
        println("The hammer is unwielded.")
        enchantment.onDeactivate()
    }
}

interface Enchantment {
    fun onActivate()
    fun apply()
    fun onDeactivate()
}


class FlyingEnchantment : Enchantment {
    override fun onActivate() {
        println("The item begins to glow faintly.")
    }

    override fun apply() {
        println("The item flies and strikes the enemies finally returning to owner's hand.")
    }

    override fun onDeactivate() {
        println("The item's glow fades.")
    }
}

class SoulEatingEnchantment : Enchantment {
    override fun onActivate() {
        println("The item spreads bloodlust.")
    }

    override fun apply() {
        println("The item eats the soul of enemies.")
    }

    override fun onDeactivate() {
        println("Bloodlust slowly disappears.")
    }
}

fun main(){
    val enchantedSword = Sword(SoulEatingEnchantment())
    enchantedSword.wield()
    enchantedSword.swing()
    enchantedSword.unwield()

    val hammer = Hammer(FlyingEnchantment())
    hammer.wield()
    hammer.swing()
    hammer.unwield()

}