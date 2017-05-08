package grupo11.ecohogar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaViviendas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaViviendas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaViviendas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ListView lista;
    ArrayList<Vivienda>Al;
    ItemAdaptador adaptador;
    Activity activity;

    ObtenerWebService hiloconexion;

   // String IP="http://h2ogar.esy.es";
//    String GET=IP +"obtener_nombre_de_zona_por_id_cliente.php";

    private OnFragmentInteractionListener mListener;

    public ListaViviendas() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ListaViviendas newInstance(String param1, String param2) {
        ListaViviendas fragment = new ListaViviendas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_lista_viviendas,container,false);
        activity=getActivity();
        lista =(ListView)view.findViewById(R.id.lvListaviviendas);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Al=new ArrayList<Vivienda>();

        //hiloconexion=new ObtenerWebService();
       // hiloconexion.execute(GET);

        //Al=hiloconexion.devuelve();


        //adaptador=new ItemAdaptador(activity,Al);
        //lista.setAdapter(adaptador);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ObtenerWebService extends AsyncTask<String,Void,ArrayList<String>>
    {
        ArrayList<Vivienda> dev = new ArrayList<>();
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String cadena = params[0];
            URL url = null;
            ArrayList<String> devuelve = new ArrayList<>();
            try {
                url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                //connection.setHeader("content-type", "application/json");

                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK) {


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

                    String resultJSON = respuestaJSON.getString("estado");// estado es el nombre del campo en el JSON

                    if (resultJSON.equals("1")){      // hay un alumno que mostrar
                        JSONArray productosJSON = respuestaJSON.getJSONArray("vivienda");   // estado es el nombre del campo en el JSON
                        for(int i=0;i<productosJSON.length();i++){
                            devuelve.add(productosJSON.getJSONObject(i).getString("id"));
                            devuelve.add(productosJSON.getJSONObject(i).getString("nombre"));
                        }
                    }
                    else if (resultJSON.equals("2")){
                        devuelve.add("0");
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return devuelve;
        }

        @Override
        protected void onCancelled(ArrayList<String> s) {
            super.onCancelled(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if(!s.get(0).equals("0"))
            {
                llenar(s);
            }
            else {
                //Toast toast= Toast.makeText(getApplicationContext(),"nada",Toast.LENGTH_SHORT);
                //toast.show();
            }
            //super.onPostExecute(s);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        public void llenar(ArrayList<String>a)
        {
            for (int i =0;i<a.size();i+=4)
            {
                Vivienda vivienda= new Vivienda(a.get(i+1),Long.parseLong(a.get(i)));
                dev.add(vivienda);
            }
        }
        public ArrayList<Vivienda> devuelve()
        {
            return dev;
        }
    }
}
