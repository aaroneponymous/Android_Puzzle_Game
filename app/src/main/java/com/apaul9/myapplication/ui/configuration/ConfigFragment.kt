package com.apaul9.myapplication.ui.configuration

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apaul9.myapplication.R



class ConfigFragment : Fragment() {

    private val viewModel: ConfigViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var recyclerScrollImage: RecyclerView
    private lateinit var prefs: SharedPreferences
    private lateinit var radioGroup: RadioGroup

    private lateinit var easyModeButton: RadioButton
    private lateinit var mediumModeButton: RadioButton
    private lateinit var hardModeButton: RadioButton
    private var modeChecked = false




    companion object {
        fun newInstance() = ConfigFragment()
        const val POSITION = "adapter_position"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        val view = inflater.inflate(R.layout.fragment_config, container, false)
        radioGroup = view.findViewById(R.id.difficulty_radioButton)
        easyModeButton = view.findViewById(R.id.easy_radioButton)
        mediumModeButton = view.findViewById(R.id.medium_radioButton)
        hardModeButton = view.findViewById(R.id.hard_radioButton)


        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            when (radio.id) {
                R.id.easy_radioButton -> {
                    viewModel.setModeSelection("easy")
                    modeChecked = true
                }
                R.id.medium_radioButton -> {
                    viewModel.setModeSelection("medium")
                    modeChecked = true
                }
                R.id.hard_radioButton -> {
                    viewModel.setModeSelection("hard")
                    modeChecked = true
                }
            }
        }



        recyclerScrollImage = view.findViewById(R.id.recyclerView)
        recyclerScrollImage.layoutManager = LinearLayoutManager(activity)
        recyclerScrollImage.adapter = ImageAdapter(getImageList())
        prefs = PreferenceManager.getDefaultSharedPreferences(context)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerScrollImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // RecyclerView was not scrolled
                    val viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(prefs.getInt(POSITION, 0))
                    if (viewHolder != null) {
                        (viewHolder as ImageViewHolder).setWasScrolled(false)
                    }
                } else {
                    // RecyclerView was scrolled
                    val viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(prefs.getInt(POSITION, 0))
                    if (viewHolder != null) {
                        (viewHolder as ImageViewHolder).setWasScrolled(true)
                    }
                }
            }
        })
    }

    // Write the position of the last scrolled item to shared preferences
    override fun onPause() {
        super.onPause()
        val position =
            (recyclerScrollImage.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        prefs.edit().putInt(POSITION, position).apply()
    }

    private fun getImageList(): List<Int> {
        return listOf(
            R.drawable.d3fern,
            R.drawable.d1forest_path,
            R.drawable.d2motorbike
        )
    }

    private inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var resourceId: Int = 0
        private val imageSlider: CardView = view.findViewById(R.id.imageSlide_cardView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Get the resource id of the image that was clicked
            val imageId = this.resourceId
            if (modeChecked) {
                viewModel.setImageSelection(imageId)
                v?.findNavController()?.navigate(R.id.action_configFragment_to_playGameFragment)
            }

        }

        fun bind(resourceId: Int) {
            this.resourceId = resourceId
            // Bind Drawable resource to ImageView
            imageSlider.setBackgroundResource(resourceId)
        }

        fun setWasScrolled(wasScrolled: Boolean) {
            // Handle wasScrolled state
        }
    }

    private inner class ImageAdapter(private val imageIds: List<Int>) :
        RecyclerView.Adapter<ImageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.imageslider, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageId = imageIds[position]
            holder.bind(imageId)
        }

        override fun getItemCount(): Int {
            return imageIds.size
        }
    }
}
