package com.capstone.feminacare.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.feminacare.data.local.BloodCheckup
import com.capstone.feminacare.data.local.BloodCheckupDao

class BloodCheckupPagingSource(private val dao: BloodCheckupDao) : PagingSource<Int, BloodCheckup>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, BloodCheckup>): Int? {
        return  state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BloodCheckup> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val data = dao.getCheckupHistory(params.loadSize, position * params.loadSize)

            LoadResult.Page(
                data = data,
                prevKey = if(position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if(data.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}