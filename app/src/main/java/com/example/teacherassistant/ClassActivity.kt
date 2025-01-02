package com.example.teacherassistant

import android.content.Intent.ACTION_INSERT
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.example.teacherassistant.databinding.DetailsBinding
import com.example.teacherassistant.ui.classes.AddEditClassFragment
import com.example.teacherassistant.ui.classes.ClassDetailsFragment


class ClassActivity : AppCompatActivity() {

    private lateinit var binding: DetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DetailsBinding.inflate(layoutInflater)
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

        // Setup actionbar
        val appBar = binding.appbar
        setSupportActionBar(appBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
        supportActionBar?.title = "Class Details"

        // Open fragment based on intent action
        val action = intent.action
        if (action != null) {
            val fragmentToOpen = when (action) {
                ACTION_INSERT -> AddEditClassFragment()
                ACTION_VIEW -> ClassDetailsFragment()
                else -> null
            }
            intent.action = null
            if (fragmentToOpen != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_details, fragmentToOpen)
                    .commit()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}