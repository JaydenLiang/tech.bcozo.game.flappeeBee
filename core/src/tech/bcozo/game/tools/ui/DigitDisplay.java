/**
 * 
 */
package tech.bcozo.game.tools.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
//import com.google.gwt.i18n.client.NumberFormat;

/**
 * <p>
 * A Class to display digits. The texture for each digit is user defined.
 * </p>
 * 
 * @ClassName: DigitDisplay
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 20, 2015 2:32:10 PM
 */
public class DigitDisplay extends Actor implements Disposable {
    private Texture digitTexture;
    private TextureRegion[] digits;
    private TextureRegion[] numbers;
    private TextureRegion radixPoint;
    // private NumberFormat formatter;
    private int minIntegerDigits;
    private int maxIntegerDigits;
    private int textMarginX;
    private int textMarginY;

    /**
     * <p>
     * This is the constructor of DigitDisplay
     * </p>
     * 
     * @param source the file path of texture source.
     * @param regions A two-dimension array of region's definitions. The first
     *            dimension contains 11 sets of definition with the 1st - 10th
     *            for 0 to 9 in order and the 11th for radix point. Each second
     *            dimension of definition consists of the x, y, width, height of
     *            the region in the texture.
     */
    public DigitDisplay(String source, int[][] regions) {
        digitTexture = new Texture(Gdx.files.internal(source));
        digits = new TextureRegion[10];
        // formatter = NumberFormat.getDecimalFormat();
        minIntegerDigits = 0;
        maxIntegerDigits = 0;
        textMarginX = 0;
        textMarginY = 0;
        createTextureRegions(regions);
    }

    /**
     * <p>
     * This is the constructor of DigitDisplay
     * </p>
     * 
     * @param source the file path of texture source.
     * @param region the file path of source from which to read a string
     *            representing a two-dimension array of region's definitions.
     *            The first dimension contains 11 sets of definition with the
     *            1st - 10th for 0 to 9 in order and the 11th for radix point.
     *            Each second dimension of definition consists of the x, y,
     *            width, height of the region in the texture. The format of this
     *            string is using a semicolon as the separator for the first
     *            dimension, and a comma for the second dimension. Example: x1,
     *            y1, w1, h1; x2, y2, w2, h2;
     */
    public DigitDisplay(String source, String region) {
        digitTexture = new Texture(Gdx.files.internal(source));
        digits = new TextureRegion[10];
        // formatter = NumberFormat.getDecimalFormat();

        String regionSettings = Gdx.files.internal(region).readString();
        int[][] regions = new int[11][4];
        String[] digitSettings = regionSettings.split(";");
        String[] digitSetting;
        for (int i = 0; i < digitSettings.length; i++) {
            digitSetting = digitSettings[i].split(",");
            regions[i][0] = Integer.parseInt(digitSetting[0].trim());
            regions[i][1] = Integer.parseInt(digitSetting[1].trim());
            regions[i][2] = Integer.parseInt(digitSetting[2].trim());
            regions[i][3] = Integer.parseInt(digitSetting[3].trim());
        }

        createTextureRegions(regions);
    }

    private void createTextureRegions(int[][] regions) {
        int height = 0;
        for (int i = 0; i < digits.length; i++) {
            digits[i] = new TextureRegion(digitTexture);
            digits[i].setRegion(regions[i][0], regions[i][1], regions[i][2],
                    regions[i][3]);
            if (digits[i].getRegionHeight() > height) {
                height = digits[i].getRegionHeight();
            }
        }
        radixPoint = new TextureRegion(digitTexture);
        radixPoint.setRegion(radixPoint, regions[10][0], regions[10][1],
                regions[10][2], regions[10][3]);
        if (radixPoint.getRegionHeight() > height) {
            height = radixPoint.getRegionHeight();
        }
        setHeight(height);
    }

    /**
     * <p>
     * Set the display number.
     * </p>
     * 
     * @param number the number to display.
     * @param integerDigits how many digits of integer are to display (can cause
     *            leading zeros).
     * @param fractionDigits how many digits of fraction are to display (can
     *            cause leading zeros).
     */
    public void setNumber(long number, int integerDigits, int fractionDigits) {
        setDisplayFormat(integerDigits, integerDigits, fractionDigits,
                fractionDigits);
        setNumber(number);
    }

    /**
     * <p>
     * Set the display number.
     * </p>
     * 
     * @param number the number to display.
     * @param minIntegerDigits minimum digits of integer are to display (can
     *            cause leading zeros).
     * @param maxIntegerDigits maximum digits of fraction are to display (can
     *            cause truncating).
     * @param minFractionDigits minimum digits of integer are to display (can
     *            cause leading zeros).
     * @param maxFractionDigits maximum digits of fraction are to display (can
     *            cause truncating).
     */
    public void setNumber(long number, int minIntegerDigits,
            int maxIntegerDigits, int minFractionDigits,
            int maxFractionDigits) {
        setDisplayFormat(minIntegerDigits, maxIntegerDigits, minFractionDigits,
                maxFractionDigits);
        setNumber(number);
    }

    /**
     * <p>
     * Set the display number.
     * </p>
     * 
     * @param number the number to display using the default display format.
     */
    public void setNumber(long number) {
        int width = 0;
        // String string = formatter.format(number).replace(",", "");
        String string = padZero(number, minIntegerDigits, maxIntegerDigits);
        clearNumber();
        numbers = new TextureRegion[string.length()];
        int index = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (string.charAt(i) == '.') {
                numbers[i] = radixPoint;
            } else {
                index = Integer.parseInt(string.substring(i, i + 1));
                numbers[i] = digits[index];
            }
            width += numbers[i].getRegionWidth() + textMarginX;
        }
        setWidth(width);
    }

    /**
     * <p>
     * Set the default display format.
     * </p>
     * 
     * @param minIntegerDigits minimum digits of integer are to display (can
     *            cause leading zeros).
     * @param maxIntegerDigits maximum digits of fraction are to display (can
     *            cause truncating).
     * @param minFractionDigits minimum digits of integer are to display (can
     *            cause leading zeros).
     * @param maxFractionDigits maximum digits of fraction are to display (can
     *            cause truncating).
     */
    public void setDisplayFormat(int minIntegerDigits, int maxIntegerDigits,
            int minFractionDigits, int maxFractionDigits) {
        // formatter.setMinimumIntegerDigits(minIntegerDigits);
        // formatter.setMaximumIntegerDigits(maxIntegerDigits);
        // formatter.setMinimumFractionDigits(minFractionDigits);
        // formatter.setMaximumFractionDigits(maxFractionDigits);
        this.minIntegerDigits = minIntegerDigits;
        this.maxIntegerDigits = maxIntegerDigits;

    }

    private String padZero(long number, int minIntegerDigits,
            int maxIntegerDigits) {
        String numString = Long.toString(number);
        String paddingString;
        int length = numString.length();
        if (length >= maxIntegerDigits) {
            return numString.substring(length - maxIntegerDigits);
        } else if (length < minIntegerDigits) {
            paddingString = "";
            for (int i = 0; i < minIntegerDigits - length; i++) {
                paddingString += "0";
            }
            return paddingString += numString;
        } else
            return numString;
    }

    private void clearNumber() {
        if (numbers != null && numbers.length > 0) {
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = null;
            }
        }
        numbers = null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // batch.draw(region, x, y);
        if (numbers != null) {
            int drawX = 0;
            for (int i = 0; i < numbers.length; i++) {
                batch.draw(numbers[i], getX() + drawX, getY());
                drawX += numbers[i].getRegionWidth() + textMarginX;
            }
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        clearNumber();
        if (digits != null && digits.length > 0) {
            for (int i = 0; i < digits.length; i++) {
                digits[i] = null;
            }
        }
        digits = null;
        radixPoint = null;
        // formatter = null;
        digitTexture.dispose();
        digitTexture = null;
    }

    public int getTextMarginX() {
        return textMarginX;
    }

    public void setTextMarginX(int marginX) {
        textMarginX = marginX;
    }
    //
    // public int getTextMarginY() {
    // return textMarginY;
    // }
    //
    // public void setTextMarginY(int textMarginY) {
    // this.textMarginY = textMarginY;
    // }
    //
    // public void setTextMargin(int marginX, int marginY) {
    // setTextMarginX(marginX);
    // setTextMarginY(marginY);
    // }
}
