package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.database.models.Code
import ru.novolmob.database.models.ids.GrantedRightId
import ru.novolmob.database.models.ids.WorkerId

@Serializable
@Resource("granted_rights")
class GrantedRights(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val grantedRights: GrantedRights, val id: GrantedRightId)
    @Serializable
    @Resource("worker_id/{workerId}")
    class ByWorkerId(val grantedRights: GrantedRights, val workerId: WorkerId) {
        @Serializable
        @Resource("codes")
        class Codes(val byWorkerId: ByWorkerId) {
            @Serializable
            @Resource("{code}")
            class Id(val codes: Codes, val code: Code)
        }
    }
}