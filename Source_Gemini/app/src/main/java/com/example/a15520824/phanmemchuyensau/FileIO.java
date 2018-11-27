package com.example.a15520824.phanmemchuyensau;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by QuocTuyen on 6/17/2018.
 */

public class FileIO {
    private static FileIO _instance;
    private static final int READ_BLOCK_SIZE = 100;
    private FileIO(){}
    public static FileIO Instance(){
        if (_instance == null){
            _instance = new FileIO();
        }
        return _instance;
    }

    public ArrayList<ActionModel> readFile(Context context, String fileName) throws Exception{
        ArrayList<ActionModel> listResult = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
        String strLine;

        while ((strLine = br.readLine()) != null){
            String[] result = strLine.split("-");
            listResult.add(new ActionModel(result[0], result[1], result[2]));
        }
        return listResult;
    }

    public void writeFile(Context context, String fileName, ArrayList<ActionModel> appsList) throws Exception{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(fileName, MODE_PRIVATE)));
        for (Object item : appsList) {
            writer.write(item.toString());
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<PackageInfo> getInstalledApps(Context context) {
        ArrayList<PackageInfo> res = new ArrayList<PackageInfo>();

        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            if ((isSystemPackage(p) == false)) {
               res.add(p);
            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}
