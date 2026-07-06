package com.sunyard.api.printer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable class for text chip data
 * Used for complex text formatting in addTextChips
 */
public class PrinterChip implements Parcelable {
    public String content;       // Text content
    public float proportion;    // Text proportion/size
    public int align;           // Alignment (0=left, 1=center, 2=right)
    public int fontSize;        // Font size
    public int fontTemple;      // Font template
    public boolean isBold;      // Bold text
    public boolean isUnderLine; // Underline text

    public PrinterChip() {
        this.content = "";
        this.proportion = 1.0f;
        this.align = 0;
        this.fontSize = 1;
        this.fontTemple = 0;
        this.isBold = false;
        this.isUnderLine = false;
    }

    public PrinterChip(String content, float proportion, int align) {
        this.content = content;
        this.proportion = proportion;
        this.align = align;
        this.fontSize = 1;
        this.fontTemple = 0;
        this.isBold = false;
        this.isUnderLine = false;
    }

    protected PrinterChip(Parcel in) {
        this.content = in.readString();
        this.proportion = in.readFloat();
        this.align = in.readInt();
        this.fontSize = in.readInt();
        this.fontTemple = in.readInt();
        this.isBold = in.readByte() != 0;
        this.isUnderLine = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeFloat(this.proportion);
        dest.writeInt(this.align);
        dest.writeInt(this.fontSize);
        dest.writeInt(this.fontTemple);
        dest.writeByte((byte) (this.isBold ? 1 : 0));
        dest.writeByte((byte) (this.isUnderLine ? 1 : 0));
    }

    public static final Parcelable.Creator<PrinterChip> CREATOR = new Parcelable.Creator<PrinterChip>() {
        @Override
        public PrinterChip createFromParcel(Parcel in) {
            return new PrinterChip(in);
        }

        @Override
        public PrinterChip[] newArray(int size) {
            return new PrinterChip[size];
        }
    };

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontTemple() {
        return fontTemple;
    }

    public void setFontTemple(int fontTemple) {
        this.fontTemple = fontTemple;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isUnderLine() {
        return isUnderLine;
    }

    public void setUnderLine(boolean underLine) {
        isUnderLine = underLine;
    }
}
