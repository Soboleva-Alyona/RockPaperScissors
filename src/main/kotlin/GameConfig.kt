import java.io.File

class GameConfig : Config {

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
        }

        if (rules[figureOne] == null) {
            rules[figureOne] = mutableListOf(figureTwo)
        } else {
            rules[figureOne]?.add(figureTwo)
        }
        return ConfigParsingCodes.SUCCESS
    }

}