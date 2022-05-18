package com.example.listacompras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tratarLogin()

    }

    fun tratarLogin(){

        if (FirebaseAuth.getInstance().currentUser != null){
            Toast.makeText(this, "Entrou", Toast.LENGTH_LONG).show()
        }
        else{
            //Cria um array list com os provedores de autenticacao suportados
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

            //Solicita a abertura da atividade de autenticacao do Firebase Auth UI
            val i = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

            startActivityForResult(i, 1)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            Toast.makeText(this, "Autenticado", Toast.LENGTH_LONG).show()
        }
        else {
            finishAffinity()
        }
    }
}