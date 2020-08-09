package com.hani.testing.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*

class DateUtilsTest {

    val TODAY = "08-2020"

    @Test
    fun testGetCurrentTimeStamp_returnTimeStamp() {
        assertDoesNotThrow(object : Executable {
            override fun execute() {
                assertEquals(TODAY, DateUtil.currentTimeStamp)
                System.out.println("Timestamp generated Successfully")
            }
        })
    }

    @ParameterizedTest
    @ValueSource(ints = (intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)))
    fun getMonthFromNumber_returnSuccess(monthNumber: Int) {

        assertEquals(
            DateUtil.months[monthNumber],
            DateUtil.getMonthFromNumber(DateUtil.monthNumbers[monthNumber])
        )
        System.out.println(
            " ${DateUtil.monthNumbers[monthNumber]} : ${DateUtil.getMonthFromNumber(
                DateUtil.monthNumbers[monthNumber]
            )}"
        )

    }

    @ParameterizedTest
    @ValueSource(ints = (intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)))
    fun getMonthFromNumber_returnSError(monthNumber: Int) {

        val random = Random().nextInt(90) + 13
        assertEquals(
            DateUtil.getMonthFromNumber((monthNumber * random).toString())
            , DateUtil.GET_MONTH_ERROR
        )

        System.out.println(" ${monthNumber} : ${DateUtil.GET_MONTH_ERROR}")
    }
}