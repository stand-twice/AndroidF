package gg.rubit.adapters;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import gg.rubit.R;
import gg.rubit.api.response.Partida;

public class ResumenListViewAdapter extends ArrayAdapter<Partida> {

    List<Partida> partidas = new ArrayList<>();

    public ResumenListViewAdapter(Context context, List<Partida> datos){
        super(context, R.layout.listview_resumen,datos);
        partidas = datos;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public View getView(int position, View v, ViewGroup vg){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listview_resumen, null);

        TextView lblPregunta = (TextView)item.findViewById(R.id.lslblPregunta);
        lblPregunta.setText(partidas.get(position).getPregunta());

        TextView lblRespuesta = (TextView)item.findViewById(R.id.lslblRespuesta);
        lblRespuesta.setText(partidas.get(position).getRespuestas());

        TextView lblPuntaje = (TextView)item.findViewById(R.id.lslblPuntaje);
        lblPuntaje.setText(Integer.toString(partidas.get(position).getPuntaje()));

        TextView lblHora = (TextView)item.findViewById(R.id.lslblHora);
        lblHora.setText(partidas.get(position).getHora());

        return item;
    }
}
