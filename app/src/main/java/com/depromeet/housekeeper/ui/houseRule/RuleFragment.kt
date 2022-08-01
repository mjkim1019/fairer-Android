package com.depromeet.housekeeper.ui.houseRule

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.ui.houseRule.adapter.RuleAdapter
import com.depromeet.housekeeper.databinding.FragmentRuleBinding
import kotlinx.coroutines.flow.collect

class RuleFragment : Fragment() {
  lateinit var binding: FragmentRuleBinding
  private val viewModel: RuleViewModel by viewModels()
  lateinit var adapter: RuleAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rule, container, false)
    binding.lifecycleOwner = this.viewLifecycleOwner
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListener()
    setAdapter()
    bindingVm()
    validateName()
  }

  private fun bindingVm() {
    lifecycleScope.launchWhenStarted {
      viewModel.rules.collect {
        when {
          it.isNotEmpty() -> {
            binding.tvRule.visibility = View.VISIBLE
            binding.rvRules.visibility = View.VISIBLE
            adapter.updateDate(it.toMutableList())
          }
          else -> {
            binding.tvRule.visibility = View.GONE
            binding.rvRules.visibility = View.GONE
          }
        }
      }
    }

  }

  private fun setAdapter() {
    adapter = RuleAdapter(viewModel.rules.value.toMutableList()) { viewModel.deleteRule(it) }
    binding.rvRules.adapter = adapter
  }

  private fun initListener() {
    binding.etRule.signNameClear.setOnClickListener {
      binding.etRule.fairerEt.setText(R.string.sign_name_blank)
    }

    binding.ruleHeader.apply {
      defaultHeaderBackBtn.setOnClickListener {
        it.findNavController().navigateUp()
      }
    }

    binding.etRule.fairerEt.setOnKeyListener { v, keyCode, event ->
      if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
        val ruleName = binding.etRule.fairerEt.text
        if (ruleName.isNotEmpty()) {
          if(viewModel.rules.value.count()==10){
            Toast.makeText(requireContext(),"규칙은 최대 10개까지 만들 수 있습니다.",Toast.LENGTH_LONG).show()
            binding.etRule.fairerEt.text.clear()
            binding.etRule.fairerEt.requestFocus()
          }
          else{
            viewModel.createRule(ruleName.toString())
            binding.etRule.fairerEt.text.clear()
            binding.etRule.fairerEt.requestFocus()
          }

        }

      }
      false
    }
  }
  private fun validateName() {
    val pattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|ㆍᆢ| ]*"
    binding.etRule.fairerEt.addTextChangedListener{
      val value: String = binding.etRule.fairerEt.text.toString()
      binding.isTextChanged = true
      binding.isError = !value.matches(pattern.toRegex())
      if (value == "") {
        binding.isTextChanged = false
      }
    }
  }
}