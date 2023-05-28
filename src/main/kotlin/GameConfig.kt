import java.io.File
import java.util.*

class GameConfig: Config {

    private lateinit var figuresSet: Set<String>

    private lateinit var rules: MutableMap<String, MutableList<String>> // a -> [b] - means that a beats b (a has priority under b)

    override fun getAvailableFigures(): Set<String> {
        return figuresSet
    }

    override fun parse(path: String): ConfigParsingCodes { // inits config rules and figures set
        val inputReader = File(path).bufferedReader(Charsets.UTF_8)

        val figuresArray = inputReader.readLine().split(",\\s".toRegex()).toTypedArray()
        figuresSet = figuresArray.toSet()
        if (figuresArray.size != figuresSet.size) {
            return ConfigParsingCodes.DUPLICATES
        }
        rules = mutableMapOf()

        var lineNumber = 1
        inputReader.readLines().forEach {
            if (it.trim().isNotEmpty()) {

                val curLineElements = extractRuleElements(it.trim())
                if (curLineElements.isEmpty()) {
                    return ConfigParsingCodes.INCORRECT_FORMAT_OF_RULE
                }

                val parseRuleResult = parseRule(curLineElements)
                if (parseRuleResult != ConfigParsingCodes.SUCCESS) {
                    return parseRuleResult
                }

            }
            lineNumber++
        }
        return ConfigParsingCodes.SUCCESS
    }

    override fun firstWinsSecond(a: String, b: String): Boolean {
        return rules[a]?.contains(b) == true
    }

    private fun extractRuleElements(it: String): Array<String> {
        val curLineElements = it.split("\\s".toRegex()).toTypedArray()
        if (curLineElements.size != 3) {
            return emptyArray()
        }
        return curLineElements
    }

    private fun parseRule(curLineElements: Array<String>): ConfigParsingCodes {
        var figureOne = curLineElements[0]
        val sign = curLineElements[1]
        var figureTwo = curLineElements[2]

        if (!figuresSet.contains(figureOne) || !figuresSet.contains(figureTwo) || (sign != ">" && sign != "<")
            || figureOne == figureTwo) {
            return ConfigParsingCodes.INCORRECT_FORMAT_OF_RULE
        }
        if (sign == "<") {
            val t = figureOne
            figureOne = figureTwo
            figureTwo = t
        }
        if (rules[figureTwo]?.contains(figureOne) == true) {
            return ConfigParsingCodes.CONFLICTING_RULES
//            throw InputMismatchException(incorrectInputOfRuleMessage(
//                lineNumber,
//                "Rule in the $lineNumber line conflicts with some previous rule")
//            )
        }

        if (rules[figureOne] == null) {
            rules[figureOne] = mutableListOf(figureTwo)
        } else {
            rules[figureOne]?.add(figureTwo)
        }
        return ConfigParsingCodes.SUCCESS
    }

    private fun incorrectInputOfRuleMessage(lineNumber: Int, additionalInfo: String = "") =
        "Incorrect format of rule in string {$lineNumber}. The expected format is: A > B, " +
                "where A and B are different figures from given set in the first line of config file. $additionalInfo"

    companion object {
        private const val INCORRECT_INPUT_OF_FIGURES_SET =
            "There are duplicates of figures in config file. Please, check it and try again"
    }


}