package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GroupInfoViewModel : ViewModel() {
    init {
        getGroupName()
    }

    private val _groupName: MutableStateFlow<String> = MutableStateFlow("즐거운 우리집")
    val groupName: StateFlow<String>
        get() = _groupName

    private val _groups: MutableStateFlow<List<Assignee>> = MutableStateFlow(listOf())
    val groups: MutableStateFlow<List<Assignee>>
        get() = _groups

    private fun getGroupName() {
        viewModelScope.launch {
            Repository.getTeam().runCatching {
                collect {
                    _groupName.value = it.teamName
                    _groups.value = it.members
                }
            }
        }
    }
}