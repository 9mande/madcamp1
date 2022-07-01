package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class fragment_phonebook extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    private RecyclerView recyclerView;
    private RVAdapter_phonebook rvAdapter;
    private final int REQUEST_CONTACT = 10;
    public List<PhoneNumberVO> lstPhonebook = new ArrayList<PhoneNumberVO>();
    public Object PhoneNumberVO;

    Dialog mDialog;

    private String mParam1;
    private String mParam2;

    public fragment_phonebook() {
        // Required empty public constructor
    }

    public static fragment_phonebook newInstance(String param1, String param2) {
        fragment_phonebook fragment = new fragment_phonebook();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_phonebook, container, false);

        recyclerView = rootView.findViewById(R.id.contact_recyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), 1));
        rvAdapter = new RVAdapter_phonebook(getContext(), lstPhonebook);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(rvAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mDialog = new Dialog(rootView.getContext());
        mDialog.setContentView(R.layout.add_phonebook);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText editText = rootView.findViewById(R.id.searchPhonebook);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rvAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You can add a new address", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mDialog.show();

                Button can_btn = (Button) mDialog.findViewById(R.id.button);
                Button ok_btn = (Button) mDialog.findViewById(R.id.button2);

                final EditText nameT = (EditText) mDialog.findViewById(R.id.editTextTextPersonName);
                final EditText phoneT = (EditText) mDialog.findViewById(R.id.editTextTextPersonName2);

                can_btn.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });

                ok_btn.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameT.getText().toString();
                        String phone = phoneT.getText().toString();

                        PhoneNumberVO phoneNumberVO = new PhoneNumberVO(name, phone);
                        lstPhonebook.add(phoneNumberVO);
                        rvAdapter.notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });

            }
        });

        FloatingActionButton fab2 = rootView.findViewById(R.id.fab_connect);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CONTACT);
            }
        });


        return rootView;

    }
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            lstPhonebook.remove(position);
            rvAdapter.notifyItemRemoved(position);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK) {
            Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                    new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER},
                    null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);
            String phone = cursor.getString(1);
            cursor.close();

            PhoneNumberVO phoneNumberVO = new PhoneNumberVO(name, phone);
            lstPhonebook.add(phoneNumberVO);

            rvAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
        }
    }


    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        rvAdapter.getFilter().filter(s.toString());
    }


    public void afterTextChanged(Editable editable) {

    }

}