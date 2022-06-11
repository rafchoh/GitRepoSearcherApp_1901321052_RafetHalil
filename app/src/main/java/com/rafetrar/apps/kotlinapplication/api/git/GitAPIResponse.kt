package com.rafetrar.apps.kotlinapplication.api.git

import com.rafetrar.apps.kotlinapplication.data.GitRepoItem

data class GitAPIResponse (
    val items: List<GitRepoItem>
)