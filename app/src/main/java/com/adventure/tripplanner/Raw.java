package com.adventure.tripplanner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class Raw
{

    private static final String SHARED_REF_KEY="isLogged";

    //internet toast
    public void internetToast(View view,Activity context)
    {
        Toast toast=new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM,0,150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
    public static String getSharedRefKey()
    {
        return SHARED_REF_KEY;
    }
    public static void share(int check)
    {
        SharedPreferences.Editor editor=LoginActivity.getShared().edit();
        if(check==0) //0 for organizer
          editor.putString(Raw.getSharedRefKey(),FirebaseConnectivity.STATUS_ORGANIZER).apply();
        else //1 for traveler
            editor.putString(Raw.getSharedRefKey(),FirebaseConnectivity.STATUS_TRAVELER).apply();
    }

    public static String getMonth(int mon)
    {
        if(mon==1) return "Jan";
        if(mon==2) return "Feb";
        if(mon==3) return "Mar";
        if(mon==4) return "Apr";
        if(mon==5) return "May";
        if(mon==6) return "Jun";
        if(mon==7) return "Jul";
        if(mon==8) return "Aug";
        if(mon==9) return "Sep";
        if(mon==10) return "Oct";
        if(mon==11) return "Nov";
        if(mon==12) return "Dec";
        else return null;
    }
}
