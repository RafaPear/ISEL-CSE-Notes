package pt.isel.reversi.utils.audio.controls

import javax.sound.sampled.BooleanControl
import javax.sound.sampled.Clip

/**
 * A wrapper for a BooleanControl of a Clip, providing methods to update and reset its value.
 *
 * @constructor Creates a BooleanControlWrapper for the specified control type.
 * @param clip The Clip containing the control.
 * @param controlType The type of BooleanControl to wrap.
 * @throws IllegalArgumentException if the specified control type is not supported by the Clip.
 * @throws ClassCastException if the control retrieved is not a BooleanControl.
 */
abstract class BooleanControlWrapper(clip: Clip, controlType: BooleanControl.Type) {
    private val control: BooleanControl = clip.getControl(controlType) as BooleanControl
    private val defaultValue: Boolean = control.value

    /**
     * Retrieves the current value of the control.
     *
     * @return The current boolean value of the control.
     */
    fun getValue(): Boolean = control.value

    /**
     * Updates the value of the control.
     *
     * @param value The new value to set for the control.
     */
    fun updateValue(value: Boolean) {
        control.value = value
    }

    /**
     * Resets the control to its default value.
     */
    fun resetValue() {
        control.value = defaultValue
    }

    class MuteControl(clip: Clip) : BooleanControlWrapper(clip, BooleanControl.Type.MUTE)
}