package io.nucleartacos.diceroller.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nucleartacos.diceroller.R
import io.nucleartacos.diceroller.data.Slot
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var slotList: ArrayList<Slot> = ArrayList<Slot>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            //textView.text = it
        })

        // Initialize the Dice Space with a blank value.
        root.findViewById<TextView>(R.id.diceSpace).text = ""

        // Get all of the function buttons.  The function buttons are every button that add things
        // to the Dice Space.  Any X, dY, or +/-/whatever buttons.
        assignButtionClicks(root)

        return root
    }

    fun diceSpaceString(): String {
        var spaceString = ""

        slotList.forEach {
            spaceString += it.toString()
        }

        return spaceString.replace("  "," " ).trim()
    }

//    fun rollStack(): Int {
//        var total = 0
//
//        slotList.forEach {
//            if (it.type != Slot.SLOT_TYPE.OPERATION) {
//                var factor = 1
//
//                if (slotList.size > 1 && slotList[slotList.indexOf(it)-1].operation == Slot.OPERATION.MINUS) {
//                    factor = -1
//                }
//
//                total += factor * it.rollSlot()
//            }
//        }
//
//        resultSpace.text = total.toString()
//
//        return total
//    }


    fun dButtonPress(button: View) {
        if (button is Button) {
            // If there are no elements, the last slot counts as 1
            // Or if the last slot *isn't* a number
            if (slotList.size == 0 ) {
                var lastSlot = Slot(button.text.toString())
                lastSlot.quantity = 1
                slotList.add( lastSlot )
            }
            else {
                var lastSlot = slotList.removeAt( slotList.lastIndex )

                if (lastSlot.type == Slot.SLOT_TYPE.NUMBER) {
                    lastSlot.diceType = lastSlot.getDiceType(button.text.toString())
                    lastSlot.type = Slot.SLOT_TYPE.DIE
                    slotList.add(lastSlot)
                }
                else if (lastSlot.type == Slot.SLOT_TYPE.OPERATION) {
                    slotList.add(lastSlot)

                    var newSlot = Slot(button.text.toString())
                    newSlot.quantity = 1
                    slotList.add(newSlot)
                }
                // If the lastSlot diceType matches the diceType of the next button, increment the prior number
                else if (lastSlot.diceType == lastSlot.getDiceType( button.text.toString())){
                    lastSlot.quantity += 1
                    slotList.add(lastSlot)
                }
                else {
                    slotList.add( lastSlot )
                    slotList.add( Slot(Slot.OPERATION.PLUS) )
                    var newSlot = Slot(button.text.toString())
                    newSlot.quantity = 1
                    slotList.add(newSlot)
                }
            }

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }

    fun numButtonPress(button: View) {
       if (button is Button) {
            // You only need special logic if the array isn't empty.
            if (slotList.size != 0 ) {
                if(slotList.last().type == Slot.SLOT_TYPE.NUMBER) {
                    slotList.last().quantity = (slotList.last().quantity.toString() + button.text).toInt()
                }
                // If the previous Dice Tyep id dx, then don't add an operator beforehand.
                else if (slotList.last().diceType == Slot.DICE_TYPE.dx) {
                    if (slotList.last().y == 0) {
                        slotList.last().y = button.text.toString().toInt()
                    }
                    else {
                        slotList.last().y = (slotList.last().y.toString() + button.text).toInt()
                    }
                }
                else if (slotList.last().type == Slot.SLOT_TYPE.DIE) {
                    slotList.add(Slot(Slot.OPERATION.PLUS))
                    slotList.add(Slot(button.text.toString().toInt()))
                }
                else {
                    slotList.add(Slot(button.text.toString().toInt()))
                }
            }
            else {
                slotList.add(Slot(button.text.toString().toInt()))
            }

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }

    fun operationButtonPress(button: View, operation: Slot.OPERATION) {
        if (button is Button) {
            // We don't want to add an operation if the list is empty.
            if (slotList.size > 0) {
                if (slotList.last().type == Slot.SLOT_TYPE.OPERATION) {
                    when (button.id) {
                        R.id.sPlus -> slotList.last().operation = Slot.OPERATION.PLUS
                        R.id.sMinus -> slotList.last().operation = Slot.OPERATION.MINUS
                    }
                }
                else if (slotList.last().type == Slot.SLOT_TYPE.DIE) {
                    when (button.id) {
                        R.id.dAdd -> slotList.last().quantity += 1
                        R.id.dMinus -> slotList.last().quantity -= 1
                        R.id.sMinus -> slotList.add(Slot(Slot.OPERATION.MINUS))
                        R.id.sPlus -> slotList.add(Slot(Slot.OPERATION.PLUS))
                        else -> ""
                    }

                    // If that dice slot is now 0 or less.
                    if (slotList.last().quantity <= 0 && slotList.last().type == Slot.SLOT_TYPE.DIE) {
                        slotList.removeAt(slotList.lastIndex)

                        // If the prior slot is an Operation, then remove that too.
                        if (slotList.size > 0 && slotList.last().type == Slot.SLOT_TYPE.OPERATION)
                                slotList.removeAt(slotList.lastIndex)
                    }
                }
                else {
                    slotList.add( Slot( operation ) )
                }
            }

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }

    fun delPress(button: View) {
        if (button is Button){
            // && slotList.last().diceType != Slot.DICE_TYPE.dx && slotList.last().y == 0)
            // Only delete something if there is a thing (duh)
            if (slotList.size > 0) {
                // if the prior item is a dx slot, peel off the last digit.
                if (slotList.last().diceType == Slot.DICE_TYPE.dx && slotList.last().y > 0) {
                    slotList.last().y /= 10
                }
                else {
                    slotList.removeAt( slotList.size - 1 )
                }
            }

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }

    fun rollPress(button: View) {
        if (button is Button){
            var total = 0

            slotList.forEach {
                if (it.type != Slot.SLOT_TYPE.OPERATION) {
                    var factor = 1
                    var slotVal = 0

                    if (it.type == Slot.SLOT_TYPE.NUMBER) {
                        slotVal = it.quantity
                    }
                    else {
                        if (slotList.size > 1 && slotList[slotList.indexOf(it)-1].operation == Slot.OPERATION.MINUS) {
                            factor = -1
                        }

                        slotVal = it.rollSlot()
                    }

                    total += factor * slotVal
                }
            }

            resultSpace.text = total.toString()
        }
        else throw Exception()
    }

    fun clearPress(button: View) {
        if (button is Button){
            slotList.removeAll( slotList )

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }
    
    fun updateDiceSpaceText(button: View) {
        button.rootView.findViewById<TextView>(R.id.diceSpace).text = diceSpaceString()
    }

    fun assignButtionClicks(root: View) { //: ArrayList<Button>{
        root.findViewById<Button>(R.id.dx).setOnClickListener { dButtonPress( it ) }
        root.findViewById<Button>(R.id.d100).setOnClickListener { dButtonPress( it ) }
        root.findViewById<Button>(R.id.d20).setOnClickListener { dButtonPress( it ) }
        root.findViewById<Button>(R.id.d12).setOnClickListener { dButtonPress( it ) }
        root.findViewById<Button>(R.id.d10).setOnClickListener { dButtonPress( it ) }
        root.findViewById<Button>(R.id.d8).setOnClickListener { dButtonPress( it ) }
        root.findViewById<Button>(R.id.d6).setOnClickListener { dButtonPress( it ) }
        root.findViewById<Button>(R.id.d4).setOnClickListener { dButtonPress( it ) }

        root.findViewById<Button>(R.id.n9).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n8).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n7).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n6).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n5).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n4).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n3).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n2).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n1).setOnClickListener { numButtonPress( it ) }
        root.findViewById<Button>(R.id.n0).setOnClickListener { numButtonPress( it ) }

        root.findViewById<Button>(R.id.del).setOnClickListener { delPress( it ) }
        root.findViewById<Button>(R.id.C).setOnClickListener { clearPress( it ) }

        root.findViewById<Button>(R.id.dAdd).setOnClickListener { operationButtonPress( it, Slot.OPERATION.DICE_ADD ) }
        root.findViewById<Button>(R.id.dMinus).setOnClickListener { operationButtonPress( it, Slot.OPERATION.DICE_MINUS ) }
        root.findViewById<Button>(R.id.sPlus).setOnClickListener { operationButtonPress( it, Slot.OPERATION.PLUS ) }
        root.findViewById<Button>(R.id.sMinus).setOnClickListener { operationButtonPress( it, Slot.OPERATION.MINUS ) }
        root.findViewById<Button>(R.id.sOpen).setOnClickListener { operationButtonPress( it, Slot.OPERATION.OPEN ) }
        root.findViewById<Button>(R.id.sClose).setOnClickListener { operationButtonPress( it, Slot.OPERATION.CLOSE ) }

        root.findViewById<Button>(R.id.aRoll).setOnClickListener { rollPress( it ) }
    }
}