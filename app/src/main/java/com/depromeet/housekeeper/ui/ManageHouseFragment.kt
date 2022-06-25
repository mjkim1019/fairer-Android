package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentManageHouseBinding
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.model.enums.SignViewType

class ManageHouseFragment : Fragment() {
    lateinit var binding: FragmentManageHouseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_house, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        initListener()

        return binding.root
    }

    private fun initListener() {
        binding.manageHouseHeader.apply {
            defaultHeaderTitleTv.text = resources.getString(R.string.manage_house_header_title)

            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        binding.renameHouseRow.setOnClickListener {
            it.findNavController().navigate(ManageHouseFragmentDirections.actionManageHouseFragmentToSignNameFragment(viewType = SignViewType.ModifyGroupName, code = null))
        }

        binding.inviteRow.setOnClickListener {
            it.findNavController().navigate(ManageHouseFragmentDirections.actionManageHouseFragmentToInviteFragment(InviteViewType.SETTING,null))
        }

        binding.exitHouseRow.setOnClickListener {
            // ToDo: 하우스 나가기 API 연동 (hasTeam 정보 필요)
        }

    }


}