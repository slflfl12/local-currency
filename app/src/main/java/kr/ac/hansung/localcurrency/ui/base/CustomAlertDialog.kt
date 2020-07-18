package kr.ac.hansung.localcurrency.ui.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.databinding.DialogBaseBinding

class CustomAlertDialog : DialogFragment() {

    var okClick: (() -> Unit)? = null
    var cancelClick: (() -> Unit)? = null

    var title: String = ""
    var okText: String = ""
    var cancelText: String = ""


    private lateinit var binding: DialogBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            getString(KEY_TITLE)?.let {
                title = it
            }
            getString(KEY_OK_TEXT)?.let {
                okText = it
            }
            getString(KEY_CANCEL_TEXT)?.let {
                cancelText = it
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_base, null, false)

        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    fun okClick() {
        okClick?.invoke()
        dismiss()
    }

    fun cancelClick() {
        cancelClick?.invoke()
        dismiss()
    }

    fun setTitleText(title: String) {
        this.title = title
    }

    fun setOkClick(okText: String, okClick: () -> Unit) {
        this.okText = okText
        this.okClick = okClick
    }

    fun setCancelClick(cancelText: String, cancelClick: () -> Unit) {
        this.cancelText = cancelText
        this.cancelClick = cancelClick
    }


    companion object {
        val TAG = this::class.java.simpleName
        val KEY_TITLE = "KEY_TITLE"
        val KEY_OK_TEXT = "KEY_OK"
        val KEY_CANCEL_TEXT = "KEY_CANCEL"

        fun newInstance(
                title: String,
                okText: String,
                cancelText: String,
                okClick: () -> Unit,
                cancelClick: () -> Unit
        ) = CustomAlertDialog().apply {
            arguments = bundleOf(
                    KEY_TITLE to title,
                    KEY_OK_TEXT to okText,
                    KEY_CANCEL_TEXT to cancelText
            )
            this.okClick = okClick
            this.cancelClick = cancelClick
        }

    }


}