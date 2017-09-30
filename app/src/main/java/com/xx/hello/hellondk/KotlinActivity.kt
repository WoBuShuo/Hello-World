package com.xx.hello.hellondk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import kotlinx.android.synthetic.main.activity_kotlin.*

import android.widget.Toast
class KotlinActivity : AppCompatActivity() ,View.OnClickListener{
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.sql_in->Toast.makeText(this,"ni",Toast.LENGTH_SHORT).show()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        sql_in.setOnClickListener(this)

    }



}
