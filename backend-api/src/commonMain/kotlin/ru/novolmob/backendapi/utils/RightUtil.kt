package ru.novolmob.backendapi.utils

import ru.novolmob.backendapi.rights.Rights
import ru.novolmob.core.models.Code

object RightUtil {
    private val availableRights: List<Rights> =
        listOf(
            Rights,
            Rights.Devices, Rights.Devices.Reading, Rights.Devices.Inserting,
            Rights.Devices.Updating, Rights.Devices.Deleting, Rights.Devices.Details,
            Rights.Devices.Details.Inserting, Rights.Devices.Details.Updating,
            Rights.Devices.Details.Deleting,
            Rights.DeviceTypes, Rights.DeviceTypes.Reading, Rights.DeviceTypes.Inserting,
            Rights.DeviceTypes.Updating, Rights.DeviceTypes.Deleting, Rights.DeviceTypes.Details,
            Rights.DeviceTypes.Details.Inserting, Rights.DeviceTypes.Details.Updating,
            Rights.DeviceTypes.Details.Deleting,
            Rights.Points, Rights.Points.Reading, Rights.Points.Inserting,
            Rights.Points.Updating, Rights.Points.Deleting, Rights.Points.Details,
            Rights.Points.Details.Inserting, Rights.Points.Details.Updating,
            Rights.Points.Details.Deleting,
            Rights.Cities, Rights.Cities.Reading, Rights.Cities.Inserting,
            Rights.Cities.Updating, Rights.Cities.Deleting, Rights.Cities.Details,
            Rights.Cities.Details.Inserting, Rights.Cities.Details.Updating,
            Rights.Cities.Details.Deleting,
            Rights.OrderStatuses, Rights.OrderStatuses.Reading, Rights.OrderStatuses.Inserting,
            Rights.OrderStatuses.Updating, Rights.OrderStatuses.Deleting, Rights.OrderStatuses.Details,
            Rights.OrderStatuses.Details.Inserting, Rights.OrderStatuses.Details.Updating,
            Rights.OrderStatuses.Details.Deleting,
            Rights.Orders, Rights.Orders.Reading, Rights.Orders.Reading.BySelfPointId,
            Rights.Orders.Reading.ByPointId, Rights.Orders.Reading.ByUserId, Rights.Orders.Status,
            Rights.Orders.Status.Updating, Rights.Orders.Status.Deleting,
            Rights.Workers, Rights.Workers.Reading, Rights.Workers.Reading.ByPointId,
            Rights.Workers.Reading.List, Rights.Workers.Inserting, Rights.Workers.Updating,
            Rights.Workers.Deleting,
            Rights.Users, Rights.Users.Reading, Rights.Users.Reading.ByCityId,
            Rights.Users.Reading.List, Rights.Users.Updating, Rights.Users.Deleting
        )
    private val mapAvailableRights = availableRights.associateBy { it.code }

    fun find(code: Code): Rights? = mapAvailableRights[code]
    fun getTree(right: Rights): List<Code> =
        if (mapAvailableRights.containsKey(right.code))
            listOf(right.code) + (right.parent?.let(::getTree) ?: emptyList())
        else emptyList()
}