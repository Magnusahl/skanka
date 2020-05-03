package com.example.skanka

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.skanka.adapters.LanguageAdapters
import com.example.skanka.model.LanguageItem

class MainActivity : AppCompatActivity() , AdapterView.OnItemClickListener{

    private var arrayList: ArrayList<LanguageItem>? = null
    private var gridView: GridView? = null
    private var languageAdapters: LanguageAdapters? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.my_grid_view_list)
        arrayList = ArrayList()
        arrayList = setDataList()
        languageAdapters = LanguageAdapters(applicationContext, arrayList!!)
        gridView?.adapter = languageAdapters
        gridView?.onItemClickListener = this

    }

    private fun setDataList() : ArrayList<LanguageItem>? {
        var arrayList:ArrayList<LanguageItem> = ArrayList()

        arrayList.add(LanguageItem(R.drawable.models1, "Röd", 1000))
        arrayList.add(LanguageItem(R.drawable.models2, "Grön", 1000))
        arrayList.add(LanguageItem(R.drawable.models3, "Gul", 1000))
        arrayList.add(LanguageItem(R.drawable.models4, "Brun", 1000))
        arrayList.add(LanguageItem(R.drawable.models5, "Viol", 1000))
        arrayList.add(LanguageItem(R.drawable.models2, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models3, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models4, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models5, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models6, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models1, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models2, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models3, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models4, "Svart", 1000))
        arrayList.add(LanguageItem(R.drawable.models5, "Svart", 1000))

        return arrayList

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        var items:LanguageItem = arrayList!!.get(position)
        Toast.makeText(applicationContext, items.name , Toast.LENGTH_LONG).show()
    }
}