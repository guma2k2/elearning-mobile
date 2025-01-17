package com.example.elearningmobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elearningmobile.R;
import com.example.elearningmobile.activity.LoginActivity;
import com.example.elearningmobile.activity.MainActivity;
import com.example.elearningmobile.activity.OrderActivity;
import com.example.elearningmobile.activity.OrderDetailActivity;
import com.example.elearningmobile.activity.ProfileFormActivity;
import com.example.elearningmobile.model.UserVm;
import com.example.elearningmobile.variable.GlobalVariable;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private Context context;

    private TextView tvHoVaTen, tvNgaySinh, tvGioiTinh;

    private ImageView iv_userImage;

    private Button btnDoiMK, btn_my_order, logout_btn, btnHoVaTen, btnNgaySinh;
    public ProfileFragment(Context context) {
        this.context = context;
    }
    public ProfileFragment() {
        // Required empty public constructor
    }

    private GlobalVariable globalVariable;

    public ProfileFragment(Context context, GlobalVariable globalVariable) {
        this.context = context;
        this.globalVariable = globalVariable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void redirectToOrder() {
        Intent activityChangeIntent = new Intent(context, OrderActivity.class);
        startActivity(activityChangeIntent);
    }

    private void setEvent() {
        if (globalVariable != null) {
            if (globalVariable.isLoggedIn())  {
                if (globalVariable.getAuth() != null) {
                    UserVm userVm = globalVariable.getAuth();
                    tvHoVaTen.setText(userVm.getFirstName() + " " + userVm.getLastName());
                    tvNgaySinh.setText(userVm.getDateOfBirth());
                    tvGioiTinh.setText(userVm.getGender());
                    if (userVm.getPhotoURL() != null && !Objects.equals(userVm.getPhotoURL(), "")) {
                        Picasso.get().load(userVm.getPhotoURL()).into(iv_userImage);
                    }
                }
            } else {
                redirectToLoginPage();
            }
        }

        btnDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToProfileForm();
            }
        });

        btnNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToProfileForm();
            }
        });



        btn_my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToOrder();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalVariable.logOut();
                redirectToHomePage();
            }
        });

        btnHoVaTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToProfileForm();
            }
        });



    }

    private void redirectToProfileForm() {
        Intent activityChangeIntent = new Intent(context, ProfileFormActivity.class);
        this.startActivity(activityChangeIntent);
    }

    private void redirectToLoginPage() {
        Intent activityChangeIntent = new Intent(context, LoginActivity.class);
        this.startActivity(activityChangeIntent);
    }

    private void redirectToHomePage() {
        Intent activityChangeIntent = new Intent(context, MainActivity.class);
        this.startActivity(activityChangeIntent);
    }

    private void setControl(View view) {
        tvHoVaTen = view.findViewById(R.id.tvHoVaTen);
        tvNgaySinh = view.findViewById(R.id.tvNgaySinh);
        tvGioiTinh = view.findViewById(R.id.tvGioiTinh);
        btnDoiMK = view.findViewById(R.id.btnDoiMK);
        btn_my_order = view.findViewById(R.id.btn_my_order);
        logout_btn = view.findViewById(R.id.logout_btn);
        btnHoVaTen = view.findViewById(R.id.btnHoVaTen);
        btnNgaySinh = view.findViewById(R.id.btnNgaySinh);
        iv_userImage = view.findViewById(R.id.iv_userImage);
    }
}