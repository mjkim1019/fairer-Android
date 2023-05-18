package com.depromeet.housekeeper.ui.statistics

import android.icu.util.Calendar
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.StatisticsRepository
import com.depromeet.housekeeper.model.ui.Ranker
import com.depromeet.housekeeper.model.ui.Stats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statsRepository: StatisticsRepository
) : BaseViewModel() {

    private val _statsFlow: MutableSharedFlow<Stats> = MutableSharedFlow()
    val statsFlow get() = _statsFlow

    private val _rankFlow: MutableStateFlow<MutableList<Ranker>> = MutableStateFlow(mutableListOf())
    val rankFlow: StateFlow<List<Ranker>> get() = _rankFlow

    private val _totalChoreCnt: MutableStateFlow<Int> = MutableStateFlow(0)
    val totalChoreCnt: StateFlow<Int> get() = _totalChoreCnt

    private var _currentDate: MutableStateFlow<Date> = MutableStateFlow(Date())
    val currentDate: StateFlow<Date> get() = _currentDate

    /**
     * Network Communication
     */
    fun getStatistics(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatistics(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                _totalChoreCnt.value = result.statisticsList.size
                Timber.d("totalChoreCnt: ${_totalChoreCnt.value}")

                result.statisticsList.forEach {  status ->
                    statsRepository.getHoseWorkStatistics(status.houseWorkName, yearMonth).collectLatest {
                        val result = receiveApiResult(it) ?: return@collectLatest
                        val stats = Stats(
                            houseWorkName = status.houseWorkName,
                            totalCount = status.houseWorkCount,
                            members = result.houseWorkStatisticsList,)
                        _statsFlow.emit(stats)
                    }
                }
            }
        }
    }

    fun getRanking(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatisticsRanking(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                var size = result.houseWorkStatisticsList.size
                val list = mutableListOf<Ranker>()
                for (i: Int in 1..size) {
                    val cur = result.houseWorkStatisticsList[i-1]
                    val ranker = Ranker(
                        rank = i,
                        member = cur.member,
                        houseWorkCnt = cur.houseWorkCount
                    )
                    list.add(ranker)
                }
                _rankFlow.emit(list)
            }
        }
    }

}