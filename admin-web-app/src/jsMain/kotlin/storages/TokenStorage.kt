package storages

import kotlinx.serialization.json.Json
import ru.novolmob.backendapi.models.TokenModel

class TokenStorage(name: String? = null, json: Json = Json): SerializableStorage<TokenModel>(name, TokenModel.serializer(), json)