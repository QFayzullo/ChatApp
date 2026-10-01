package uz.gita.chatapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Singleton

class TokenStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val deviceIdKey = stringPreferencesKey("device_id")

    suspend fun saveTokens(accessToken: String, refreshToken: String, deviceId: String) {
        context.dataStore.edit { prefs ->
            prefs[accessTokenKey] = accessToken
            prefs[refreshTokenKey] = refreshToken
            prefs[deviceIdKey] = deviceId
        }
    }

    suspend fun getAccessToken(): String? {
        return context.dataStore.data.map { it[accessTokenKey] }.first()
    }

    val accessTokenFlow: Flow<String?> =
        context.dataStore.data.map { it[accessTokenKey] }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
    suspend fun hasValidSession(): Boolean {
        return getAccessToken() != null
    }
}