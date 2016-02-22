package com.clockworks.android.tablet.bigture.views.sketchbook.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BackgroundShaderStroke;
import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.action.UserAction;
import com.clockworks.android.tablet.bigture.views.sketchbook.background.BackgroundFill;
import com.clockworks.android.tablet.bigture.views.sketchbook.background.BackgroundImage;
import com.clockworks.android.tablet.bigture.views.sketchbook.sticker.CustomSticker;
import com.clockworks.android.tablet.bigture.views.sketchbook.sticker.NormalSticker;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BackgroundStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BitmapFillStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.EraserStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.FillStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.LinearGradientStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.NeonStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.NormalStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.RandomBitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.RotatedBitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.RotatedRandomBitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.ShaderStroke;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class UserDrawingManager{
	public static int DRAWING_MODE_PEN = 101;
	public static int DRAWING_MODE_BACKGROUND_PEN = 102;
	public static int DRAWING_MODE_WBRUSH = 201;
	public static int DRAWING_MODE_BACKGROUND_WBRUSH = 202;
	public static int DRAWING_MODE_CRAYON = 301;
	public static int DRAWING_MODE_BACKGROUND_CRAYON = 302;
	
	public static int DRAWING_MODE_NEON = 401;
	public static int DRAWING_MODE_RAINBOW = 402;
	public static int DRAWING_MODE_BITMAP_BRUSH = 410;
	public static int DRAWING_MODE_BITMAP_RANDOM_BRUSH = 411;
	public static int DRAWING_MODE_ROTATED_BITMAP_BRUSH = 412;
	public static int DRAWING_MODE_ROTATED_RANDOM_BITMAP_BRUSH = 413;
	
	public static int DRAWING_MODE_FILL_SOLID = 501;
	public static int DRAWING_MODE_FILL_PATTERN = 502;

	public static int DRAWING_MODE_ERASE = 601;
	public static int DRAWING_MODE_STICKER = 701;
	public static int DRAWING_MODE_CUSTOM_STICKER = 702;
	public static int DRAWING_MODE_YOU = 801;
	public static int DRAWING_MODE_AQUA = 901;

	protected static UserDrawingManager manager;
	protected Context context;
	
	protected List<UserDrawing> drawingList;
	protected List<UserDrawing> backgroundList;
	
	protected Stack<UserAction> undoStack;
	protected Stack<UserAction> redoStack;
	
	protected boolean modified = false;
	public boolean invalidate = false;
	protected DrawingChangeListener listener;
	
	public int canvasWidth;
	public int canvasHeight;
	
	public float density;
	// 1024 x 768 기준
	public float viewScale;
	
	protected Map<String, BigtureAsset> assetMap;
	
	protected UserDrawingManager(Context context){
		this.context = context;
		
		undoStack = new Stack<UserAction>();
		redoStack = new Stack<UserAction>();
		
		drawingList  = new ArrayList<UserDrawing>();
		backgroundList = new ArrayList<UserDrawing>();
		
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		
		if (metrics.widthPixels >= metrics.heightPixels)
		{
			canvasHeight = metrics.heightPixels;
			canvasWidth  = canvasHeight * 4 / 3;
		}
		else
		{
			canvasWidth = metrics.widthPixels;
			canvasHeight = canvasWidth * 3 / 4;
		}

		density = metrics.density;
		viewScale = canvasWidth / 1024.f;

		assetMap = new HashMap<String, BigtureAsset>();
		initializeAsset();
	}
	
	public static UserDrawingManager createInstance(Context context)
	{
		manager = new UserDrawingManager(context);
		
		return manager;
	}
	
	public static void deleteInstance()
	{
		manager = null;
	}
	
	public static UserDrawingManager getInstance()
	{
		return manager;
	}

	public void setCanvasSize(int canvasWidth, int canvasHeight)
	{
		this.canvasWidth  = canvasWidth;
		this.canvasHeight = canvasHeight;
		
		this.viewScale = (float)canvasWidth / 1024.f;
	}
	
	public Point getCanvasSize()
	{
		return new Point(this.canvasWidth, this.canvasHeight);
	}
	
	public float getViewScale()
	{
		return viewScale;
	}
	
	public float toDeviceUnit(float value)
	{
		return viewScale * value;
	}
	
	public float toLogicalUnit(float value)
	{
		return value / viewScale;
	}
	
	public void toDeviceUnit(PointF pointSrc, PointF pointDst)
	{
		pointDst.x = pointSrc.x * viewScale;
		pointDst.y = pointSrc.y * viewScale;
	}
	
	public void toLogicalUnit(PointF pointSrc, PointF pointDst)
	{
		pointDst.x = pointSrc.x / viewScale;
		pointDst.y = pointSrc.y / viewScale;		
	}
	
	public int getObjectCount()
	{
		return drawingList.size() + backgroundList.size();
	}
	
	public UserDrawing getDrawingObject(int pos)
	{
		if (pos < backgroundList.size())
		{
			return backgroundList.get(pos);
		}
		else
		{
			int newPos = pos - backgroundList.size();
			if(drawingList.size() <= newPos){
				return null;
			}
			return drawingList.get(newPos);
		}
	}
	
	public void drawBackground(Canvas canvas)
	{
		for (int i = 0; i < backgroundList.size(); i++)
		{
			if (isBackgroundVisible(i))
				backgroundList.get(i).draw(canvas);
		}
	}
	
	public void drawForeground(Canvas canvas){
		for (UserDrawing drawing : drawingList)
		{
			if (drawing instanceof NormalSticker)
			{
				if (((NormalSticker)drawing).getSelected() == false)
					drawing.draw(canvas);
			}
			else
			{
				drawing.draw(canvas);
			}
		}
	}
	
	public boolean isTopLevelObject(UserDrawing userDrawing){
		if (drawingList.size() > 0)
		{
			return drawingList.get(drawingList.size()-1) == userDrawing;
		}
		
		return false;
	}

	public boolean hasEraserStroke(){
		for (UserDrawing drawing : drawingList)
		{
			if (drawing instanceof EraserStroke)
			{
				return true;
			}
		}

		return false;
	}
	
	public boolean hasEraserStrokeAbove(UserDrawing userDrawing){
		boolean check = false;
		boolean found = false;
		
		for (UserDrawing drawing : drawingList){
			if (drawing == userDrawing)
				check = true;
			
			if (check == true && drawing instanceof EraserStroke){
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public void drawAbove(Canvas canvas, UserDrawing userDrawing){
		boolean draw = false;
		
		for (UserDrawing drawing : backgroundList){
			if (drawing == userDrawing)
			{
				draw = true;
			}
			else if (draw == true)
			{
				drawing.draw(canvas);
			}
		}
		
		for (UserDrawing drawing : drawingList)
		{
			if (drawing == userDrawing)
			{
				draw = true;
			}
			else if (draw == true)
			{
				drawing.draw(canvas);
			}
		}
	}
	
	public void drawBelow(Canvas canvas, UserDrawing userDrawing, boolean includeBackgroundObject){
		if (includeBackgroundObject){
			for (UserDrawing drawing : backgroundList){
				if (drawing == userDrawing)
				{
					return;
				}
				else
				{
					drawing.draw(canvas);
				}
			}
		}
		
		for (UserDrawing drawing : drawingList)
		{
			if (drawing == userDrawing)
			{
				return;
			}
			else
			{
				drawing.draw(canvas);
			}			
		}
	}
	
	public void draw(Canvas canvas, boolean drawBackground)
	{	
		// ��� ��ü�� �����ؼ� �׸� ������ ����
		if (drawBackground)
		{
			for (UserDrawing drawing : backgroundList)
			{
				drawing.draw(canvas);
			}
			// ����� �׸���.
			drawBackground(canvas);

			Bitmap bmForeground = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas2 = new Canvas(bmForeground);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setDither(true);
			canvas2.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

			// ����� �׸���.
			drawForeground(canvas2);
			
			canvas.drawBitmap(bmForeground, 0, 0, paint);
//			bmForeground.recycle();
//			bmForeground = null;
		}
		else
		{
			for (UserDrawing drawing : drawingList)
			{
				if (drawing instanceof NormalSticker)
				{
					if (((NormalSticker)drawing).getSelected() == false)
						drawing.draw(canvas);
				}
				else
				{
					drawing.draw(canvas);
				}
			}
		}		
	}
	
	public NormalSticker findSticker(float x, float y)
	{
		NormalSticker sticker = null;
		
		for (UserDrawing drawing : drawingList)
		{
			if (drawing instanceof NormalSticker)
			{
				if (((NormalSticker)drawing).hitTest(x, y))
					sticker = (NormalSticker)drawing;
			}
		}
		
		return sticker;
	}

	public void add(UserDrawing userDrawing)
	{
		// �׸��� ����Ǿ����� ǥ���Ѵ�.
		modified = true;
		
		UserAction action = new UserAction(UserAction.ACTION_REGISTER, userDrawing);
		
		undoStack.add(action);
		redoStack.clear();

		this.invalidate = true;

		// ��� ��ü������ Ȯ���Ѵ�.
		if (userDrawing.isBackgroundObject())
		{
			backgroundList.add(userDrawing);
		}
		else
		{
			drawingList.add(userDrawing);
		}

		
		if (listener != null)
			listener.onChangeDrawing(this);
	}
	
	public void deleteObject(UserDrawing userDrawing)
	{
		modified = true;

		UserAction action = new UserAction(UserAction.ACTION_DELETE, userDrawing);
		
		undoStack.add(action);
		redoStack.clear();
		
		this.invalidate = true;
			
		if (userDrawing.isBackgroundObject())
			backgroundList.remove(userDrawing);
		else
			drawingList.remove(userDrawing);
			
		if (listener != null)
			listener.onChangeDrawing(this);
	}
	
	public void layerDown(NormalSticker sticker)
	{
		int crntIndex = -1;
		
		for (int i = 0; i < drawingList.size(); i++)
		{
			UserDrawing drawing = drawingList.get(i);
			if (drawing == sticker)
			{
				crntIndex = i;
				break;
			}
		}
		
		if (crntIndex > 0)
		{
			modified = true;

			UserAction action = new UserAction(sticker, crntIndex, crntIndex-1);
			
			undoStack.add(action);
			redoStack.clear();
			
			this.invalidate = true;
			
			drawingList.remove(sticker);
			drawingList.add(crntIndex-1, sticker);
		}
	}
	
	public void layerUp(NormalSticker sticker)
	{
		int crntIndex = -1;
		
		for (int i = 0; i < drawingList.size(); i++)
		{
			UserDrawing drawing = drawingList.get(i);
			if (drawing == sticker)
			{
				crntIndex = i;
				break;
			}
		}
		
		if (crntIndex >= 0 && crntIndex < drawingList.size()-1)
		{
			modified = true;

			UserAction action = new UserAction(sticker, crntIndex, crntIndex+1);
			
			undoStack.add(action);
			redoStack.clear();
			
			this.invalidate = true;
			
			drawingList.remove(sticker);
			drawingList.add(crntIndex+1, sticker);
		}
	}
	
	public void changeMatrix(UserDrawing userDrawing, Matrix orgMatrix, Matrix newMatrix)
	{
		modified = true;

		UserAction action = new UserAction(userDrawing, orgMatrix, newMatrix);
		
		undoStack.add(action);
		redoStack.clear();
		
		if (listener != null)
			listener.onChangeDrawing(this);
	}
	
	public void clear()
	{
//		if (drawingList.size() > 0 || backgroundList.size() > 0)
//		{
//			// �׸��� ����Ǿ����� ǥ���Ѵ�.
//			modified = true;
//		}
		
		modified = false;
		
		undoStack.clear();
		redoStack.clear();
		
		drawingList.clear();
		backgroundList.clear();
		
		if (listener != null)
			listener.onChangeDrawing(this);

		invalidate = true;
	}
	
	public boolean isModified()
	{
		return modified;
	}
	
	public void setModified(boolean modified)
	{
		this.modified = modified;
	}
	
	public void clearUndoBuffer()
	{
		undoStack.clear();

		if (listener != null)
			listener.onChangeDrawing(this);
	}

	public UserDrawing undo()
	{
		if (!undoStack.isEmpty())
		{
			modified = true;

			UserAction userAction = undoStack.pop();
			redoStack.add(userAction);

			UserDrawing userDrawing = userAction.getUserDrawing();

			if (userAction.action == UserAction.ACTION_REGISTER)
			{
				if (userDrawing.isBackgroundObject())
					backgroundList.remove(userDrawing);
				else
					drawingList.remove(userDrawing);				
			}
			else if (userAction.action == UserAction.ACTION_DELETE)
			{
				if (userDrawing.isBackgroundObject())
					backgroundList.add(userDrawing);
				else
					drawingList.add(userDrawing);
			}
			else if (userAction.action == UserAction.ACTION_LAYER)
			{
				int index = userAction.orgLayerPosition;
				if (drawingList.remove(userDrawing))
					drawingList.add(index, userDrawing);
			}
			else if (userAction.action == UserAction.ACTION_MATRICS)
			{
				if (userDrawing instanceof NormalSticker)
				{
					NormalSticker sticker = (NormalSticker)userDrawing;
					sticker.setMatrix(userAction.orgMatrix);
				}
			}
			
			invalidate = true;
			
			if (listener != null)
				listener.onChangeDrawing(this);
			
			return userDrawing;
		}
		
		return null;
	}
	
	public UserDrawing redo()
	{
		if (!redoStack.isEmpty())
		{
			// �׸��� ����Ǿ����� ǥ���Ѵ�.
			modified = true;

			UserAction userAction = redoStack.pop();
			undoStack.add(userAction);
			
			UserDrawing userDrawing = userAction.getUserDrawing();
			
			if (userAction.action == UserAction.ACTION_REGISTER)
			{
				if (userDrawing.isBackgroundObject())
					backgroundList.add(userDrawing);
				else
					drawingList.add(userDrawing);
			}
			else if (userAction.action == UserAction.ACTION_DELETE)
			{
				if (userDrawing.isBackgroundObject())
					backgroundList.remove(userDrawing);
				else
					drawingList.remove(userDrawing);
			}
			else if (userAction.action == UserAction.ACTION_LAYER)
			{
				int index = userAction.newLayerPosition;
				if (drawingList.remove(userDrawing))
					drawingList.add(index, userDrawing);
			}
			else if (userAction.action == UserAction.ACTION_MATRICS)
			{
				if (userDrawing instanceof NormalSticker)
				{
					NormalSticker sticker = (NormalSticker)userDrawing;
					sticker.setMatrix(userAction.newMatrix);
				}
			}
			
			invalidate = true;

			if (listener != null)
				listener.onChangeDrawing(this);
			
			return userDrawing;
		}
		
		return null;
	}
	
	public boolean canUndo()
	{
		return !undoStack.isEmpty();
	}
	
	public boolean canRedo()
	{
		return !redoStack.isEmpty();
	}
	
	public void setDrawingChangeListener(DrawingChangeListener listener)
	{
		this.listener = listener;
	}
	
	public interface DrawingChangeListener
	{
		void onChangeDrawing(UserDrawingManager drawingManager);
	}
	
	private boolean isBackgroundVisible(int pos){
		boolean result = true;
		
		for (int i = pos+1; i < backgroundList.size(); i++)
		{
			UserDrawing drawing = backgroundList.get(i);
			if ((drawing instanceof BackgroundFill) || (drawing instanceof BackgroundImage)){
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	public List<File> getResourceFileList(){
		List<File> fileList = new ArrayList<File>();
		
		for (int i = 0; i < backgroundList.size(); i++){
			UserDrawing drawing = backgroundList.get(i);
			if (isBackgroundVisible(i) && (drawing instanceof BackgroundImage))
			{
				BackgroundImage bg = (BackgroundImage)drawing;
				String fileName = bg.getResourceFilePath();
				
				if (fileName != null)
				{
					File file = new File(fileName);
					
					boolean duplicated = false;
					for (int j = 0; j < fileList.size(); j++)
					{
						if (fileList.get(j).equals(file))
						{
							duplicated = true;
							break;
						}
					}
					
					if (!duplicated)
					{
						fileList.add(file);
					}
				}
			}
		}
		
		for (int i = 0; i < drawingList.size(); i++)
		{
			UserDrawing drawing = drawingList.get(i);
			if (drawing instanceof CustomSticker){
				CustomSticker obj = (CustomSticker)drawing;
				String fileName = obj.getResourceFilePath();
				File file = new File(fileName);
				
				boolean duplicated = false;
				for (int j = 0; j < fileList.size(); j++)
				{
					if (fileList.get(j).equals(file))
					{
						duplicated = true;
						break;
					}
				}
				
				if (!duplicated)
				{
					fileList.add(file);
				}
			}
		}
		
		return fileList;
	}
	
	public String toXML()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<BigtureDrawing width=\"" + canvasWidth + "\" height=\"" + canvasHeight + "\">");
		
		for (UserDrawing drawing : backgroundList)
		{
			sb.append(drawing.toXML());
		}
		
		for (UserDrawing drawing : drawingList)
		{
			String xml = drawing.toXML();
			Log.d("xml", xml);
			sb.append(drawing.toXML());
		}
		
		sb.append("</BigtureDrawing>");
		
		return sb.toString();
	}
	
	public void parse(Context context, Element element){

		Element child = XmlUtil.getFirstChildElement(element);
		
		while (child != null)
		{
			String elementName = XmlUtil.getElementName(child);
			UserDrawing obj = null;
			Log.d("elementName", elementName);
			
			if (elementName.equals("NormalStroke"))
			{
				obj = new NormalStroke();
				obj.parse(child);
			}
			else if (elementName.equals("EraserStroke"))
			{
				obj = new EraserStroke();
				obj.parse(child);
			}
			else if (elementName.equals("BackgroundStroke"))
			{
				obj = new BackgroundStroke();
				obj.parse(child);
			}
			else if (elementName.equals("BackgroundShaderStroke"))
			{
				obj = new BackgroundShaderStroke();
				obj.parse(child);
			}
			else if (elementName.equals("ShaderStroke"))
			{
				obj = new ShaderStroke(context);
				obj.parse(child);
			}
			else if (elementName.equals("FillStroke"))
			{
				obj = new FillStroke();
				obj.parse(child);
			}
			else if (elementName.equals("BitmapFillStroke"))
			{
				obj = new BitmapFillStroke(context);
				obj.parse(child);
			}
			else if (elementName.equals("NeonStroke"))
			{
				obj = new NeonStroke();
				obj.parse(child);
			}
			else if (elementName.equals("LinearGradientStroke"))
			{
				obj = new LinearGradientStroke();
				obj.parse(child);
			}
			else if (elementName.equals("BitmapStroke"))
			{
				obj = new BitmapStroke(context);
				obj.parse(child);
			}
			else if (elementName.equals("RotatedBitmapStroke"))
			{
				obj = new RotatedBitmapStroke(context);
				obj.parse(child);
			}
			else if (elementName.equals("RandomBitmapStroke"))
			{
				obj = new RandomBitmapStroke(context);
				obj.parse(child);
			}
			else if (elementName.equals("RotatedRandomBitmapStroke"))
			{
				obj = new RotatedRandomBitmapStroke(context);
				obj.parse(child);
			}
			else if (elementName.equals("NormalSticker"))
			{
				obj = new NormalSticker(context);
				obj.parse(child);
			}
			else if (elementName.equals("CustomSticker"))
			{
				obj = new CustomSticker(context);
				obj.parse(child);
			}
			else if (elementName.equals("BackgroundFill"))
			{
				obj = new BackgroundFill();
				obj.parse(child);
			}
			else if (elementName.equals("BackgroundImage"))
			{
				obj = new BackgroundImage(context);
				obj.parse(child);
			}
			
			if (obj != null)
			{
				if (obj.isBackgroundObject())
					backgroundList.add(obj);
				else
					drawingList.add(obj);
			}
			
			child = XmlUtil.getNextSiblingElement(child);
		}
	}
	
	private void initializeAsset()
	{
		BigtureAsset asset = null;
		
		asset = new BigtureAsset("banner", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_banner.png", 320, 320, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("redlion", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_redlion.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("redtail", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_redtail.png", 500, 220, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("blacktip", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_blacktip.png", 500, 220, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("boxfish", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_boxfish.png", 300, 200, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("shark", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_shark.png", 500, 200, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("jelly", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_jellyfish.png", 280, 500, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("penguin", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_penguin.png", 244, 500, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("walrus", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_walrus.png", 500, 324, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("dolphin", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_popup_sticker_hanhwa_b_dolphin.png", 500, 280, 0.5f);
		assetMap.put(asset.assetId, asset);

		asset = new BigtureAsset("balloon", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_balloon.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("flower01", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_flower01.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("flower02", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_flower02.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("leaf01", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_leaf01.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("leaf02", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_leaf02.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("broccoli", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_broccoli.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("orange", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_orange.png", 400, 400, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("kiwi", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_kiwi.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("lotus", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_lotusroot.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("ramyeon", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_ramyeon.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("wrap002", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_wrap002.png", 300, 291, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("wrap001", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_wrap001.png", 300, 293, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cap001", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cap001.png", 300, 299, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cap002", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cap002.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cap003", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cap003.png", 300, 301, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cap004", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cap004.png", 250, 252, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cap005", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cap005.png", 300, 302, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cap006", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cap006.png", 70, 350, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cap007", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cap007.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cork001", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_awesome_cork001.png", 300, 332, 0.5f);
		assetMap.put(asset.assetId, asset);
		
		
		asset = new BigtureAsset("fabric1", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_fabric01.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("fabric2", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_fabric02.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("cellophane", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_cellophane.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("velcro", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_velcro.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("plastic", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_plastic.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("wire1", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_wire01.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("wire2", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_wire02.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("rope", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_rope.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("tape", BigtureAsset.ASSET_TYPE_STICKER, "sketchbook_tool_sticker_you_tape.png", 300, 300, 0.5f);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("crayon1", BigtureAsset.ASSET_TYPE_STROKE_PATTERN, "crayon_pattern.png", 100, 100);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("fill_heart", BigtureAsset.ASSET_TYPE_FILL_PATTERN, "sketchbook_tool_painttool_pattern_heart.png", 100, 100);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("fill_check", BigtureAsset.ASSET_TYPE_FILL_PATTERN, "sketchbook_tool_painttool_pattern_check01.png", 100, 100);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("fill_cloud", BigtureAsset.ASSET_TYPE_FILL_PATTERN, "sketchbook_tool_painttool_pattern_cloud.png", 200, 200);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("fill_dot", BigtureAsset.ASSET_TYPE_FILL_PATTERN, "sketchbook_tool_painttool_pattern_dot.png", 200, 200);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("fill_star", BigtureAsset.ASSET_TYPE_FILL_PATTERN, "sketchbook_tool_painttool_pattern_star.png", 150, 150);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("fill_stripe1", BigtureAsset.ASSET_TYPE_FILL_PATTERN, "sketchbook_tool_painttool_pattern_stripe01.png", 100, 100);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("fill_stripe2", BigtureAsset.ASSET_TYPE_FILL_PATTERN, "sketchbook_tool_painttool_pattern_stripe02.png", 100, 100);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("magic_birdfootprint", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_birdfooprint.png", 53, 83);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_snow", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_snow.png", 63, 70);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_bubble", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_bubble.png", 60, 60);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_hair", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_hair.png", 180, 174);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_leaf1", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_leaf_01.png", 99, 149);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_leaf2", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_leaf_02.png", 99, 149);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_leaf3", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_leaf_03.png", 99, 149);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_leaf4", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_leaf_04.png", 99, 149);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_leaf5", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_leaf_05.png", 99, 149);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("magic_drop1", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_droppaint_01.png", 280, 193);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_drop2", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_droppaint_02.png", 162, 158);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_drop3", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_droppaint_03.png", 312, 217);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("magic_drop4", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_droppaint_04.png", 181, 177);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("magic_twinkle", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_twinkle.png", 90, 136);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("magic_rainbow", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_popup_magictool_pattern_rainbow.png", 48, 48);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("korean_text001", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_books001.png", 170, 108);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("korean_text002", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_books002.png", 205, 124);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("korean_text003", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_brushes001.png", 97, 263);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("korean_text004", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_flower001.png", 229, 225);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("korean_text005", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_inkstone001.png", 162, 144);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("korean_text006", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_pottery001.png", 178, 180);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("korean_text007", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_pottery002.png", 168, 321);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("korean_text008", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_koreanpainting_pottery003.png", 143, 343);
		assetMap.put(asset.assetId, asset);
		
		
		asset = new BigtureAsset("island_text001", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_001_p.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_text002", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_002_u.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_text003", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_003_e.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_text004", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_004_r.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("island_text005", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_005_t.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_text006", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_006_o.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_text007", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_007_i.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_text008", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_text_008_c.png", 140, 240);
		assetMap.put(asset.assetId, asset);
		
		asset = new BigtureAsset("island_tile", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_tile001.png", 202, 590);
		assetMap.put(asset.assetId, asset);
	
		asset = new BigtureAsset("island_wheel", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_wheel001.png", 300, 300);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_column", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_column001.png", 39, 390);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_flower", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_flower001.png", 256, 350);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_coqui", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_coqui001.png", 350, 280);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_lantern", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_lantern001.png", 252, 400);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_archi", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_archi001.png", 500, 192);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_shape", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_shape001.png", 300, 300);
		assetMap.put(asset.assetId, asset);
		asset = new BigtureAsset("island_paper", BigtureAsset.ASSET_TYPE_MAGIC_BRUSH, "sketchbook_tool_sticker_enchant_paper001.png", 300, 323);
		assetMap.put(asset.assetId, asset);
		
		
	}
	
	public BigtureAsset getAsset(String key)
	{
		return assetMap.get(key);
	}
}
