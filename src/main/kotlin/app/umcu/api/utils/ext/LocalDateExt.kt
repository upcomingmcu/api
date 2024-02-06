package app.umcu.api.utils.ext

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalDate as jLocalDate

/**
 * @return `true` if the date has already passed, otherwise `false`.
 */
fun LocalDate.hasHappened() = !this.toJavaLocalDate().isAfter(jLocalDate.now())
