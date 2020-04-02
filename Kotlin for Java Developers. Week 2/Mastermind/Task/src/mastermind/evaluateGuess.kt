package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val repo = "ABCDEF"
    val rp = secret.zip(guess).count { it.first == it.second}
    val cp = repo.sumBy { ch ->
        secret.count { it == ch }.coerceAtMost(guess.count { it == ch })
    }
    return Evaluation(rp, cp - rp)
}
