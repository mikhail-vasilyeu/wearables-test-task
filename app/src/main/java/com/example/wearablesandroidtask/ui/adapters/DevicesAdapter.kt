package com.example.wearablesandroidtask.ui.adapters

import android.view.ViewGroup
import com.example.wearablesandroidtask.data.models.DeviceItem
import com.example.wearablesandroidtask.databinding.ItemDeviceBinding
import com.example.wearablesandroidtask.ui.adapters.base.BaseViewHolder
import com.example.wearablesandroidtask.ui.adapters.base.RecyclerItemAdapter

class DevicesAdapter(val onDeviceClicked: (DeviceItem) -> Unit) : RecyclerItemAdapter<String, DeviceItem, DevicesAdapter.DevicesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder {
        return DevicesViewHolder(parent.inflateBinding(ItemDeviceBinding::inflate))
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DevicesViewHolder(private val viewBinding: ItemDeviceBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(item: DeviceItem) {
            with(viewBinding) {
                root.setOnClickListener {
                    onDeviceClicked.invoke(item)
                }
            }
        }
    }


    /*private val devices = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_device, parent, false)
        return DevicesViewHolder(view)
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        val deviceName = devices[position]
        holder.deviceName.text = deviceName
    }

    fun setData(newDevices: List<String>) {
        devices.clear()
        devices.addAll(newDevices)

    }

    override fun getItemCount(): Int {
        return devices.size
    }*/

    /* class DevicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val deviceName: TextView = itemView.findViewById(R.id.tv_device_name)

     }*/
}