package com.rprikhodko.habrareader

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utils {
    companion object {
        fun formatTime(timeString: String): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxxxx")
            val dateTime: LocalDateTime = LocalDateTime.parse(timeString, formatter)
            val currentDate = LocalDateTime.now()

            var datepart: String = dateTime.dayOfMonth.toString() + " " + dateTime.monthStringValue
            if(dateTime.year == currentDate.year) {
                if(dateTime.dayOfYear == currentDate.dayOfYear) {
                    datepart = "сегодня"
                } else if(dateTime.dayOfYear.plus(1) == currentDate.dayOfYear) {
                    datepart = "вчера"
                }
            } else {
                datepart += " " + dateTime.year
            }
            return datepart + " в " + dateTime.hour + ":" + dateTime.minute
        }

        val LocalDateTime.monthStringValue: String
            get() = when(this.monthValue) {
                1 -> "января"
                2 -> "февраля"
                3 -> "марта"
                4 -> "апреля"
                5 -> "мая"
                6 -> "июня"
                7 -> "июля"
                8 -> "августа"
                9 -> "сентября"
                10 -> "октября"
                11 -> "ноября"
                12 -> "декабря"
                else -> ""
            }
    }

}