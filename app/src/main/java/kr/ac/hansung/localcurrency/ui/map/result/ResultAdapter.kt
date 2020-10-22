package kr.ac.hansung.localcurrency.ui.map.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_search.view.*
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.databinding.ItemResultBinding
import kr.ac.hansung.localcurrency.ui.map.MapViewModel
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData

class ResultAdapter(
        private val viewModel: MapViewModel
) : ListAdapter<PlaceUIData, ResultAdapter.ResultViewHolder>(object :
        DiffUtil.ItemCallback<PlaceUIData>() {
    override fun areContentsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return oldItem.title == oldItem.title
    }

    override fun areItemsTheSame(oldItem: PlaceUIData, newItem: PlaceUIData): Boolean {
        return newItem == oldItem
    }
}) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = DataBindingUtil.inflate<ItemResultBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_result, parent, false
        )
        val holder = ResultViewHolder(binding)
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

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) =
            holder.bind(getItem(position))


    class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(placeUiData: PlaceUIData) {
            binding.uiData = placeUiData
        }

    }


}