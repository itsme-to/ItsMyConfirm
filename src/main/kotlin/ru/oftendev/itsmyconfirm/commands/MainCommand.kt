package ru.oftendev.itsmyconfirm.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import org.bukkit.command.CommandSender
import ru.oftendev.itsmyconfirm.pluginId

class MainCommand(plugin: EcoPlugin): PluginCommand(
    plugin,
    pluginId,
    "$pluginId.use",
    false
) {
    init {
        this.addSubcommand(ReloadCommand(plugin))
    }

    override fun onExecute(sender: CommandSender, args: MutableList<String>) {
        sender.sendMessage(plugin.langYml.getMessage("invalid-command"))
    }

    override fun getAliases(): MutableList<String> {
        return mutableListOf(
            "cf"
        )
    }
}