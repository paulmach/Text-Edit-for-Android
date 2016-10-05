package org.paulmach.textedit;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/* FontTypePreference
 * 		Special file type preference so that each option is actually 
 * 		an example of the font.
 */
public class FontTypePreference extends DialogPreference
{
	private List<String> fonts = null;
	private int selected;
	
	// This is the constructor called by the inflater
	public FontTypePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// figure out what is currently selected
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
		String font = sharedPref.getString("font", "Monospace");
		
		if (font.equals("Serif"))
			selected = 1;
		else if (font.equals("Sans Serif"))
			selected = 2;
		else  
       		selected = 0;	
	}
	
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder){
	    // Data has changed, notify so UI can be refreshed!
		builder.setTitle("Choose a font type");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
				
				if (selected == 0)
					editor.putString("font", "Monospace");
				else if (selected == 1)
					editor.putString("font", "Serif");
				else  
					editor.putString("font", "Sans Serif");
				
				editor.commit();
				
				notifyChanged();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// do nothing on a cancel 
			}
		});
					
		// load the font names
		String[] arrayOfFonts = { "Monospace", "Serif", "Sans Serif" };
        fonts = Arrays.asList(arrayOfFonts);

		FontTypeArrayAdapter adapter = new FontTypeArrayAdapter(getContext(), android.R.layout.simple_list_item_single_choice, fonts);
		builder.setSingleChoiceItems(adapter, selected, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// make sure we know what is selected
				selected = which;
			}
		});		
    } // onPrepareDialogBuilder()
	

	/********************************************************************
	 * class FontTypeArrayAdapter
	 * 		Array adapter for font type picker */
	public class FontTypeArrayAdapter extends ArrayAdapter<String>
	{
		// just a basic constructor
		public FontTypeArrayAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
			
		} // end constructor one
		
		/****************************************************************
		 * getView
		 * 		the overroad getView method */
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			// get the view that would normally be returned
			View v = super.getView(position, convertView, parent);
			final TextView tv = (TextView) v;
			
			final String option = tv.getText().toString();			
			if (option.equals("Serif"))
				tv.setTypeface(Typeface.SERIF);
			else if (option.equals("Sans Serif"))
				tv.setTypeface(Typeface.SANS_SERIF);
			else if (option.equals("Monospace"))
				tv.setTypeface(Typeface.MONOSPACE);

			// general options
			tv.setTextColor(Color.BLACK);
			tv.setPadding(10, 3, 3, 3);
		
			return v;	
		} // end getView()
				
	} // end class FontTypeArrayAdapter
	
} // end class FontTypePreference