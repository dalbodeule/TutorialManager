package space.mori.tutorialmanager.listener

import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerConnectEvent.Reason.*
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import space.mori.tutorialmanager.TutorialManager.Companion.instance
import space.mori.tutorialmanager.config.Config.messageColor
import space.mori.tutorialmanager.config.Config.prefix
import space.mori.tutorialmanager.config.Config.tutorialServer
import space.mori.tutorialmanager.utils.getColored

object MainListener: Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    internal fun onServerConnectEvent(event: ServerConnectEvent) {
        val targetServer: ServerInfo? = instance.proxy.getServerInfo(tutorialServer)

        if (targetServer != null) {
            when (event.reason) {
                JOIN_PROXY -> {
                    if (!event.player.hasPermission("tutorialmanager.clear") && event.target != targetServer) {
                        event.target = targetServer
                        (event.player as ProxiedPlayer).sendMessage(
                            TextComponent(
                                "$prefix$messageColor You not completed tutorials!".getColored
                            )
                        )
                    }

                    return
                }
                PLUGIN, PLUGIN_MESSAGE -> {
                    if (!event.player.hasPermission("tutorialmanager.clear") && event.target != targetServer) {
                        event.isCancelled = true
                        event.player.sendMessage(TextComponent("$prefix$messageColor You not completed tutorials!".getColored))
                    }

                    return
                }
                else -> {
                    return
                }
            }
        } else {
            if (event.player.hasPermission("")) {
                event.player.sendMessage(
                    TextComponent(
                        "$prefix$messageColor Tutorial server is not valid server".getColored
                    )
                )
            }
        }
    }
}