package com.mrntlu.recyclerviewguide.adapters

import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.utils.RecyclerViewEnum
import com.mrntlu.recyclerviewguide.utils.RVState
import com.mrntlu.recyclerviewguide.utils.RecyclerViewDiffUtilCallBack
import com.mrntlu.recyclerviewguide.utils.printLog

abstract class BaseAdapter<T>(open val interaction: Interaction<T>? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    override fun getItemViewType(position: Int) = when (rvState.rvEnum) {
        RecyclerViewEnum.PaginationLoading,
        RecyclerViewEnum.PaginationError,
        RecyclerViewEnum.PaginationExhaust -> {
            if (position != itemCount.minus(1))
                RecyclerViewEnum.View.value
            else
                rvState.rvEnum.value
        }
        else -> rvState.rvEnum.value
    }

    override fun getItemCount() = when (rvState.rvEnum) {
        RecyclerViewEnum.Empty, RecyclerViewEnum.Loading, RecyclerViewEnum.Error -> 1
        RecyclerViewEnum.View -> (rvState as RVState.View).list.size
        RecyclerViewEnum.PaginationLoading,
        RecyclerViewEnum.PaginationError,
        RecyclerViewEnum.PaginationExhaust -> (rvState as RVState.View).list.size + 1
    }

    fun setData(newState: RVState<T>) {
        printLog("$rvState\n$newState")
        when(rvState) {
            RVState.Empty -> {
                rvState = if (newState is RVState.Error || newState is RVState.Loading) {
                    newState
                } else {
                    RVState.View(
                        (newState as RVState.View).list.toList(),
                        if (newState.rvEnum == RecyclerViewEnum.Empty)
                            RecyclerViewEnum.View
                        else
                            newState.rvEnum,
                    )
                }
                notifyDataSetChanged()
            }
            is RVState.Error -> {
                rvState = newState
                notifyDataSetChanged()
            }
            RVState.Loading -> {
                rvState = if (newState is RVState.View) {
                    RVState.View(
                        newState.list.toList(),
                        newState.rvEnum
                    )
                } else {
                    newState
                }

                notifyDataSetChanged()
            }
            is RVState.View -> {
//                printLog("RVState Enum:${rvState.rvEnum}\n" +
//                        "List:${(rvState as RVState.View).list}\n" +
//                        "NewState:${newState.rvEnum}\n" +
//                        "List:${(newState as RVState.View).list}")
                if (newState is RVState.View) {
                    if (rvState.rvEnum == newState.rvEnum) {
                        handleDiffUtil(newState)
                    } else {
                        if (isStatePagination(newState)) { //If new state is pagination
                            onNewStateIsPagination()
                        } else if (isStatePagination(rvState)) { //If old state is pagination
                            notifyItemRemoved(itemCount)
                        }

                        rvState = RVState.View( //New State clone
                            newState.list.toList(),
                            newState.rvEnum
                        )

                        handleDiffUtil(rvState)
                    }
                }  else { // Current RVState is different from NewState, so change ViewHolder and list.
                    rvState = newState
                    notifyDataSetChanged()
                }
            }
        }


//        if (newState.rvEnum.value != rvState.rvEnum.value) { //State changed, new state
//            if (newState is RVState.View) {
//                if (isStatePagination(newState)) { //If new state is pagination
//                    onNewStateIsPagination()
//                } else if (isStatePagination(rvState)) { //If old state is pagination
//                    notifyItemRemoved(itemCount)
//                } else {
//                    notifyDataSetChanged()
//                }
//
//                rvState = RVState.View( //New State clone
//                    newState.list.toList(),
//                    newState.rvEnum
//                )
//
//                handleDiffUtil(rvState)
//            }  else { //ViewType & ViewHolder changes
//                rvState = newState
//                notifyDataSetChanged()
//            }
//        } else if (rvState is RVState.View) {
//            handleDiffUtil(newState)
//        } else if (rvState is RVState.Empty) {
//            rvState = RVState.View(
//                (newState as RVState.View).list.toList(),
//                newState.rvEnum
//            )
//            notifyDataSetChanged()
//        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    protected abstract fun handleDiffUtil(newState: RVState<T>)

    private fun onNewStateIsPagination() {
        notifyItemInserted(itemCount + 1)
        recyclerView?.scrollToPosition(itemCount)
    }

    private fun isStatePagination(state: RVState<T>) = (
        state.rvEnum.value == RecyclerViewEnum.PaginationLoading.value ||
        state.rvEnum.value == RecyclerViewEnum.PaginationError.value ||
        state.rvEnum.value == RecyclerViewEnum.PaginationExhaust.value
    )
}