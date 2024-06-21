package com.example.myemployee.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myemployee.R
import com.example.myemployee.constants.Constant.Companion.USER_ID
import com.example.myemployee.databinding.FragmentEmployeeDetailsBinding
import com.example.myemployee.model.User
import com.example.myemployee.viewmodel.UserViewModel

class EmployeeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEmployeeDetailsBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        showProgressBar()

        val userId = arguments?.getInt(USER_ID) ?: -1
        if (userId != -1) {
            viewModel.fetchUserDetails(userId)
        }

        viewModel.userDetails.observe(viewLifecycleOwner) { user ->
            updateUserProfile(user)
            updateBasicDetails(user)
            updatePersonalDetails(user)
            updateAddress(user)
            updateCompanyDetails(user)
            updateBankDetails(user)
            updateCryptoDetails(user)
            Handler(Looper.getMainLooper()).postDelayed({
                hideProgressBar()
            }, 1500)        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }

    private fun updateCryptoDetails(user: User) {
        binding.tvCryptoDetails.text = user.crypto.coin
        binding.tvCryptoNetwork.text = user.crypto.network
        binding.tvIp.text = user.ip
        binding.tvMacId.text = user.macAddress
        binding.tvCryptoWallet.text = user.crypto.wallet
        binding.tvUserAgent.text = user.userAgent
    }

    private fun updateBankDetails(user: User) {
        binding.tvCardExpiry.text = user.bank.cardExpire
        binding.tvCardNumber.text = user.bank.cardNumber
        binding.tvCardType.text = user.bank.cardType
        binding.tvCurrency.text = user.bank.currency
        binding.tvIban.text = user.bank.iban
    }

    private fun updateCompanyDetails(user: User) {
        binding.tvCompanyName.text = user.company.name
        binding.tvDepartment.text = user.company.department
        binding.tvJobRole.text = user.company.title
        binding.tvCompanyCity.text = user.company.address.city
        binding.tvCompanyAddress.text = user.company.address.address
        binding.tvCompanyStateCode.text = user.company.address.stateCode
        binding.tvCompanyState.text = user.company.address.state
        binding.tvCompanyCountry.text = user.company.address.country
        binding.tvCompanyPostalCode.text = user.company.address.state
        binding.tvCompanyLatitude.text = user.company.address.coordinates.lat.toString()
        binding.tvCompanyLongitude.text = user.company.address.coordinates.lng.toString()
        binding.tvUniversity.text = user.university
    }

    private fun updateAddress(user: User) {
        binding.tvAddress.text = user.address.address
        binding.tvCity.text = user.address.city
        binding.tvState.text = user.address.state
        binding.tvStateCode.text = user.address.stateCode
        binding.tvPostalCode.text = user.address.postalCode
        binding.tvCountry.text = user.address.country
        binding.tvLatitude.text = user.address.coordinates.lat.toString()
        binding.tvLongitude.text = user.address.coordinates.lng.toString()
    }

    private fun updatePersonalDetails(user: User) {
        binding.tvFullName.text = getString(R.string.full_name, user.firstName, user.lastName)
        binding.tvAge.text = user.age.toString()
        binding.tvGender.text = user.gender
        binding.tvBirthDate.text = user.birthDate
        binding.tvHeight.text = getString(R.string.height_in, user.height.toInt())
        binding.tvWeight.text = getString(R.string.height_in, user.weight.toInt())
        binding.tvBloodGroup.text = user.bloodGroup
        binding.tvEyeColor.text = user.eyeColor
        binding.tvHairColor.text = user.hair.color
        binding.tvHairType.text = user.hair.type
    }

    private fun updateBasicDetails(user: User) {
        binding.tvPhoneNumber.text = user.phone
        binding.tvEmail.text = user.email
        binding.tvLocation.text = user.address.state
        binding.tvUserName.text = user.username
        binding.tvPassword.text = user.password
        binding.tvEin.text = user.ein
        binding.tvSsn.text = user.ssn
    }

    private fun updateUserProfile(user: User) {
        binding.tvName.text = getString(R.string.full_name, user.firstName, user.lastName)
        binding.tvRole.text = user.role
        Glide.with(requireContext())
            .load(user.image)
            .placeholder(R.drawable.ic_user_place_holder)
            .error(R.drawable.ic_user_place_holder)
            .into(binding.ivProfileImg)
    }

}