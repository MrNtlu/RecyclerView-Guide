package com.mrntlu.recyclerviewguide.adapters

import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.utils.*

@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<T>(open val interaction: Interaction<T>? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /*TODO
     * Update doesn't work!!
     * Pagination Scroll Listener
     * https://medium.com/@mohitrajput987/recyclerview-pagination-scroll-listener-in-android-98e14ce911b
     */

    /* Sources
    https://github.com/MrNtlu/BiSU-Task/blob/main/app/src/main/java/com/mrntlu/bisu/ui/HomeFragment.kt
    https://github.com/MrNtlu/BiSU-Task/tree/main/app/src/main/java/com/mrntlu/bisu/ui
    https://github.com/MrNtlu/SOLNetwork/blob/master/app/src/main/java/com/mrntlu/solnetwork/view/adapter/community/InfoAdapter.kt
    https://github.com/MrNtlu/MyAnimeInfo2/blob/master/app/src/main/java/com/mrntlu/myanimeinfo2/adapters/BaseAdapter.kt
     */

    var rvState: RVState<T> = RVState.Loading
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemViewType(position: Int) = when (rvState) {
        is RVState.View -> {
            val state = (rvState as RVState.View<T>)

            if ((state.isPaginating || state.paginationErrorMessage != null) && position == itemCount.minus(1)) {
                if (state.isPaginating)
                    RecyclerViewEnum.PaginationLoading.value
                else
                    RecyclerViewEnum.PaginationError.value
            } else
                rvState.value
        }
        else -> rvState.value
    }

    override fun getItemCount() = when(rvState) {
        RVState.Empty, is RVState.Error, RVState.Loading -> 1
        is RVState.View -> {
            val state = (rvState as RVState.View<T>)
            state.list.size.plus(if(state.isPaginating || state.paginationErrorMessage != null) 1 else 0)
        }
        else -> 0
    }

    fun setData(newState: RVState<T>) {
        printLog("$rvState\n$newState")
        when(rvState) {
            RVState.Empty, is RVState.Error, RVState.Loading -> {
                rvState = if (newState is RVState.CUDOperation) {
                    RVState.View(newState.list)
                } else newState
                notifyDataSetChanged()
            }
            is RVState.View -> {
                when(newState) {
                    RVState.Empty -> {
                        rvState = newState
                        notifyDataSetChanged()
                    }
                    is RVState.Error -> {
                        rvState = newState.copy()
                        notifyDataSetChanged()
                    }
                    RVState.Loading -> {
                        rvState = newState
                        notifyDataSetChanged()
                    }
                    is RVState.View -> {
                        (rvState as RVState.View<T>).isPaginating = newState.isPaginating
                        (rvState as RVState.View<T>).paginationErrorMessage = newState.paginationErrorMessage

                        if ((rvState as RVState.View<T>).isPaginating || (rvState as RVState.View<T>).paginationErrorMessage != null) {
                            notifyItemInserted(itemCount + 1)

                            recyclerView?.scrollToPosition(itemCount - 1)
                        } else
                            notifyItemRemoved(itemCount + 1)

                        handleDiffUtil(newState)
                    }
                    is RVState.CUDOperation -> {
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