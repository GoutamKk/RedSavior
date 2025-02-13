package com.example.redsavior

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redsavior.databinding.FragmentDetailsBinding
import com.example.redsavior.databinding.FragmentLoginBinding


class DetailsFragment : Fragment() {
    private lateinit var binding : FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")!!
        if (name.isNotEmpty()){
            binding.nameView.text = name
        }
        else{
            binding.nameView.text = "No Name"
        }
        val email = arguments?.getString("email")
        val password = arguments?.getString("password")

        binding.emailView.text = email
        binding.passView.text = password

    }


}