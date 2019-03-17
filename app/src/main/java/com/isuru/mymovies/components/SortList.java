package com.isuru.mymovies.components;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 *
 * -- Sort component
 */

public class SortList{

    private AlertDialog.Builder sortDialogBuilder;
    public SortInterface delegate = null; //Call back interface
    CharSequence[] charSequenceItems;   // CharSequence arry to store sort elements

    public SortList(Context mContext, CharSequence[] sortItemArray) {
        sortDialogBuilder = new AlertDialog.Builder(mContext);
        sortDialogBuilder.setTitle("Sort order");   // Title of the sort menu
        charSequenceItems = sortItemArray;
    }

    public void showSortingDialog (SortInterface sortItem) {

        int set_selected_item = 0;  // Initialize the selected item

        sortDialogBuilder.setSingleChoiceItems(charSequenceItems, set_selected_item, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();   // Close the sort menu after clicking on an option
                delegate.clickedItem(item);
            }
        });

        delegate = sortItem;

        sortDialogBuilder.create().show();  // Show the sort menu
    }
}
