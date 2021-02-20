package com.envercelik.coffeeorder

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.envercelik.coffeeorder.databinding.ActivityMainBinding

/**
 * This app sends mail an order form to order coffee.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private var quantityOfCoffee = 1
    private var priceOfCoffee = 5
    private var totalPrice = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //initializes
        binding.textViewQuantity.text = quantityOfCoffee.toString()
        binding.textViewPrice.text = priceOfCoffee.toString()


        setListeners()


    }


    private fun setListeners() {
        val clickAbleViews:List<View> = listOf(
            binding.buttonIncrement,
            binding.buttonDecrement,
            binding.buttonOrder,
            binding.checkboxChocolate,
            binding.checkboxWhippedCream
        )


        for (item in clickAbleViews) {
            when(item.id) {
                R.id.button_decrement -> item.setOnClickListener { decrement() }
                R.id.button_increment -> item.setOnClickListener { increment() }
                R.id.button_order -> item.setOnClickListener { order() }
                R.id.checkbox_chocolate -> item.setOnClickListener { calculatePrice() }
                R.id.checkbox_whipped_cream -> item.setOnClickListener { calculatePrice() }
            }
        }
    }


    //Receive the order and send it to the mail application
    private fun order() {

        val name = binding.editTextName.text.toString()
        val whippedCream = binding.checkboxWhippedCream.isChecked
        val chocolate = binding.checkboxChocolate.isChecked

        val orderSummary = createOrderSummary(name,whippedCream,chocolate)

        val subject = resources.getString(R.string.mail_subject)

        val intentToMailApp = Intent(Intent.ACTION_SENDTO)
        intentToMailApp.data = Uri.parse("mailto:") //only email apps should handle this
        intentToMailApp.putExtra(Intent.EXTRA_EMAIL, arrayOf("coffeeshop@gmail.com"))
        intentToMailApp.putExtra(Intent.EXTRA_SUBJECT,subject)
        intentToMailApp.putExtra(Intent.EXTRA_TEXT,orderSummary)

        if (intentToMailApp.resolveActivity(packageManager) != null) {
            startActivity(intentToMailApp)
        }

    }


    //this function calculates of the order price and displays.
    private fun calculatePrice() {

        totalPrice = quantityOfCoffee*priceOfCoffee

        if (binding.checkboxChocolate.isChecked) {
            totalPrice += quantityOfCoffee*2
        }

        if (binding.checkboxWhippedCream.isChecked) {
            totalPrice += quantityOfCoffee*1
        }

        binding.textViewPrice.text = totalPrice.toString()

    }


    //this function  decreases quantity of coffee and displays on the quantity text view. It also
    //updates the price
    private fun decrement() {

        if (quantityOfCoffee > 1) {

            quantityOfCoffee--
            binding.textViewQuantity.text = quantityOfCoffee.toString()

        }

        calculatePrice()
    }


    //this method  increases quantity of coffee and displays on the quantity text view. It also
    //updates the price
    private fun increment() {
        quantityOfCoffee++
        binding.textViewQuantity.text = quantityOfCoffee.toString()

        calculatePrice()
        binding.textViewPrice.text = totalPrice.toString()


    }


    private fun createOrderSummary(name: String, addWhippedCream:Boolean,addChocolate:Boolean):String {

        val hasItChocolate = if (addChocolate) {
            resources.getString(R.string.yes)
        } else {
            resources.getString(R.string.no)
        }


        val hasItWhippedCream = if (addWhippedCream) {
            resources.getString(R.string.yes)
        } else {
            resources.getString(R.string.no)
        }


        val currency = resources.getString(R.string.currency)

        return resources.getString(R.string.name)              + " : " + name + "\n" +
        resources.getString(R.string.add_whipped_cream) + " : " + hasItWhippedCream + "\n" +
        resources.getString(R.string.add_chocolate)     + " : " + hasItChocolate + "\n" +
        resources.getString(R.string.quantity)          + " : " + quantityOfCoffee + "\n" +
        resources.getString(R.string.total)             + " : " + totalPrice + " "+  currency +"\n"+
        resources.getString(R.string.thank_you)
    }


}