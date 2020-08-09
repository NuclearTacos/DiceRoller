package io.nucleartacos.diceroller.data

import kotlin.random.Random

class Slot {
    
    lateinit var operation: OPERATION
    lateinit var diceType: DICE_TYPE
    var quantity = 0
    var y = 0

    lateinit var type: SLOT_TYPE


    constructor() {
        type = SLOT_TYPE.NONE
        operation = OPERATION.NONE
        diceType = DICE_TYPE.none
        quantity = 0
    }

    constructor( buttonText: String ) {
        type = SLOT_TYPE.DIE
        diceType = getDiceType(buttonText)

        // The other values are set to their "none" values
        operation = OPERATION.NONE
        quantity = 0
    }

    constructor( op: OPERATION ) {
        type = SLOT_TYPE.OPERATION
        operation = op

        // The other values are set to their "none" values
        diceType = DICE_TYPE.none
        quantity = 0
    }

    constructor( num: Int ) {
        type = SLOT_TYPE.NUMBER
        quantity = num

        // The other values are set to their "none" values
        operation = OPERATION.NONE
        diceType = DICE_TYPE.none
    }

    fun getDiceType (buttonText: String): DICE_TYPE {
        return (
                when (buttonText) {
                    "dx" -> DICE_TYPE.dx
                    "d100" -> DICE_TYPE.d100
                    "d20" -> DICE_TYPE.d20
                    "d12" -> DICE_TYPE.d12
                    "d10" -> DICE_TYPE.d10
                    "d8" -> DICE_TYPE.d8
                    "d6" -> DICE_TYPE.d6
                    "d4" -> DICE_TYPE.d4
                    else -> DICE_TYPE.none
                }
            )
    }

    override fun toString(): String {
        return (
                when (type) {
                    SLOT_TYPE.OPERATION ->
                        when (operation) {
                            OPERATION.MINUS -> " – "
                            OPERATION.PLUS -> " + "
                            OPERATION.CLOSE -> " ) "
                            OPERATION.OPEN -> " ( "
                            else -> ""
                        }
                    SLOT_TYPE.DIE ->
                        quantity.toString() + ( when (diceType) {
                            DICE_TYPE.dx -> "d̲" + if (y == 0) "" else y.toString()
                            DICE_TYPE.d100 -> {
                                y = 100
                                "d100"
                            }
                            DICE_TYPE.d20 -> {
                                y = 20
                                "d20"
                            }
                            DICE_TYPE.d12 -> {
                                y = 12
                                "d12"
                            }
                            DICE_TYPE.d10 -> {
                                y = 10
                                "d10"
                            }
                            DICE_TYPE.d8 -> {
                                y = 8
                                "d8"
                            }
                            DICE_TYPE.d6 -> {
                                y = 6
                                "d6"
                            }
                            DICE_TYPE.d4 -> {
                                y = 4
                                "d4"
                            }
                            else -> ""
                        })
                    SLOT_TYPE.NUMBER -> quantity.toString()
                    else -> ""
                }
            )
    }

    fun rollSlot(): Int {
        var outcome = 0

        if (diceType != Slot.SLOT_TYPE.OPERATION) {
            for (i in 1..quantity) {
                outcome += Random.nextInt(1, y)
            }
        }

        return outcome
    }


    enum class SLOT_TYPE {
        DIE,
        OPERATION,
        NUMBER,
        NONE
    }

    enum class OPERATION {
        PLUS,
        MINUS,
        DICE_ADD,
        DICE_MINUS,
        OPEN,
        CLOSE,
        NONE
    }

    enum class DICE_TYPE {
        none,
        dx,
        d100,
        d20,
        d12,
        d10,
        d8,
        d6,
        d4
    }
}