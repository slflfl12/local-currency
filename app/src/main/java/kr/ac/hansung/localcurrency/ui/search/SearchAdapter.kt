package kr.ac.hansung.localcurrency.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_search.view.*
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.databinding.ItemSearchBinding
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData

class SearchAdapter(val viewModel: SearchViewModel) : ListAdapter<PlaceUIData, SearchAdapter.SearchViewHolder>(object :
    DiffUtil.ItemCallback<PlaceUIData>() {
    override fun areContentsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return oldItem.title == oldItem.title
    }

    override fun areItemsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return newItem == oldItem
    }
}) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = DataBindingUtil.inflate<ItemSearchBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search, parent, false
        )
        val holder = SearchViewHolder(binding)
        holder.itemView.setOnClickListener {
            viewModel.onItemClick(getItem(holder.adapterPosition))
        }
        holder.itemView.tv_call.setOnClickListener {
            viewModel.onNavigateCall(getItem(holder.adapterPosition))
        }
        holder.itemView.tv_find_load.setOnClickListener {
            viewModel.onNavigateFindLoad(getItem(holder.adapterPosition))
        }


        return holder
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bind(getItem(position))




    class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(placeUIData: PlaceUIData) {
            binding.uiData = placeUIData
        }
    }
}