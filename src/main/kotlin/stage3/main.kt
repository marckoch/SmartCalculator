package stage3

fun main() {
    while (true) {
        val line = readLine()!!

        if (line.isEmpty()) continue
        if (line == "/exit") break
        if (line == "/help") {
            println("The program calculates the sum of numbers")
            continue
        }

        println(line
            .split(" ")
            .map { it.toInt() }
            .sum())
    }
    println("Bye!")
}