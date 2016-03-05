package io.tatsuki.wifiscan;

/**
 * Created by TI on 2015/10/14.
 */
public class Some_param {

    private String[] result;
    private String[] ssid;
    private int[] frequency;
    private int[] level;
    private boolean view_flg;
    private String str_ssid;
    private int fre_value;
    private int level_value;

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }

    public boolean getView_flg() {
        return view_flg;
    }

    public void setView_flg(boolean view_flg) {
        this.view_flg = view_flg;
    }

    public String[] getSSID() {
        return ssid;
    }

    public void setSSID(String[] ssid) {
        this.ssid = ssid;
    }

    public int[] getFrequency() {
        return frequency;
    }

    public void setFrequency(int[] frequency) {
        this.frequency = frequency;
    }

    public int[] getLevel() {
        return level;
    }

    public void setLevel(int[] level) {
        this.level = level;
    }

    public int getFre_value() {
        return fre_value;
    }

    public void setFre_value(int fre_value) {
        this.fre_value = fre_value;
    }

    public int getLevel_value() {
        return level_value;
    }

    public void setLevel_value(int level_value) {
        this.level_value = level_value;
    }

    public String getStr_ssid() {
        return str_ssid;
    }

    public void setStr_ssid(String str_ssid) {
        this.str_ssid = str_ssid;
    }
}
