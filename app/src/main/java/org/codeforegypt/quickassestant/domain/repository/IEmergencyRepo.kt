package org.codeforegypt.quickassestant.domain.repository

import org.codeforegypt.quickassestant.data.model.Emergency
import org.codeforegypt.quickassestant.data.model.ModifyEmergency

interface IEmergencyRepo {

    suspend fun addEmergency(report: ModifyEmergency): Boolean

    suspend fun getMyEmergencies(): List<Emergency>

    suspend fun getEmergency(): List<Emergency>
}