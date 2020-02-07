package space.mori.tutorialmanager.config

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

    internal val tutorialServer: String
        get() = data.tutorialServer
}

data class ConfigData (
    var prefix: String = "&6[Tutorial]&r",
    var messageColor: String = "&f",
    var tutorialServer: String = "tutorial"
)