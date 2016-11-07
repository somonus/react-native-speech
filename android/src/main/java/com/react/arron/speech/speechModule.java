package com.react.arron.speech;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;


import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.common.ReactConstants;

import java.util.Locale;
import java.util.Set;

/**
 * Created by Arron on 16/11/2.
 */

public class speechModule extends ReactContextBaseJavaModule {

    private static TextToSpeech tts;

    public speechModule(ReactApplicationContext reactContext) {
        super(reactContext);
        init();
    }

    /***
     * This method will expose all the available languages in TTS engine
     *
     * @param callback
     */
    @ReactMethod
    public void getLocale(final Callback callback) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                try {
                    if (tts == null) {
                        init();
                    }
                    Locale[] locales = Locale.getAvailableLocales();
                    WritableArray data = Arguments.createArray();
                    for (Locale locale : locales) {
                        int res = tts.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            data.pushString(locale.getLanguage());
                        }
                    }
                    callback.invoke(null, data);
                } catch (Exception e) {
                    callback.invoke(e.getMessage());
                }
            }
        }.execute();
    }

    @ReactMethod
    public void isSpeaking(final Callback callback) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                try {
                    if (tts.isSpeaking()) {
                        callback.invoke(null, true);
                    } else {
                        callback.invoke(null, false);
                    }
                } catch (Exception e) {
                    callback.invoke(e.getMessage());
                }
            }
        }.execute();
    }

    public void init() {
        tts = new TextToSpeech(getReactApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {
                    FLog.e(ReactConstants.TAG, "Not able to initialized the TTS object");
                }
            }
        });
    }

    @ReactMethod
    public void stop() {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                tts.stop();
            }
        }.execute();
    }

    @ReactMethod
    public void shutDown(final Callback callBack) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                if (tts == null) {
                    callBack.invoke(true);
                }
                try {
                    tts.shutdown();
                    callBack.invoke(null, true);
                } catch (Exception e) {
                    callBack.invoke(e.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public String getName() {
        return "SpeechModule";
    }

    @ReactMethod
    public void speak(final ReadableMap args, final Callback callback) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                if (tts == null) {
                    init();
                }
                String text = args.hasKey("text") ? args.getString("text") : null;
                String language = args.hasKey("language") ? args.getString("language") : null;
                Boolean forceStop = args.hasKey("forceStop") ? args.getBoolean("forceStop") : null;
                Float pitch = args.hasKey("pitch") ? (float) args.getDouble("pitch") : null;
                if (tts.isSpeaking()) {
                    //Force to stop and start new speech
                    if (forceStop != null && forceStop) {
                        tts.stop();
                    } else {
                        callback.invoke("TTS is already speaking something , Please shutdown or stop  TTS and try again");
                        return;
                    }
                }
                if (args.getString("text") == null || text == "") {
                    callback.invoke("t can not be blank");
                    return;
                }
                try {
                    if (language != null && language != "") {
                        tts.setLanguage(new Locale(language));
                    } else {
                        //Setting up default language
                        tts.setLanguage(new Locale("en"));
                    }
                    //Set the pitch if provided by the user
                    if (pitch != null) {
                        tts.setPitch(pitch);
                    }
                    //TODO:: Need to implement the UTTERANCE Id and give the callback
                    int speakResult = 0;
                    if (Build.VERSION.SDK_INT >= 21)
                        speakResult = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    else
                        speakResult = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

                    if (speakResult < 0)
                        throw new Exception("Speak failed, make sure that TTS service is installed on you device");

                    callback.invoke(null, true);
                } catch (Exception e) {
                    callback.invoke(e.getMessage());
                }
            }
        }.execute();
    }
}
