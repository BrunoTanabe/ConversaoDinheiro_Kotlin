package com.example.convercaodemoedas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity()
{

    private lateinit var resultado: TextView
    private lateinit var botaoconversor: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultado = findViewById<TextView>(R.id.resultado_txt) // A função findViewById<>() é usada para achar o elemento através
                                                               // do Id dele <TipodoElemento> R (Entra na pasta "Resources") id
                                                               // (Entra na pasta ids) resultado_txt é o elemento
        botaoconversor = findViewById<Button>(R.id.conversor_bt)

        botaoconversor.setOnClickListener()
        {
            converter()
        }
    }

    private fun converter()
    {
        val gpselecao: RadioGroup
        gpselecao = findViewById<RadioGroup>(R.id.selecao_gprd)

        val gpselecionado = gpselecao.checkedRadioButtonId

        var atual: String
        atual = when(gpselecionado)
        {
            R.id.dolar_rd -> {"USD"}

            R.id.euro_rd -> {"EUR"}

            R.id.libra_rd -> {"GBP"}

            R.id.iene_rd -> {"JPY"}

            else -> {"ERRO"}
        }

        val campovalor= findViewById<EditText>(R.id.valor_nbr)

        val valor: String
        valor = campovalor.text.toString()

        if(valor.isEmpty())
        {
            return
        } // Isso garante que o programa não tente executar com o cmapo vazio, podia também determinar o valor como zero

        Thread { // Isso será executado em paralelo, enquanto a interface está rodando para não travar a interface do usuário
                 // Ideal para ser executado com cchamadas remotas ou qualquer chamada que demore
            val url: URL
            url = URL("https://free.currconv.com/api/v7/convert?q=${atual}_BRL&compact=ultra&apiKey=dbfc0e631dcae6e5ed82")

            val conexao = url.openConnection() as HttpsURLConnection

            try
            {
                val dados = conexao.inputStream.bufferedReader().readText() // Recebendo os daddos da conexão e jogando na variável
                // {USD_BRL":5.731802}
                val dadosconv = JSONObject(dados)

                runOnUiThread {
                    val dadosfinal = dadosconv.getDouble("${atual}_BRL")

                    resultado.text = "${atual} ${"%.2f".format(valor.toDouble() / dadosfinal)}"
                    resultado.visibility = View.VISIBLE

                }
            } finally
            {
                conexao.disconnect()
            }
        }.start()


    }
}