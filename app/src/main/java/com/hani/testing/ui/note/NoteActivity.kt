package com.hani.testing.ui.note

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.OnDoubleTapListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.hani.testing.R
import com.hani.testing.models.Note
import com.hani.testing.ui.Resource
import com.hani.testing.utils.DateUtil
import com.hani.testing.utils.LinedEditText
import com.hani.testing.viewmodels.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class NoteActivity : DaggerAppCompatActivity(), OnTouchListener,
    GestureDetector.OnGestureListener, OnDoubleTapListener, View.OnClickListener,
    TextWatcher {
    // UI components
    private var linedEditText: LinedEditText? = null
    private var editText: EditText? = null
    private var viewTitle: TextView? = null
    private var checkContainer: RelativeLayout? = null
    private var backArrowContrainer: RelativeLayout? = null
    private var check: ImageButton? = null
    private var backArrow: ImageButton? = null
    private var parent: ConstraintLayout? = null

    @Inject
    lateinit var providerFactory: ViewModelFactory
    lateinit var viewModel: NoteViewModel

    private var mGestureDetector: GestureDetector? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        linedEditText = findViewById(R.id.note_text)
        editText = findViewById(R.id.note_edit_title)
        viewTitle = findViewById(R.id.note_text_title)
        check = findViewById(R.id.toolbar_check)
        backArrow = findViewById(R.id.toolbar_back_arrow)
        checkContainer = findViewById(R.id.check_container)
        backArrowContrainer = findViewById(R.id.back_arrow_container)
        parent = findViewById(R.id.parent)
        viewModel = ViewModelProviders.of(this, providerFactory)
            .get(NoteViewModel::class.java)
        subscribeObservers()
        setListeners()
        if (savedInstanceState == null) {
            incomingIntent
            enableEditMode()
        }
    }

    private val incomingIntent: Unit
        private get() {
            try {
                val note: Note
                if (intent.hasExtra(getString(R.string.intent_note))) {
                    note = Note(
                        (intent.getParcelableExtra<Parcelable>(getString(R.string.intent_note)) as Note)
                    )
                    viewModel.setIsNewNote(false)
                } else {
                    note = Note("Title", "", DateUtil.currentTimeStamp)
                    viewModel.setIsNewNote(true)
                }
                viewModel.setNote(note)
            } catch (e: Exception) {
                e.printStackTrace()
                showSnackBar(getString(R.string.error_intent_note))
            }
        }

    private fun setListeners() {
        mGestureDetector = GestureDetector(this, this)
        linedEditText!!.setOnTouchListener(this)
        check!!.setOnClickListener(this)
        viewTitle!!.setOnClickListener(this)
        backArrow!!.setOnClickListener(this)
        editText!!.addTextChangedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("has_started", true)
    }

    private fun subscribeObservers() {
        viewModel.noteLiveData
            .observe(this, Observer<Note> { note -> setNoteProperties(note) })
        viewModel.observeViewState()
            .observe(this, Observer<Any?> { viewState ->
                when (viewState) {
                    NoteViewModel.ViewState.EDIT -> {
                        enableContentInteraction()
                    }
                    NoteViewModel.ViewState.VIEW -> {
                        disableContentInteraction()
                    }
                }
            })
    }

    private fun saveNote() {
        Log.d(TAG, "saveNote: called.")
        try {
            viewModel.saveNote().observe(
                this,
                Observer<Resource<Int?>> { integerResource ->
                    try {
                        if (integerResource != null) {
                            when (integerResource.status) {
                                Resource.Status.SUCCESS -> {
                                    Log.e(
                                        TAG,
                                        "onChanged: save note: success..."
                                    )
                                    showSnackBar(integerResource.message)
                                }
                                Resource.Status.ERROR -> {
                                    Log.e(
                                        TAG,
                                        "onChanged: save note: error..."
                                    )
                                    showSnackBar(integerResource.message)
                                }
                                Resource.Status.LOADING -> {
                                    Log.e(
                                        TAG,
                                        "onChanged: save note: loading..."
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
            showSnackBar(e.message)
        }
    }

    private fun showSnackBar(message: String?) {
        if (!TextUtils.isEmpty(message)) {
            Snackbar.make(parent!!, message!!, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setNoteProperties(note: Note) {
        try {
            viewTitle!!.text = note.title
            editText!!.setText(note.title)
            linedEditText!!.setText(note.content)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            showSnackBar("Error displaying note properties")
        }
    }

    private fun enableEditMode() {
        Log.d(TAG, "enableEditMode: called.")
        viewModel.setViewState(NoteViewModel.ViewState.EDIT)
    }

    private fun disableEditMode() {
        Log.d(TAG, "disableEditMode: called.")
        viewModel.setViewState(NoteViewModel.ViewState.VIEW)
        if (!TextUtils.isEmpty(linedEditText!!.text)) {
            try {
                viewModel.updateNote(
                    editText!!.text.toString(),
                    linedEditText!!.text.toString()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                showSnackBar("Error setting note properties")
            }
        }
        saveNote()
    }

    private fun disableContentInteraction() {
        hideKeyboard(this)
        backArrowContrainer!!.visibility = View.VISIBLE
        checkContainer!!.visibility = View.GONE
        viewTitle!!.visibility = View.VISIBLE
        editText!!.visibility = View.GONE
        linedEditText!!.keyListener = null
        linedEditText!!.isFocusable = false
        linedEditText!!.isFocusableInTouchMode = false
        linedEditText!!.isCursorVisible = false
        linedEditText!!.clearFocus()
    }

    private fun enableContentInteraction() {
        backArrowContrainer!!.visibility = View.GONE
        checkContainer!!.visibility = View.VISIBLE
        viewTitle!!.visibility = View.GONE
        editText!!.visibility = View.VISIBLE
        linedEditText!!.keyListener = EditText(this).keyListener
        linedEditText!!.isFocusable = true
        linedEditText!!.isFocusableInTouchMode = true
        linedEditText!!.isCursorVisible = true
        linedEditText!!.requestFocus()
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        viewTitle!!.text = s.toString()
    }

    override fun afterTextChanged(s: Editable) {}
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        enableEditMode()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {}
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.toolbar_back_arrow -> {
                finish()
            }
            R.id.toolbar_check -> {
                disableEditMode()
            }
            R.id.note_text_title -> {
                enableEditMode()
                editText!!.requestFocus()
                editText!!.setSelection(editText!!.length())
            }
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return mGestureDetector!!.onTouchEvent(event)
    }

    override fun onBackPressed() {
        if (viewModel.shouldNavigateBack()) {
            super.onBackPressed()
        } else {
            onClick(check!!)
        }
    }

    companion object {
        private const val TAG = "NoteActivity"
        fun hideKeyboard(activity: Activity) {
            val imm =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}