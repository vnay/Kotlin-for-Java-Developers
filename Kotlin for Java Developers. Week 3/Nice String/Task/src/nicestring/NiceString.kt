package nicestring


fun Boolean.toInt() = if (this) 1 else 0

fun String.isNice(): Boolean {
    val noCnt = arrayOf("bu", "ba", "be").none { contains(it) }

    var vowCnt = 0
    forEach { c -> if("aeiou".contains(c)) vowCnt++ }

    val dupCnt = zipWithNext().count { it.first == it.second }

    return (noCnt.toInt() + (vowCnt > 2).toInt() + (dupCnt > 0).toInt()) > 1
}