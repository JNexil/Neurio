package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.exception.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.webs.*
import com.github.jnexil.neuvi.util.*
import com.github.jnexil.neuvi.util.Layers.linear
import com.github.jnexil.skribe.*
import com.github.jnexil.skribe.calling.*
import com.github.jnexil.skribe.expect.*
import com.github.jnexil.skribe.expect.Expect.*
import com.github.jnexil.skribe.expect.dev.*
import com.github.jnexil.skribe.expect.extensions.*
import com.github.jnexil.skribe.util.*


class LayerAttachingTest: Skribe("attach one layer to other") {
    data class LayerPair(val attachment: FlexibleLayer = linear(),
                         val target: FlexibleLayer = linear(),
                         val direction: Direction = LEFT,
                         val web: Web? = null)

    init {
        describe("attaching").intermediate
                .share("with each direction")
                .couple(values = Direction.values().asSequence()) { unit, direction: Direction ->
                    LayerPair(direction = direction).apply {
                        target.attach(direction, attachment)
                    }
                }
                .stepwise("created web") {
                    couple(RIGHT, LEFT) { pair, direction ->
                        when (direction) {
                            pair.direction -> pair.copy(web = pair.target[direction])
                            else           -> pair.copy(web = pair.attachment[direction])
                        }
                    }.move("by direction") {
                        shouldR("not be null") { web != null }
                        testR("contain both sides") {
                            web.should.has.layerAt(direction).that.equal(attachment)
                            web.should.has.layerAt(direction.reverse).that.equal(target)
                        }
                    }
                    testR("be same") {
                        target[direction].should.be.identity(attachment[direction.reverse])
                    }
                }

        root.intermediate
                .share("attach to self").moveCalling {
            val layer = linear()
            layer.attach(LEFT, layer)
        }
                .testR("should throw AttachLayerException") {
                    should.fails.with.throwable.like(AttachLayerException::class.java)
                }

        root.intermediate
                .move("given two layers which is already attached") {
                    LayerPair().apply {
                        target.attach(LEFT, attachment)
                    }
                }
                .share("when attach layer") {
                    share("to same direction").test("should successfully attach to same direction") {
                        it.target.attach(LEFT, it.attachment)
                    }
                    share("to other direction").test("should return AttachLayerException for attaching to other direction") {
                        Calling {
                            it.target.attach(RIGHT, it.attachment)
                        }.should.fails.with.throwable.like(AttachLayerException::class.java).with.message("already attached to other direction")
                    }
                }
    }


    private fun Expect<Web>.layerAt(direction: Direction): Expect<Layer> = (this as Backend<Web>)
            .append("layerAt")
            .append(direction).map { value!![direction] }
}
