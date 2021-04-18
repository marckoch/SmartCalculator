package stage2

fun main() {
    while (true) {
        val line = readLine()!!

        if (line.isEmpty()) continue
        if (line == "/exit") break

        println(line
            .split(" ")
            .map { it.toInt() }
            .sum())
    }
    println("Bye!")
}