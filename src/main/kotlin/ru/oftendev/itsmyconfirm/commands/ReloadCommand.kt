package ru.oftendev.itsmyconfirm.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.util.toNiceString
import org.bukkit.command.CommandSender
import ru.oftendev.itsmyconfirm.pluginId

class ReloadCommand(plugin: EcoPlugin): Subcommand(
    plugin,
    "reload",
    "$pluginId.reload",
    false
) {
    override fun onExecute(sender: CommandSender, args: MutableList<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("reloaded")
                .replace("%time%", plugin.reloadWithTime().toNiceString())
        )
    }
}