package com.rafetrar.apps.kotlinapplication.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rafetrar.apps.kotlinapplication.api.git.IGitAPI
import retrofit2.HttpException
import java.io.IOException

private const val GIT_STARTING_PAGE_INDEX = 1

class GitPagingSource(
    private val gitAPI: IGitAPI,
    private val query: String
) : PagingSource<Int, GitRepoItem>() {

    override fun getRefreshKey(state: PagingState<Int, GitRepoItem>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitRepoItem> {

        val position = params.key ?: GIT_STARTING_PAGE_INDEX

        return try {

            val response = gitAPI.searchRepos(query, position, params.loadSize)

            val repoList = response.items

            LoadResult.Page(
                data = repoList,
                prevKey = if (position == GIT_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repoList.isEmpty()) null else position + 1
            )
        }
        catch (exception: IOException) {

            LoadResult.Error(exception)
        }
        catch (exception: HttpException) {

            LoadResult.Error(exception)
        }
    }
}