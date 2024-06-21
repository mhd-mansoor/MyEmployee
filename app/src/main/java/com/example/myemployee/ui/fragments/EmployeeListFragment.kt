package com.example.myemployee.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myemployee.R
import com.example.myemployee.api.NetworkUtil
import com.example.myemployee.constants.Constant.Companion.USER_ID
import com.example.myemployee.databinding.FragmentEmployeeListBinding
import com.example.myemployee.ui.adapter.UserAdapter
import com.example.myemployee.viewmodel.UserViewModel

class EmployeeListFragment : Fragment() {
    private lateinit var binding: FragmentEmployeeListBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initAdapter()
        setupRetryButton()
        loadUsers()
        setupPagination()
    }

    private fun initAdapter() {
        adapter = UserAdapter { user ->
            val bundle = bundleOf(USER_ID to user.id)
            findNavController().navigate(
                R.id.action_employeeListFragment_to_employeeDetailsFragment,
                bundle
            )
        }
        binding.rvEmployeeList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEmployeeList.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner) { users ->
            if (users.isNullOrEmpty()) {
                showErrorLayout(
                    getString(R.string.no_data_available_title),
                    getString(R.string.no_data_available_desc)
                )
            } else {
                adapter.setUsers(users)
                hideErrorLayout()
            }
        }
    }

    private fun showErrorLayout(title: String, message: String) {
        binding.errorLayout.visibility = View.VISIBLE
        binding.rvEmployeeList.visibility = View.GONE
        binding.noContentTitle.text = title
        binding.noContentMessage.text = message
        Handler(Looper.getMainLooper()).postDelayed({
            hideProgressBar()
        }, 1000)
    }

    private fun hideErrorLayout() {
        binding.errorLayout.visibility = View.GONE
        binding.rvEmployeeList.visibility = View.VISIBLE
        hideProgressBar()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    private fun setupRetryButton() {
        binding.tryAgainBtn.setOnClickListener {
            loadUsers()
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvEmployeeList.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun loadUsers() {
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            showProgressBar()
            viewModel.fetchUsers()
            viewModel.users.observe(viewLifecycleOwner) { users ->
                if (users != null) {
                    adapter.setUsers(users)
                    hideErrorLayout()
                } else {
                    showErrorLayout(getString(R.string.error_title), getString(R.string.error_desc))
                }
                hideProgressBar()
            }
        } else {
            showProgressBar()
            showErrorLayout(
                getString(R.string.no_internet_title),
                getString(R.string.no_internet_desc)
            )
        }
    }

    private fun setupPagination() {
        binding.rvEmployeeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadUsers()
                }
            }
        })
    }
}