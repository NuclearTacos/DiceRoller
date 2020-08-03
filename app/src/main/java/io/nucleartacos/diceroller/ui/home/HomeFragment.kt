package io.nucleartacos.diceroller.ui.home

import android.os.Bundle
import android.text.TextUtils.substring
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

        slotList.forEach( {
            spaceString += it.toString()
        })

        return spaceString.replace("  "," " ).trim()
    }


    fun dButtonPress(button: View) {
        if (button is Button) {
            // If there are no elements, the last slot counts as 1
            // Or if the last slot *isn't* a number
            if (slotList.size == 0 ) {
                var lastSlot = Slot(1)
                slotList.add( lastSlot )
                slotList.add( Slot(button.text.toString()) )
            }
            else {
                var lastSlot = slotList.removeAt( slotList.lastIndex )

                if (lastSlot.type == Slot.SLOT_TYPE.NUMBER) {
                    slotList.add(lastSlot)
                    slotList.add( Slot(button.text.toString()) )
                }
                // If the lastSlot diceType matches the diceType of the next button, increment the prior number
                else if (lastSlot.diceType == lastSlot.getDiceType( button.text.toString())){
                    slotList[slotList.lastIndex].number += 1
                    slotList.add(lastSlot)
                }
                else {
                    slotList.add( lastSlot )
                    slotList.add( Slot(1) )
                    slotList.add( Slot(button.text.toString()) )
                }
            }

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }

    fun numButtonPress(button: View) {
        if (button is Button) {
            slotList.add( Slot( button.text.toString().toInt() ) )

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }

    fun operationButtonPress(button: View, operation: Slot.OPERATION) {
        if (button is Button) {
            slotList.add( Slot( operation ) )

            updateDiceSpaceText(button)
        }
        else throw Exception()
    }

    fun delPress(button: View) {
        if (button is Button){
            slotList.removeAt( slotList.size - 1 )

            updateDiceSpaceText(button)
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

        root.findViewById<Button>(R.id.sDivide).setOnClickListener { operationButtonPress( it, Slot.OPERATION.DIVIDE ) }
        root.findViewById<Button>(R.id.sMultiply).setOnClickListener { operationButtonPress( it, Slot.OPERATION.MULTIPLY ) }
        root.findViewById<Button>(R.id.sMinus).setOnClickListener { operationButtonPress( it, Slot.OPERATION.MINUS ) }
        root.findViewById<Button>(R.id.sPlus).setOnClickListener { operationButtonPress( it, Slot.OPERATION.PLUS ) }
        root.findViewById<Button>(R.id.sOpen).setOnClickListener { operationButtonPress( it, Slot.OPERATION.OPEN ) }
        root.findViewById<Button>(R.id.sClose).setOnClickListener { operationButtonPress( it, Slot.OPERATION.CLOSE ) }
    }
}