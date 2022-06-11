package com.rafetrar.apps.kotlinapplication.data

data class GitRepoItem(
    val id: Long,
    val name: String,
    val owner: Owner,
    val language: String,
    val html_url: String,
    val description: String,
    val watchers_count: Int
) {
    data class Owner(
        val id: Long,
        val login: String,
        val avatar_url: String,
        val html_url: String
    )
}