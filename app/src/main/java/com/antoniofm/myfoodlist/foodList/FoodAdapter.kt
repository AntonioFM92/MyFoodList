package com.antoniofm.myfoodlist.foodList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoniofm.myfoodlist.R
import com.antoniofm.myfoodlist.api.FoodResponse
import kotlinx.android.synthetic.main.row_food.view.*

class FoodAdapter(private val items: List<FoodResponse?>?, private val context: Context) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun getItemCount(): Int {
        return items?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FoodViewHolder {
        return FoodViewHolder(LayoutInflater.from(context).inflate(R.layout.row_food, parent, false))
    }

    override fun onBindViewHolder(viewHolder: FoodViewHolder, position: Int) {

        val item = items!![position]

        viewHolder.itemView.rowFoodT.text = context.getString(R.string.food_name)+" "+item?.title
        viewHolder.itemView.sourceTextView.text = context.getString(R.string.food_brand)+" "+item?.body

    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

