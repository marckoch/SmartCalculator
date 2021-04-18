package stage5

fun main() {
    while (true) {
        val line = readLine()!!

        if (line.isEmpty()) continue

        if (line.startsWith("/")) {
            when (line) {
                "/exit" -> break
                "/help" -> {
                    println("The program is an awesome calculator!")
                    continue
                }
                else -> {
                    println("Unknown command")
                    continue
                }
            }
        }

        val result = processInput(line)

        println(result)
    }
    println("Bye!")
}

private fun processInput(line: String): Int {
    var result = 0
    var operation = "+"

    val tokens = line.split(" ").toMutableList()
    while (tokens.isNotEmpty()) {
        var token = tokens.removeAt(0)

        when {
            // even multiple of minus signs --> +
            token.matches(Regex("(--)*")) -> token = "+"
            // multiple minus signs (because of previous line sure to be an odd number!)
            token.matches(Regex("-*")) -> token = "-"
            // multiple plus signs
            token.matches(Regex("\\+*")) -> token = "+"
        }

        if (token in "+-") {
            operation = token
        } else {
            try {
                when (operation) {
                    "+" -> result += token.toInt()
                    "-" -> result -= token.toInt()
                }
            } catch (e: NumberFormatException) {
                println("Invalid expression")
            }
        }
    }
    return result
}