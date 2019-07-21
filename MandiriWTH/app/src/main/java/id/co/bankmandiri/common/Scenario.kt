package id.co.bankmandiri.common

object Scenario {
    val questionToAnwerListMap: Map<String, List<String>> = mapOf(
            "Hello, perkenalkan saya Mei, asisten pribadi Anda apa yang bisa saya bantu?" to listOf(
                    "Buka Rekening Baru"
            ),
            "Ok, Mei bicara dengan siapa?" to listOf(),
            "Enaknya Mei panggil apa?" to listOf(
                    "Bapak", "Ibu", "Kakak"
            ),
            "Sekalian no HP kakak Budi" to listOf()
    )

    fun getQuestionFromAnswer(answer: String): String {
        return when (answer.toLowerCase()) {
            "buka rekening baru" -> "Ok, Mei bicara dengan siapa?"
            "Bapak", "Ibu", "Kakak" -> "Ok dech, boleh minta email kakak Budi"
            "budi123@gmail.com" -> "Sekalian no HP kakak Budi"
            else -> "Enaknya mei panggil apa?"
        }
    }
}