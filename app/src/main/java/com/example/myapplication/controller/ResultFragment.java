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

        // Retrieve the correct winning car
        int winningCar = DataUtils.getInstance().getWinCar();

        // Ensure we have a valid winning car
        if (winningCar == -1) {
            winnerTextView.setText("No winner detected");
            winningsTextView.setText("0 VND");
            AudioMixer.getInstance().playAudio(AudioStage.LOSE, getContext());
        } else {
            // Calculate and update winnings correctly
            double winnings = BetService.getInstance().calculateBet(winningCar);
            double newBalance = DataUtils.getInstance().getCurrentUser().getCash() + winnings;
            DataUtils.getInstance().getCurrentUser().setCash(newBalance);

            // Display the correct winner and winnings
            winnerTextView.setText("Car " + (winningCar + 1) + " Won!");
            winningsTextView.setText(winnings > 0 ? "+" + winnings + " VND" : "0 VND");

            // Play the appropriate sound
            if (winnings > 0) {
                AudioMixer.getInstance().playAudio(AudioStage.WIN, getContext());
            } else {
                AudioMixer.getInstance().playAudio(AudioStage.LOSE, getContext());
            }
        }

        // Reset the winCar after displaying results
        DataUtils.getInstance().setWinCar(-1);

        backTextView.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, homeFragment)
                    .commit();
        });

        return view;
    }
}
