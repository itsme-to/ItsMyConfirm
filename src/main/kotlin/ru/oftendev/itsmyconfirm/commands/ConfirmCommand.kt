package ru.oftendev.itsmyconfirm.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.command.impl.Subcommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil
import ru.oftendev.itsmyconfirm.confirmations.Confirmations
import ru.oftendev.itsmyconfirm.gui.ConfirmationGui
import ru.oftendev.itsmyconfirm.pluginId

class ConfirmCommand(plugin: EcoPlugin): PluginCommand(
    plugin,
    "confirm",
    "$pluginId.confirm",
    false
) {
    override fun onExecute(sender: CommandSender, args: MutableList<String>) {
        val player = Bukkit.getPlayer(

            args.firstOrNull() ?: run {
                sender.sendMessage(plugin.langYml.getMessage("must-specify-player"))
                return
            }

        ) ?: run {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }

        val confirmation = Confirmations.getById(

            args.getOrNull(1) ?: run {
                sender.sendMessage(plugin.langYml.getMessage("must-specify-confirmation"))
                return
            }

        ) ?: run {
            sender.sendMessage(plugin.langYml.getMessage("invalid-confirmation"))
            return
        }

        ConfirmationGui().create(player, confirmation).open(player)

        sender.sendMessage(
            plugin.langYml.getMessage("confirmation-opened")
                .replace("%confirmation%", confirmation.id)
                .replace("%playername%", player.name)
        )
    }

    override fun tabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        return when(args.size) {
            1 -> StringUtil.copyPartialMatches(args.first(), Bukkit.getOnlinePlayers().map { it.name },
                mutableListOf())
            2 -> StringUtil.copyPartialMatches(args[1], Confirmations.values().map { it.id },
                mutableListOf())
            else -> mutableListOf()
        }
    }
}