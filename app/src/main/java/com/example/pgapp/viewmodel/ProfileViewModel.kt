package com.example.pgapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.data.model.pgmodel.UserModel
import com.example.pgapp.data.remote.FirebaseService
import com.example.pgapp.data.repository.AuthRepository
import com.example.pgapp.data.repository.PgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val firebase: FirebaseService
) : ViewModel() {

    var user = mutableStateOf<UserModel?>(null)
    var isLoading = mutableStateOf(false)

    fun loadUser() {
        val uid = firebase.getCurrentUserId() ?: return

        viewModelScope.launch {
            isLoading.value = true
            user.value = repo.getUser(uid)
            isLoading.value = false
        }
    }

    fun logout() {
        repo.logout()
    }
}