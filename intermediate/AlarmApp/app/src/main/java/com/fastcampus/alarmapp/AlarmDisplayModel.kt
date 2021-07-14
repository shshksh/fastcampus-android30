package com.fastcampus.alarmapp

data class AlarmDisplayModel(
    val hour: Int,
    val minute: Int,
    var onOff: Boolean
) {
    val timeText: String
        get() {
            val h = "%02d".format(if (hour < 12) hour else hour - 12)
            val m = "%02d".format(minute)

            return "$h:$m"
        }

    val amPmText = if (hour < 12) "AM" else "PM"

    val onOffText = if (onOff) "알람 끄기" else "알람 켜기"

    fun makeDataForDB() = "$hour:$minute"
}
