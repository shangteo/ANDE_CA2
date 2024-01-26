package com.example.ca1_mainscreen;

public class CheckboxData {

    private String text;
    private boolean isChecked;

    // Empty constructor for Firebase
    public CheckboxData() {
    }

    public CheckboxData(String text, boolean isChecked) {
        this.text = text;
        this.isChecked = isChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
