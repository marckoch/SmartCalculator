package stage8


import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    Calculator().go()
}

class Calculator {
    private val variables = emptyMap<String, BigInteger>().toMutableMap()

    fun go() {
        while (true) {
            val line = readLine()!!

            if (line.isEmpty()) continue

            if (line.startsWith("/")) {
                when (line) {
                    "/exit", "/x" -> break
                    "/help", "/h" -> println("The program is an awesome calculator!")
                    "/v" -> println(variables)
                    else -> println("Unknown command")
                }
                continue
            }

            processInput(line)
        }
        println("Bye!")
    }

    private fun processInput(line: String) {
        try {
            if (line.contains("=")) {
                assignVariable(line)
            } else {
                val queue = convertToRPN(line)
                val result = evaluateRPN(queue)
                println(result)
            }
        } catch (e: UnknownVariableException) {
            println(e.message)
        } catch (e: InvalidExpressionException) {
            println(e.message)
        } catch (e: InvalidIdentifierException) {
            println(e.message)
        }
    }

    private fun evaluateRPN(queue: ArrayDeque<Token>): BigInteger {
        val stack = Stack<Token>()

        for (token in queue) {
            if (token.isNumber()) {
                stack.push(token)
            } else if (token.isOperator()) {
                val y = stack.pop().toBigInteger() // order is reversed! the R in RPN!
                val x = stack.pop().toBigInteger() // order is reversed! the R in RPN!

                val result = when (token.toString()) {
                    "+" -> x + y
                    "-" -> x - y
                    "*" -> x * y
                    "/" -> x / y
                    "^" -> x.pow(y.toInt()).toBigDecimal().toBigInteger()
                    else -> IllegalArgumentException("Unknown operator: $token")
                }

                stack.push(Token(result.toString()))
            }
        }

        assert(stack.size == 1) { "stack should only have one element at the end of RPN evaluation! but has: $stack" }

        return stack.pop().toBigInteger()
    }

    private fun tokenize(line: String): MutableList<String> {
        // https://stackoverflow.com/questions/3373885/splitting-a-simple-maths-expression-with-regex
        val regex = "(?<=op)|(?=op)".replace("op", "[-+*/()^]")

        // super ugly :-(
        return cleanup(line)
            .trim()
            .split(regex.toRegex())
            .filter { it.trim().isNotEmpty() }
            .map { it.trim() }
            .toMutableList()
    }

    // https://brilliant.org/wiki/shunting-yard-algorithm/
    // https://en.wikipedia.org/wiki/Shunting-yard_algorithm
    private fun convertToRPN(line: String): ArrayDeque<Token> {
        val operatorStack = Stack<Token>()
        val outputQueue = ArrayDeque<Token>()

        val tokens = tokenize(line)

        while (tokens.isNotEmpty()) {
            val token = Token(tokens.removeAt(0))

            if (token.isNumber()) {
                outputQueue.add(token)
            } else if (token.isOperator()) {
                while (operatorStack.isNotEmpty() &&
                    !operatorStack.peek().isLeftBracket() &&
                    operatorStack.peek() >= token
                ) {
                    outputQueue.add(operatorStack.pop())
                }
                operatorStack.push(token)
            } else if (token.isLeftBracket()) {
                operatorStack.push(token)
            } else if (token.isRightBracket()) {
                try {
                    while (!operatorStack.peek().isLeftBracket()) {
                        outputQueue.add(operatorStack.pop())
                    }
                } catch (e: EmptyStackException) {
                    // e.g. left bracket should be there
                    throw InvalidExpressionException()
                }
                if (operatorStack.isNotEmpty() && operatorStack.peek().isLeftBracket()) {
                    operatorStack.pop() // discard left bracket
                }
            } else {
                val variable = variables[token.toString()] ?: throw UnknownVariableException()
                outputQueue.add(Token(variable.toString()))
            }
        }

        // check for non matching brackets
        if (operatorStack.contains(Token("(")) ||
            operatorStack.contains(Token(")"))
        ) {
            throw InvalidExpressionException()
        }

        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.pop())
        }

        return outputQueue
    }

    // remove multiple plus signs and minus signs +
    // replace odd number of minus with single minus
    // throw InvalidExpression for multiple ** or //
    private fun cleanup(line: String): String {
        if (line.contains("**")) {
            throw InvalidExpressionException()
        }
        if (line.contains("//")) {
            throw InvalidExpressionException()
        }

        var cleanLine = line.replace("\\+{2,}".toRegex(), "+")
        cleanLine = "-{2,}".toRegex().replace(cleanLine) { matchResult ->
            if (matchResult.value.length % 2 == 0) "+" else "-"
        }
        return cleanLine
    }

    // something like:  a = 5
    // left part is variable, right part is number or other variable we already know
    private fun assignVariable(line: String) {
        val variableName = line.substringBefore("=").trim()
        if (!isValidVariableName(variableName)) {
            throw InvalidIdentifierException()
        }

        val value = line.substringAfter("=").trim()
        if (!isNumber(value) && !isValidVariableName(value)) {
            throw InvalidIdentifierException()
        }

        variables[variableName] = value.toBigIntegerOrNull() ?: variables[value] ?: throw UnknownVariableException()
    }

    private fun isValidVariableName(name: String): Boolean {
        return name.matches("[a-z]*".toRegex())
    }

    private fun isNumber(name: String): Boolean {
        return name.toBigIntegerOrNull() != null
    }
}

class UnknownVariableException : Exception("Unknown variable")
class InvalidExpressionException : Exception("Invalid expression")
class InvalidIdentifierException : Exception("Invalid identifier")

data class Token(private val token: String) {
    fun isNumber(): Boolean {
        return token.toBigIntegerOrNull() != null
    }

    fun isOperator(): Boolean {
        return token in "+-*/^"
    }

    fun isLeftBracket(): Boolean {
        return token == "("
    }

    fun isRightBracket(): Boolean {
        return token == ")"
    }

    fun toBigInteger(): BigInteger {
        return token.toBigInteger()
    }

    // used for operator precedence
    operator fun compareTo(other: Token): Int {
        if (token == "^" && other.token != "^") return 1
        if (token != "^" && other.token == "^") return -1

        if (token in "+-" && other.token in "*/") return -1
        if (token in "*/" && other.token in "+-") return +1

        return 0
    }

    override fun toString(): String {
        return token
    }
}
