package com.demo.ubike.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.demo.ubike.R
import com.demo.ubike.databinding.DialogExceptionBinding

class ExceptionDialog private constructor() : DialogFragment() {

    companion object {
        const val TAG: String = "ExceptionDialog"

        fun builder(fragmentActivity: FragmentActivity): DialogBuilder {
            return DialogBuilder(fragmentActivity)
        }
    }

    private lateinit var myActivity: FragmentActivity
    private lateinit var binding: DialogExceptionBinding
    private var title: String? = null
    private var content: String? = null
    private var isOneButton: Boolean = false
    private var btnConfirmTxt: String? = null
    private var btnCancelTxt: String? = null
    private var onButtonConfirmClick: (() -> Unit)? = null
    private var onButtonCancelClick: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        isCancelable = false
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_exception,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.tvTitle.text = title
        binding.tvMessage.text = content
        binding.btnSubmit.text = btnConfirmTxt
        binding.btnSubmit.setOnClickListener {
            onButtonConfirmClick?.invoke()
            dismissAllowingStateLoss()
        }
        if (isOneButton) {
            binding.btnCancel.visibility = View.GONE
        } else {
            binding.btnCancel.text = btnCancelTxt
            binding.btnCancel.visibility = View.VISIBLE
            binding.btnCancel.setOnClickListener {
                onButtonCancelClick?.invoke()
                dismissAllowingStateLoss()
            }
        }
    }

    fun show() {
        show(myActivity.supportFragmentManager, TAG)
    }

    override fun dismiss() {
        onButtonConfirmClick = null
        onButtonCancelClick = null
        super.dismiss()
    }

    data class DialogBuilder(private val fragmentActivity: FragmentActivity) {
        private var title: String = fragmentActivity.getString(R.string.default_dialog_title)
        private var content: String? = null
        private var isOneButton: Boolean = false
        private var btnConfirmTxt: String = fragmentActivity.getString(R.string.submit)
        private var btnCancelTxt: String = fragmentActivity.getString(R.string.cancel)
        private var onButtonConfirmClick: (() -> Unit)? = null
        private var onButtonDismissClick: (() -> Unit)? = null

        fun setTitle(title: String): DialogBuilder = apply {
            this.title = title
        }

        fun setContent(content: String): DialogBuilder = apply {
            this.content = content
        }

        fun setOneBtn(isOneButton: Boolean): DialogBuilder = apply {
            this.isOneButton = isOneButton
        }

        fun setSubmitTxt(str: String): DialogBuilder = apply {
            this.btnConfirmTxt = str
        }

        fun setCancelTxt(str: String): DialogBuilder = apply {
            this.btnCancelTxt = str
        }

        fun setConfirmClickListener(callback: () -> Unit): DialogBuilder = apply {
            this.onButtonConfirmClick = callback
        }

        fun setDismissClickListener(callback: () -> Unit): DialogBuilder = apply {
            this.onButtonDismissClick = callback
        }

        fun build(): ExceptionDialog {
            val dialog = ExceptionDialog()
            dialog.myActivity = fragmentActivity
            dialog.title = title
            dialog.content = content
            dialog.isOneButton = isOneButton
            dialog.btnConfirmTxt = btnConfirmTxt
            dialog.btnCancelTxt = btnCancelTxt
            dialog.onButtonConfirmClick = onButtonConfirmClick
            dialog.onButtonCancelClick = onButtonDismissClick
            return dialog
        }
    }
}