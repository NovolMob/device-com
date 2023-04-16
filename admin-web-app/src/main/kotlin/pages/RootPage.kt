package pages

import models.Tab
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import ru.novolmob.backendapi.models.WorkerModel

external interface RootProps: Props {
    var workerModel: WorkerModel
    var tabs: List<Tab>
}

val RootPage = FC<RootProps> {


    //Profile
    div {
        h4 {
            + "Hello, World!!!"
        }
    }

}