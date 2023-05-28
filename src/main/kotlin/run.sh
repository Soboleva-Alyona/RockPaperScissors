#!/bin/bash

kotlinc Config.kt ConfigParsingCodes.kt GameConfig.kt GameExceptions.kt MachinePlayer.kt Main.kt Player.kt RockPaperScissors.kt UserPlayer.kt -include-runtime -d game.jar

# run app: optionally add a relative path to config file
java -jar game.jar # ../resources/bad_config.txt