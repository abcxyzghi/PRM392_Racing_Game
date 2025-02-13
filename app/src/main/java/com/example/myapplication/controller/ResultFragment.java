package com.example.myapplication.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.constant.AudioStage;
import com.example.myapplication.model.Bet;
import com.example.myapplication.service.AudioMixer;
import com.example.myapplication.service.BetService;
import com.example.myapplication.utils.DataUtils;

public class ResultFragment extends Fragment {

    private TextView winnerTextView, winningsTextView, backTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_layout, container, false);

        winnerTextView = view.findViewById(R.id.tvWinner);
        winningsTextView = view.findViewById(R.id.tvWinnings);
        backTextView = view.findViewById(R.id.tvBack);

        // Lấy xe thắng cuộc
        int winningCar = DataUtils.getInstance().getWinCar();
        double netWinnings = BetService.getInstance().calculateBet(winningCar); // Lấy lợi nhuận thực tế

        // Cập nhật số dư người chơi
        double newBalance = DataUtils.getInstance().getCurrentUser().getCash() + netWinnings;
        DataUtils.getInstance().getCurrentUser().setCash(newBalance);

        // Hiển thị kết quả
        if (winningCar == -1) {
            winnerTextView.setText("No winner detected");
            winningsTextView.setText("0 VND");
            AudioMixer.getInstance().playAudio(AudioStage.LOSE, getContext());
        } else {
            winnerTextView.setText("Car " + (winningCar + 1) + " Won!");
            winningsTextView.setText((netWinnings >= 0 ? "+" : "-") + Math.abs(netWinnings) + " VND");
            if (netWinnings > 0) {
                AudioMixer.getInstance().playAudio(AudioStage.WIN, getContext());
            } else {
                AudioMixer.getInstance().playAudio(AudioStage.LOSE, getContext());
            }
        }

        // Reset lại xe thắng cuộc
        DataUtils.getInstance().setWinCar(-1);

        // Xử lý nút quay về màn hình chính
        backTextView.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            BetService.getInstance().betReset();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, homeFragment)
                    .commit();
        });

        return view;
    }
}
