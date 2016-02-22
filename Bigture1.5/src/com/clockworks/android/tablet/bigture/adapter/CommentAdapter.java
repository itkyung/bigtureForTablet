package com.clockworks.android.tablet.bigture.adapter;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommentEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkDetailCell;
import com.clockworks.android.tablet.bigture.views.common.CommentCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentAdapter extends BaseAdapter {
	private ArrayList<CommentEntity> comments;
	private Context context;
	
	public CommentAdapter(Context context){
		this.context = context;
		this.comments = new ArrayList<CommentEntity>();
	}
	
	public void setComments(ArrayList<CommentEntity> comments){
		this.comments.clear();
		this.comments.addAll(comments);
	}
	
	@Override
	public int getCount() {
		
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentCell cell = (CommentCell)convertView;
		
		if (cell == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			cell = (CommentCell)inflater.inflate(R.layout.cell_comment, parent, false);
		}
		CommentEntity entity = (CommentEntity)getItem(position);
		cell.updateCell(entity);
		
		return cell;
	}

	
	
}
