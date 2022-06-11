package com.rafetrar.apps.kotlinapplication.ui.repos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.rafetrar.apps.kotlinapplication.R
import com.rafetrar.apps.kotlinapplication.data.GitRepoItem
import com.rafetrar.apps.kotlinapplication.databinding.FragmentReposBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReposItemFragment : Fragment(R.layout.fragment_repos), GitReposAdapter.OnItemClickListener {
    private val viewModel by viewModels<ReposViewModel>()

    private var _binding: FragmentReposBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentReposBinding.bind(view)

        val adapter = GitReposAdapter(this)

        binding.apply {

            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null

            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = ItemRepoLoadStateAdapter { adapter.retry() },
                footer = ItemRepoLoadStateAdapter { adapter.retry() }
            )

            buttonRetry.setOnClickListener { adapter.retry() }
        }

        viewModel.repos.observe(viewLifecycleOwner) {

            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->

            binding.apply {

                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh !is LoadState.Loading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //for empty view
                if (
                    loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {

                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                }
                else {

                    textViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }


    override fun onItemClick(repo: GitRepoItem) {

        val uri = Uri.parse(repo.html_url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_repos, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {

                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchRepos(query)
                    searchView.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}