package usagitoneko.nekof;

import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by DareBacon on 24/2/2017.
 */

public class LedState {
    final public  int LED2 =0x1 ;
    final public int GREEN = 0x02;
    final public int BLUE =0x04;
    final public int ORANGE =0x08 ;
    private boolean led2State;
    private boolean GreenLedState;
    private boolean BlueLedState;
    private boolean OrangeLedState;

    public boolean isLed2State() {
        return led2State;
    }

    public void setLed2State(boolean led2State) {
        this.led2State = led2State;
    }

    public boolean isGreenLedState() {
        return GreenLedState;
    }

    public void setGreenLedState(boolean greenLedState) {
        GreenLedState = greenLedState;
    }

    public boolean isBlueLedState() {
        return BlueLedState;
    }

    public void setBlueLedState(boolean blueLedState) {
        BlueLedState = blueLedState;
    }

    public boolean isOrangeLedState() {
        return OrangeLedState;
    }

    public void setOrangeLedState(boolean orangeLedState) {
        OrangeLedState = orangeLedState;
    }

    private TextView nfc_result;
    private int ledStatus;
    public LedState(TextView nfc_result, int ledStatus){
        this.nfc_result = nfc_result;
        this.ledStatus = ledStatus;
    }
    public LedState(){

    }

    public boolean isLed(int led_colour){
        if((led_colour &ledStatus)!=0 )
        {
            return true;
        }
        return false;

    }
    public String getName(int ledColour){
        switch (ledColour){
            case LED2:
                return "Led2";
            case GREEN:
                return "Green Led";
            case BLUE:
                return "Blue Led";
            case ORANGE:
                return"Orange Led";
            default:
                return "Unknown Led";
        }
    }
    public void printLedState(int ledColour){
        if(isLed(ledColour)){
            nfc_result.append("\n"+getName(ledColour) +" is on");
        }
        else {
            nfc_result.append("\n"+getName(ledColour) +" is off");
        }

    }
}
