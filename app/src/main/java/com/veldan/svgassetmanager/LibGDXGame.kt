package com.veldan.svgassetmanager

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.veldan.svgassetmanager.svg.SVGAssetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

val stage by lazy { Stage(ScreenViewport()) }

class LibGDXGame : ApplicationAdapter() {

    private val progressText by lazy { Label("", Label.LabelStyle(BitmapFont(), Color.BLACK)) }

    override fun create() {
        SVGAssetManager.apply {
          loadList = listOf(*SVGAssetManager.EnumSVG.values())
        }

        stage.addActor(progressText.apply {
            debug()
            setAlignment(Align.center)
            setBounds(100f, 600f, 100f, 100f)
        })

        CoroutineScope(Dispatchers.Main).launch {
            val time = measureTimeMillis {
                SVGAssetManager.load(
                    progress = {
                        Log.i("VLAD", "progress = $it")
                        progressText.setText(it)
                    },
                    loaded = {
                        stage.addActor(Image(SVGAssetManager.EnumSVG._63.svg.texture).apply {
                            setBounds(10f, 10f, 500f, 500f)
                        })
                    }
                )
            }
            Log.i("VLAD", "time = $time")
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render() {
        ScreenUtils.clear(Color.WHITE)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        SVGAssetManager.dispose()
    }
}


//val bitmap = SVGHelper.noContext().open(activityContext.assets.open(svgs[0].path).bufferedReader().use { it.readText() })
//    .setRequestBounds(300, 300).bitmap
//
//val img = Texture(bitmap.width, bitmap.height, Pixmap.Format.RGBA8888)
//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, img.textureObjectHandle)
//GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
//bitmap.recycle()