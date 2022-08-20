package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.EditProfileModel
import com.depromeet.housekeeper.model.ProfileData
import com.depromeet.housekeeper.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SettingProfileViewModel : ViewModel() {

    init {
        getMe()
    }

    private val _myData: MutableStateFlow<ProfileData?> = MutableStateFlow(null)
    val myData: StateFlow<ProfileData?>
        get() = _myData


    private fun getMe() {
        viewModelScope.launch {
            Repository.getMe().runCatching {
                collect {
                    _myData.value = it
                }
            }
        }
    }

    fun updateMe(
        memberName: String,
        profilePath: String,
        statueMessage: String,
    ) {
        viewModelScope.launch {
            Repository.updateMe(
                EditProfileModel(
                    memberName, profilePath, statueMessage
                )
            )
                .runCatching {
                    collect {
                        it.message
                    }
                }
        }
    }

    fun updateProfile(profilePath: String) {
        _myData.value = _myData.value?.copy(profilePath = profilePath)
    }
}