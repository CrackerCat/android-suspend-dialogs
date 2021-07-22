package com.xeinebiu.demo.suspendDialogs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xeinebiu.demo.suspendDialogs.databinding.ActivityMainBinding
import com.xeinebiu.suspend.dialogs.SuspendAlertDialog
import com.xeinebiu.suspend.dialogs.alert
import com.xeinebiu.suspend.dialogs.confirm
import com.xeinebiu.suspend.dialogs.setMultiChoiceItems
import com.xeinebiu.suspend.dialogs.setSingleChoiceItems
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        with(binding) {
            alertDialog()

            confirmDialog()

            multiChoiceDialog()

            singleChoiceDialog()

            multiChoiceDialogExt()

            singleChoiceDialogExt()

            confirmDialogExt()

            alertdialogExt()
        }
    }

    private fun ActivityMainBinding.alertdialogExt() {
        btnAlertExt.setOnClickListener {
            lifecycleScope.launch {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Selected Option")
                    .alert("Ok")

                tvResult.text = getString(R.string.alert_finished)
            }
        }
    }

    private fun ActivityMainBinding.confirmDialogExt() {
        btnConfirmExt.setOnClickListener {
            lifecycleScope.launch {
                val result = MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Title")
                    .setMessage("Message")
                    .confirm(
                        positiveButtonText = "Save",
                        negativeButtonText = "Cancel",
                        neutralButtonText = "Minimize",
                    )

                tvResult.text = result.toString()
            }
        }
    }

    private fun ActivityMainBinding.singleChoiceDialogExt() {
        btnSingleChoiceExt.setOnClickListener {
            lifecycleScope.launch {
                val result = MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Title")
                    .setSingleChoiceItems(
                        positiveButtonText = "Save",
                        negativeButtonText = "Cancel",
                        neutralButtonText = "Minimize",
                        items = SuspendAlertDialog.SingleChoiceItems(
                            items = listOf("Hello", "World", "Berlin", "Germany"),
                            selectedIndex = 1
                        )
                    )

                tvResult.text = result.toString()
            }
        }
    }

    private fun ActivityMainBinding.multiChoiceDialogExt() {
        btnMultiChoiceExt.setOnClickListener {
            lifecycleScope.launch {
                val result = MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Title")
                    .setMultiChoiceItems(
                        positiveButtonText = "Save",
                        negativeButtonText = "Cancel",
                        neutralButtonText = "Minimize",
                        items = SuspendAlertDialog.MultiChoiceItems(
                            items = listOf("Hello", "World", "Berlin", "Germany"),
                            checked = listOf(false, false, false, false)
                        )
                    )

                tvResult.text = result.toString()
            }
        }
    }

    private fun ActivityMainBinding.singleChoiceDialog() {
        btnSingleChoice.setOnClickListener {
            lifecycleScope.launch {
                val singleChoiceResult = SuspendAlertDialog.setSingleChoiceItems(
                    positiveButtonText = "Save",
                    negativeButtonText = "Cancel",
                    neutralButtonText = "Minimize",
                    items = SuspendAlertDialog.SingleChoiceItems(
                        items = listOf("Hello", "World", "Berlin", "Germany"),
                        selectedIndex = 1
                    )
                ) {
                    MaterialAlertDialogBuilder(this@MainActivity).setTitle("Title")
                }

                tvResult.text = singleChoiceResult.toString()
            }
        }
    }

    private fun ActivityMainBinding.multiChoiceDialog() {
        btnMultiChoice.setOnClickListener {
            lifecycleScope.launch {
                val multiChoiceResult = SuspendAlertDialog.setMultiChoiceItems(
                    positiveButtonText = "Save",
                    negativeButtonText = "Cancel",
                    neutralButtonText = "Minimize",
                    items = SuspendAlertDialog.MultiChoiceItems(
                        items = listOf("Hello", "World", "Berlin", "Germany"),
                        checked = listOf(false, false, false, false)
                    )
                ) {
                    MaterialAlertDialogBuilder(this@MainActivity).setTitle("Title")
                }

                tvResult.text = multiChoiceResult.toString()
            }
        }
    }

    private fun ActivityMainBinding.confirmDialog() {
        btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                val result = SuspendAlertDialog.confirm(
                    positiveButtonText = "Positive",
                    negativeButtonText = "Negative",
                    neutralButtonText = "Neutral"
                ) {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Title")
                        .setMessage("Message")
                }

                tvResult.text = result.toString()
            }
        }
    }

    private fun ActivityMainBinding.alertDialog() {
        btnAlert.setOnClickListener {
            lifecycleScope.launch {
                SuspendAlertDialog.alert("Ok") {
                    MaterialAlertDialogBuilder(this@MainActivity).setTitle("Selected Option")
                }

                tvResult.text = getString(R.string.alert_finished)
            }
        }
    }
}