package com.xiao.lib.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author 沈小建
 * @data 2018/7/10
 */
public class AudioManagerUtils {

    /**
     * 切换到听筒模式.
     *
     * @param context 上下文
     */
    public static void changeAudioToTel(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(false);
    }

    /**
     * 切换到蓝牙模式.
     *
     * @param context 上下文
     */
    public static void changeAudioTBluetooth(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.startBluetoothSco();
        audioManager.setBluetoothScoOn(true);
        audioManager.setSpeakerphoneOn(false);
    }

    /**
     * 切换到外放模式.
     *
     * @param context 上下文
     */
    public static void changeAudioToSpeakOut(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }

    /**
     * 切换到普通模式.
     * @param context
     */
    public static void changeAudioToNormal(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }
}
