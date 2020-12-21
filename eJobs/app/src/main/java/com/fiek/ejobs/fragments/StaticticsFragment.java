package com.fiek.ejobs.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.LegendLayout;
import com.fiek.ejobs.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaticticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaticticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AnyChartView pieChartView,columnChartView,differentChartView,column1ChartView,pie1ChartView;
    FirebaseFirestore db;

    String[] econimicSectors={"Tregti","Ndertimtari","Prodhim","Arsim","Bujqësi","Administrim publik","Gastronomi","Informim dhe komunikim","Tjera"};
    double[] persetige={17.0,12.6,11.9,10.0,5.2,6.6,6.4,3.8,27.1};

    String[] genders={"Mashkull","Femer","Gjithsej nga popullata"};
    double[] values={46.2,13.9,30.1};
    double[] values1={22.6,34.4,25.7};

    String[] category={"Terciar","Shkollë e mesme gjimnazi","Arsim i mesëm profesional","Fillor","Pa shkollë"};
    double[] valuesInPersentige={28.5,12.8,43.6,14.6,0.5};
    double[] valuesInPersentige1={22.7,13.8,42.8,20.2,0.5};
    String[] palette={"#ffcc80", "#ffab91", "#f8bbd0"};

    public StaticticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaticticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaticticsFragment newInstance(String param1, String param2) {
        StaticticsFragment fragment = new StaticticsFragment();
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
        db= FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statictics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChartView=view.findViewById(R.id.pieChartView);
        columnChartView=view.findViewById(R.id.columnChartView);
        differentChartView=view.findViewById(R.id.differentChartView);
        column1ChartView=view.findViewById(R.id.column1ChartView);
        pie1ChartView=view.findViewById(R.id.pie1ChartView);

        pieChartView.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_from_bottom));
        columnChartView.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_from_bottom));
        differentChartView.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_from_bottom));
        column1ChartView.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_from_bottom));
        pie1ChartView.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_from_bottom));
        pieChartMethod();
    }

    public void pieChartMethod(){
        //Sektorët ekonomik të punësimit
        APIlib.getInstance().setActiveAnyChartView(pieChartView);
        Pie pieChart= AnyChart.pie();
        List<DataEntry> dataEntries=new ArrayList<>();

        for (int i=0;i<econimicSectors.length;i++){
            dataEntries.add(new ValueDataEntry(econimicSectors[i],persetige[i]));
        }
        pieChart.data(dataEntries);
        pieChart.title("Sektorët ekonomik të punësimit");
        pieChart.labels().position("outside");
        pieChart.legend().position("top").itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE);
        pieChartView.setChart(pieChart);

        //Punësimi sipas gjinisë
        APIlib.getInstance().setActiveAnyChartView(columnChartView);
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data=new ArrayList<>();

        for (int i=0;i<genders.length;i++){
            data.add(new ValueDataEntry(genders[i],values[i]));
        }
        Column column = cartesian.column(data);
        cartesian.animation(true);
        cartesian.title("Punësimi sipas gjinisë");
        cartesian.labels().padding().bottom(15d);
        cartesian.palette(palette);
        columnChartView.setChart(cartesian);

        //Papunësimi sipas gjinisë
        APIlib.getInstance().setActiveAnyChartView(column1ChartView);
        Cartesian cartesian1 = AnyChart.column();
        List<DataEntry> data1=new ArrayList<>();

        for (int i=0;i<genders.length;i++){
            data1.add(new ValueDataEntry(genders[i],values1[i]));
        }
        Column column1 = cartesian1.column(data1);
        cartesian1.animation(true);
        cartesian1.title("Papunësimi sipas gjinisë");
        cartesian1.labels().padding().bottom(15d);
        column1ChartView.setChart(cartesian1);

        //Punësimi sipas nivelit arsimor
        APIlib.getInstance().setActiveAnyChartView(differentChartView);
        Pie pieChart1= AnyChart.pie();
        List<DataEntry> dataEntryList=new ArrayList<>();

        for (int i=0;i<category.length;i++){
            dataEntryList.add(new ValueDataEntry(category[i],valuesInPersentige[i]));
        }
        pieChart1.data(dataEntryList);
        pieChart1.title("Punësimi sipas nivelit arsimor");
        pieChart1.labels().position("outside");
        pieChart1.legend().position("top").itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE);
        differentChartView.setChart(pieChart1);

        //Papunësimi sipas nivelit arsimor
        APIlib.getInstance().setActiveAnyChartView(pie1ChartView);
        Pie pieChart2= AnyChart.pie();
        List<DataEntry> dataEntryArrayList=new ArrayList<>();

        for (int i=0;i<category.length;i++){
            dataEntryArrayList.add(new ValueDataEntry(category[i],valuesInPersentige1[i]));
        }
        pieChart2.data(dataEntryArrayList);
        pieChart2.title("Papunësimi sipas nivelit arsimor");
        pieChart2.labels().position("outside");
        pieChart2.legend().position("top").itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE);
        pie1ChartView.setChart(pieChart2);

    }
}
