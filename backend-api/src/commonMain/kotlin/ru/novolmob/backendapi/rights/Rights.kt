package ru.novolmob.backendapi.rights

import ru.novolmob.backendapi.utils.RightUtil
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.Code.Companion.code


sealed class Rights(string: String, open val parent: Rights? = Companion) {
    open val code: Code = string.code()
    val tree: List<Code> by lazy { RightUtil.getTree(this) }
    companion object: Rights("all", null)
    sealed class Devices(string: String, override val parent: Rights = Devices): Rights(string) {
        companion object: Rights("devices-all")
        object Reading: Devices("devices-reading")
        object Inserting: Devices("device-inserting")
        object Updating: Devices("device-updating")
        object Deleting: Devices("device-deleting")
        sealed class Details(string: String, override val parent: Devices = Details): Devices(string) {
            companion object: Devices("device-details-all")
            object Inserting: Details("device-details-inserting")
            object Updating: Details("device-detail-updating")
            object Deleting: Details("device-detail-deleting")
        }
    }
    sealed class DeviceTypes(string: String, override val parent: Rights = DeviceTypes): Rights(string) {
        companion object: Rights("device-types-all")
        object Reading: DeviceTypes("device-types-reading")
        object Inserting: DeviceTypes("device-type-inserting")
        object Updating: DeviceTypes("device-type-updating")
        object Deleting: DeviceTypes("device-type-deleting")
        sealed class Details(string: String, override val parent: DeviceTypes = Details): DeviceTypes(string) {
            companion object: DeviceTypes("device-type-details-all")
            object Inserting: Details("device-type-details-inserting")
            object Updating: Details("device-type-detail-updating")
            object Deleting: Details("device-type-detail-deleting")
        }
    }
    sealed class Points(string: String, override val parent: Rights = Points): Rights(string) {
        companion object: Rights("points-all")
        object Reading: Points("points-reading")
        object Inserting: Points("point-inserting")
        object Updating: Points("point-updating")
        object Deleting: Points("point-deleting")
        sealed class Details(string: String, override val parent: Points = Details): Points(string) {
            companion object: Points("point-details-all")
            object Inserting: Details("point-details-inserting")
            object Updating: Details("point-detail-updating")
            object Deleting: Details("point-detail-deleting")
        }
    }
    sealed class Cities(string: String, override val parent: Rights = Cities): Rights(string) {
        companion object: Rights("cities-all")
        object Reading: Cities("cities-reading")
        object Inserting: Cities("city-inserting")
        object Updating: Cities("city-updating")
        object Deleting: Cities("city-deleting")
        sealed class Details(string: String, override val parent: Cities = Details): Cities(string) {
            companion object: Cities("city-details-all")
            object Inserting: Details("city-details-inserting")
            object Updating: Details("city-detail-updating")
            object Deleting: Details("city-detail-deleting")
        }
    }
    sealed class OrderStatuses(string: String, override val parent: Rights = OrderStatuses): Rights(string) {
        companion object: Rights("order-statuses-all")
        object Reading: OrderStatuses("order-statuses-reading")
        object Inserting: OrderStatuses("order-status-inserting")
        object Updating: OrderStatuses("order-status-updating")
        object Deleting: OrderStatuses("order-status-deleting")
        sealed class Details(string: String, override val parent: OrderStatuses = Details): OrderStatuses(string) {
            companion object: OrderStatuses("order-status-details-all")
            object Inserting: Details("order-status-details-inserting")
            object Updating: Details("order-status-detail-updating")
            object Deleting: Details("order-status-detail-deleting")
        }
    }
    sealed class Orders(string: String, override val parent: Rights = Orders): Rights(string) {
        companion object: Rights("orders-all")
        sealed class Reading(string: String, override val parent: Orders = Reading): Orders(string) {
            companion object: Orders("orders-reading-all")
            object BySelfPointId: Reading("orders-reading-by-self-point-id")
            object ByPointId: Reading("orders-reading-by-point-id")
            object ByUserId: Reading("orders-reading-by-user-id")
        }
        sealed class Status(string: String, override val parent: Orders = Status): Orders(string) {
            companion object: Orders("orders-status-all")
            object Updating: Status("orders-status-updating")
            object Deleting: Status("orders-status-deleting")
        }
    }
    sealed class Workers(string: String, override val parent: Rights = Workers): Rights(string) {
        companion object: Rights("workers-all")
        sealed class Reading(string: String, override val parent: Workers = Reading): Workers(string) {
            companion object: Workers("workers-reading-all")
            object ByPointId: Reading("workers-reading-by-point-id")
            object List: Reading("workers-reading-list")
        }
        object Inserting: Workers("worker-inserting")
        object Updating: Workers("worker-updating")
        object Deleting: Workers("worker-deleting")
    }
    sealed class Users(string: String, override val parent: Rights = Users): Rights(string) {
        companion object: Rights("users-all")
        sealed class Reading(string: String, override val parent: Users = Reading): Users(string) {
            companion object: Users("users-reading-all")
            object ByCityId: Reading("users-reading-by-city-id")
            object List: Reading("users-reading-list")
        }
        object Updating: Users("users-updating")
        object Deleting: Users("users-deleting")
    }

}
