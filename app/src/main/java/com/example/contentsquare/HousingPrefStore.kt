package com.example.contentsquare

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.reflect.Type

enum class PreferencesKeys(val keyName: String) {
    USER_SIGN_IN_DETAILS("USER_SIGN_IN_DETAILS"),
    TRUECALLER_VALIDATE_DATA_RESPONSE("TRUECALLER_VALIDATE_DATA_RESPONSE"),
    IS_ADD_DETAILS_SCREEN_REQUIRED("IS_ADD_DETAILS_SCREEN_REQUIRED"),
    IS_LOGIN_REQUIRED("IS_LOGIN_REQUIRED")
}


class HousingPrefStore(private val context: Context, private val gson: Gson) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        PREFERENCE_STORE_NAME
    )

    /**
     * @param preferencesKeys is used for mapping the value to it
     * @param value will be saved in mutable preferences and mapped to the key
     */
    suspend fun storeValue(preferencesKeys: PreferencesKeys, value: String) =
        try {
            context.dataStore.edit {
                it[stringPreferencesKey(preferencesKeys.keyName)] = value
            }
            Result.success(0)
        } catch (ex: Exception) {
            Result.failure<Exception>(ex)
        }
    /**
     * @param preferencesKeys is used for mapping the value to it
     * @param value will be saved in mutable preferences and mapped to the key
     */
    suspend fun storeIntValue(preferencesKeys: PreferencesKeys, value: Int) =
        try {
            context.dataStore.edit {
                it[intPreferencesKey(preferencesKeys.keyName)] = value
            }
            Result.success(0)
        } catch (ex: Exception) {
            Result.failure<Exception>(ex)
        }

    /**
     * @param preferencesKeys is used for mapping the value to it
     * @param value will be saved in mutable preferences and mapped to the key
     */
    suspend fun storeLongValue(preferencesKeys: PreferencesKeys, value: Long) =
        try {
            context.dataStore.edit {
                it[longPreferencesKey(preferencesKeys.keyName)] = value
            }
            Result.success(0)
        } catch (ex: Exception) {
            Result.failure<Exception>(ex)
        }

    /**
     * @param preferencesKeys is used for mapping the value to it
     * @param obj will be saved in mutable preferences and mapped to the key
     */
    suspend fun <T> storeAsObject(preferencesKeys: PreferencesKeys, obj: T) {
        val valueAsString = gson.toJson(obj)
        context.dataStore.edit {
            it[stringPreferencesKey(preferencesKeys.keyName)] = valueAsString
        }
    }

    /**
     * You can use this method to get the object given by class type in parameters
     * @param preferencesKeys is used for mapping the value to it
     * @param type is the class type in which you want the value of that class's object
     * @return value as class type object which is wrapped in
     */
    fun <T> readValueAsObject(
        preferencesKeys: PreferencesKeys,
        type: Class<T>
    ): Flow<T?> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                kotlin.runCatching {
                    fromJson(it[stringPreferencesKey(preferencesKeys.keyName)], type)
                }.getOrNull()
            }
            .distinctUntilChanged()

    /**
     * You can use this method to get the object given by class type in parameters
     * @param preferencesKeys is used for mapping the value to it
     * @param type is the class type in which you want the value of that class's object
     * @return value as class type object which is wrapped in
     */
    fun <T> readValueAsObject(
        preferencesKeys: PreferencesKeys,
        type: Type
    ): Flow<T?> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                kotlin.runCatching {
                    fromJson(it[stringPreferencesKey(preferencesKeys.keyName)], type) as T
                }.getOrNull()
            }
            .distinctUntilChanged()

    /**
     * You can use this method to get value saved against the key
     * @param preferencesKeys is used for mapping the value to it
     * @return value which was saved in datastore against the key
     */
    fun <T> readValue(preferencesKeys: PreferencesKeys): Flow<T> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                it[stringPreferencesKey(preferencesKeys.keyName)] as T
            }
            .distinctUntilChanged()
    /**
     * You can use this method to get value saved against the key
     * @param preferencesKeys is used for mapping the value to it
     * @return value which was saved in datastore against the key
     */
    fun <T> readIntValue(preferencesKeys: PreferencesKeys): Flow<T> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                it[intPreferencesKey(preferencesKeys.keyName)] as T
            }
            .distinctUntilChanged()

    /**
     * You can use this method to get value saved against the key
     * @param preferencesKeys is used for mapping the value to it
     * @return value which was saved in datastore against the key
     */
    fun <T> readLongValue(preferencesKeys: PreferencesKeys): Flow<T> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                it[longPreferencesKey(preferencesKeys.keyName)] as T
            }
            .distinctUntilChanged()
    private fun <T> fromJson(json: String?, type: Class<T>): T {
        return gson.fromJson(json, type)
    }

    private fun <T> fromJson(json: String?, type: Type): T {
        return gson.fromJson(json, type)
    }

    suspend fun resetDataStore() {
        try {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
            Result.success(0)
        } catch (ex: Exception) {
            Result.failure<Exception>(ex)
        }
    }

    companion object {
        private const val PREFERENCE_STORE_NAME = "temp"
    }
}
