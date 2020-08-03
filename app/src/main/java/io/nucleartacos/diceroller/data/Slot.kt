package io.nucleartacos.diceroller.data

import android.media.VolumeShaper

class Slot {
    
    lateinit var operation: OPERATION
    lateinit var diceType: DICE_TYPE
    var number = 0

    lateinit var type: SLOT_TYPE


    constructor() {
        type = SLOT_TYPE.NONE
        operation = OPERATION.NONE
        diceType = DICE_TYPE.none
        number = 0
    }

    constructor( buttonText: String ) {
        type = SLOT_TYPE.DIE
        diceType = getDiceType(buttonText)

        // The other values are set to their "none" values
        operation = OPERATION.NONE
        number = 0
    }

    constructor( op: OPERATION ) {
        type = SLOT_TYPE.OPERATION
        operation = op

        // The other values are set to their "none" values
        diceType = DICE_TYPE.none
        number = 0
    }

    constructor( num: Int ) {
        type = SLOT_TYPE.NUMBER
        number = num

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
                            OPERATION.DIVIDE -> " ÷ "
                            OPERATION.MULTIPLY -> " × "
                            OPERATION.MINUS -> " – "
                            OPERATION.PLUS -> " + "
                            OPERATION.CLOSE -> " ( "
                            OPERATION.OPEN -> " ) "
                            else -> ""
                        }
                    SLOT_TYPE.DIE ->
                        when (diceType) {
                            DICE_TYPE.dx -> "dx"
                            DICE_TYPE.d100 -> "d100"
                            DICE_TYPE.d20 -> "d20"
                            DICE_TYPE.d12 -> "d12"
                            DICE_TYPE.d10 -> "d10"
                            DICE_TYPE.d8 -> "d8"
                            DICE_TYPE.d6 -> "d6"
                            DICE_TYPE.d4 -> "d4"
                            else -> ""
                        }
                    SLOT_TYPE.NUMBER -> number.toString()
                    else -> ""
                }
            )
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
        MULTIPLY,
        DIVIDE,
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