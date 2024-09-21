package com.tayyar.tiletap.game

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tayyar.tiletap.R
import com.tayyar.tiletap.utils.disable
import com.tayyar.tiletap.utils.highlight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private var celebration: KonfettiView? = null
    val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
        position = Position.Relative(0.5, 0.3)
    )
    private lateinit var screen: ViewGroup
    private lateinit var starLayout: View

    companion object {
        const val SCORE_PLAYER = "SCORE_PLAYER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)

        // set customizable options
        val speed = intent.getStringExtra("speed")
        val music = intent.getBooleanExtra("music", true)
        val vibration = intent.getBooleanExtra("vibration", true)
        val speedIncrease = intent.getBooleanExtra("speedIncrease", false)

        GameView.music = music
        GameView.vibration = vibration
        Tile.speedIncrease = speedIncrease

        // set tile speed according to resolution
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        val height = displayMetrics.heightPixels
        Tile.speed = speed!!.toDouble() * height / 1280
        GameView.initialSpeed = speed.toInt()

        // add game view
        screen = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        gameView = GameView(this)
        gameView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        screen.addView(gameView)

        starLayout = View.inflate(this, R.layout.layout_star, null)

        starLayout.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        screen.addView(starLayout)

        // remove notification bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun showDialogGameOver() {
        this@GameActivity.runOnUiThread {
            lifecycleScope.launch {
                delay(600)
                val dialogGameOver = DialogGameOver()
                dialogGameOver.show(supportFragmentManager, "")
                val bundle = Bundle()
                bundle.putInt(SCORE_PLAYER, gameView.score)
                dialogGameOver.arguments = bundle
                dialogGameOver.onClickReplay = {
                    resetStar()
                    gameView.restart()
                }
            }
        }
    }

    private fun resetStar() {
        starLayout.findViewById<ImageView>(R.id.star1)?.disable(this@GameActivity)
        starLayout.findViewById<ImageView>(R.id.star2)?.disable(this@GameActivity)
        starLayout.findViewById<ImageView>(R.id.star3)?.disable(this@GameActivity)
        starLayout.findViewById<ImageView>(R.id.star4)?.disable(this@GameActivity)
        starLayout.findViewById<ImageView>(R.id.star5)?.disable(this@GameActivity)
    }

    fun showCelebrate() {
        this@GameActivity.runOnUiThread {
            when(gameView.score) {
                in 20 until 40 -> {
                    starLayout.findViewById<ImageView>(R.id.star1)?.highlight(this@GameActivity)
                }

                in 40 until 60 -> {
                    starLayout.findViewById<ImageView>(R.id.star2)?.highlight(this@GameActivity)
                }

                in 60 until 80 -> {
                    starLayout.findViewById<ImageView>(R.id.star3)?.highlight(this@GameActivity)
                }

                in 80 until 100 -> {
                    starLayout.findViewById<ImageView>(R.id.star4)?.highlight(this@GameActivity)
                }

                else -> {
                    starLayout.findViewById<ImageView>(R.id.star5)?.highlight(this@GameActivity)
                }
            }
            celebration = KonfettiView(this, null, 0)
            celebration?.setBackgroundResource(android.R.color.transparent)
            celebration?.layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            celebration?.start(party)
            screen.addView(celebration)
        }
    }

    override fun onDestroy() {
        gameView.destroy()
        super.onDestroy()
    }
}
