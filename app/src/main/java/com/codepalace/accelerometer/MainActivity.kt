package com.codepalace.accelerometer

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelStore
import com.codepalace.accelerometer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setUpSensorStuff()
    }

    private fun setUpSensorStuff() {
        // Create the sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also { gyro ->
            sensorManager.registerListener(
                this,
                gyro,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val type = event?.sensor?.type
        when (type) {
            Sensor.TYPE_ACCELEROMETER -> {
                //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

                // Sides = Tilting phone left(10) and right(-10)
                val sides = event.values[0]

                // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
                val upDown = event.values[1]
                if (!viewModel.isALreadySet) {
                    viewModel.updateXAxis(sides)
                    viewModel.updateIsALreadySet(true)
                }

                binding.tvSquare.apply {
                    rotationX = upDown * 3f
                    rotationY = sides * 3f
                    rotation = -sides
                    translationX = sides * -10
                    translationY = upDown * 10
                }

                // Changes the colour of the square if it's completely flat
                val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
                binding.tvSquare.setBackgroundColor(color)

                binding.tvSquare.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}, xAxis: ${viewModel.xAxis}"
            }

            Sensor.TYPE_GYROSCOPE -> {

            }

            else -> {

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}

