package ru.novolmob.exposeddatabase.utils

import org.jetbrains.exposed.sql.SizedIterable
import ru.novolmob.core.models.Language
import ru.novolmob.exposeddatabase.entities.*

object DetailsUtil {

    fun SizedIterable<PointDetail>.get(language: Language): PointDetail =
        this.find { it.language == language } ?: this.first()
    fun SizedIterable<CityDetail>.get(language: Language): CityDetail =
        this.find { it.language == language } ?: this.first()
    fun SizedIterable<DeviceDetail>.get(language: Language): DeviceDetail =
        this.find { it.language == language } ?: this.first()
    fun SizedIterable<DeviceTypeDetail>.get(language: Language): DeviceTypeDetail =
        this.find { it.language == language } ?: this.first()
    fun SizedIterable<OrderStatusDetail>.get(language: Language): OrderStatusDetail =
        this.find { it.language == language } ?: this.first()

}