package com.example.alanflores.fragmentapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static class DetalleActivity extends AppCompatActivity{
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                finish();
                return;
            }

            if(savedInstanceState == null){
                DetalleFragmento detalleFragmento = new DetalleFragmento();
                detalleFragmento.setArguments(getIntent().getExtras());
                getFragmentManager().beginTransaction().add(android.R.id.content, detalleFragmento).commit();
            }
        }
    }

    public static class ListaElementosFragment extends ListFragment{
        boolean dualPanel;
        int checkPosition = 0;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, SO.SISTEMASOPERATIVOS));

            View detalleFrame = getActivity().findViewById(R.id.fDetalle);
            dualPanel = detalleFrame != null && detalleFrame.getVisibility() == View.VISIBLE;

            if(savedInstanceState != null){
                checkPosition = savedInstanceState.getInt("choice",0);
            }

            if(dualPanel){
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                showDetail(checkPosition);
            }else{
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                getListView().setItemChecked(checkPosition,true);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("choice",checkPosition);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            showDetail(position);
        }

        void showDetail(int index){
            if(dualPanel){
                getListView().setItemChecked(index, true);
                DetalleFragmento detalleFragmento = (DetalleFragmento) getFragmentManager().findFragmentById(R.id.fDetalle);

                if(detalleFragmento == null || detalleFragmento.getShowIndex() != index){
                    detalleFragmento = DetalleFragmento.newInstance(index);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fDetalle, detalleFragmento);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            }else{
                Intent intent = new Intent();
                intent.setClass(getActivity(), DetalleActivity.class);
                intent.putExtra("index",index);
                startActivity(intent);
            }
        }
    }

    public static class DetalleFragmento extends Fragment{

        public static DetalleFragmento newInstance(int index){
            DetalleFragmento detalleFragmento = new DetalleFragmento();
            Bundle args = new Bundle();
            args.putInt("index", index);
            detalleFragmento.setArguments(args);
            return detalleFragmento;
        }

        public int getShowIndex(){
            return getArguments().getInt("index", 0);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //return super.onCreateView(inflater, container, savedInstanceState);
            ScrollView scrollView = new ScrollView(getActivity());
            TextView textView = new TextView(getActivity());
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,getActivity().getResources().getDisplayMetrics());
            textView.setPadding(padding, padding, padding, padding);
            scrollView.addView(textView);
            textView.setText(SO.DESCRIPTION[getShowIndex()]);
            return scrollView;
        }
    }

}
