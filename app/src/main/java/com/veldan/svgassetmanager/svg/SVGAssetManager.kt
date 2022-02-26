package com.veldan.svgassetmanager.svg

import android.opengl.GLES20
import android.opengl.GLUtils
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.scand.svg.SVGHelper
import com.veldan.svgassetmanager.activityContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object SVGAssetManager : Disposable {

    private val coroutineLoad = CoroutineScope(Dispatchers.Main)
    private val coroutineProgress = CoroutineScope(Dispatchers.Main)
    private val mutex = Mutex()

    var loadList = listOf<EnumSVG>()


    enum class EnumSVG(val svg: SVGTextureData) {
        A(SVGTextureData("a.svg", 500, 500)),
    }



    override fun dispose() {
        coroutineLoad.cancel()
        coroutineProgress.cancel()
    }



    private fun getAssetString(path: String) = activityContext.assets.open(path).bufferedReader().use { it.readText() }

    private suspend fun SVGTextureData.generateTexture() = CompletableDeferred<Boolean>().also { continuation ->
        val bitmap = SVGHelper.noContext().open(getAssetString(path)).setRequestBounds(width, height).bitmap
        Gdx.app.postRunnable {
            texture = Texture(bitmap.width, bitmap.height, Pixmap.Format.RGBA8888)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture!!.textureObjectHandle)
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            bitmap.recycle()
            continuation.complete(true)
        }
    }.await()

    suspend fun load(
        progress: (Int) -> Unit = { },
        loaded: () -> Unit = { }
    ) = CompletableDeferred<Boolean>().also { continuation ->

        if (loadList.isEmpty()) throw Exception("loadList isEmpty = true")

        val progressFlow = MutableStateFlow(0f)
        coroutineProgress.launch {
            progressFlow.collect {
                progress(it.toInt())
            }
        }

        val onePercentProgress = 100f / loadList.size
        val listJob = mutableListOf<Job>()

        loadList.onEach { enumSvg ->
            coroutineLoad.launch {
                enumSvg.svg.generateTexture()
                mutex.withLock { progressFlow.value += onePercentProgress }
            }.apply { listJob.add(this) }
        }
        listJob.joinAll()
        progressFlow.value = 100f
        loaded()
        continuation.complete(true)
    }.await()

}