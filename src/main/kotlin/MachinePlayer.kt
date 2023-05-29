import java.util.ResourceBundle

class MachinePlayer(
    private val name: String,
    private val resourceBundle: ResourceBundle
) : Player {

    private lateinit var config: Config

    override fun getPlayerName(): String {
        return name
    }

    override fun setConfig(config: Config) {
        this.config = config
    }

    override fun makeMove(): String {
        return config.getAvailableFigures().elementAt((config.getAvailableFigures().indices).random())
    }

    override fun nextCommand(): String {
        return resourceBundle.getString("game.ContinueCommand")
    }

    override fun setResponse(message: String) {
        // do nothing
    }

}
