package com.veldan.svgassetmanager

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.veldan.svgassetmanager.svg.SVGAssetManager
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

const val WIDTH = 700f
const val HEIGHT = 1400f
val stage by lazy { Stage(FitViewport(WIDTH, HEIGHT)) }

class LibGDXGame : ApplicationAdapter() {

    private val coroutineLoad = CoroutineScope(Dispatchers.Main)



    override fun create() {
        SVGAssetManager.loadList = listOf(*SVGAssetManager.EnumSVG.values())
        loadAssets()
    }

    override fun render() {
        ScreenUtils.clear(Color.WHITE)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        SVGAssetManager.dispose()
        coroutineLoad.cancel()
    }



    private fun loadAssets() {
        coroutineLoad.launch {
            val time = measureTimeMillis {
                SVGAssetManager.load(
                    progress = { doInProgressLoadAssets(it) },
                    loaded = { doAfterLoadAssets() }
                )
            }
            Log.i("VLAD", "time = $time")
        }
    }

    private fun doInProgressLoadAssets(progress: Int) {
        Log.i("VLAD", "progress = $progress")
    }

    private fun doAfterLoadAssets() {
        stage.addActorsOnStage()
    }



    private fun Stage.addActorsOnStage() {
        addRect()
    }


    private fun Stage.addRect() {
        val image = Image(SVGAssetManager.EnumSVG.A.svg.texture).apply {
            setBounds(10f, 10f, 500f, 500f)
        }
        batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA)
        addActor(image)
    }
}