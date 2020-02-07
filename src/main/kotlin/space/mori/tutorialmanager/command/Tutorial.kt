package space.mori.tutorialmanager.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import space.mori.tutorialmanager.TutorialManager.Companion.instance
import space.mori.tutorialmanager.config.Config
import space.mori.tutorialmanager.config.Config.messageColor
import space.mori.tutorialmanager.config.Config.prefix
import space.mori.tutorialmanager.config.Config.tutorialServer
import space.mori.tutorialmanager.utils.CommandBase
import space.mori.tutorialmanager.utils.SubCommand

object Tutorial: CommandBase(
    mutableListOf(
        object: SubCommand(
            "send",
            "Sending specific user for tutorial server. if not set player, Sending you for tutorial server",
            "[player]",
            "tutorialmanager.admin"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                when (args.size) {
                    1 -> when {
                        sender !is ProxiedPlayer -> {
                            Tutorial.sendMessage(sender, "$prefix$messageColor This command can only be run a player.")
                        }
                        sender.server.info.name != tutorialServer -> {
                            sender.connect(instance.proxy.getServerInfo(tutorialServer))
                            Tutorial.sendMessage(sender, "$prefix$messageColor Sending tutorial server!")
                        }
                        else -> {
                            Tutorial.sendMessage(
                                sender,
                                "$prefix$messageColor You are already connected to the Tutorial Server."
                            )
                        }
                    }
                    else -> {
                        val user = instance.proxy.getPlayer(args[1])

                        if (user != null ) {
                            if (user.server.info.name != tutorialServer) {
                                user.connect(instance.proxy.getServerInfo(tutorialServer))
                                sendMessage(user, "$prefix$messageColor Sending tutorial server!")
                                sendMessage(sender, "$prefix$messageColor Sending ${user.name} for tutorial server!")
                            } else {
                                sendMessage(sender, "$prefix$messageColor ${user.name} is already connected to the Tutorial Server.")
                            }
                        } else {
                            sendMessage(sender, "$prefix$messageColor ${args[1]} is not valid user.")
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                when (args.size) {
                    1 -> {
                        arg = instance.proxy.players.map { it.name }.toMutableList()
                        arg.add("")
                    }
                }

                return arg
            }
        },
        object: SubCommand(
            "reload",
            "Reload the this plugin's configure",
            "",
            "tutorialmanager.admin"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                Config.load()
                sendMessage(sender, "$prefix$messageColor Reloaded config!")

                return true
            }
        }
    ).associateBy { it.name }.toMutableMap(),
    "tutorial",
    "",
    arrayOf()
) {
    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("tutorialmanager.admin")) {
            when {
                sender !is ProxiedPlayer -> {
                    sendMessage(sender, "$prefix$messageColor This command can only be run a player.")
                }
                sender.server.info.name != tutorialServer -> {
                    sender.connect(instance.proxy.getServerInfo(tutorialServer))
                    sendMessage(sender, "$prefix$messageColor Sending tutorial server!")
                }
                else -> {
                    sendMessage(sender, "$prefix$messageColor You are already connected to the Tutorial Server.")
                }
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