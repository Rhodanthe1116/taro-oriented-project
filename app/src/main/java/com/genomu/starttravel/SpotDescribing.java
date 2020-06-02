package com.genomu.starttravel;

import android.app.Activity;

public class SpotDescribing {
    private int whichScene;
    private String[] packs;
    private int[] img_ids ;


    private String[] queries;

    public SpotDescribing(Activity activity, int whichScene) {
        this.whichScene = whichScene;
        packs = activity.getResources().getStringArray(R.array.res_txt_pack);
        queries = activity.getResources().getStringArray(R.array.suggest_queries);
        img_ids = new int[]{R.drawable.ph_jp,R.drawable.ph_kr,R.drawable.ph_ne,R.drawable.ph_se,
                            R.drawable.tx_00,R.drawable.tx_01,R.drawable.tx_02,R.drawable.tx_03};
    }

    public String getParagraph(){
        return packs[whichScene*2+1];
    }

    public String getHeading(){
        return packs[whichScene*2];
    }

    public String getQuery() {
        return queries[whichScene];
    }
    public int getImgResId(){
        return img_ids[whichScene];
    }

}
