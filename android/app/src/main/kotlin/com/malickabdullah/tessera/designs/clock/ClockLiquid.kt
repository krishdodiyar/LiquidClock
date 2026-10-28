
package com.malickabdullah.tessera.designs.clock

import android.graphics.Paint
import com.malickabdullah.tessera.engine.BgKind
import com.malickabdullah.tessera.engine.Category
import com.malickabdullah.tessera.engine.Scene
import com.malickabdullah.tessera.engine.SceneInputs
import com.malickabdullah.tessera.engine.SizeClass
import com.malickabdullah.tessera.engine.Style
import com.malickabdullah.tessera.engine.Toggle
import com.malickabdullah.tessera.engine.WidgetDesign
import java.time.format.DateTimeFormatter
import kotlin.math.max

object ClockLiquid : WidgetDesign {
    override val id = "clock.liquid"
    override val category = Category.CLOCK
    override val name = "Liquid Glass"
    override val blurb =
        "Giant translucent outlined digits with a glass-inspired finish."

    override val sizes = listOf(
        SizeClass.WIDE,
        SizeClass.LARGE
    )

    override val defaults = Style.of(
        font = "grotesk",
        weight = 700,
        text = 0xEFFFFFFF,
        accent = 0xFFBDEBFF,
        background = 0x00000000,
        kind = BgKind.TRANSPARENT,
        opacity = 0f,
        radius = 0f,
        padding = 10f,
        tracking = -0.025f
    )

    override val toggles = listOf(
        Toggle.Switch("ampm", "Show AM / PM", true),
        Toggle.Switch("glow", "Glass highlight", true)
    )

    // The engine redraws the bitmap when the minute changes.
    override fun liveKey(scene: SceneInputs): String =
        "${scene.now.hour}:${scene.now.minute}"

    override fun draw(s: Scene) {
        val b = s.box
        val rowH = b.height() / 2.12f
        val size = s.fit(
            "88",
            b.width() * 0.96f,
            rowH * 0.82f,
            font = s.style.font,
            weight = s.style.weight
        ) * s.hero

        val hour = s.now.format(DateTimeFormatter.ofPattern("hh"))
        val minute = s.now.format(DateTimeFormatter.ofPattern("mm"))
        val ampm = s.now.format(DateTimeFormatter.ofPattern("a"))

        drawGlassText(
            s, hour, b.centerX(), b.top + rowH * 0.53f,
            size
        )

        drawGlassText(
            s, minute, b.centerX(), b.top + rowH * 1.57f,
            size
        )

        if (s.flag("ampm")) {
            val label = s.paint(
                max(9f, size * 0.105f),
                s.accent,
                font = "mono",
                weight = 500,
                align = Paint.Align.CENTER,
                tracking = 0.16f
            )
            s.textMid(ampm, b.centerX(), b.bottom - size * 0.045f, label)
        }
    }

    private fun drawGlassText(
        s: Scene,
        value: String,
        x: Float,
        centerY: Float,
        size: Float
    ) {
        // Soft outer highlight.
        if (s.flag("glow")) {
            val glow = s.paint(
                size, s.ink(0.20f, s.accent),
                align = Paint.Align.CENTER
            ).apply {
                style = Paint.Style.STROKE
                strokeWidth = max(2f, size * 0.045f)
                strokeJoin = Paint.Join.ROUND
            }
            s.textMid(value, x, centerY, glow)
        }

        // Bright glass edge.
        val outline = s.paint(
            size, s.ink(0.92f, s.text),
            align = Paint.Align.CENTER
        ).apply {
            style = Paint.Style.STROKE
            strokeWidth = max(1f, size * 0.012f)
            strokeJoin = Paint.Join.ROUND
        }
        s.textMid(value, x, centerY, outline)

        // Low-opacity fill lets the wallpaper show through the digits.
        val fill = s.paint(
            size, s.ink(0.13f, s.text),
            align = Paint.Align.CENTER
        ).apply {
            style = Paint.Style.FILL
        }
        s.textMid(value, x, centerY, fill)

        // Fine inner highlight for a glass-like edge.
        val highlight = s.paint(
            size, s.ink(0.30f, s.accent),
            align = Paint.Align.CENTER
        ).apply {
            style = Paint.Style.STROKE
            strokeWidth = max(0.5f, size * 0.003f)
        }
        s.textMid(value, x, centerY, highlight)
    }
}
