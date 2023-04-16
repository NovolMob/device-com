package services

import arrow.core.Either
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repositories.IWorkerRepository
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.WorkerCreateModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.models.WorkerUpdateModel
import ru.novolmob.core.models.ids.WorkerId
import utils.EitherUtil.ifRight

interface IWorkerService: IService {
    val workers: StateFlow<List<WorkerModel>>

    fun createWorker(worker: WorkerCreateModel)
    fun updateWorker(id: WorkerId, worker: WorkerUpdateModel)
    fun deleteWorker(id: WorkerId)
}

class WorkerServiceImpl(
    val workerRepository: IWorkerRepository
): IWorkerService, AbstractService() {
    private val _workers = MutableStateFlow(emptyList<WorkerModel>())
    override val workers: StateFlow<List<WorkerModel>> = _workers.asStateFlow()

    init {
        serviceScope.launch {
            startLoading()
            update()
            stopLoading()
        }
    }

    override fun createWorker(worker: WorkerCreateModel) {
        serviceScope.launch {
            workerRepository.post(worker).ifRight { workerModel ->
                _workers.update { (it + workerModel).distinctBy { it.id } }
            }
        }
    }

    override fun updateWorker(id: WorkerId, worker: WorkerUpdateModel) {
        serviceScope.launch {
            workerRepository.put(id, worker).ifRight { workerModel ->
                _workers.update { it.map { model -> if (model.id == workerModel.id) workerModel else model } }
            }
        }
    }

    override fun deleteWorker(id: WorkerId) {
        serviceScope.launch {
            workerRepository.delete(id).ifRight { answer ->
                if (answer)
                    _workers.update { it.mapNotNull { model -> if (model.id == id) null else model } }
            }
        }
    }

    override suspend fun update(): Either<AbstractBackendException, Page<WorkerModel>> =
        workerRepository.getAll().ifRight { workers ->
            _workers.update { workers.list }
            workers
        }

    override suspend fun clear() {
        _workers.update { emptyList() }
    }

}