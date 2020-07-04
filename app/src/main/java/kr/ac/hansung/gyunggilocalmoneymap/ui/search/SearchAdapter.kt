package kr.ac.hansung.gyunggilocalmoneymap.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_search.view.*
import kr.ac.hansung.gyunggilocalmoneymap.R
import kr.ac.hansung.gyunggilocalmoneymap.databinding.ItemSearchBinding
import kr.ac.hansung.gyunggilocalmoneymap.ui.model.PlaceUIData

class SearchAdapter : ListAdapter<PlaceUIData, SearchAdapter.SearchViewHolder>(object :
    DiffUtil.ItemCallback<PlaceUIData>() {
    override fun areContentsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return oldItem.title == oldItem.title
    }

    override fun areItemsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return newItem == oldItem
    }
}) {


    var itemClickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = DataBindingUtil.inflate<ItemSearchBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search, parent, false
        )
        val holder = SearchViewHolder(binding)
        holder.itemView.setOnClickListener {
            itemClickListener?.itemClick(getItem(holder.adapterPosition))
        }
        holder.itemView.tv_call.setOnClickListener {
            itemClickListener?.callClick()
        }
        holder.itemView.tv_find_load.setOnClickListener {
            itemClickListener?.findLoad()
        }


        return holder
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bind(getItem(position))


    interface ItemClickListener {
        fun itemClick(placeUIData: PlaceUIData)
        fun callClick()
        fun findLoad()
    }


    class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(placeUIData: PlaceUIData) {
            binding.uiData = placeUIData
        }
    }
}