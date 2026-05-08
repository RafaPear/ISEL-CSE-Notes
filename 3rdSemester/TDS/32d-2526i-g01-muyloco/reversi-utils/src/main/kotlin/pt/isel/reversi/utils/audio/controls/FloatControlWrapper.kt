package pt.isel.reversi.utils.audio.controls

import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl

/**
 * A wrapper class for managing FloatControl types in audio Clips.
 * @constructor Creates a FloatControlWrapper for the specified control type.
 * @param clip The Clip containing the control.
 * @param controlType The type of FloatControl to wrap.
 * @throws IllegalArgumentException if the specified control type is not supported by the Clip.
 * @throws ClassCastException if the control retrieved is not a FloatControl.
 */
@Suppress("Unused")
abstract class FloatControlWrapper(clip: Clip, controlType: FloatControl.Type) {
    protected val control: FloatControl = clip.getControl(controlType) as FloatControl
    val defaultValue: Float = control.value
    val minimumValue: Float = control.minimum
    val maximumValue: Float = control.maximum

    /**
     * Retrieves the current value of the control.
     *
     * @return The current value of the control.
     */
    fun getValue(): Float = control.value

    /**
     * Updates the value of the control.
     *
     * @param value The new value to set for the control.
     */
    fun updateValue(value: Float) {
        val oldValue = control.value
        val newValue = value.coerceIn(minimumValue, maximumValue)
        control.shift(oldValue, newValue, 1000)
    }

    /**
     * Adds a delta to the current value of the control.
     *
     * @param delta The amount to add to the current value.
     */
    fun addValue(delta: Float) {
        updateValue(control.value + delta)
    }

    /**
     * Resets the control to its default value.
     */
    fun resetValue() {
        updateValue(defaultValue)
    }

    class MasterVolumeControl(clip: Clip) : FloatControlWrapper(clip, FloatControl.Type.MASTER_GAIN)
    class PanControl(clip: Clip) : FloatControlWrapper(clip, FloatControl.Type.PAN)
    class BalanceControl(clip: Clip) : FloatControlWrapper(clip, FloatControl.Type.BALANCE)
}