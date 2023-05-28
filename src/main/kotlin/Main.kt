class MyApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var path: String? = null
            if (args.size == 1) {
                path = args[0]
            }

            val user = UserPlayer("игрок")
            val machinePlayer = MachinePlayer("машина")
            val config = GameConfig()

            try {
                val resultScore = RockPaperScissors(
                    user,
                    machinePlayer,
                    config,
                    path
                ).startTheGame()
                println("Игра окончена с итоговым счётом: игрок - $resultScore - машина")
            } catch (e: ConfigParsingException) {
                println(e.message)
                return
            }
        }
    }
}
