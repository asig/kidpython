/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.ide;

import com.programmablefun.ide.util.OS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    private static final String KEY_SELECTEDSTYLESHEET = "SelectedStylesheet";
    private static final String KEY_SELECTEDSOURCE = "SelectedSource";

    private static Settings instance = null;

    private String fileName = null;
    private Properties properties = null;
//    private List<SettingsChangedListener> listeners = new LinkedList<SettingsChangedListener>();

    public static String getDataDirectory() {
        File dir = new File(OS.getAppDataDirectory() + "/ProgrammableFun/");
        dir.mkdirs();
        return dir.getAbsolutePath();
    }

    public static String getConfigDirectory() {
        File dir = new File(OS.getConfigDirectory() + "/ProgrammableFun/");
        dir.mkdirs();
        return dir.getAbsolutePath();
    }

    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getSelectedStylesheet() {
        return get(KEY_SELECTEDSTYLESHEET, null);
    }

    public void setSelectedstylesheet(String name) {
        set(KEY_SELECTEDSTYLESHEET, name);
    }

    public int getSelectedSourceIndex() {
        return getInt(KEY_SELECTEDSOURCE, 0);
    }

    public void setSelectedSourceIndex(int idx) {
        set(KEY_SELECTEDSOURCE, idx);
    }

    private Settings() {
        fileName = getConfigDirectory() + "/settings.properties";
        properties = new Properties(getDefaultProperties());
        FileInputStream is = null;
        try {
            is = new FileInputStream(fileName);
            properties.load(is);
        } catch (FileNotFoundException e) {
            // File not here... save default properties
            save(getDefaultProperties()); // make sure properties are written if
            // the file didn't exist
        } catch (IOException e) {
            // do nothing
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public void save() {
        save(properties);
    }

    private void save(Properties props) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
            props.store(os, "Settings for Programmable Fun");
        } catch (IOException e) {
            // do nothing
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    private Properties getDefaultProperties() {

        Properties props = new Properties();
        InputStream is = getClass().getResourceAsStream("/default-settings.properties");
        try {
            props.load(is);
        } catch (Exception e) {
            // do nothing
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
        }
        return props;
    }
//
//    public void addListener(SettingsChangedListener listener) {
//        listeners.add(listener);
//    }
//
//    public void removeListener(SettingsChangedListener listener) {
//        listeners.remove(listener);
//    }
//

    private void fireSettingsChanged(String property) {
//        for (SettingsChangedListener l : listeners) {
//            l.settingsChanged(property);
//        }
    }

    public Settings remove(String key) {
        properties.remove(key);
        return this;
    }

    public Settings set(String key, String newVal) {
        String oldVal = properties.getProperty(key);
        if (!(oldVal != null && oldVal.equals(newVal))) {
            properties.setProperty(key, newVal);
            fireSettingsChanged(key);
        }
        return this;
    }

    public Settings set(String key, boolean newVal) {
        this.set(key, Boolean.toString(newVal) );
        return this;
    }

    public Settings set(String key, int newVal) {
        this.set(key, Integer.toString(newVal) );
        return this;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key, int dflt) {
        String res = properties.getProperty(key);
        return res == null ? dflt : Integer.parseInt(res);
    }

    public boolean getBoolean(String key, boolean dflt) {
        String res = properties.getProperty(key);
        return res == null ? dflt : Boolean.parseBoolean(res);
    }

    public String get(String key, String dflt) {
        String res = properties.getProperty(key);
        return res == null ? dflt : res;
    }
}
