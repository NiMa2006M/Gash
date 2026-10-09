package com.example.gash.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.usecase.activation.GetDeviceReferenceCodeUseCase
import com.example.gash.domain.usecase.activation.ObserveFarmNameUseCase
import com.example.gash.domain.usecase.animal.GetAnimalCountUseCase
import com.example.gash.domain.usecase.profile.ObserveProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeFarmNameUseCase: ObserveFarmNameUseCase,
    getAnimalCountUseCase: GetAnimalCountUseCase,
    observeProfileImageUseCase: ObserveProfileImageUseCase,
    getDeviceReferenceCodeUseCase: GetDeviceReferenceCodeUseCase
) : ViewModel() {

    private val deviceReferenceCode: String = getDeviceReferenceCodeUseCase()

    val uiState: StateFlow<HomeUiState> = combine(
        observeFarmNameUseCase(),
        getAnimalCountUseCase(),
        observeProfileImageUseCase()
    ) { farmName, count, profileImagePath ->
        HomeUiState(
            farmName = farmName.orEmpty(),
            deviceReferenceCode = deviceReferenceCode,
            totalAnimalCount = count,
            profileImagePath = profileImagePath
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}