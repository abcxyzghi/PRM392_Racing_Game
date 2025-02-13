package com.example.myapplication.controller;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.constant.AudioStage;
import com.example.myapplication.model.Bet;
import com.example.myapplication.service.AudioMixer;
import com.example.myapplication.service.BetService;
import com.example.myapplication.utils.DataUtils;
import com.example.myapplication.utils.RandomnessUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private CheckBox car1, car2, car3;
    private EditText car1Text, car2Text, car3Text;
    private SeekBar sbcar1, sbcar2, sbcar3;
    private EditText betAmountEditText;
    private TextView cashTextView;
    private TextView balanceText;
    private final List<Handler> handlers = new ArrayList<>();
    private final List<Runnable> runnables = new ArrayList<>();
    private final List<Boolean> finished = new ArrayList<>();
    private double userBalance = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        // Init objects
        car1 = view.findViewById(R.id.car1);
        car2 = view.findViewById(R.id.car2);
        car3 = view.findViewById(R.id.car3);
        car1Text = view.findViewById(R.id.car1Text);
        car2Text = view.findViewById(R.id.car2Text);
        car3Text = view.findViewById(R.id.car3Text);
        sbcar1 = view.findViewById(R.id.seekBar);
        sbcar2 = view.findViewById(R.id.seekBar2);
        sbcar3 = view.findViewById(R.id.seekBar3);

        sbcar1.setEnabled(false);
        sbcar2.setEnabled(false);
        sbcar3.setEnabled(false);

        betAmountEditText = view.findViewById(R.id.betAmount);
        balanceText = view.findViewById(R.id.editTextNumber3);
        cashTextView = view.findViewById(R.id.tvCash);
        TextView goTextView = view.findViewById(R.id.tvGo);

        // Disable edit texts initially
        car1Text.setEnabled(false);
        car2Text.setEnabled(false);
        car3Text.setEnabled(false);

        // Add event listeners
        initEventClickChange();
        initEventBetChange(car1Text, 1);
        initEventBetChange(car2Text, 2);
        initEventBetChange(car3Text, 3);

        AudioMixer.getInstance().playAudio(AudioStage.HOME, getContext());

        initRaceHandlers();
        cashTextView.setOnClickListener(v -> {
            CashFragment cashFragment = new CashFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.container, cashFragment)
                    .addToBackStack(null)
                    .commit();
        });

        goTextView.setOnClickListener(v -> {
            if (BetService.getInstance().validateBet()) {
                double betAmount = Double.parseDouble(betAmountEditText.getText().toString());
                if (DataUtils.getInstance().getCurrentUser().getCash() < betAmount) {
                    Toast.makeText(getActivity(), "Insufficient balance!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    AudioMixer.getInstance().playAudio(AudioStage.BEGIN, getContext());
                    startRace();
                }
            } else {
                Toast.makeText(getActivity(), "Please select a car and enter a valid bet amount", Toast.LENGTH_SHORT).show();
            }
        });
        updateBetPrice ();
        balanceText.setText(String.valueOf(DataUtils.getInstance().getCurrentUser().getCash()));
        return view;
    }

    private void initEventBetChange(EditText editText, int carId) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (((CheckBox) editText.getTag()).isChecked()) {
                    String newData = s.toString();

                    if (!newData.isEmpty()) {
                        double betAmount = Double.parseDouble(editText.getText().toString());
                        Bet bet = Bet.builder()
                                .carId(carId)
                                .username(DataUtils.getInstance().getCurrentUser().getUsername())
                                .bet(betAmount)
                                .build();
                        BetService.getInstance().addBet(bet);
                        balanceText.setText(String.valueOf(DataUtils.getInstance().getCurrentUser().getCash()));
                    }
                }
                updateBetPrice ();
            }
        });
        editText.setTag(getCorrespondingCheckBox(editText));
    }

    private void initEventClickChange() {
        car1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handlecarSelection(isChecked, car1Text, 1);
            updateBetPrice ();
        });

        car2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handlecarSelection(isChecked, car2Text, 2);
            updateBetPrice ();
        });

        car3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handlecarSelection(isChecked, car3Text, 3);
            updateBetPrice ();
        });
    }

    private void handlecarSelection(boolean isChecked, EditText carText, int carId) {
        if (isChecked) {
            carText.setEnabled(true);
            Bet temp = Bet.builder()
                    .carId(carId)
                    .bet(0)
                    .build();
            BetService.getInstance().addBet(temp);
        } else {
            BetService.getInstance().betReset();
            carText.setEnabled(false);
            carText.setText("");
        }
    }

    private CheckBox getCorrespondingCheckBox(EditText editText) {
        if (editText == car1Text) {
            return car1;
        } else if (editText == car2Text) {
            return car2;
        } else if (editText == car3Text) {
            return car3;
        }
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateBetPrice();
    }

    private void updateBetPrice () {
        betAmountEditText.setText(BetService.getInstance().totalBet().toString());
    }

    private void startRace() {
        // Reset the previous winner before a new race
        DataUtils.getInstance().setWinCar(-1);

        stopRace();
        resetRace();
        for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).post(runnables.get(i));
        }
    }

    private void initRaceHandlers() {
        SeekBar[] seekBars = {sbcar1, sbcar2, sbcar3};

        for (int i = 0; i < seekBars.length; i++) {
            int finalI = i;
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                int progress = 0;

                @Override
                public void run() {
                    int increment = RandomnessUtil.getRandomSpeed();
                    progress += increment;
                    seekBars[finalI].setProgress(progress);

                    if (progress < 100) {
                        handler.postDelayed(this, 100);
                    } else {
                        finished.set(finalI, true);
                        if (DataUtils.getInstance().getWinCar() == -1) {
                            DataUtils.getInstance().setWinCar(finalI);
                        }
                        if (!finished.contains(false)) {
                            announceWinner();
                        }
                    }
                }
            };
            handlers.add(handler);
            runnables.add(runnable);
            finished.add(false);
        }
    }

    private void announceWinner() {

        double winnings = BetService.getInstance().calculateBet(DataUtils.getInstance().getWinCar());

        userBalance += winnings;

        requireActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.container, new ResultFragment())
                .addToBackStack(null)
                .commit();
        resetRace();

    }

    private void resetRace() {
        sbcar1.setProgress(0);
        sbcar2.setProgress(0);
        sbcar3.setProgress(0);
        stopRace();
    }

    private void stopRace() {
        for (Handler handler : handlers) {
            handler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}