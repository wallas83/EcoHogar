package grupo11.ecohogar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText usuario;
    EditText contrasenia;
    Button ingresar;


    String IP = "http://h2ogar.esy.es/";
    String GET_BY_ID= IP + "obtener_cliente_por_usuario.php";
    ObtenerWebService hiloconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario=(EditText)findViewById(R.id.edtUsuario);
        contrasenia=(EditText)findViewById(R.id.edtContrasenia);
        ingresar=(Button)findViewById(R.id.btnIngresar);
        ingresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIngresar:
                hiloconexion = new ObtenerWebService();
                String cadenallamada = GET_BY_ID + "?usuario=" + usuario.getText().toString();
                String pass = contrasenia.getText().toString();
                hiloconexion.execute(cadenallamada,pass);
                break;
        }
    }
    public class ObtenerWebService extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String sw = "0";


            try {
                url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                //connection.setHeader("content-type", "application/json");

                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK){


                    InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                    // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                    // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                    // StringBuilder.

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);        // Paso toda la entrada al StringBuilder
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                    if (resultJSON=="1") {      // hay un cliente que mostrar
                        if (params[1] .equals( respuestaJSON.getJSONObject("cliente").getString("password"))) {
                            sw = respuestaJSON.getJSONObject("cliente").getString("nombres");
                        }

                    }
                    else if (resultJSON=="2"){

                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sw;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("0"))
            {
                Toast toast= Toast.makeText(getApplicationContext(),"El usuario o la contraseña son incorrectas",Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                Intent intent = new Intent(MainActivity.this, Vistas.class);
                Toast toast= Toast.makeText(getApplicationContext(),"Bienvenido "+s,Toast.LENGTH_LONG);
                toast.show();
                startActivity(intent);
                finish();
            }
            //super.onPostExecute(s);
        }
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
    }

}
