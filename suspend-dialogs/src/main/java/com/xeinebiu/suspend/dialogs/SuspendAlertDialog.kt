package com.xeinebiu.suspend.dialogs

import androidx.appcompat.app.AlertDialog
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Display different dialogs on screen by suspending the next call till the dialog is finished
 */
object SuspendAlertDialog {

    /**
     * Show a dialog returned from [builder] with a title and message
     *
     * Clicking on a button will dismiss the dialog.
     */
    suspend inline fun alert(
        buttonText: CharSequence = "Ok",
        crossinline builder: () -> AlertDialog.Builder
    ) = suspendCancellableCoroutine<Unit> { continuation ->
        val dialogBuilder = builder()

        dialogBuilder.setPositiveButton(buttonText) { _, _ ->
        }

        dialogBuilder.setOnDismissListener {
            continuation.resume(Unit)
        }

        val dialog = dialogBuilder.show()

        continuation.invokeOnCancellation {
            dialog.dismiss()
        }
    }

    /**
     * Show a dialog returned from [builder] with a title and message
     *
     * Clicking on a button will dismiss the dialog.
     */
    suspend inline fun confirm(
        positiveButtonText: CharSequence? = null,
        negativeButtonText: CharSequence? = null,
        neutralButtonText: CharSequence? = null,
        crossinline builder: () -> AlertDialog.Builder
    ) = suspendCancellableCoroutine<DialogAction> { continuation ->
        val dialogBuilder = builder()

        var result: DialogAction = DialogAction.None

        dialogBuilder.setPositiveButton(positiveButtonText) { _, _ ->
            result = DialogAction.Positive
        }

        dialogBuilder.setNegativeButton(negativeButtonText) { _, _ ->
            result = DialogAction.Negative
        }

        dialogBuilder.setNeutralButton(neutralButtonText) { _, _ ->
            result = DialogAction.Neutral
        }

        dialogBuilder.setOnDismissListener {
            continuation.resume(result)
        }

        val dialog = dialogBuilder.show()

        continuation.invokeOnCancellation {
            dialog.dismiss()
        }
    }

    /**
     * Set a list of items to be displayed in the dialog as the content.
     *
     * The list will have a check mark displayed to
     * the right of the text for the checked item. Clicking on an item in the list will not
     * dismiss the dialog. Clicking on a button will dismiss the dialog.
     */
    suspend inline fun setSingleChoiceItems(
        positiveButtonText: CharSequence? = null,
        negativeButtonText: CharSequence? = null,
        neutralButtonText: CharSequence? = null,
        items: SingleChoiceItems,
        crossinline builder: () -> AlertDialog.Builder
    ) = suspendCancellableCoroutine<SingleChoiceResult> { continuation ->
        val dialogBuilder = builder()

        var selectedIndex = items.selectedIndex

        var action: DialogAction = DialogAction.None

        dialogBuilder.setSingleChoiceItems(
            items.items.toTypedArray(),
            items.selectedIndex
        ) { _, which ->
            selectedIndex = which
        }

        dialogBuilder.setPositiveButton(positiveButtonText) { _, _ ->
            action = DialogAction.Positive
        }

        dialogBuilder.setNegativeButton(negativeButtonText) { _, _ ->
            action = DialogAction.Negative
        }

        dialogBuilder.setNeutralButton(neutralButtonText) { _, _ ->
            action = DialogAction.Neutral
        }

        dialogBuilder.setOnDismissListener {
            continuation.resume(
                SingleChoiceResult(action = action, selectedIndex = selectedIndex)
            )
        }

        val dialog = dialogBuilder.show()

        continuation.invokeOnCancellation {
            dialog.dismiss()
        }
    }

    /**
     * Set a list of items to be displayed in the dialog as the content.
     * The list will have a check mark displayed to the right of the text
     * for each checked item. Clicking on an item in the list will not
     * dismiss the dialog. Clicking on a button will dismiss the dialog.
     */
    suspend inline fun setMultiChoiceItems(
        positiveButtonText: CharSequence? = null,
        negativeButtonText: CharSequence? = null,
        neutralButtonText: CharSequence? = null,
        items: MultiChoiceItems,
        crossinline builder: () -> AlertDialog.Builder
    ) = suspendCancellableCoroutine<MultiChoiceResult> { continuation ->
        val dialogBuilder = builder()

        val checkedResult = items.checked.toMutableList()

        var action: DialogAction = DialogAction.None

        dialogBuilder.setMultiChoiceItems(
            items.items.toTypedArray(),
            items.checked.toBooleanArray()
        ) { _, which, isChecked ->
            checkedResult[which] = isChecked
        }

        dialogBuilder.setPositiveButton(positiveButtonText) { _, _ ->
            action = DialogAction.Positive
        }

        dialogBuilder.setNegativeButton(negativeButtonText) { _, _ ->
            action = DialogAction.Negative
        }

        dialogBuilder.setNeutralButton(neutralButtonText) { _, _ ->
            action = DialogAction.Neutral
        }

        dialogBuilder.setOnDismissListener {
            continuation.resume(
                MultiChoiceResult(action = action, checked = checkedResult)
            )
        }

        val dialog = dialogBuilder.show()

        continuation.invokeOnCancellation {
            dialog.dismiss()
        }
    }

    /**
     * Options to prompt on dialog
     */
    data class SingleChoiceItems(

        /**
         * Represents all options which one of them may be selected
         */
        val items: List<CharSequence>,

        /**
         * Default selected index
         */
        val selectedIndex: Int
    )

    /**
     * Represents the result for single choice dialog's
     */
    data class SingleChoiceResult(

        /**
         * Action which finished the dialog
         */
        val action: DialogAction,

        /**
         * Selected option index
         *
         * Returns -1 in case [action] is [DialogAction.None]
         */
        val selectedIndex: Int
    )

    /**
     * Options to prompt on dialog
     */
    data class MultiChoiceItems(

        /**
         * Represents all options which may be checked|unchecked
         */
        val items: List<CharSequence>,

        /**
         * Default checked state for [items]
         *
         * Index should match with option
         */
        val checked: List<Boolean>
    )

    /**
     * Represents the result for multi choice dialog's
     */
    data class MultiChoiceResult(

        /**
         * Action which finished the dialog
         */
        val action: DialogAction,

        /**
         * The [checked] list is equivalent to number of options
         *
         * The index matches the option user selected
         *
         * An example of options like `Berlin`, `Potsdam`, `Bayern`
         * will result to `True`, `True`, `False` in case the first two options are checked
         */
        val checked: List<Boolean>
    )

    /**
     * Represents the action which finished the dialog
     */
    sealed class DialogAction {

        /**
         * Dialog was finished by taping outside or back-press
         */
        object None : DialogAction()

        /**
         * Dialog was finished by taping Positive button
         */
        object Positive : DialogAction()

        /**
         * Dialog was finished by taping Negative button
         */
        object Negative : DialogAction()

        /**
         * Dialog was finished by taping Neutral button
         */
        object Neutral : DialogAction()
    }
}