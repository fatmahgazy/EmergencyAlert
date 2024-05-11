package org.codeforegypt.quickassestant.ui.fragments.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.codeforegypt.quickassestant.data.model.Contact

import org.codeforegypt.quickassestant.databinding.ItemPersonalContactBinding

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var contactsList = emptyList<Contact>()

    internal var onCallClick: (Contact) -> Unit = { _ -> }
    internal var onDeleteClick: (Contact) -> Unit = { _ -> }


    inner class ContactViewHolder(val binding: ItemPersonalContactBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, onCallClick: (Contact) -> Unit, onDeleteClick: (Contact) -> Unit) {
            binding.tvContactName.text = contact.name

            binding.callContact.setOnClickListener {

                onCallClick(contact)
            }

            binding.imgDelete.setOnClickListener {
                onDeleteClick(contact)
            }
        }
    }

    fun submitList(newContacts: List<Contact>) {
        contactsList = newContacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ItemPersonalContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactsList[position], onCallClick, onDeleteClick)
    }


}