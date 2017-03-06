package usagitoneko.nekof;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;



public class Loading_dialog extends android.support.v4.app.DialogFragment {
    boolean returnDialog;

    public static interface Callbacks {
        void onButtonClicked(Loading_dialog dialogFragment);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the Builder class because this dialog has a simple UI
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("please place your phone close to the tag.");
        return builder.create();
        /*while(true){//while new intent didnt pass in

        }*/
        //MainActivity mainActivity = new MainActivity();
        //return builder.create();

    }
    public void setReturnDialog (boolean returnDialog){
        this.returnDialog = returnDialog;
    }
    private void onSomeButtonClicked() {
        Callbacks callbacks = (Callbacks) getActivity();
        //
        // Should I dismiss here?
        // dismiss();
        //
        callbacks.onButtonClicked(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.dismiss();
    }
}
