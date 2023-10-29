package ru.oftendev.itsmyconfirm.confirmations

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.price.Prices
import com.willfp.eco.util.formatEco
import com.willfp.eco.util.toNiceString
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.effects.executors.impl.NormalExecutorFactory
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ru.oftendev.itsmyconfirm.confirmPlugin

class Confirmation(val config: Config) {
    val id = config.getString("id")
    val price = Prices.create(
        config.getString("price.value"),
        config.getString("price.type")
    )
    val priceDisplay = config.getString("price.display")
    val guiTitle = config.getStringOrNull("gui-title")
    val guiItem = ConfiguredItem(config.getSubsection("item"), this)

    val confirmEffects = Effects.compileChain(
        config.getSubsections("effects.confirm"),
        NormalExecutorFactory.create(),
        ViolationContext(
            confirmPlugin,
            "Confirmation $id confirm effects"
        )
    )

    val denyEffects = Effects.compileChain(
        config.getSubsections("effects.deny"),
        NormalExecutorFactory.create(),
        ViolationContext(
            confirmPlugin,
            "Confirmation $id deny effects"
        )
    )

    fun confirm(player: Player) {
        confirmEffects?.trigger(
            TriggerData(
                player = player,
                text = id
            ).dispatch(player)
        )
    }

    fun deny(player: Player) {
        denyEffects?.trigger(
            TriggerData(
                player = player,
                text = id
            ).dispatch(player)
        )
    }
}

class ConfiguredItem(config: Config, private val parent: Confirmation) {
    private val item = Items.lookup(config.getString("item"))
    private val name = config.getString("name")
    private val lore = config.getStrings("lore")

    fun getForPlayer(player: Player): ItemStack {
        val display = parent.priceDisplay
            .replace("%value%", parent.price.getValue(player).toNiceString())

        return ItemStackBuilder(item.item.clone())
            .setDisplayName(name.replace("%price%", display)
                .formatEco(player, true))
            .addLoreLines(
                lore.map {
                    it.replace("%price%", display)
                        .formatEco(player, true)
                }
            ).build()
    }
}