package com.asigner.kidpython.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    public enum Key {
        Action_Run,
        Action_Resume,
        Action_Pause,
        Action_Stop,
        Action_Step_Into,
        Action_Step_Over,
        Action_About,
        Action_Preferences,
        Action_Help,

        VM_Execution_Started,
        VM_Execution_Stopped,
        VM_Execution_Paused,
        VM_Error_While_Compiling,
    }

    private ResourceBundle bundle;

    public Messages() {
        this.bundle = ResourceBundle.getBundle("com.asigner.kidpython.Messages", Locale.GERMAN);
    }

    public String get(Key key) {
        return bundle.getString(key.toString());
    }
}
