package stage4

fun main() {
    while (true) {
        val line = readLine()!!

        if (line.isEmpty()) continue
        if (line == "/exit") break
        if (line == "/help") {
            println("The program calculates the sum of numbers")
            continue
        }

        var sum = 0
        var operation = "+"

        val tokens = line.split(" ").toMutableList()
        while (tokens.isNotEmpty()) {
            var token = tokens.removeAt(0)

            when {
                // even multiple of minus signs --> +
                token.matches(Regex("(--)*")) -> token = "+"
                // multiple minus signs (can only be odd number!)
                token.matches(Regex("-*")) -> token = "-"
                // multiple plus signs
                token.matches(Regex("\\+*")) -> token = "+"
            }

            if (token in "+-") {
                operation = token
            } else {
                try {
                    when (operation) {
                        "+" -> sum += token.toInt()
                        "-" -> sum -= token.toInt()
                    }
                } catch (e: NumberFormatException) {
                    println("$token is not a valid number!")
                }
            }
        }

        println(sum)
    }
    println("Bye!")
}