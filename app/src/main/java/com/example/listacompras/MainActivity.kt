package com.example.listacompras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.listacompras.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var database: DatabaseReference
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tratarLogin()

        binding.fab.setOnClickListener {
            novoItem()
        }
    }

    fun novoItem() {
        val caixaTexto = EditText(this)
        caixaTexto.hint = "Nome do item"

        AlertDialog.Builder(this)
            .setTitle("Novo item")
            .setView(caixaTexto)
            .setPositiveButton("Adicionar") { dialog, button ->
                val prod = Produto(nome = caixaTexto.text.toString())
                val novoNo = database.child("produtos").push()
                novoNo.key?.let{
                    prod.id = it
                }
                novoNo.setValue(prod)
            }
            .create()
            .show()
    }

    fun tratarLogin(){

        if (FirebaseAuth.getInstance().currentUser != null){
            Toast.makeText(this, "Entrou", Toast.LENGTH_LONG).show()
            configurarBase()
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
            configurarBase()
        }
        else {
            finishAffinity()
        }
    }

    fun configurarBase(){

        val usuario = FirebaseAuth.getInstance().currentUser

        if(usuario != null){
            database = FirebaseDatabase.getInstance().reference.child(usuario.uid)
        }
    }
}