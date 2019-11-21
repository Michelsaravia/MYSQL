package com.example.mysql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText et_codigo, et_descripcion, et_precio;
    private Button btn_guardar, btn_consultaCodigo, btn_consultaDescripcion, btn_eliminar, btn_actualizar;

    boolean inputEt=false;
    boolean inputEd=false;
    boolean input1=false;
    int resultadoInsert=0;
    final Context context = this;

    String senal = "";
    String codigo = "";
    String descripcion = "";
    String precio = "";

    MantenimientoMySQL manto = new MantenimientoMySQL();
    Dto datos = new Dto();

    //Banderas para saber estados de métodos del CRUD.
    boolean estadoGuarda = false;
    boolean estadoEliminar = false;

    AlertDialog.Builder dialogo;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_close)
                    .setTitle("Advertencia")
                    .setMessage("¿Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finishAffinity();
                        }
                    })
                    .show();
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ///y esto para pantalla completa (oculta incluso la barra de estado)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        et_codigo = (EditText) findViewById(R.id.et_codigo);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        et_precio = (EditText) findViewById(R.id.et_precio);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_consultaCodigo = (Button) findViewById(R.id.btn_consultaCodigo);
        btn_consultaDescripcion = (Button) findViewById(R.id.btn_consultaDescripcion);
        btn_eliminar = (Button) findViewById(R.id.btn_eliminar);
        btn_actualizar = (Button) findViewById(R.id.btn_actualizar);
        //tv_resultado = (TextView) findViewById(R.id.tv_resultado);




        /******************************************************************/
        //BLOQUE DE CÓDIGO PARA MOSTRAR DATOS DE LA BUSQUEDA//
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                senal = bundle.getString("senal");
                codigo = bundle.getString("codigo");
                descripcion = bundle.getString("descripcion");
                precio = bundle.getString("precio");
                if (senal.equals("1")) {
                    et_codigo.setText(codigo);
                    et_descripcion.setText(descripcion);
                    et_precio.setText(precio);
                    //finish();
                }else if(senal.equals("2")){

                }
            }
        }catch (Exception e){

        }

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_codigo.getText().toString().length()==0){
                    et_codigo.setError("Campo obligatorio");
                    inputEt = false;
                }else {
                    inputEt=true;
                }
                if(et_descripcion.getText().toString().length()==0){
                    et_descripcion.setError("Campo obligatorio");
                    inputEd = false;
                }else {
                    inputEd=true;
                }
                if(et_precio.getText().toString().length()==0){
                    et_precio.setError("Campo obligatorio");
                    input1 = false;
                }else {
                    input1=true;
                }

                if (inputEt && inputEd && input1){
                    String codigo = et_codigo.getText().toString();
                    String descripcion = et_descripcion.getText().toString();
                    String precio = et_precio.getText().toString();
                    manto.guardar(MainActivity.this, codigo, descripcion, precio);

                    limpiarDatos();
                    et_codigo.requestFocus();

                    /*
                    estadoGuarda = manto.guardar1(MainActivity.this, codigo, descripcion, precio);
                    if(estadoGuarda){
                        Toast.makeText(MainActivity.this, "Registro Almacenado Correctamente.", Toast.LENGTH_SHORT).show();
                        limpiarDatos();
                        et_codigo.requestFocus();
                    }*/

                }


            }
        });


        //Evento clic del botón eliminar.
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_codigo.getText().toString().length()==0){
                    et_codigo.setError("campo obligatorio");
                    inputEt = false;
                }else {
                    inputEt=true;
                }

                if(inputEt){
                    String codigo = et_codigo.getText().toString();
                    manto.eliminar(MainActivity.this, codigo);

                    limpiarDatos();
                    et_codigo.requestFocus();
                    /*
                    if(estadoEliminar){
                        Toast.makeText(MainActivity.this, "Registro Eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        limpiarDatos();
                    }else{
                         Toast toast = Toast.makeText(getApplicationContext(), "--> Nothing." +
                                        "\nNo hay información que eliminar.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                        limpiarDatos();
                    }*/
                }
            }
        });

        btn_consultaCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Begin...
                if(et_codigo.getText().toString().length()==0){
                    et_codigo.setError("campo obligatorio");
                    inputEt = false;
                }else {
                    inputEt=true;
                }

                if(inputEt) {
                    String codigo = et_codigo.getText().toString();
                    manto.consultarCodigo(MainActivity.this, codigo);
                    et_codigo.requestFocus();
                }
                //End

            }
        });



        btn_consultaDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_descripcion.getText().toString().length()==0){
                    et_descripcion.setError("Campo obligatorio");
                    inputEd = false;
                }else {
                    inputEd=true;
                }
                if(inputEd){
                    String descripcion = et_descripcion.getText().toString();
                    //datos.setDescripcion(descripcion);
                    manto.consultarDescripcion(MainActivity.this, descripcion);
                    et_descripcion.requestFocus();
                    //Hilo();

                }

            }
        });


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_codigo.getText().toString().length()==0){
                    et_codigo.setError("campo obligatorio");
                    inputEt = false;
                }else {
                    inputEt=true;
                }

                if(inputEt) {

                    String cod = et_codigo.getText().toString();
                    String descripcion = et_descripcion.getText().toString();
                    String precio = et_precio.getText().toString();

                    datos.setCodigo(Integer.parseInt(cod));
                    datos.setDescripcion(descripcion);
                    datos.setPrecio(Double.parseDouble(precio));
                    manto.modificar(MainActivity.this, datos);
                    limpiarDatos();
                    et_codigo.requestFocus();
                }

            }
        });

    }


    public void limpiarDatos(){
        et_codigo.setText(null);
        et_descripcion.setText(null);
        et_precio.setText(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        int id = item.getItemId();
        if(id == R.id.action_listaArticulos){
            Intent spinnerActivity = new Intent(MainActivity.this, Consulta_RecyclerView.class);
            startActivity(spinnerActivity);
            return true;
        }else if(id == R.id.action_salir){
            DialogConfirmacion();
            return true;
        }

        if (id == R.id.action_Acercade){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            alertDialogBuilder.setTitle("Proyecto creado por:");

                    .setMessage("Pichón  \nSIS 22")
                    .setCancelable(false)
                    .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
