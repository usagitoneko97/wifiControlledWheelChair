package usagitoneko.nekof;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.support.v4.app.Fragment;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.List;

import static usagitoneko.nekof.R.id.tickerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements Loading_dialog.Callbacks, OnClickListener {

    protected View mView;
    private JellyToggleButton led2;
    private JellyToggleButton ledBlue;
    private JellyToggleButton ledGreen;
    private JellyToggleButton ledOrange;
    private TickerView tickerView;
    private Croller croller;
    private Button confirmButton;
    private TextView knobTemperatureText;
    private boolean[] allBool =new boolean[5];
    private List<Boolean> allLedStatus = new ArrayList<>();
    public final int WRITE_PERMISSION = 0;
    public final int LED2 =1;
    public final int LED_GREEN =2;
    public final int LED_BLUE = 3;
    public final int LED_ORANGE = 4;

    public MainFragment() {
        // Required empty public constructor
    }

    public interface onSomeEventListener{
        public void someEvent(List<Boolean> allLedStatus);
    }
    onSomeEventListener someEventListener;

    public void dummy(){
        Toast.makeText(getActivity(), "toast!!!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void getWriteStatus(boolean writeStatus) {
        allBool[4] = writeStatus;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if(context instanceof Activity){
            a = (Activity) context;
        }
        else{
            a= null;
        }
        try{
            someEventListener = (onSomeEventListener) a;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "must implement onSomeEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        someEventListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //non graphical initialization
        super.onCreate(savedInstanceState);
        allLedStatus.add(WRITE_PERMISSION,false);
        allLedStatus.add(LED2,false);
        allLedStatus.add(LED_GREEN,false);
        allLedStatus.add(LED_BLUE,false);
        allLedStatus.add(LED_ORANGE,false);

    }

    @Override
    public void onClick(View v) {
        //display
        Toast.makeText(getActivity(), "on CLICK", Toast.LENGTH_SHORT).show();
        switch (v.getId()){
            case R.id.led2:
                if(led2.isChecked()){
                    tickerView.setText("65");
                    allLedStatus.set(LED2, true);
                }
                else{
                    allLedStatus.set(LED2, false);
                }
                break;
            case R.id.ledGreen:
                if(ledGreen.isChecked()){
                    tickerView.setText("89");
                    allLedStatus.set(LED_GREEN, true);
                }
                else{
                    allLedStatus.set(LED_GREEN, false);
                }
                break;
            case R.id.ledBlue:
                if(ledBlue.isChecked()){
                    tickerView.setText("63");
                    allLedStatus.set(LED_BLUE, true);
                }
                else{
                    allLedStatus.set(LED_BLUE, false);
                }
                break;
            case R.id.ledOrange:
                if(ledOrange.isChecked()){
                    tickerView.setText("63");
                    allLedStatus.set(LED_ORANGE, true);
                }
                else{
                    allLedStatus.set(LED_ORANGE, false);
                }
                break;
            case R.id.croller:
                Toast.makeText(getActivity(), "croller!!", Toast.LENGTH_SHORT).show();
                knobTemperatureText.setText(croller.getProgress() + "°C");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        led2 = (JellyToggleButton)view.findViewById(R.id.led2);
        ledBlue = (JellyToggleButton) view.findViewById(R.id.ledBlue);
        ledGreen = (JellyToggleButton) view.findViewById(R.id.ledGreen);
        ledOrange = (JellyToggleButton) view.findViewById(R.id.ledOrange) ;
        knobTemperatureText = (TextView)view.findViewById(R.id.temperature);
        tickerView = (TickerView)view.findViewById(R.id.tickerView);
        tickerView.setCharacterList(TickerUtils.getDefaultNumberList());
        croller = (Croller) view.findViewById(R.id.croller);
        croller = initCroller(croller);
        croller.setMax(100);
        croller.setOnClickListener(this);
        led2.setOnClickListener(this);
        ledBlue.setOnClickListener(this);
        ledGreen.setOnClickListener(this);
        ledOrange.setOnClickListener(this);

        tickerView.setText("55");
        knobTemperatureText.setText("0°C");  //initialize
        this.mView = view;

       /* led2.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener(){
            @Override
            public void onStateChange (float process, State state, JellyToggleButton jtb){
                if(state.equals(State.RIGHT)){
                    tickerview.setText("65");
                    allLedStatus.set(LED2, true);
                }
                else if (state.equals(State.LEFT)){
                    allLedStatus.set(LED2, false);
                }
            }
        });*/
       /* ledGreen.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener(){
            @Override
            public void onStateChange (float process, State state, JellyToggleButton jtb){
                if(state.equals(State.RIGHT)){
                    tickerview.setText("89");
                    allLedStatus.set(LED_GREEN, true);
                }
                else if (state.equals(State.LEFT)){
                    allLedStatus.set(LED_GREEN, false);
                }
            }
        });
        ledBlue.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener(){
            @Override
            public void onStateChange (float process, State state, JellyToggleButton jtb){
                if(state.equals(State.RIGHT)){
                    tickerview.setText("85");
                    allLedStatus.set(LED_BLUE, true);
                }
                else if (state.equals(State.LEFT)){
                    allLedStatus.set(LED_BLUE, false);
                }
            }
        });
        ledOrange.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener(){
            @Override
            public void onStateChange (float process, State state, JellyToggleButton jtb){
                if(state.equals(State.RIGHT)){
                    tickerview.setText("25");
                    allLedStatus.set(LED_ORANGE, true);
                }
                else if (state.equals(State.LEFT)){
                    allLedStatus.set(LED_ORANGE, false);
                }
            }
        });*/

        /*croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                knobTemperatureText.setText(progress + "°C");*/

               /*if (progress<20){
                   tickerview.setTextColor(Color.rgb(00,0xed,0xff));    //light blue
                }
                else if(progress<40&&progress>=20){
                   tickerview.setTextColor(Color.rgb(00, 0x83, 0xff));
               }
               else if(progress<60&&progress>=40){
                   tickerview.setTextColor(Color.rgb(0x66, 00, 0xff));
               }
               else if(progress<80&&progress>=60){
                   tickerview.setTextColor(Color.rgb(0xff, 00, 0xe5));
               }
               else{
                   tickerview.setTextColor(Color.rgb(0xff, 00, 0x4c));
               }*/

        someEventListener.someEvent(allLedStatus);
        return view;
    }

    private Croller initCroller(Croller croller){
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
        croller.setMainCircleColor(Color.WHITE);
        croller.setIsContinuous(false);
        croller.setLabelColor(Color.BLACK);
        croller.setProgressPrimaryColor(Color.parseColor("#0B3C49"));
        croller.setIndicatorColor(Color.parseColor("#0B3C49"));
        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
        croller.setIndicatorWidth(10);
        return croller;
    }


}
//**
//**UNSUSED CODE**
//---------------------
/*if(progress<50){
                    //colder
                    int temp = (int)Math.round(255.00-(((50-progress)/50.00)*255.00));
                    String s = String.valueOf(temp);
                    temperatureColor.setText(s);
                    temperatureColor.setBackgroundColor(Color.rgb(255, 0,temp));
                }
                else if(progress>50){
                    int temp = (int)Math.round(255.00-(((progress-50.00)/50.00)*255.00));
                    String s = String.valueOf(temp);
                    temperatureColor.setText(s);
                    //hotter
                    temperatureColor.setBackgroundColor(Color.rgb(temp, 0, 255));
                }
                else{
                    //0.5
                    temperatureColor.setBackgroundColor(Color.rgb(255, 0, 255));
                }*/