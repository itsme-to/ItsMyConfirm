package ru.oftendev.itsmyconfirm.gui

import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.util.formatEco
import com.willfp.eco.util.toNiceString
import org.bukkit.entity.Player
import ru.oftendev.itsmyconfirm.confirmations.Confirmation
import ru.oftendev.itsmyconfirm.confirmPlugin

class ConfirmationGui {
    fun create(player: Player, confirm: Confirmation): Menu {
        val maskPattern = confirmPlugin.configYml.getStrings("gui.mask.pattern").toTypedArray()

        val maskItems = confirmPlugin.configYml.getStrings("gui.mask.materials")
            .mapNotNull { Items.lookup(it) }
            .toTypedArray()

        return menu(maskPattern.size) {
            title = confirm.guiTitle?.formatEco(player, true)
                ?: confirmPlugin.langYml.getFormattedString("menu.title")

            setMask(FillerMask(MaskItems(*maskItems), *maskPattern))
            allowChangingHeldItem()

            val priceDisplay = confirm.priceDisplay.replace(
                "%value%", confirm.price.getValue(player).toNiceString()
            )

            setSlot(
                confirmPlugin.configYml.getInt("gui.buttons.confirm.location.row"),
                confirmPlugin.configYml.getInt("gui.buttons.confirm.location.column"),
                slot(
                    ItemStackBuilder(
                        Items.lookup(
                            confirmPlugin.configYml.getString("gui.buttons.confirm.item")
                        )
                    )
                        .setDisplayName(
                            confirmPlugin.configYml.getString("gui.buttons.confirm.name")
                            .replace("%price%", priceDisplay)
                            .formatEco(player))
                        .addLoreLines(
                            confirmPlugin.configYml.getStrings("gui.buttons.confirm.lore")
                                .map {
                                    it.replace("%price%", priceDisplay)
                                }
                                .formatEco(player)
                        )
                        .build()
                ) {
                    onLeftClick { _, _ ->
                        if (confirm.price.canAfford(player)) {
                            confirm.price.pay(player)
                            confirm.confirm(player)
                            player.closeInventory()
                        }
                    }
                }
            )

            setSlot(
                confirmPlugin.configYml.getInt("gui.buttons.deny.location.row"),
                confirmPlugin.configYml.getInt("gui.buttons.deny.location.column"),
                slot(
                    ItemStackBuilder(
                        Items.lookup(
                            confirmPlugin.configYml.getString("gui.buttons.deny.item")
                        )
                    )
                        .setDisplayName(
                            confirmPlugin.configYml.getString("gui.buttons.deny.name")
                            .replace("%price%", priceDisplay)
                            .formatEco(player))
                        .addLoreLines(
                            confirmPlugin.configYml.getStrings("gui.buttons.deny.lore")
                                .map { it.replace("%price%", priceDisplay) }
                                .formatEco(player)
                        )
                        .build()
                ) {
                    onLeftClick { _, _ ->
                        if (!confirm.price.canAfford(player)) {
                            confirm.deny(player)
                            player.closeInventory()
                        }
                    }
                }
            )

            setSlot(
                confirmPlugin.configYml.getInt("gui.buttons.confirmation.location.row"),
                confirmPlugin.configYml.getInt("gui.buttons.confirmation.location.column"),
                slot(confirm.guiItem.getForPlayer(player))
            )
        }
    }
}