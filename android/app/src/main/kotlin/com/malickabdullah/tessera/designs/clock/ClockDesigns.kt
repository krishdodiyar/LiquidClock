package com.malickabdullah.tessera.designs.clock

import com.malickabdullah.tessera.engine.WidgetDesign

/** Clock designs in gallery order; the first that fits a slot is that slot's default. */
val clockDesigns: List<WidgetDesign> = listOf(
    ClockMatrix,
    ClockStack,
    ClockDial,
    ClockWords,
    ClockDual,
    ClockMinimal,
    ClockLiquid
)
