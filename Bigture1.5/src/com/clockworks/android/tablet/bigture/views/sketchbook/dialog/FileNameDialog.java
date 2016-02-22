package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

public class FileNameDialog extends Dialog{
	Context context;
	OnOkListener listener;
	EditText editFilename;
	
	String filename;
	
	public FileNameDialog(Context context, String filename){
		super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.filename = filename;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_ske_filename);
		
		LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		
		editFilename = (EditText)findViewById(R.id.editFilename);
		editFilename.setText(filename);
		
		Button btnOK = (Button)findViewById(R.id.btnOk);
		btnOK.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				String filename = editFilename.getText().toString();
				if (filename.length() > 0)
				{
					if (listener != null)
						listener.onOK(FileNameDialog.this, filename);
				}
				else
				{
					BigtureEnvironment.showAlertMessage(context, "Enter file name.");
				}
			}
		});
		
		findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		
	}
	
	public void setOnOkListener(OnOkListener listener)
	{
		this.listener = listener;
	}
	
	public interface OnOkListener
	{
		void onOK(FileNameDialog dialog, String filename);
	}
	
}
