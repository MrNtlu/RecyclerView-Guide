package com.mrntlu.recyclerviewguide.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.adapters.viewholders.ErrorViewHolderBind
import com.mrntlu.recyclerviewguide.adapters.viewholders.ItemViewHolderBind
import com.mrntlu.recyclerviewguide.adapters.viewholders.PaginationExhaustViewHolderBind
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.utils.*

@Suppress("UNCHECKED_CAST")
@SuppressLint("NotifyDataSetChanged")
abstract class BaseAdapter<T>(open val interaction: Interaction<T>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var errorMessage: String? = null
    var isLoading = true
    var isPaginating = false
    var canPaginate = true

    protected var arrayList: ArrayList<T> = arrayListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            RecyclerViewEnum.View.value -> {
                (holder as ItemViewHolderBind<T>).bind(arrayList[position], position, interaction)
            }
            RecyclerViewEnum.Error.value -> {
                (holder as ErrorViewHolderBind<T>).bind(errorMessage, interaction)
            }
            RecyclerViewEnum.PaginationExhaust.value -> {
                (holder as PaginationExhaustViewHolderBind<T>).bind(interaction)
            }
        }
    }

    override fun getItemViewType(position: Int) : Int {
        return if (isLoading)
            RecyclerViewEnum.Loading.value
        else if (errorMessage != null)
            RecyclerViewEnum.Error.value
        else if (isPaginating && position == arrayList.size)
            RecyclerViewEnum.PaginationLoading.value
        else if (!canPaginate && position == arrayList.size)
            RecyclerViewEnum.PaginationExhaust.value
        else if (arrayList.isEmpty())
            RecyclerViewEnum.Empty.value
        else
            RecyclerViewEnum.View.value
    }

    override fun getItemCount(): Int {
        return if (isLoading || errorMessage != null || arrayList.isEmpty())
            1
        else {
            if (arrayList.isNotEmpty() && !isPaginating && canPaginate) //View Type
                arrayList.size
            else
                arrayList.size.plus(1)
        }
    }

    fun setError(errorMessage: String, isPaginationError: Boolean) {
        if (isPaginationError) {
            setState(RecyclerViewEnum.PaginationExhaust)
            notifyItemInserted(itemCount + 1)
        } else {
            setState(RecyclerViewEnum.Error)
            this.errorMessage = errorMessage
            notifyDataSetChanged()
        }
    }

    fun setLoadingView(isPaginating: Boolean) {
        if (isPaginating) {
            setState(RecyclerViewEnum.PaginationLoading)
            notifyItemInserted(itemCount)
        } else {
            setState(RecyclerViewEnum.Loading)
            notifyDataSetChanged()
        }
    }

    fun handleOperation(operation: Operation<T>) {
        val newList = arrayList.toMutableList()

        when(operation.operationEnum) {
            OperationEnum.Insert -> {
                newList.add(operation.data)
            }
            OperationEnum.Delete -> {
                newList.remove(operation.data)
            }
            OperationEnum.Update -> {
                val index = newList.indexOfFirst {
                    it == operation.data
                }
                newList[index] = operation.data
            }
        }

        handleDiffUtil(newList as ArrayList<T>)
    }

    fun setData(newList: ArrayList<T>, isPaginationData: Boolean = false) {
        setState(RecyclerViewEnum.View)

        if (!isPaginationData) {
            if (arrayList.isNotEmpty())
                arrayList.clear()
            arrayList.addAll(newList)
            notifyDataSetChanged()
        } else {
            notifyItemRemoved(itemCount)

            newList.addAll(0, arrayList)
            handleDiffUtil(newList)
        }
    }

    protected abstract fun handleDiffUtil(newList: ArrayList<T>)

    private fun setState(rvEnum: RecyclerViewEnum) {
        when(rvEnum) {
            RecyclerViewEnum.Empty -> {
                isLoading = false
                isPaginating = false
                errorMessage = null
            }
            RecyclerViewEnum.Loading -> {
                isLoading = true
                isPaginating = false
                errorMessage = null
                canPaginate = true
            }
            RecyclerViewEnum.Error -> {
                isLoading = false
                isPaginating = false
            }
            RecyclerViewEnum.View -> {
                isLoading = false
                isPaginating = false
                errorMessage = null
            }
            RecyclerViewEnum.PaginationLoading -> {
                isLoading = false
                isPaginating = true
                errorMessage = null
            }
            RecyclerViewEnum.PaginationExhaust -> {
                isLoading = false
                isPaginating = false
                canPaginate = false
            }
        }
    }
}