package com.veldan.svgassetmanager.svg

import com.badlogic.gdx.graphics.Texture

data class SVGTexture(
    val path: String,
    val width: Int,
    val height: Int,
    var texture: Texture? = null
)