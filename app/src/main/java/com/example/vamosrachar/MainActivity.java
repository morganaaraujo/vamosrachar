package com.example.vamosrachar;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText edtValor, edtPessoas;
    TextView tvResultado;
    FloatingActionButton share, tocar;
    TextToSpeech ttsPlayer;
    String resultadoFormatado = "0,00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtValor = (EditText) findViewById(R.id.edTextValor);
        edtValor.addTextChangedListener(this);

        edtPessoas = (EditText) findViewById(R.id.edTextPessoa);
        edtPessoas.addTextChangedListener(this);

        tvResultado = (TextView) findViewById(R.id.textViewResultado);
        share = (FloatingActionButton) findViewById(R.id.fActButtonShare);
        share.setOnClickListener(this);

        tocar = (FloatingActionButton) findViewById(R.id.fActionTocar);
        tocar.setOnClickListener(this);

        //Check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122 );

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //quando não tiver dado, cria o texto
                ttsPlayer = new TextToSpeech(this, this);
            } else {
                //não tiver dado, instalar imediatamente
                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.v("PDM", edtValor.getText().toString());

        try {
            double qtPessoas = 2.0;
            if (!edtPessoas.getText().toString().equals("")) {
                qtPessoas = Double.parseDouble(edtPessoas.getText().toString());
            }

            if (qtPessoas == 0) {
                qtPessoas = 2.0;
            }

            double resultado = Double.parseDouble(edtValor.getText().toString());
            resultado = (resultado / qtPessoas);
            DecimalFormat df = new DecimalFormat("#.00");
            tvResultado.setText("R$" + df.format(resultado));
        } catch (Exception e) {
            tvResultado.setText("R$ 0.00");
        }

    }

    @Override
    public void onClick(View view) {
        if (view == share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.texto_informativo_valor) + tvResultado.getText().toString());
            startActivity(intent);

        }
        if (view == tocar) {
            if (ttsPlayer != null) {
                ttsPlayer.speak(getString(R.string.texto_informativo_valor) + tvResultado.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }

        }
    }

    @Override
    public void onInit(int initStatus) {
        //checando inicialização
        if (initStatus == TextToSpeech.SUCCESS) {
            Toast.makeText(this, getString(R.string.aviso_TTS_ativado), Toast.LENGTH_LONG).show();
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Se, TTS habilitado...", Toast.LENGTH_LONG).show();
        }
    }
}