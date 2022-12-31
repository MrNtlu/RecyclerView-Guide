package com.mrntlu.recyclerviewguide.adapters

import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.interfaces.Interaction

abstract class BaseAdapter<T>(open val interaction: Interaction<T>? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /** TODO
    Change holders to enum State
    Implement DiffUtil
     how to bind ViewBinding viewholder
     */

    /* Sources
    https://github.com/MrNtlu/BiSU-Task/blob/main/app/src/main/java/com/mrntlu/bisu/ui/HomeFragment.kt
    https://github.com/MrNtlu/BiSU-Task/tree/main/app/src/main/java/com/mrntlu/bisu/ui
    https://github.com/MrNtlu/SOLNetwork/blob/master/app/src/main/java/com/mrntlu/solnetwork/view/adapter/community/InfoAdapter.kt
    https://github.com/MrNtlu/MyAnimeInfo2/blob/master/app/src/main/java/com/mrntlu/myanimeinfo2/adapters/BaseAdapter.kt
     */

    //Conditions
    private var isAdapterSet = false
    private var isErrorOccurred = false
    private var isPaginationLoading = false
    //Holders
    protected val LOADING_ITEM_HOLDER = 0
    val ITEM_HOLDER = 1
    protected val ERROR_HOLDER = 2
    protected val NO_ITEM_HOLDER = 3
    protected val PAGINATION_LOADING_HOLDER = 4

    protected open var errorMessage = "Error!"
    protected var arrayList: ArrayList<T> = arrayListOf()

    override fun getItemViewType(position: Int) = if (isAdapterSet){
        when {
            isErrorOccurred -> ERROR_HOLDER
            arrayList.size == 0 -> NO_ITEM_HOLDER
            isPaginationLoading && position == arrayList.size -> PAGINATION_LOADING_HOLDER
            else -> ITEM_HOLDER
        }
    } else LOADING_ITEM_HOLDER

    override fun getItemCount() = if (isAdapterSet && !isErrorOccurred && arrayList.size != 0 && !isPaginationLoading)
        arrayList.size
    else if (isAdapterSet && !isErrorOccurred && isPaginationLoading)
        arrayList.size+1
    else
        1
}