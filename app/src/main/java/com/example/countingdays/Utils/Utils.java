package com.example.countingdays.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.countingdays.Listener.WildLambda;
import com.example.countingdays.R;

public final   class Utils {


    public static void showSuccessDialog(Context context, WildLambda lambda){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.delete_dialog);
        Button buttonCancel = dialog.findViewById(R.id.cancel_btn);
        Button buttonOk = dialog.findViewById(R.id.ok_button);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                lambda.lambda();


            }
        });
        dialog.show();
    }




    public static void showFeedBackDialog(Context context, WildLambda lambda){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.feedback_dialog);
        Button buttonOk = dialog.findViewById(R.id.back_btn);


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lambda.lambda();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    double getProgress(double total, double current) {
        return current / total * 100 ;
    }

}
