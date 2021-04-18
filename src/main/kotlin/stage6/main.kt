package stage6


import java.util.*

fun main() {
    Calculator().go()
}

class Calculator {
    private val variables = emptyMap<String, Int>().toMutableMap()

    fun go() {
        while (true) {
            val line = readLine()!!

            if (line.isEmpty()) continue

            if (line.startsWith("/")) {
                when (line) {
                    "/exit", "/x" -> break
                    "/help", "/h" -> {
                        println("The program is an awesome calculator!")
                        continue
                    }
                    "/v" -> {
                        println(variables)
                        continue
                    }
                    else -> {
                        println("Unknown command")
                        continue
                    }
                }
            } else {
                processInput(line)
            }
        }
        println("Bye!")
    }

    private fun processInput(line: String) {
        try {
            if (line.contains("=")) {
                assignVariable(line)
            } else {
                println(calculate(line))
            }
        } catch (e: UnknownVariableException) {
            println(e.message)
        }
    }

    private fun calculate(line: String): Int {
        val operation = Stack<String>()
        val stack = Stack<Int>()

        val tokens = line.trim().split(" ").toMutableList()
        while (tokens.isNotEmpty()) {
            var token = tokens.removeAt(0)

            token = cleanupRedundantSigns(token)

            if (token in "+-*/") {
                operation.push(token)
            } else {
                if (operation.isEmpty()) {
                    stack.push(getValue(token))
                } else {
                    when (operation.pop()) {
                        "+" -> stack.push(stack.pop() + getValue(token))
                        "-" -> stack.push(stack.pop() - getValue(token))
                        "*" -> stack.push(stack.pop() * getValue(token))
                        "/" -> stack.push(stack.pop() / getValue(token))
                        else -> throw UnknownOperatorException()
                    }
                }
            }
        }
        return stack.pop()
    }

    private fun cleanupRedundantSigns(token: String): String {
        return when {
            // even multiple of minus signs --> +
            token.matches(Regex("(--)*")) -> "+"
            // multiple minus signs (because of previous line sure to be an odd number!)
            token.matches(Regex("-*")) -> "-"
            // multiple plus signs
            token.matches(Regex("\\+*")) -> "+"
            else -> token.trim()
        }
    }

    // something like:  a = 5
    // left part is variable, right part is number or other variable we already know
    private fun assignVariable(line: String) {
        val variable = line.substringBefore("=").trim()
        if (!isValidVariableName(variable)) {
            println("Invalid identifier")
            return
        }

        val value = line.substringAfter("=").trim()
        if (!isNumber(value) && !isValidVariableName(value)) {
            println("Invalid identifier")
            return
        }

        variables[variable] = getValue(value)
    }

    private fun isValidVariableName(name: String): Boolean {
        return name.matches("[a-z]*".toRegex())
    }

    private fun isNumber(name: String): Boolean {
        return name.toIntOrNull() != null
    }

    private fun getValue(token: String): Int {
        return token.toIntOrNull() ?: variables[token] ?: throw UnknownVariableException()
    }
}

class UnknownVariableException : Exception("Unknown variable")
class UnknownOperatorException : Exception("Unknown operator")