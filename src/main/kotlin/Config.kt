interface Config {

    fun parse(path: String): ConfigParsingCodes

    fun getAvailableFigures(): Set<String>

    fun firstWinsSecond(a: String, b: String): Boolean
}