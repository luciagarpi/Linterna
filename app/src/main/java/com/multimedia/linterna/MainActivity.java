package com.multimedia.linterna;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    //Código del permiso
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 23;
    private ToggleButton toggleButton_encender_apagar;
    private CameraManager camManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton_encender_apagar = (ToggleButton) findViewById(R.id.toggleButton_encender_apagar);

        //Al pulsar el toggleButton
        toggleButton_encender_apagar.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    comprobarPermisos();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }//Fin onCreate

    /**
     * Método que comprueba si los permisos son denegados o aceptados
     * @throws CameraAccessException
     */
    public void comprobarPermisos() throws CameraAccessException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            Toast.makeText(this, "Esta versión no es Android 6 o susperior." + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
        } else {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);

            //Lanza cuadro para aceptar o denegra permisos, llama por si solo al método onRequestPermissionsResult
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
            //Si los permisos ya han sido concedidos
            } else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){
               encender_apagar();

            }
        }
    }//comprobarPermisos



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_LONG).show();
                try {
                    encender_apagar();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }//Fin onRequestPermissionsResult

    /**
     * Método que comprueba si el botón está pulsado o no
     * @throws CameraAccessException
     */
    public void encender_apagar() throws CameraAccessException {
        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //El 0 es la cámara externa, el 1 la interna
        String cameraId = camManager.getCameraIdList()[0];
        //Si está pulsado
        if(toggleButton_encender_apagar.isChecked()) {
            //Se enciende el flash externo
            camManager.setTorchMode(cameraId, true);
        } else { //Sino
            //Se apaga
            camManager.setTorchMode(cameraId, false);
        }
    }//Fin encender_apagar

}//Fin clase MainActivity
