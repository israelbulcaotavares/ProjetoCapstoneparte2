package com.example.mechatronicse.projetocapstoneparte2.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.VERSION_SDK_PERSMISSION;

public class Permissions {
    public static boolean validPermissions(String[] permissions, Activity activity, int requestCode){
        if (Build.VERSION.SDK_INT >= VERSION_SDK_PERSMISSION ){

            List<String> list = new ArrayList<>();
            for ( String permissao : permissions ){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if ( !temPermissao ) list.add(permissao);
            }

            if ( list.isEmpty() ) return true;
            String[] newPermissions = new String[ list.size() ];
            list.toArray( newPermissions );
            ActivityCompat.requestPermissions(activity, newPermissions, requestCode );

        }

        return true;

    }

}
