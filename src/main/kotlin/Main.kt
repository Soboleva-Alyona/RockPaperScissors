import java.util.*

class MyApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) { // --path src/main/resources --locale en
            val bundleAndPath = parseLocaleAndPathFromArguments(args)

            val resourceBundle = bundleAndPath.first
            val path = bundleAndPath.second
            if (resourceBundle == null) {
                return
            }

            val user = UserPlayer(resourceBundle.getString("Player"))
            val machinePlayer = MachinePlayer(
                resourceBundle.getString("Machine"),
                resourceBundle
            )

            val config = GameConfig()

            try {
                val resultScore = RockPaperScissors(
                    user,
                    machinePlayer,
                    config,
                    resourceBundle,
                    path,
                ).startTheGame()

                println(resourceBundle.getString("PlayerToMachineGameOverWithScore") + "$resultScore")
            } catch (e: ConfigParsingException) {
                println(e.message)
                return
            }
        }

        private fun parseLocaleAndPathFromArguments(args: Array<String>): Pair<ResourceBundle?, String?> {
            var path: String? = null
            var resourceBundle = ResourceBundle.getBundle(BASE_BUNDLE_NAME + "_" + RU_CODE)

            if (args.size >= 2) {
                when (args[0]) {
                    PATH_OPTION -> {
                        path = args[1]
                    }
                    LOCALE_OPTION -> {
                        when (args[1]) {
                            "ru" -> {
                                // do nothing
                            }
                            "en" -> {
                                resourceBundle = ResourceBundle.getBundle(BASE_BUNDLE_NAME + "_" + EN_CODE)
                            }
                            "" -> {
                                println("Unsupported locale")
                                return null to null
                            }
                        }
                    }
                    else -> {
                        println("Unknown option")
                        return null to null
                    }
                }

                if (args.size == 4) {
                    when (args[2]) {
                        PATH_OPTION -> {
                            path = args[3]
                        }
                        LOCALE_OPTION -> {
                            when (args[3]) {
                                "ru" -> {
                                    // do nothing
                                }
                                "en" -> {
                                    resourceBundle = ResourceBundle.getBundle(BASE_BUNDLE_NAME + "_" + EN_CODE)
                                }
                                "" -> {
                                    println("Unsupported locale")
                                    return null to null
                                }
                            }
                        }
                        else -> {
                            println("Unknown option")
                            return null to null
                        }
                    }
                }
            }
            return resourceBundle to path
        }

        private const val PATH_OPTION = "--path"
        private const val LOCALE_OPTION = "--locale"

        private const val BASE_BUNDLE_NAME = "game"
        private const val RU_CODE = "ru_RU"
        private const val EN_CODE = "en_us"
    }
}
