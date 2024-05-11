package org.codeforegypt.quickassestant.ui.fragments.emergencies.emergancy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.codeforegypt.quickassestant.data.model.Emergency
import org.codeforegypt.quickassestant.databinding.ListEmergancyItemBinding


class EmergencyAdapter: RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder>(){

    private var emergencyList = emptyList<Emergency>()

    internal var onCallClick: (Emergency) -> Unit = { _ -> }
    internal var onShowLocationClick: (Emergency) -> Unit = { _ -> }


    class EmergencyViewHolder(val binding: ListEmergancyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(emergency: Emergency, onCallClick: (Emergency) -> Unit, onShowLocationClick: (Emergency) -> Unit ){
            binding.tvUserName.text = emergency.username
            binding.tvProblem.text = emergency.message
            binding.tvDate.text = emergency.date
            binding.btnCall.setOnClickListener {
                onCallClick(emergency)
            }
            binding.btnLocation.setOnClickListener {
                onShowLocationClick(emergency)
            }
        }

    }
      fun submitList(newList: List<Emergency>){
         emergencyList = newList
         notifyDataSetChanged()
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        return EmergencyViewHolder(
           ListEmergancyItemBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        )
    }

    override fun getItemCount(): Int {
       return emergencyList.size
    }

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
      holder.bind(emergencyList[position] , onCallClick , onShowLocationClick)
    }


}