package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_me#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_me extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_me() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_me.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_me newInstance(String param1, String param2) {
        fragment_me fragment = new fragment_me();
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

        View vi = inflater.inflate(R.layout.activity_main, container, false);

        /**
         * 버튼
         */
        ImageButton btn1 = (ImageButton) vi.findViewById(R.id.url_button);

//        btn1.setOnClickListener(v -> {
//            Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
//            Sharing_intent.setType("text/plain");
//
//            String Test_Message = "공유할 Text";
//
//            Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message);
//
//            Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
//            startActivity(Sharing);
//        });

        return inflater.inflate(R.layout.fragment_me, container, false);


    }
}