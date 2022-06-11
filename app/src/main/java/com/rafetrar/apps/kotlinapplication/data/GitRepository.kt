package com.rafetrar.apps.kotlinapplication.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.rafetrar.apps.kotlinapplication.api.git.IGitAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepository
    @Inject
    constructor(
        private val api: IGitAPI
    )
{

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                prefetchDistance = 15,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GitPagingSource(
                    gitAPI = api,
                    query = query
                )
            }
        ).liveData
}