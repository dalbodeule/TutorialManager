package space.mori.tutorialmanager.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import space.mori.tutorialmanager.TutorialManager.Companion.instance
import space.mori.tutorialmanager.config.Config.messageColor
import space.mori.tutorialmanager.config.Config.prefix
import space.mori.tutorialmanager.config.Config.tutorialServer
import space.mori.tutorialmanager.utils.CommandBase
import space.mori.tutorialmanager.utils.SubCommand

object Tutorial: CommandBase(
    mutableListOf(
        object: SubCommand(
            "send",
            "Sending specific user for tutorial server",
            "<player>",
            "tutorialmanager.admin"
        ) {

        },
        object: SubCommand(
            "reload",
            "Reload the this plugin's configure",
            "",
            "tutorialmanager.admin"
        ) {

        }
    ).associateBy { it.name }.toMutableMap(),
    "tutorial",
    "",
    arrayOf()
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("tutorialmanager.admin")) {
            if ((sender as ProxiedPlayer).server.info.name == tutorialServer) {
                sender.connect(instance.proxy.getServerInfo(tutorialServer))
            } else {
                sendMessage(sender, "$prefix$messageColor You are already connected to the Tutorial Server")
            }
        } else if (args.isEmpty() || args[0] == "help" || !SubCommands.keys.contains(args[0])) {
            sendMessage(sender, "$prefix$messageColor Whitelist Commands")
            SubCommands.forEach {
                when {
                    it.value.permissions == null ->
                        sendMessage(sender, "/$commandName ${it.value.name} ${it.value.parameter} - ${it.value.description}")
                    sender.hasPermission(it.value.permissions) ->
                        sendMessage(sender, "/$commandName ${it.value.name} ${it.value.parameter} - ${it.value.description}")
                }
            }
        } else {
            if (!SubCommands[args[0]]!!.commandExecutor(sender, args)) {
                sendMessage(sender, "$prefix$messageColor wrong command")
                sendMessage(sender, "$prefix$messageColor /$commandName ${SubCommands[args[0]]!!.name} ${SubCommands[args[0]]!!.parameter}")
            }
        }
    }
}