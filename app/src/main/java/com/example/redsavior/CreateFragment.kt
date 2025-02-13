package com.example.redsavior

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.redsavior.databinding.FragmentCreateBinding
import com.google.android.material.textfield.TextInputLayout

class CreateFragment : Fragment() {
    private  lateinit var binding : FragmentCreateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eMailId.editText?.addTextChangedListener(emailTextWatcher)
        binding.passwordId.editText?.addTextChangedListener(passwordTextWatcher)
        binding.loginId.setOnClickListener {
            findNavController().navigate(R.id.action_createFragment_to_loginFragment)
        }
        
        binding.registerBtn.setOnClickListener {
            if(checkValidation()){
                val bundle = Bundle()
                bundle.putString("name", binding.fullNameId.editText?.text.toString())
                bundle.putString("email", binding.eMailId.editText?.text.toString())
                bundle.putString("password", binding.passwordId.editText?.text.toString())
                findNavController().navigate(R.id.action_createFragment_to_detailsFragment, bundle)
            }
        }
    }


    private fun checkValidation(): Boolean {
        if(binding.fullNameId.editText?.text.toString().isEmpty()){
            binding.fullNameId.error = "Please enter your name"
            return false
        }
        binding.fullNameId.isErrorEnabled = false

        if(binding.eMailId.editText?.text.toString().isEmpty()){
            binding.eMailId.error = "Please enter your email"
            return false
        }
        binding.eMailId.isErrorEnabled = false
        if(binding.passwordId.editText?.text.toString().isEmpty()){
            binding.passwordId.error = "Please enter your password"
            return false
        }
        binding.passwordId.isErrorEnabled = false
        if (binding.passwordId.editText?.text.toString() != binding.confirmPasswordId.editText?.text.toString()){
            binding.confirmPasswordId.error = "Password is not matching..."
            return false
        }
        binding.confirmPasswordId.isErrorEnabled = false
        return true
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val email = p0.toString()
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                binding.eMailId.error = "Invalid email format"
            }
            else{
                binding.eMailId.isErrorEnabled = false
            }
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val password = p0.toString()
            binding.passwordId.helperText = getPasswordStrength(password)
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun getPasswordStrength(password: String): String{
        var upper = false
        var lower = false
        var number = false
        var special = false
        for(char in password){
            when{
                char.isUpperCase() -> upper = true
                char.isLowerCase() -> lower =true
                char.isDigit() -> number = true
                !char.isLetterOrDigit() -> special = true
            }
        }
        return when{
            password.length < 6 -> "Password too short (Min 6 characters)"
            upper && lower && number && special -> "Strong"
            else -> "Medium( Use numbers , uppercase , and special characters)"
        }
    }
}