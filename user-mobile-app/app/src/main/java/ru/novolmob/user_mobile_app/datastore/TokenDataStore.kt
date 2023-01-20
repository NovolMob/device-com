package ru.novolmob.user_mobile_app.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.models.TokenModel
import ru.novolmob.core.models.AccessToken

interface ITokenDataStore {
    val tokenFlow: StateFlow<TokenModel?>
    suspend fun token(tokenModel: TokenModel?)

    fun initialized(): Boolean
}

class TokenDataStore(private val context: Context): ITokenDataStore, AbstractDataStore() {

    companion object {
        val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
    }

    private val _tokenFlow = MutableStateFlow<TokenModel?>(null)
    override val tokenFlow: StateFlow<TokenModel?> = _tokenFlow.asStateFlow()
    private val initialized = MutableStateFlow(false)

    override fun initialized(): Boolean = initialized.value

    init {
        dataStoreCoroutineScope.launch {
            launch {
                context.tokenDataStore.data.map { preferences ->
                    val accessToken = preferences[ACCESS_TOKEN_KEY]?.let { AccessToken(it) } ?: return@map null
                    TokenModel(
                        accessToken = accessToken
                    )
                }.collect(_tokenFlow)
            }
            launch {
                tokenFlow.collectLatest {
                    initialized.update { true }
                    cancel()
                }
            }
        }
    }

    override suspend fun token(tokenModel: TokenModel?) {
        context.tokenDataStore.edit { preferences ->
            tokenModel?.let {
                preferences[ACCESS_TOKEN_KEY] = it.accessToken.string
            } ?: preferences.remove(ACCESS_TOKEN_KEY)
        }
    }

}