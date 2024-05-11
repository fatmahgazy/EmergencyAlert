package org.codeforegypt.quickassestant.ui.fragments.emergencies.myemergency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.codeforegypt.quickassestant.data.model.Emergency
import org.codeforegypt.quickassestant.databinding.ListMyemergencyItemBinding


class MyEmergencyAdapter: RecyclerView.Adapter<MyEmergencyAdapter.MyEmergencyViewHolder>() {

    private var myEmergencyList = emptyList<Emergency>()
     internal var onShowLocationClick: (Emergency) -> Unit = { _ -> }

    inner class MyEmergencyViewHolder(val binding: ListMyemergencyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myEmergency: Emergency, onShowLocationClick: (Emergency) -> Unit) {
            binding.tvDate.text = myEmergency.date
            binding.tvProblem.text = myEmergency.message
            binding.btnLocation.setOnClickListener {
                onShowLocationClick(myEmergency)
            }
        }
    }

    fun submitList(newList: List<Emergency>){
       myEmergencyList = newList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyEmergencyViewHolder {
        return MyEmergencyViewHolder(
            ListMyemergencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyEmergencyViewHolder, position: Int) {
        holder.bind(myEmergencyList[position], onShowLocationClick)
    }

    override fun getItemCount(): Int {
         return myEmergencyList.size
    }

}
