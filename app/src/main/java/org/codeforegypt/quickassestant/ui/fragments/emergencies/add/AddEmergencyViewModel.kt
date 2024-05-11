package org.codeforegypt.quickassestant.ui.fragments.emergencies.add

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.model.ModifyEmergency
import org.codeforegypt.quickassestant.domain.repository.IEmergencyRepo
import org.codeforegypt.quickassestant.ui.fragments.emergencies.emergancy.EmergencyResult
import javax.inject.Inject

@HiltViewModel
class AddEmergencyViewModel @Inject constructor(
    private val myEmergencyRepo: IEmergencyRepo
): ViewModel() {
    var userLocation: Location? = null

    private var _addEmergencyState = MutableStateFlow(EmergencyResult.NORMAL)
    val addEmergencyState = _addEmergencyState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EmergencyResult.NORMAL
    )

    fun addEmergency(report: ModifyEmergency){
        viewModelScope.launch(Dispatchers.IO){
            val addEmergency = myEmergencyRepo.addEmergency(report)
            _addEmergencyState.update {
                if (addEmergency)
                    EmergencyResult.SUCCESS
                else
                    EmergencyResult.FAILED

            }
        }
    }
}