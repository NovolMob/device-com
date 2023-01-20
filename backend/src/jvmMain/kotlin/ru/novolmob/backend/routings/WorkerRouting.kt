package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.worker.Points
import ru.novolmob.backend.ktorrouting.worker.Worker
import ru.novolmob.backend.ktorrouting.worker.Workers
import ru.novolmob.backend.util.AuthUtil.delete
import ru.novolmob.backend.util.AuthUtil.get
import ru.novolmob.backend.util.AuthUtil.post
import ru.novolmob.backend.util.AuthUtil.put
import ru.novolmob.backend.util.AuthUtil.worker
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.models.WorkerCreateModel
import ru.novolmob.backendapi.models.WorkerUpdateModel
import ru.novolmob.backendapi.repositories.IWorkerRepository
import ru.novolmob.backendapi.rights.Rights

object WorkerRouting: IRouting, KoinComponent {
    val workerRepository: IWorkerRepository by inject()

    override fun Route.routingForWorker() {
        get<Worker> {
            val worker = worker()
            val either = workerRepository.get(worker.id)
            call.respond(either)
        }
        get<Workers>(Rights.Workers.Reading.List) {
            val either = workerRepository.getAll(it)
            call.respond(either)
        }
        get<Points.Id.Workers>(Rights.Workers.Reading.ByPointId) {
            val either = workerRepository.getAllByPointId(it.id.id)
            call.respond(either)
        }
        post<Workers>(Rights.Workers.Inserting) {
            val model: WorkerCreateModel = call.receive()
            val either = workerRepository.post(model)
            call.respond(either)
        }
        post<Workers.Id>(Rights.Workers.Updating) {
            val model: WorkerCreateModel = call.receive()
            val either = workerRepository.post(it.id, model)
            call.respond(either)
        }
        put<Workers.Id>(Rights.Workers.Updating) {
            val model: WorkerUpdateModel = call.receive()
            val either = workerRepository.put(it.id, model)
            call.respond(either)
        }
        delete<Workers.Id>(Rights.Workers.Deleting) {
            val either = workerRepository.delete(it.id)
            call.respond(either)
        }
    }

}