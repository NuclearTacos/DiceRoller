package io.nucleartacos.diceroller.ui.home

import android.os.Bundle
import android.text.TextUtils.substring
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nucleartacos.diceroller.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

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
        var functionButtonList: ArrayList<Button> = getFunctionButtons(root)
        functionButtonList.forEach {
            it.setOnClickListener {
                functionPress(it)
            }
        }

        // When the delete key is pressed, remove the last character from the Dice Space
        root.findViewById<Button>(R.id.del).setOnClickListener{
            delPress(it)
        }

        // When the clear key is pressed, clear the entire Dice Space
        root.findViewById<Button>(R.id.C).setOnClickListener{
            clearPress(it)
        }

        return root
    }

    fun functionPress(button: View){
        if (button is Button){
            var diceValue = button.rootView.findViewById<TextView>(R.id.diceSpace).text as String
            button.rootView.findViewById<TextView>(R.id.diceSpace).text = diceValue + button.text
        }
    }

    fun delPress(button: View){
        if (button is Button){
            var diceValue = button.rootView.findViewById<TextView>(R.id.diceSpace).text as String

            if ( diceValue.substring( diceValue.length-1 ) == " "){
                button.rootView.findViewById<TextView>(R.id.diceSpace)
                    .text = substring( diceValue, 0, diceValue.length-3 )
            }
            else {
                button.rootView.findViewById<TextView>(R.id.diceSpace)
                    .text = substring( diceValue, 0, diceValue.length-1 )
            }
        }
    }

    fun clearPress(button: View){
        if (button is Button){
            button.rootView.findViewById<TextView>(R.id.diceSpace).text = ""
        }
    }

    fun getFunctionButtons(root: View): ArrayList<Button>{
        val functionButtons: ArrayList<Button> = ArrayList<Button>()

        functionButtons.add(root.findViewById<Button>(R.id.d100))
        functionButtons.add(root.findViewById<Button>(R.id.dx))
        functionButtons.add(root.findViewById<Button>(R.id.d4))
        functionButtons.add(root.findViewById<Button>(R.id.d6))
        functionButtons.add(root.findViewById<Button>(R.id.d8))
        functionButtons.add(root.findViewById<Button>(R.id.d10))
        functionButtons.add(root.findViewById<Button>(R.id.d12))
        functionButtons.add(root.findViewById<Button>(R.id.d20))
        functionButtons.add(root.findViewById<Button>(R.id.n9))
        functionButtons.add(root.findViewById<Button>(R.id.n8))
        functionButtons.add(root.findViewById<Button>(R.id.n7))
        functionButtons.add(root.findViewById<Button>(R.id.n6))
        functionButtons.add(root.findViewById<Button>(R.id.n5))
        functionButtons.add(root.findViewById<Button>(R.id.n4))
        functionButtons.add(root.findViewById<Button>(R.id.n3))
        functionButtons.add(root.findViewById<Button>(R.id.n2))
        functionButtons.add(root.findViewById<Button>(R.id.n1))
        functionButtons.add(root.findViewById<Button>(R.id.n0))
        functionButtons.add(root.findViewById<Button>(R.id.sOpen))
        functionButtons.add(root.findViewById<Button>(R.id.sClose))
        functionButtons.add(root.findViewById<Button>(R.id.sDivide))
        functionButtons.add(root.findViewById<Button>(R.id.sMultiply))
        functionButtons.add(root.findViewById<Button>(R.id.sMinus))
        functionButtons.add(root.findViewById<Button>(R.id.sPlus))

        return functionButtons
    }
}