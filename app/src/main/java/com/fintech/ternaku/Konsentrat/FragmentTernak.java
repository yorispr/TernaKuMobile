package com.fintech.ternaku.Konsentrat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.EditTernakActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTernak.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTernak#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTernak extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterautocomplete;
    ArrayList<String>arrayTernak = new ArrayList<>();
    ArrayList<String> arrayIDTernakAutoComplete = new ArrayList<>();
    boolean isSelesai = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public FragmentTernak() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTernak.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTernak newInstance(String param1, String param2) {
        FragmentTernak fragment = new FragmentTernak();
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
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayTernak);
        adapterautocomplete = new ArrayAdapter<String>
                (getContext(),android.R.layout.simple_list_item_1,arrayIDTernakAutoComplete);

        if(isSelesai) {
            String param = "idpeternakan=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
            new GetIDTernak().execute("http://developer.ternaku.com/C_HistoryKonsentrat/getIDTernakByPeternakan", param);
        }
        ((InsertKonsetratPerSapi) getActivity()).hideFab();
    }

    public void setSelesai(){
        isSelesai = true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_ternak, container, false);

        if(isSelesai) {
            Button btnSelesai = (Button) view.findViewById(R.id.btnSelesai);

            ListView listternak = (ListView) view.findViewById(R.id.listTernak);
            final AutoCompleteTextView edtIDternak = (AutoCompleteTextView) view.findViewById(R.id.edtIDTernak);
            listternak.setAdapter(adapter);
            edtIDternak.setAdapter(adapterautocomplete);

            edtIDternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!cekList(arrayIDTernakAutoComplete.get(position))) {
                        arrayTernak.add(arrayIDTernakAutoComplete.get(position));
                        adapter.notifyDataSetChanged();
                        edtIDternak.setText("");

                    }else{
                        Toast.makeText(getContext(),"Ternak Sudah ditambahkan",Toast.LENGTH_SHORT).show();
                        edtIDternak.setText("");

                    }
                }
            });

            btnSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelesai = false;
                    ((InsertKonsetratPerSapi) getActivity()).hideFragment();
                    ((InsertKonsetratPerSapi) getActivity()).showFab();

                }
            });
            listternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Hapus Ternak")
                            .setMessage("Apakah anda ingin menghapus ternak ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    arrayTernak.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

        }
        return view;
    }

    public boolean cekList(String search){
        for(String str: arrayTernak) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public ArrayList<String> getArrayTernak(){
        return arrayTernak;
    }

    private class GetIDTernak extends AsyncTask<String, Integer, String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Mengambil Data");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES_Insert", result);
            try{
                JSONArray jsonArray = new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jobj = jsonArray.getJSONObject(i);
                    arrayIDTernakAutoComplete.add(jobj.getString("id_ternak"));
                }
                adapterautocomplete.notifyDataSetChanged();
            }catch(JSONException je){je.printStackTrace();}
            pDialog.dismiss();

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
}
