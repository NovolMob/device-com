package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.worker.Workers
import ru.novolmob.backend.util.AuthUtil.worker
import ru.novolmob.backend.util.AuthUtil.workerPermission
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.repositories.IWorkerRepository

object WorkerRouting: IRouting, KoinComponent {
    val workerRepository: IWorkerRepository by inject()

    override fun Route.routingForWorker() {
        resource<Workers.Worker> {
            workerPermission {
                get {
                    val worker = worker()
                    val either = workerRepository.get(worker.id)
                    call.respond(either)
                }
            }
        }
    }

}