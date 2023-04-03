package com.android.translator_hin_eng;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_1;
    TextView txt;
    Spinner txt_lan_1, txt_lan_2;
    Translator AtoBTranslator;
    ClipboardManager clipboard;
    ClipData clip;
    MDToast mdToast;
    Boolean flag = true;
    Dialog dialog;
    TranslatorOptions options;
    String[] langs;
    String[] langCodes;
    int input_index, output_index;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        langs = new String[]{"ENGLISH","POLISH","SPANISH","FRENCH","GERMAN","ITALIAN","RUSSIAN","JAPANESE"};
        langCodes = new String[]{"en","pl","es","fr","de","it","ru","ja"};
        setContentView(R.layout.activity_main);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, langs);
        et_1 = findViewById(R.id.et_1);
        txt = findViewById(R.id.txt);
        txt_lan_1 = findViewById(R.id.txt_lan_1);
        txt_lan_2 = findViewById(R.id.txt_lan_2);
        txt_lan_1.setAdapter(adapter);
        txt_lan_2.setAdapter(adapter);
        input_index = txt_lan_1.getSelectedItemPosition();
        output_index = txt_lan_2.getSelectedItemPosition();
        findViewById(R.id.swap).setOnClickListener(this);
        findViewById(R.id.mic).setOnClickListener(this);
        findViewById(R.id.cp_1).setOnClickListener(this);
        findViewById(R.id.cp_2).setOnClickListener(this);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        dialog = new Dialog(MainActivity.this, android.R.style.Theme_Dialog);
        open_dialog();

        et_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
               translate_eng(et_1.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        // Create an English-Polish translator:
          /*options = new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.POLISH)
                        .build();

           options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.POLISH)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();*/

        txt_lan_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                input_index = arg2;
                String input_lang = langCodes[input_index];
                String output_lang = langCodes[output_index];
                options = new TranslatorOptions.Builder()
                        .setSourceLanguage(input_lang)
                        .setTargetLanguage(output_lang)
                        .build();

                AtoBTranslator = Translation.getClient(options);

                DownloadConditions conditions = new DownloadConditions.Builder().build();
                AtoBTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener (
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void v) {
                                        // Model downloaded successfully. Okay to start translating.
                                        //download_pol_eng();

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be downloaded or other internal error.
                                        txt.setText(e.getMessage());
                                    }
                                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txt_lan_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                output_index = arg2;
                String input_lang = langCodes[input_index];
                String output_lang = langCodes[output_index];
                options = new TranslatorOptions.Builder()
                        .setSourceLanguage(input_lang)
                        .setTargetLanguage(output_lang)
                        .build();

                AtoBTranslator = Translation.getClient(options);

                DownloadConditions conditions = new DownloadConditions.Builder().build();
                AtoBTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener (
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void v) {
                                        // Model downloaded successfully. Okay to start translating.
                                        //download_pol_eng();

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be downloaded or other internal error.
                                        txt.setText(e.getMessage());
                                    }
                                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    /*void download_pol_eng() {
        polishEnglishTranslator = Translation.getClient(options_2);
        polishEnglishTranslator.downloadModelIfNeeded()
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
//                                findViewById(R.id.btn).setEnabled(true);
//                                Toast.makeText(getApplicationContext(),"Second done",Toast.LENGTH_SHORT).show();
                                txt.setText(null);
                                dialog.dismiss();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                                txt.setText(e.getMessage());
                            }
                        });
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mic:
                voice();
                break;

            case R.id.swap:
                swap();
                break;

            case R.id.cp_1:
                try {
                    copy(et_1.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.cp_2:
                try {
                    copy(txt.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    void toast(String message,int type){
        mdToast = MDToast.makeText(getApplicationContext(), message, MDToast.LENGTH_SHORT, type);
        mdToast.show();
    }

    void voice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        /*if (flag)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        else intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"pl");*/
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, langCodes[input_index]);
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException a) {
            toast("Intent Problem",3);
        }
    }

    public void open_dialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setCancelable(false);
        dialog.show();
    }

    void swap() {
        int a = txt_lan_1.getSelectedItemPosition();
        int b = txt_lan_2.getSelectedItemPosition();
        txt_lan_1.setSelection(b);
        txt_lan_2.setSelection(a);
        if (flag)
            flag = false;
        else flag = true;
        et_1.setText(null);
        txt.setText(null);
        //toast("Language Changed",1);
    }

    void copy(String text) {
        if (!text.equals("")) {
            clip = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(clip);
            toast("Text Copied",1);
        } else {
            toast("Text Copied",2);
//            mdToast = MDToast.makeText(getApplicationContext(), "There is no text", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        }
        mdToast.show();
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                assert result != null;
                et_1.setText(result.get(0));

               translate_eng(et_1.getText().toString().trim());

            }
        }
    }

    /*void translate_pol(String text) {
        englishPolishTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                // Translation successful.
                                txt.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                                // ...
                                txt.setText(e.getMessage());
                            }
                        });
    }*/

    void translate_eng(String text) {
        AtoBTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                // Translation successful.
                                txt.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                                // ...
                                txt.setText(e.getMessage());
                            }
                        });
    }

}