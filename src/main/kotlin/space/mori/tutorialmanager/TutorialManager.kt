package space.mori.tutorialmanager

import net.md_5.bungee.api.plugin.Plugin
import space.mori.tutorialmanager.command.Tutorial
import space.mori.tutorialmanager.config.Config
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

        logger.info("${Config.prefix}${Config.messageColor} TutorialManager has enabled.".getColored)
    }

    override fun onDisable() {
        logger.info("${Config.prefix}${Config.messageColor} TutorialManager has disabled.".getColored)

        Config.save()
    }
}