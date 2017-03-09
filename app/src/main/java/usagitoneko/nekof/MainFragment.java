package usagitoneko.nekof;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
    private boolean firstClick;

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
        final Bundle args = getArguments();
        firstClick = args.getBoolean("firstClick");
        args.putBoolean("firstClick", false);
        if(firstClick){
            Snackbar.make(getView(), "Please place your phone near to the tag.", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            led2.setChecked(args.getBoolean("led2"));
                            allLedStatus.set(LED2,args.getBoolean("led2"));
                            ledGreen.setChecked(args.getBoolean("ledGreen"));
                            allLedStatus.set(LED_GREEN,args.getBoolean("ledGreen"));
                            ledBlue.setChecked(args.getBoolean("ledBlue"));
                            allLedStatus.set(LED_BLUE,args.getBoolean("ledBlue"));
                            ledOrange.setChecked(args.getBoolean("ledOrange"));
                            allLedStatus.set(LED_ORANGE,args.getBoolean("ledOrange"));
                            args.putBoolean("firstClick", true); //snackbar again if onclick detected
                            Toast.makeText(getActivity(), "Settings reverted to initial state.", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
            Toast.makeText(getActivity(), "you have succeed!", Toast.LENGTH_SHORT).show();
        }

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

        croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                final Bundle args = getArguments();
                firstClick = args.getBoolean("firstClick");
                args.putBoolean("firstClick", false);
                if(firstClick){
                    Snackbar.make(getView(), "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                knobTemperatureText.setText(progress + "°C");

            }
        });

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

/*if (progress < 20) {
                    tickerview.setTextColor(Color.rgb(00, 0xed, 0xff));    //light blue
                } else if (progress < 40 && progress >= 20) {
                    tickerview.setTextColor(Color.rgb(00, 0x83, 0xff));
                } else if (progress < 60 && progress >= 40) {
                    tickerview.setTextColor(Color.rgb(0x66, 00, 0xff));
                } else if (progress < 80 && progress >= 60) {
                    tickerview.setTextColor(Color.rgb(0xff, 00, 0xe5));
                } else {
                    tickerview.setTextColor(Color.rgb(0xff, 00, 0x4c));
                }*/