package usagitoneko.nekof;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


public class Loading_dialog extends android.support.v4.app.DialogFragment {
    boolean returnDialog;

    public static interface Callbacks {
        void getWriteStatus(boolean writeStatus);
    }
    Callbacks callbacks;

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
            callbacks = (Callbacks) a;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "must implement onSomeEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the Builder class because this dialog has a simple UI
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("please place your phone close to the tag.");
        return new MaterialDialog.Builder(getContext()).customView(R.layout.loading_dialog, true).
                title("Place your phone close to the tag").negativeText("Cancel").negativeColor(Color.DKGRAY).canceledOnTouchOutside(false)
                .onNegative(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        callbacks.getWriteStatus(false);
                    }
                }).show();
        /*while(true){//while new intent didnt pass in

        }*/
        //MainActivity mainActivity = new MainActivity();
        //return builder.create();

    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_dialog, null);
    }*/

    public void setReturnDialog (boolean returnDialog){
        this.returnDialog = returnDialog;
    }


    @Override
    public void onPause() {
        super.onPause();
        this.dismiss();
    }
}
