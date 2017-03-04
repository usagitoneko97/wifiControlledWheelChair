package usagitoneko.nekof;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    protected View mView;
    private Switch led2;
    private Switch led_blue;
    private Switch led_green;
    private Switch led_orange;
    private Button set_Led2;
    private TextView nfc_result;
    private TextView temperature_result_text;
    private TextView temperatureColor;
    private int temperature_result;
    private boolean led2State;
    private boolean ledBlueState;
    private boolean ledGreenState;
    private boolean ledOrangeState;
    private boolean PermissionSetLed2;
    private boolean[] allBool =new boolean[5];

    public MainFragment() {
        // Required empty public constructor
    }

    /*public static MainFragment newInstance(String Message){
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle(1);

    }*/
    public interface onSomeEventListener{
        public void someEvent(boolean[] allBool);
    }
    onSomeEventListener someEventListener;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        nfc_result = (TextView) view.findViewById(R.id.nfc_result);
        led2 = (Switch)view.findViewById(R.id.led2);
        led_blue = (Switch) view.findViewById(R.id.led_blue);
        led_green = (Switch) view.findViewById(R.id.led_green);
        led_orange = (Switch) view.findViewById(R.id.led_orange) ;
        set_Led2 = (Button) view.findViewById(R.id.set_led2);
        temperature_result_text = (TextView)view.findViewById(R.id.temperature);
        temperatureColor = (TextView)view.findViewById(R.id.temperatureColor);
        temperature_result_text.setText("0°C");  //initialize
        this.mView = view;
        set_Led2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                        PermissionSetLed2 = true;
                        allBool[4] = PermissionSetLed2;
                        Toast.makeText(getActivity(), "please place your phone close to the tag.", Toast.LENGTH_SHORT).show();
                /*Loading_dialog loading_dialog = new Loading_dialog();
                loading_dialog.show(getFragmentManager(), "123");*/
            }
        });
        led2.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
                    public void onCheckedChanged(
                            CompoundButton buttonView, boolean isChecked){
                        if(isChecked){
                            led2State=true;
                            allBool[0] = led2State;
                        }
                        else{
                            led2State=false;
                            allBool[0] = led2State;
                        }
                    }
                }
        );
        led_green.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
                    public void onCheckedChanged(
                            CompoundButton buttonView, boolean isChecked){
                        if(isChecked){
                            ledGreenState=true;
                            allBool[1] = ledGreenState;
                        }
                        else{
                            ledGreenState=false;
                            allBool[1] = ledGreenState;
                        }
                    }
                }
        );
        led_blue.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
                    public void onCheckedChanged(
                            CompoundButton buttonView, boolean isChecked){
                        if(isChecked){
                            ledBlueState=true;
                            allBool[2] = ledBlueState;
                        }
                        else{
                            ledBlueState=false;
                            allBool[2] = ledBlueState;
                        }
                    }
                }
        );
        led_orange.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
                    public void onCheckedChanged(
                            CompoundButton buttonView, boolean isChecked){
                        if(isChecked){
                            ledOrangeState=true;
                            allBool[3] = ledOrangeState;
                        }
                        else{
                            ledOrangeState=false;
                            allBool[3] = ledOrangeState;
                        }
                    }
                }
        );
        Croller croller = (Croller) view.findViewById(R.id.croller);
        croller = initCroller(croller);
        croller.setMax(100);
        croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                temperature_result = progress;
                temperature_result_text.setText(progress + "°C");
                if(progress<50){
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
                }
            }
        });
        croller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "im the best!", Toast.LENGTH_SHORT).show();
            }
        });
        someEventListener.someEvent(allBool);
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
        croller.setLabel("Temperature of heater");
        croller.setProgress(50);
        return croller;
    }


}
