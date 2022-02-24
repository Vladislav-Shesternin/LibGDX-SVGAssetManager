package com.veldan.svgassetmanager.svg

import android.graphics.Bitmap
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
import java.nio.ByteBuffer



object SVGAssetManager : Disposable {

    private val coroutineLoad = CoroutineScope(Dispatchers.Main)
    private val coroutineProgress = CoroutineScope(Dispatchers.Main)
    private val mutex = Mutex()


    var loadList = listOf<EnumSVG>()


    enum class EnumSVG(val svg: SVGTexture) {
        //      _1(SVGTexture("a/Ellipse 17.svg", 2000, 2000)),
//      _2(SVGTexture("a/Ellipse 18.svg", 2000, 2000)),
//      _3(SVGTexture("a/Ellipse 19.svg", 2000, 2000)),
//      _4(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _5(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _6(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _7(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _8(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _9(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _10(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _11(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _12(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _13(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _14(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _15(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _16(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _17(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _18(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _19(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _20(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _21(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _22(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _23(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _24(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _25(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _26(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _27(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _28(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _29(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _30(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _31(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _32(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//      _33(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
//
        _34(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _35(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _36(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _37(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _38(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _39(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _40(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _41(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _42(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _43(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _44(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _45(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _46(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _47(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _48(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _49(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _50(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _51(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _52(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _53(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _54(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _55(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _56(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _57(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _58(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _59(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _60(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _61(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _62(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
        _63(SVGTexture("a/Ellipse 20.svg", 2000, 2000)),
    }



    override fun dispose() {
        coroutineLoad.cancel()
        coroutineProgress.cancel()
    }



    private suspend fun generateTextureFromSVG(svg: SVGTexture) = CompletableDeferred<Boolean>().also { continuation ->
        val bitmap = SVGHelper.noContext().open(activityContext.assets.open(svg.path).bufferedReader().use { it.readText() })
            .setRequestBounds(svg.width, svg.height).bitmap

        Gdx.app.postRunnable {
            svg.texture = Texture(bitmap.width, bitmap.height, Pixmap.Format.RGBA8888)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, svg.texture!!.textureObjectHandle)
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
                generateTextureFromSVG(enumSvg.svg)
                mutex.withLock { progressFlow.value += onePercentProgress }
            }.apply { listJob.add(this) }
        }
        listJob.joinAll()
        progressFlow.value = 100f
        loaded()
        continuation.complete(true)
    }.await()

}