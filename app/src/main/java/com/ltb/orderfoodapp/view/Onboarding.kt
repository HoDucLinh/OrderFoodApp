package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R


class Onboarding : AppCompatActivity() {
     var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        val next_onboarding = findViewById<Button>(R.id.next_onboarding)
        val skip_onboarding = findViewById<Button>(R.id.skip_onboarding)
        next_onboarding.setOnClickListener{
            if(count == 1 ){
                nextSignIn()

            } else {
                val img = findViewById<ImageView>(R.id.imageOnboarding)
                val radio = findViewById<RadioButton>(R.id.radioButtonAfter)
                radio.setChecked(true);
                img.setImageResource(R.drawable.onboard1)
                count++
            }

        }
        skip_onboarding.setOnClickListener{
            nextSignIn()
        }

    }
    fun nextSignIn(){
        val nextSignIn = Intent(this, Home::class.java)
        startActivity(nextSignIn)
    }

}