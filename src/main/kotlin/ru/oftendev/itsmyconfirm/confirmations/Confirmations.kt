package ru.oftendev.itsmyconfirm.confirmations

object Confirmations {
    private val confirmations = mutableListOf<Confirmation>()

    fun register(confirmation: Confirmation) {
        confirmations.removeIf { it.id == confirmation.id }
        confirmations.add(confirmation)
    }

    fun clear() {
        confirmations.clear()
    }

    fun getById(id: String): Confirmation? {
        return confirmations.firstOrNull { it.id.equals(id, true) }
    }

    fun values(): List<Confirmation> {
        return confirmations
    }
}