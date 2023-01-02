package com.mrntlu.recyclerviewguide.adapters

import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.utils.*

@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<T>(open val interaction: Interaction<T>? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /*TODO
     * Rewrite state
     * Remove state
     * Pagination Scroll Listener
     * https://medium.com/@mohitrajput987/recyclerview-pagination-scroll-listener-in-android-98e14ce911b
     */

    /* Sources
    https://github.com/MrNtlu/BiSU-Task/blob/main/app/src/main/java/com/mrntlu/bisu/ui/HomeFragment.kt
    https://github.com/MrNtlu/BiSU-Task/tree/main/app/src/main/java/com/mrntlu/bisu/ui
    https://github.com/MrNtlu/SOLNetwork/blob/master/app/src/main/java/com/mrntlu/solnetwork/view/adapter/community/InfoAdapter.kt
    https://github.com/MrNtlu/MyAnimeInfo2/blob/master/app/src/main/java/com/mrntlu/myanimeinfo2/adapters/BaseAdapter.kt
     */

    private var isLoading = true
    private var errorMessage: String? = null
    private var isPaginating = false
    private var canPaginate = false

    private var recyclerView: RecyclerView? = null
    private var arrayList: ArrayList<T> = arrayListOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemViewType(position: Int) : Int {
        return if (isLoading)
            RecyclerViewEnum.Loading.value
        else if (errorMessage != null)
            RecyclerViewEnum.Error.value
        else if (isPaginating && position == arrayList.size)
            RecyclerViewEnum.PaginationLoading.value
        else if (canPaginate && position == arrayList.size)
            RecyclerViewEnum.PaginationError.value
        else if (arrayList.isEmpty())
            RecyclerViewEnum.Empty.value
        else
            RecyclerViewEnum.View.value
    }


    override fun getItemCount(): Int {
        return if (isLoading || errorMessage != null || arrayList.isEmpty())
            1
        else {
            if (arrayList.isNotEmpty() && !isPaginating) //View Type
                arrayList.size
            else
                arrayList.size.plus(1)
        }
    }

    fun setData(newState: RVState<T>) {
        when(rvState) {
            RVState.Empty, is RVState.Error, RVState.Loading -> {
                rvState = if (newState is RVState.CUDOperation) {
                    RVState.View(newState.list.toMutableList())
                } else {
                    newState
                }

                notifyDataSetChanged()
            }
            is RVState.View -> {
                when(newState) {
                    RVState.Empty, is RVState.Error, RVState.Loading -> {
                        rvState = newState
                        notifyDataSetChanged()
                    }
                    is RVState.View -> {
                        //Is Paginating & Error check
                        (rvState as RVState.View<T>).isPaginating = newState.isPaginating
                        (rvState as RVState.View<T>).paginationErrorMessage = newState.paginationErrorMessage

                        if (
                            (rvState as RVState.View<T>).isPaginating ||
                            (rvState as RVState.View<T>).paginationErrorMessage != null
                        ) {
                            notifyItemInserted(itemCount + 1)

                            recyclerView?.scrollToPosition(itemCount - 1)
                        } else {
                            notifyItemRemoved(itemCount + 1)
                        }

                        handleDiffUtil(newState)
                    }
                    is RVState.CUDOperation -> {
                        printLog("$rvState\n$newState")
                        handleDiffUtil(RVState.View(newState.list))
                    }
                }
            }
            is RVState.CUDOperation -> {}
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    protected abstract fun handleDiffUtil(newState: RVState<T>)
}