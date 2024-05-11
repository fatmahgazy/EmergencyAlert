package org.codeforegypt.quickassestant.ui.fragments.emergencies.myemergency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.model.Emergency
import org.codeforegypt.quickassestant.data.model.ModifyEmergency
import org.codeforegypt.quickassestant.domain.repository.IEmergencyRepo
import org.codeforegypt.quickassestant.ui.fragments.emergencies.emergancy.EmergencyResult
import javax.inject.Inject
@HiltViewModel
class MyEmergencyViewModel @Inject constructor(
    private val emergencyRepo: IEmergencyRepo
): ViewModel() {
    private var _emergencyState = MutableStateFlow<List<Emergency>>(emptyList())
    val emergencyState = _emergencyState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )


    fun getMyEmergency(){
        viewModelScope.launch(Dispatchers.IO){
            val getMyEmergency = emergencyRepo.getMyEmergencies()
            _emergencyState.update {
                getMyEmergency
            }
        }
    }
}