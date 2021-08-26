package com.example.g_maintain.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.g_maintain.db.MyDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class AutoCompleteClientsArrayAdapter(context: Context) : ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line),Filterable {
    private var mArrayList = ArrayList<String>()

    override fun getCount(): Int {
        return mArrayList.size
    }

    override fun getItem(position: Int): String? {
        return mArrayList[position]
    }

    override fun getFilter(): Filter {
        val mFilter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val mFilterResults = FilterResults()
                if (constraint != null) {
                    runBlocking {
                        mArrayList.clear()
                        mArrayList.addAll(MyDatabase(context).getClientDao().getAllClientsByNameList(constraint.toString()))
                        mFilterResults.values = mArrayList
                        mFilterResults.count = mArrayList.size
                    }
                }
                return mFilterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }

        }
        return mFilter
    }

}