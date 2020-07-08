package kr.ac.hansung.localcurrency.ui.map.cluster

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.databinding.ItemClusterBinding
import kr.ac.hansung.localcurrency.databinding.ItemSearchBinding
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData
import kr.ac.hansung.localcurrency.ui.search.SearchAdapter

class ClusterAdapter : ListAdapter<PlaceUIData, ClusterAdapter.ClusterViewHolder>(object :
        DiffUtil.ItemCallback<PlaceUIData>() {
    override fun areContentsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return oldItem.title == oldItem.title
    }

    override fun areItemsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return newItem == oldItem
    }
}) {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClusterViewHolder {
        val binding = DataBindingUtil.inflate<ItemClusterBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_cluster, parent, false
        )
        val holder = ClusterViewHolder(binding)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onPlaceClick(getItem(holder.adapterPosition))
        }

        return holder
    }

    override fun onBindViewHolder(holder: ClusterViewHolder, position: Int) =
            holder.bind(getItem(position))


    class ClusterViewHolder(private val binding: ItemClusterBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(placeUIData: PlaceUIData) {
            binding.uiData = placeUIData
        }
    }

    interface OnItemClickListener {
        fun onPlaceClick(placeUIData: PlaceUIData)
    }


}