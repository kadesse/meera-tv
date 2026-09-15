package com.meera.tv.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meera.tv.data.model.Announcement
import com.meera.tv.data.model.LiveStatus
import com.meera.tv.data.model.Program
import com.meera.tv.data.model.Replay
import com.meera.tv.data.repository.MeeraRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val loading: Boolean = true,
    val liveStatus: LiveStatus = LiveStatus(isLive = false),
    val currentProgram: Program? = null,
    val nextProgram: Program? = null,
    val announcements: List<Announcement> = emptyList(),
    val latestReplays: List<Replay> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val repository: MeeraRepository = MeeraRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHome()
        pollLiveStatus()
    }

    private fun loadHome() {
        viewModelScope.launch {
            try {
                val current = repository.getCurrentProgram()
                val next = repository.getNextProgram()
                val announcements = repository.getAnnouncements()
                val replays = repository.getReplays().take(6)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    currentProgram = current,
                    nextProgram = next,
                    announcements = announcements,
                    latestReplays = replays
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message)
            }
        }
    }

    /**
     * Vérifie régulièrement le statut du direct auprès du backend, qui lui-même
     * détecte automatiquement quand OBS commence/arrête d'envoyer un flux RTMP.
     * Un système "push" (websocket ou notification FCM silencieuse) peut
     * remplacer ce polling plus tard pour une détection instantanée.
     */
    private fun pollLiveStatus() {
        viewModelScope.launch {
            while (true) {
                try {
                    val status = repository.getLiveStatus()
                    _uiState.value = _uiState.value.copy(liveStatus = status)
                } catch (_: Exception) {
                    // Silencieux : on retentera au prochain cycle
                }
                delay(15_000)
            }
        }
    }
}
