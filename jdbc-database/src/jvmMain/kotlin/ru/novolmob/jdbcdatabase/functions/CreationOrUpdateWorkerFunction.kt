package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Credentials
import ru.novolmob.jdbcdatabase.tables.Workers
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

object CreationOrUpdateWorkerFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val workerId = idColumn(name = "f_worker_id", constructor = ::WorkerId)
    val pointId = idColumn(name = "f_point_id", constructor = ::PointId)
    val firstname = firstname("f_firstname")
    val lastname = lastname("f_lastname")
    val patronymic = patronymic("f_patronymic")
    val lang = language("f_language")
    val email = email("f_email")
    val phoneNumber = phoneNumber("f_phone_number")
    val password = password("f_password")

    override fun body(): String =
        "DECLARE\n" +
                "    new_worker_id uuid;\n" +
                "    ref refcursor DEFAULT 'cursor_$name';\n" +
                "BEGIN\n" +
                "    IF ($workerId IS NOT NULL) THEN\n" +
                "        UPDATE $Workers SET " +
                "${Workers.pointId.name} = $pointId, " +
                "${Workers.firstname.name} = $firstname, " +
                "${Workers.lastname.name} = $lastname, " +
                "${Workers.patronymic.name} = $patronymic, " +
                "${Workers.language.name} = $lang " +
                "WHERE ${Workers.id} = $workerId;\n" +
                "        UPDATE ${Credentials.WorkerCredentials} SET " +
                "${Credentials.WorkerCredentials.email.name} = $email, " +
                "${Credentials.WorkerCredentials.phoneNumber.name} = $phoneNumber, " +
                "${Credentials.WorkerCredentials.password.name} = $password " +
                "WHERE ${Credentials.WorkerCredentials.ownerId} = $workerId;\n" +
                "        OPEN ref FOR SELECT * FROM ${CredentialView.WorkerCredentialView} WHERE ${CredentialView.WorkerCredentialView.id} = $workerId;\n" +
                "    ELSE\n" +
                "        INSERT INTO $Workers (" +
                "${Workers.pointId.name}, " +
                "${Workers.firstname.name}, " +
                "${Workers.lastname.name}, " +
                "${Workers.patronymic.name}, " +
                Workers.language.name +
                ") VALUES ($pointId, $firstname, $lastname, $patronymic, $lang) RETURNING ${Workers.id} INTO new_worker_id;\n" +
                "        INSERT INTO ${Credentials.WorkerCredentials} (" +
                "${Credentials.WorkerCredentials.ownerId.name}, " +
                "${Credentials.WorkerCredentials.email.name}, " +
                "${Credentials.WorkerCredentials.phoneNumber.name}, " +
                Credentials.WorkerCredentials.password.name +
                ") VALUES (new_worker_id, $email, $phoneNumber, $password);\n" +
                "        OPEN ref FOR SELECT * FROM ${CredentialView.WorkerCredentialView} WHERE ${CredentialView.WorkerCredentialView.id} = new_worker_id;\n" +
                "    END IF;\n" +
                "    RETURN ref;\n" +
                "END;"

    suspend fun <T> call(
        workerId: WorkerId? = null,
        pointId: PointId? = null,
        firstname: Firstname,
        lastname: Lastname,
        patronymic: Patronymic? = null,
        language: Language,
        email: Email? = null,
        phoneNumber: PhoneNumber,
        password: Password,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.workerId valueOf workerId,
            this.pointId valueOf pointId,
            this.firstname valueOf firstname,
            this.lastname valueOf lastname,
            this.patronymic valueOf patronymic,
            this.lang valueOf language,
            this.email valueOf email,
            this.phoneNumber valueOf phoneNumber,
            this.password valueOf password,
            block = block
        )

}