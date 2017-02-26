package usagitoneko.nekof;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentLog extends Fragment{

    private TextView log;

    public static FragmentLog newInstance (String message){
        //create a fragment
        FragmentLog fragment = new FragmentLog();
        Bundle bundle = new Bundle(1);
        bundle.putString("testing 123", message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        log = (TextView)view.findViewById(R.id.log);
        log.setMovementMethod(new ScrollingMovementMethod());   //to set it scrollable
        log.setText("here is Fragment Log java");

        return view;
    }
}
