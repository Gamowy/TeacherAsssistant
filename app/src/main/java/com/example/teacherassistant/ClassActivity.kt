package com.example.teacherassistant

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.example.teacherassistant.databinding.ClassDetailsBinding
import com.example.teacherassistant.ui.classes.classdetails.ClassDetailsFragment

class ClassActivity : AppCompatActivity() {

    private lateinit var binding: ClassDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ClassDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set the padding of the root view to the system insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()

            )
            val topInset = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = systemInsets.left
                bottomMargin = systemInsets.bottom
                rightMargin = systemInsets.right
            }
            v.updatePadding(
                top = topInset.top,
            )
            WindowInsetsCompat.CONSUMED
        }

        // Setup app bar
        val appBar = binding.appbar
        setSupportActionBar(appBar)
        supportActionBar?.apply {
            title = "Class details"
            setDisplayHomeAsUpEnabled(true)
        }


        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_class, ClassDetailsFragment())
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}