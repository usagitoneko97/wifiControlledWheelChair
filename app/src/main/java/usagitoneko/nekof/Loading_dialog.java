package usagitoneko.nekof;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


public class Loading_dialog extends android.support.v4.app.DialogFragment {

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
        return new MaterialDialog.Builder(getContext()).customView(R.layout.loading_dialog, true).
                title("Place your phone close to the tag").negativeText("Cancel").negativeColor(Color.DKGRAY).canceledOnTouchOutside(false)
                .onNegative(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        callbacks.getWriteStatus(false);
                    }
                }).show();

    }


    @Override
    public void onPause() {
        super.onPause();
        this.dismiss();
    }
}
