package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.util.*
import com.github.jnexil.neuvi.util.Layers.linear
import com.github.jnexil.skribe.*
import com.github.jnexil.skribe.util.*

class LayerDetachingTest: Skribe("detaching layers") {
    init {
        describe("detach empty side")
                .intermediate
                .each(LEFT, RIGHT)
                .should("should return null") {
                    linear().detach(it) == null
                }

        data class LayersWithDirection(val layer: FlexibleLayer = linear(), val attachment: FlexibleLayer = linear(), val direction: Direction)
        describe("detach non-empty side")
                .intermediate
                .each(LEFT, RIGHT)
                .move { LayersWithDirection(direction = it) }
                .stepwise {
                    stepR("attach any layer") { layer.attach(direction, attachment) }

                    move("when detach reverse side") { it.apply { layer detach direction.reverse } }
                            .shouldR("save attachment") { attachment == layer through direction }

                    checkingR("when detach attachment") { attachment == layer detach direction }

                    share("given layer")
                            .shouldR("remove layer from attachment") { attachment[direction.reverse] == null }

                    share("detached layer")
                            .shouldR("remove attachment from layer") { layer[direction] == null }
                }
    }
}
