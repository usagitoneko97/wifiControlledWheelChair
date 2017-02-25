package usagitoneko.nekof;



import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    NfcAdapter mNfcAdapter;
    public TextView nfc_result;
    private Switch led2;
    private Switch led_blue;
    private Switch led_green;
    private Switch led_orange;
    private Button set_Led2;
    private boolean PermissionSetLed2;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    int buffer_receive[];
    private boolean led2State;
    private boolean ledBlueState;
    private boolean ledGreenState;
    private boolean ledOrangeState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfc_result = (TextView) findViewById(R.id.nfc_result);
        led2 = (Switch) findViewById(R.id.led2);
        led_blue = (Switch)findViewById(R.id.led_blue);
        led_green = (Switch)findViewById(R.id.led_green);
        led_orange = (Switch)findViewById(R.id.led_orange) ;
        set_Led2 = (Button)findViewById(R.id.set_led2);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //listen to button clicks
        set_Led2.setOnClickListener(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC. ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            //inform user NFC is disabled
        } else {
            //display whatever title desired
        }
        //for the switch
        final LedState ledWrite = new LedState();
        led2.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
                    public void onCheckedChanged(
                        CompoundButton buttonView, boolean isChecked){
                        if(isChecked){
                            led2State=true;
                        }
                        else{
                            led2State=false;
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
                        }
                        else{
                            ledGreenState=false;
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
                        }
                        else{
                            ledWrite.setBlueLedState(false);
                            ledBlueState=false;
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
                        }
                        else{
                            ledOrangeState=false;
                        }
                    }
                }
        );
        handleIntent(getIntent());


    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } /*else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }*/ //TECH_DISCOVERED will filter on the onNewIntent
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    public void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))
        {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NfcV nfcv = NfcV.get(detectedTag);
            if(nfcv == null){
                //not nfcV type
                Toast.makeText(this, "you are doom!", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    nfcv.connect();
                    if (nfcv.isConnected()) {
                        nfc_result.append("Connected to the tag");
                        nfc_result.append("\nTag DSF: " + Byte.toString(nfcv.getDsfId()));
                        byte[] buffer;

                        //take value from switch which listen in onCreate function
                        if (PermissionSetLed2) {
                            PermissionSetLed2=false;
                            int resultAllLed = 0x10;//initial value predefined
                            if(led2State){
                                resultAllLed = resultAllLed|(1<<0); //set bit 0
                            }
                            if(ledGreenState){
                                resultAllLed = resultAllLed|(1<<1); //set bit 1
                            }
                            if(ledBlueState){
                                resultAllLed = resultAllLed|(1<<2); //set bit 2
                            }
                            if(ledOrangeState){
                                resultAllLed = resultAllLed|(1<<3); //set bit 3
                            }
                                buffer = nfcv.transceive(new byte[]{(byte) 0x02, (byte) 0x21, (byte) 0, (byte) resultAllLed, (byte) 0x00, (byte) 0x72, (byte) 0x75}); //11 instead of 01 is because to avoid nfcv cant read 00 bug
                                // TODO: 23/2/2017   should do checking at buffer
                                Toast.makeText(this, "successfully write in the tag! ", Toast.LENGTH_SHORT).show();
                                nfcv.close();

                            String buffer_hex;
                            buffer = nfcv.transceive(new byte[]{0x02, 0x20, (byte) 0}); //read 0th byte (total 4 bytes)
                            int ledStatus = toInteger(buffer);
                            nfc_result.append("\nled status: "+ ledStatus +", "+ numberToHex(ledStatus));   //checking purpose
                            //buffer_hex = toHex(new String(buffer));     //bugs:a line of 00000000 will appear // TODO: 23/2/2017 solve the bugs
                            //long buffer_long = Long.parseLong(buffer_hex, 16);
                            LedState ledState = new LedState(nfc_result, ledStatus);
                            ledState.printLedState(ledState.LED2);
                            ledState.printLedState(ledState.BLUE);
                            ledState.printLedState(ledState.GREEN);
                            ledState.printLedState(ledState.ORANGE);
                        }

                    }else
                        nfc_result.append("Not connected to the tag");
                } catch (IOException e) {
                    nfc_result.append("Error");
                }

            }
        }

        else {  //has NDEF inside the tag
            handleIntent(intent); //read data on the tag and display to the textview
            if (isNfcIntent(intent)) {
                NdefMessage ndefMessage = createTextMessage("hello there!");

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                writeTag(tag, ndefMessage);
            }
        }

    }

    boolean isNfcIntent(Intent intent) {
        return intent.hasExtra(NfcAdapter.EXTRA_TAG);
    }

    boolean writeTag( Tag detectedTag, NdefMessage message) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(detectedTag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is read-only.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "The data cannot written to tag, Tag capacity is " + ndef.getMaxSize() + " bytes, message is "
                                    + size + " bytes.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                ndef.close();
                Toast.makeText(this, "Message is written!",
                        Toast.LENGTH_SHORT).show();
                return true;
            } else {
                NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
                if (ndefFormat != null) {
                    try {
                        ndefFormat.connect();
                        ndefFormat.format(message);
                        ndefFormat.close();
                        Toast.makeText(this, "The data is written to the tag ",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (IOException e) {
                        Toast.makeText(this, "Failed to format tag",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "NDEF is not supported",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Write opreation is failed",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{new String[]{NfcV.class.getName()}}; //added NfcV

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);

        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@ink BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.set_led2:
                PermissionSetLed2 = true;
                Toast.makeText(this, "please place your phone close to the tag.", Toast.LENGTH_SHORT).show();
                /*Loading_dialog loading_dialog = new Loading_dialog();
                loading_dialog.show(getFragmentManager(), "123");*/
                break;
        }

    }

    public boolean isPermissionSetLed2(){
        return PermissionSetLed2;
    }

    public int toInteger(byte[] bytes){
        int result =0;
        for(int i=0;i<4;i++){
            result<<=8;
            result +=bytes[i];
        }
        return result;
    }



    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                nfc_result.setText("Read content: " + result);
            }
        }
    }

    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    public String numberToHex(int value) {
        return String.format("0x%x",value);
    }

}
// TODO: 22/2/2017 interacting with nucleo device with a simple program
// TODO: 22/2/2017 try out aar and verify*
// TODO: 23/2/2017 unable to store 0 into int or hex
// TODO: 23/2/2017 switching between NDEF data and non-NDEF data*
// TODO: 23/2/2017 simplyfy checking of led state and others
// TODO: 23/2/2017 anonymous class of the switch should be modified to make it shorter (probably dont use anonymous)