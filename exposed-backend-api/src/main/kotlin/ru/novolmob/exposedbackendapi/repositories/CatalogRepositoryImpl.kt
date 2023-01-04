package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.CatalogModel
import ru.novolmob.backendapi.models.CatalogSearchSample
import ru.novolmob.backendapi.models.DeviceShortModel
import ru.novolmob.backendapi.repositories.ICatalogRepository
import ru.novolmob.core.models.Language
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.tables.Devices
import kotlin.math.ceil

class CatalogRepositoryImpl: ICatalogRepository {
    override suspend fun getCatalog(
        sample: CatalogSearchSample,
        language: Language
    ): Either<BackendException, CatalogModel> =
        newSuspendedTransaction {
            val page = sample.page?.let { maxOf(it, 0) } ?: 0
            val pageSize = sample.pageSize?.let { maxOf(it, 1) } ?: 1

            val iterable = (sample.deviceTypeId?.let {
                Device.find { Devices.type eq it }
            } ?: Device.all())

            val searchString = sample.searchString

            val list = (searchString?.let { string ->
                iterable.mapNotNull { device ->
                    device.details.firstOrNull { it.language == language }?.let {
                        if (it.title.string.startsWith(string)) {
                            device to it
                        } else null
                    } ?: null
                }
            } ?: iterable.mapNotNull { device ->
                device.details.firstOrNull { it.language == language }?.let {
                    device to it
                } ?: null
            })

            val fromIndex = page * pageSize
            val toIndex = (page + 1) * pageSize

            val result = if (fromIndex < list.size) {
                list.subList(fromIndex, minOf(list.size, toIndex))
                    .parTraverse { (device, detail) ->
                        DeviceShortModel(
                            id = device.id.value,
                            title = detail.title,
                            description = detail.description,
                            price = device.price
                        )
                    }
            } else emptyList()

            CatalogModel(
                page = page,
                pageSize = result.size,
                devices = result,
                amountOfPages = ceil(list.size.toDouble() / pageSize).toInt()
            ).right()
        }
}