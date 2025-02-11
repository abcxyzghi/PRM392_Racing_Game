package com.example.myapplication.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.constant.AudioStage;
import com.example.myapplication.service.AudioMixer;
import com.example.myapplication.utils.DataUtils;

public class CashFragment extends Fragment {
    private EditText depositAmountEditText;
    private TextView saveTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cash_layout, container, false);
        depositAmountEditText = view.findViewById(R.id.depositAmount);
        saveTextView = view.findViewById(R.id.tvSave);
        AudioMixer.getInstance().playAudio(AudioStage.CASH, getContext());

        saveTextView.setOnClickListener(v -> {
            double depositAmount = Double.parseDouble(depositAmountEditText.getText().toString());
            if (depositAmount > 0) {
                HomeFragment homeFragment = new HomeFragment();
                DataUtils.getInstance().getCurrentUser().setCash(DataUtils.getInstance().getCurrentUser().getCash() + depositAmount);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, homeFragment).commit();
            } else {
                Toast.makeText(getActivity(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
