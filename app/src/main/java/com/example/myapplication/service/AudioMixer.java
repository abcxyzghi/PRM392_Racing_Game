package com.example.myapplication.service;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.myapplication.R;
import com.example.myapplication.constant.AudioStage;

import java.util.EnumMap;
import java.util.Map;

public class AudioMixer {
    private static AudioMixer instance;
    private Map<AudioStage, Integer> mediaPlayerList;
    private MediaPlayer currentPlay;

    private AudioMixer () {
        mediaPlayerList = new EnumMap<>(AudioStage.class);
        mediaPlayerList.put(AudioStage.LOGIN, R.raw.login);
        mediaPlayerList.put(AudioStage.BEGIN, R.raw.game_8bit);
        mediaPlayerList.put(AudioStage.CASH, R.raw.cash);
        mediaPlayerList.put(AudioStage.HOME, R.raw.home);
        mediaPlayerList.put(AudioStage.LOSE, R.raw.lose);
        mediaPlayerList.put(AudioStage.WIN, R.raw.goodresult);
    }
    public static AudioMixer getInstance () {
        if (instance == null) {
            instance = new AudioMixer();
        }
        return instance;
    }

    public void playAudio (AudioStage stage, Context context) {
        if (currentPlay == null) {
            currentPlay = MediaPlayer.create(context, mediaPlayerList.get(stage));
            currentPlay.setLooping(true);
            currentPlay.start();
        } else {
            currentPlay.stop();
            currentPlay.release();
            currentPlay = MediaPlayer.create(context,mediaPlayerList.get(stage));
            currentPlay.setLooping(true);
            currentPlay.start();
        }
    }

    public void pauseAudio () {
        if (currentPlay != null && currentPlay.isPlaying()) {
            currentPlay.pause();
        }
    }

    public void continueAudio () {
        if (currentPlay != null && !currentPlay.isPlaying()) {
            currentPlay.start();
        }
    }

}
