package space.mori.tutorialmanager.config

import space.mori.tutorialmanager.TutorialManager.Companion.instance
import space.mori.tutorialmanager.utils.ConfigBase
import space.mori.tutorialmanager.utils.getTarget

object Config: ConfigBase<ConfigData>(
    data = ConfigData(),
    target = getTarget("config.json")
) {
    internal val prefix: String
        get() = data.prefix

    internal val messageColor: String
        get() = data.messageColor

    internal var tutorialServer: String
        get() = data.tutorialServer
        set(value) {
            data.tutorialServer = value

            if (instance.proxy.getServerInfo(value) != null) {
                isEnabled = false
            }
        }

    internal var isEnabled: Boolean
        get() = data.isEnabled
        set(value) { data.isEnabled = value }

    internal var userCanUseCommand: Boolean
        get() = data.userCanUseCommand
        set(value) { data.userCanUseCommand = value }
}

data class ConfigData (
    var prefix: String = "&6[Tutorial]&r",
    var messageColor: String = "&f",
    var tutorialServer: String = "tutorial",
    var isEnabled: Boolean = true,
    var userCanUseCommand: Boolean = true
)