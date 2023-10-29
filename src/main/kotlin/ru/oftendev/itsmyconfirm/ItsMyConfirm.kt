package ru.oftendev.itsmyconfirm

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.libreforge.loader.LibreforgePlugin
import ru.oftendev.itsmyconfirm.commands.ConfirmCommand
import ru.oftendev.itsmyconfirm.commands.MainCommand
import ru.oftendev.itsmyconfirm.confirmations.Confirmation
import ru.oftendev.itsmyconfirm.confirmations.Confirmations

lateinit var confirmPlugin: ItsMyConfirm
    private set

const val pluginId = "itsmyconfirm"

class ItsMyConfirm: LibreforgePlugin() {
    init {
        confirmPlugin = this
    }

    override fun loadPluginCommands(): MutableList<PluginCommand> {
        return mutableListOf(
            MainCommand(this),
            ConfirmCommand(this)
        )
    }

    override fun handleAfterLoad() {
        this.configYml.getSubsections("confirmations")
            .map { Confirmations.register(Confirmation(it)) }
    }

    override fun handleReload() {
        this.configYml.getSubsections("confirmations")
            .map { Confirmations.register(Confirmation(it)) }
    }
}