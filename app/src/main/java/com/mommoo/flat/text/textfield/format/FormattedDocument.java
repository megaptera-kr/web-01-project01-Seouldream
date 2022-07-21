package com.mommoo.flat.text.textfield.format;

import com.mommoo.flat.text.textfield.FlatTextField;
import com.mommoo.util.StringUtils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.*;

public class FormattedDocument extends PlainDocument{
    private Set<FlatTextFormat> formatSet = new HashSet<>();
    private boolean isHintStatus;
    private StringBuilder stringBuilder = new StringBuilder();
    private int limitTextLength = FlatTextField.NONE_LIMIT_TEXT_LENGTH;

    public List<FlatTextFormat> getFormatList() {
        return new ArrayList<>(formatSet);
    }

    public void setFormat(FlatTextFormat... formats){
        formatSet.addAll(Arrays.asList(formats));
    }

    public void setHintStatus(boolean hintStatus){
        this.isHintStatus = hintStatus;
    }

    @Override
    public void insertString(int offs, String string, AttributeSet a) throws BadLocationException {
        if (limitTextLength > FlatTextField.NONE_LIMIT_TEXT_LENGTH && getLength() + string.length() > limitTextLength) {
            string = string.substring(0, limitTextLength - getLength());
        }

        if (isHintStatus) {
            super.insertString(offs, string, a);
            return;
        }

        stringBuilder.delete(0, stringBuilder.length());

        for (char c : string.toCharArray()){
            appendTextFitFormat(c);
        }

        super.insertString(offs, stringBuilder.toString(), a);
    }

    public void setLimitTextLength(int limitTextLength) {
        this.limitTextLength = limitTextLength;
    }

    public int getLimitTextLength() {
        return this.limitTextLength;
    }

    private void appendTextFitFormat(char c){
        if (formatSet.size() == 0){
            stringBuilder.append(c);
            return;
        }

        for (FlatTextFormat format : formatSet){
            if (format == FlatTextFormat.NUMBER_DECIMAL){

                if (StringUtils.isNumberChar(c)) {
                    stringBuilder.append(c);
                    return ;
                }

            } else if (format == FlatTextFormat.TEXT){

                if (!StringUtils.isNumberChar(c) && !StringUtils.isSpecialChar(c)) {
                    stringBuilder.append(c);
                    return ;
                }

            } else if (format == FlatTextFormat.SPECIAL_CHARACTER){

                if (StringUtils.isSpecialChar(c)) {
                    stringBuilder.append(c);
                    return ;
                }

            } else if (format == FlatTextFormat.HEX_COLOR){

                if (StringUtils.isHexColorChar(c)){
                    stringBuilder.append(c);
                    return;
                }

            } else{
                stringBuilder.append(c);
            }
        }

    }
}
