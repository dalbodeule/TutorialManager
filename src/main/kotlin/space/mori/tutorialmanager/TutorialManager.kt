package space.mori.tutorialmanager

import net.md_5.bungee.api.plugin.Plugin
import space.mori.tutorialmanager.command.Tutorial
import space.mori.tutorialmanager.config.Config
import space.mori.tutorialmanager.config.Config.isEnabled
import space.mori.tutorialmanager.config.Config.messageColor
import space.mori.tutorialmanager.config.Config.prefix
import space.mori.tutorialmanager.config.Config.tutorialServer
import space.mori.tutorialmanager.listener.MainListener
import space.mori.tutorialmanager.utils.getColored

class TutorialManager: Plugin() {
    companion object {
        lateinit var instance: TutorialManager
    }

    override fun onEnable() {
        instance = this

        Config.load()

        proxy.pluginManager.run {
            this.registerCommand(this@TutorialManager, Tutorial)
            this.registerListener(this@TutorialManager, MainListener)
        }

        logger.info("$prefix$messageColor TutorialManager has enabled.".getColored)

        if (proxy.getServerInfo(tutorialServer) == null && isEnabled) {
            isEnabled = false
            logger.info("$prefix$messageColor Tutorial server is not valid. Disable tutorial logic.".getColored)
        } else {
            logger.info("$prefix$messageColor Tutorial server is valid. Enable tutorial logic.".getColored)
        }
    }

    override fun onDisable() {
        logger.info("$prefix$messageColor TutorialManager has disabled.".getColored)

        Config.save()
    }
}