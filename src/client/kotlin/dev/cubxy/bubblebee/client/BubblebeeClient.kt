package dev.cubxy.bubblebee.client

import net.fabricmc.api.ClientModInitializer

object BubblebeeClient : ClientInitializer {
    override fun onIntializeClient() {
        println("[BubbleBeeClient] Client featrues loaded.")
    }
}