package com.clockworks.android.tablet.bigture;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.ArtworkType;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.PuzzlePart;
import com.clockworks.android.tablet.bigture.common.ShareType;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;

import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PuzzleArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.ArtClassHandleTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.ArtworkRegisterTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.ArtworkRegisterTask.ArtworkRegisterListener;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.utils.ZipUtil;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;
import com.clockworks.android.tablet.bigture.views.sketchbook.FileStoreTask;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.views.sketchbook.WorkLayerView2;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.DrawingMode;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager.DrawingChangeListener;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.AquaStickerDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.AreaPaintDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.AwesomeStickerDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.BackgroundDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.BasicBrushDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.BrushSizeDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.ColorPaletteDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.FileNameDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.FileOpenDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.IslandStickerDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.KoreanStickerDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.MagicBrushDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.PaletteView;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.ShareBigtureDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.ShareBigtureDialog2;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.SketchbookSaveShareMenuDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.WorldStickerDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.YousStickerDialog;







import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SketchbookActivity extends AbstractBigtureActivity implements DrawingChangeListener{

	private int LeftMarginForMenu = 477;
	private Context context;
	DrawingPurpose drawingPurpose;
	int canvasWidth;
	int canvasHeight;
	WorkLayerView2 workLayerView;
	ImageView selectedColorView;
	
	View leftMenu;
	View leftContainer;
	
	boolean shared = false;
	String bigtureFileName;
	MenuListener menuListener;
	
	Button tbBrushSize;
	View sketchbookRoot;
	
	BigtureEnvironment.PhotoPickupListener photoListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_sketchbook);
		
		sketchbookRoot = findViewById(R.id.sketchbookRoot);
		
	//	sketchbookRoot.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		
		findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goBack();
			}
		});
		
		workLayerView = (WorkLayerView2)findViewById(R.id.workLayerView1);
		
		// initialize canvas
		initCanvas();

		// initialize child view and controls
		initChildViews();
		
		if (savedInstanceState != null){
			String xml = savedInstanceState.getString("xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			try{
				InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));  
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(is);
				Element root = dom.getDocumentElement();

				UserDrawingManager.getInstance().parse(this, root);				
			}catch(Exception e){
				e.printStackTrace();
			}

			workLayerView.setNeedDraw(true);

			int photoImportMode = savedInstanceState.getInt("photoImportMode");

			if (photoImportMode == 1)
				photoListener = new PhotoListenerForSticker();
			else if (photoImportMode == 2)
				photoListener = new PhotoListenerForBackground();

			int selectedToolId = savedInstanceState.getInt("selectedToolId");
		//	toolbarOnClickListener.setSelectedToolButtonId(selectedToolId);

			drawingPurpose = savedInstanceState.getParcelable("drawingPurpose");
		}else{
			Bundle bundle = getIntent().getExtras();
	
			if (bundle != null){
				drawingPurpose = bundle.getParcelable("drawingPurpose");
				if (drawingPurpose != null && drawingPurpose.reason == DrawingPurpose.ARTCLASS && drawingPurpose.puzzleId != null){
					ArtClassHandleTask task = new ArtClassHandleTask(new ArtClassHandleTask.Callback(){
						@Override
						public void onComplete(ArtClassHandleTask handler, boolean result)
						{
							WaitDialog.sweepWaitDialog();
							if (result)
								readyPuzzle(handler.puzzleArtworkEntity);
							else
								BigtureEnvironment.showAlertMessage(SketchbookActivity.this, 99);
						}
					});
	
					WaitDialog.showWailtDialog(SketchbookActivity.this, true);
					task.getPuzzleArtwork(drawingPurpose.puzzleId, drawingPurpose.puzzlePart);
				}
			}
			
		}
		
		workLayerView.stickerToolbar = (LinearLayout)findViewById(R.id.stickerToolbar);
		workLayerView.deleteButton = (ImageView)findViewById(R.id.imageDelete);		
		
		if(drawingPurpose.reason == DrawingPurpose.POSTCARD){
			ImageView iv = (ImageView)findViewById(R.id.btnShareImg);
			iv.setImageDrawable(getResources().getDrawable(R.drawable.sketchbook_func_btn_icon_postcard));
			
			TextView tv = (TextView)findViewById(R.id.btnShareTitle);
			tv.setText(getResources().getString(R.string.Send));
		}
		
	}


	@Override
	protected void onDestroy(){
		
		super.onDestroy();
	}


//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		if (keyCode == KeyEvent.KEYCODE_BACK)
//		{
//			this.setResult(Activity.RESULT_CANCELED);
//			finish();
//			return true;
//		}
//		
//		return super.onKeyDown(keyCode, event);
//	}

	
	

	private void readyPuzzle(final PuzzleArtworkEntity entity){
		WaitDialog.showWailtDialog(this, true);

		String imageURL = entity.getSketchImageURL();
		BitmapDownloader.getInstance().downloadBitmap(imageURL, null, new BitmapDownloader.BitmapDownloaderListener(){
			@Override
			public void onComplete(Bitmap bitmap){
				WaitDialog.sweepWaitDialog();

				if (bitmap != null){
					Bitmap bitmapNew = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmapNew);

					Paint paint = new Paint();
					paint.setAntiAlias(true);
					paint.setDither(true);
					paint.setAlpha(77);

					Rect dst = new Rect(0,0,canvasWidth,canvasHeight);
					Rect src = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());

					if (entity.part.equals(PuzzlePart.TOP_LEFT)){
						src.right = bitmap.getWidth() / 2;
						src.bottom = bitmap.getHeight() / 2;
					}else if (entity.part.equals(PuzzlePart.TOP_RIGHT)){
						src.left = bitmap.getWidth() / 2;
						src.bottom = bitmap.getHeight() / 2;
					}else if (entity.part.equals(PuzzlePart.BOTTOM_LEFT)){
						src.top = bitmap.getHeight() / 2;
						src.right = bitmap.getWidth() / 2;
					}else if (entity.part.equals(PuzzlePart.BOTTM_RIGHT)){
						src.top = bitmap.getHeight() / 2;
						src.left = bitmap.getWidth() / 2;
					}

					canvas.drawBitmap(bitmap, src, dst, paint);
					bitmap.recycle();
					bitmap = null;

					paint = null;

					String filePath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + "puzzle.image";
					ImageUtil.saveBitmap(bitmapNew, filePath);

					bitmapNew.recycle();
					bitmapNew = null;

					workLayerView.setBackgroundBitmap(filePath);

					UserDrawingManager.getInstance().setModified(false);
					UserDrawingManager.getInstance().clearUndoBuffer();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == BigtureEnvironment.REQ_CODE_TAKE_PICTURE){
			if (resultCode == RESULT_OK){
				String tempPath = FileUtil.getTempPath(this) + "/taken.image";
				File file = new File(tempPath);
				Uri imageUri = Uri.fromFile(file);

				if (photoListener != null && photoListener.isCropImage()){
					Intent intent = new Intent(this, CropActivity.class);
					intent.putExtra("imageUri", imageUri);
					intent.putExtra("returnImageWidth", 200);
					intent.putExtra("returnImageHeight", 200);
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);

					startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				}else{
					if (photoListener != null)
					{
						photoListener.onPhotoPickedUp(tempPath);
					}
				}
			}
		}else if (requestCode == BigtureEnvironment.REQ_CODE_CROP_PICTURE){
			if (resultCode == RESULT_OK)
			{
				final Bundle bundle = data.getExtras();

				String imagePath = bundle.getString("imagePath");

				if (photoListener != null)
					photoListener.onPhotoPickedUp(imagePath);
			}
		}
		else if (requestCode == BigtureEnvironment.REQ_CODE_PICKUP_PICTURE){
			if (resultCode == RESULT_OK){
				Uri imageUri = data.getData();

				if (photoListener != null && photoListener.isCropImage()){
					Intent intent = new Intent(this, CropActivity.class);
					intent.putExtra("imageUri", imageUri);
					intent.putExtra("returnImageWidth", 200);
					intent.putExtra("returnImageHeight", 200);
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);

					startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				}else{
					if (photoListener != null){
						String[] projection = {MediaStore.Images.Media.DATA};

						Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
						cursor.moveToFirst();

						int columnIndex = cursor.getColumnIndex(projection[0]);
						String filePath = cursor.getString(columnIndex);

						cursor.close();

						photoListener.onPhotoPickedUp(filePath);
					}
				}
			}
		}else if(requestCode == BigtureEnvironment.EVENT_CODE_SEND_CARD){
			if (resultCode == RESULT_OK){
				setResult(RESULT_OK);
				finish();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initCanvas(){
		DisplayMetrics metrics = BigtureEnvironment.getDisplayMetrics(this);
		//canvas에서 왼쪽메뉴영역을 제외시키고 우측에 worklayer를 위치시킨다.
		//즉 leftMargin을 재 계산한다.
//		final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 368, getResources().getDisplayMetrics());
//		
		
		// set canvas radio to 4 : 3.
		if (metrics.widthPixels >= metrics.heightPixels){
			canvasHeight = metrics.heightPixels;
			canvasWidth  = canvasHeight * 4 / 3;
			
			final int diff = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
			
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)workLayerView.getLayoutParams();
			params.width = canvasWidth;
			params.height = canvasHeight;
			params.leftMargin = (metrics.widthPixels - canvasWidth - diff);
			
			//(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
			
			workLayerView.setLayoutParams(params);
		}else{
			canvasWidth = metrics.widthPixels;
			canvasHeight = canvasWidth * 3 / 4;

			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)workLayerView.getLayoutParams();
			params.width = canvasWidth;
			params.height = canvasHeight;
			params.topMargin = (metrics.heightPixels - canvasHeight) / 2;
			workLayerView.setLayoutParams(params);
		}
	}

	//하위 뷰들을 초기화한다.
	private void initChildViews(){
		findViewById(R.id.imageFlipHorz).setOnClickListener(workLayerView);
		findViewById(R.id.imageFlipVert).setOnClickListener(workLayerView);
//		findViewById(R.id.imageLayerUp).setOnClickListener(workLayerView);
//		findViewById(R.id.imageLayerDown).setOnClickListener(workLayerView);		
		findViewById(R.id.imageDelete).setOnClickListener(workLayerView);

		
		UserDrawingManager.getInstance().setDrawingChangeListener(this);
		
		
		
		selectedColorView = (ImageView)findViewById(R.id.imageSelectedColor);
		GradientDrawable drawable = (GradientDrawable)selectedColorView.getBackground();
		drawable.setColor(getResources().getColor(R.color.black));
		
		selectedColorView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				clearToolBtn();
				final ColorPaletteDialog cDialog = new ColorPaletteDialog(context, new PaletteView.SelectColorListener(){

					@Override
					public void selectColor(int color,ColorPaletteDialog dialog) {
						workLayerView.setStrokeColor(color);
						
						GradientDrawable drawable = (GradientDrawable)selectedColorView.getBackground();
						drawable.setColor(color);
						dialog.dismiss();
					}
					
				});
				cDialog.setCanceledOnTouchOutside(true);
				cDialog.show();
			}
		});

		leftMenu = findViewById(R.id.leftMenu);
//		saveMenu  = (LinearLayout)findViewById(R.id.submenu_save);
//		shareMenu = (LinearLayout)findViewById(R.id.submenu_share);

		// Palettes
//		colorPalette = findViewById(R.id.pal_color);
//		magicPalette = findViewById(R.id.pal_magic);
//		backPalette  = findViewById(R.id.pal_back);
//		fillPalette  = findViewById(R.id.pal_fill);
//		stickerPalette = findViewById(R.id.pal_sticker);
//		youPalette   = findViewById(R.id.pal_you);
//		aquaPalette  = findViewById(R.id.pal_aqua);

		leftContainer = findViewById(R.id.leftContainer);
		
		initMenu();
		initToolbar();
		initPalettes();		
	}
	
	
	
	private void initMenu(){
		menuListener = new MenuListener();

		
	}
	
	private void setBrushButtonImage(boolean first){
		
		int strokeThickLevel = 1;
		if(!first)
			strokeThickLevel = workLayerView.getStrokeThickLevel();
		
		int brushBtn = -1;
		switch(strokeThickLevel){
		case 1:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_1;
			break;
		case 2:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_2;
			break;		
		case 3:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_3;
			break;
		case 4:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_4;
			break;
		case 5:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_5;
			break;
		case 6:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_6;
			break;
		case 7:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_7;
			break;
		case 8:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_8;
			break;
		case 9:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_9;
			break;
		case 10:
			brushBtn = R.drawable.sketchbook_tool_icon_brushsize_10;
			break;			
		}
		
		tbBrushSize.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(brushBtn), null, null, null);
	}

	private Button tbBack;
	private Button tbPen;
	private Button tbMBrush;
	private Button tbAreaPaint;
	private Button tbEraser;
	private Button tbSticker;
	private Button tbYou;
	private Button tbAqua;
	private Button tbIsland;
	private Button tbKorean;
	private Button tbWorld;
	
	private void clearToolBtn(){
		tbBack.setSelected(false);
		tbPen.setSelected(false);
		tbMBrush.setSelected(false);
		tbAreaPaint.setSelected(false);
		tbEraser.setSelected(false);
		tbSticker.setSelected(false);
		tbYou.setSelected(false);
		tbAqua.setSelected(false);
		tbIsland.setSelected(false);
		tbKorean.setSelected(false);
		tbWorld.setSelected(false);
		
	}
	
	private void initToolbar(){
		
		tbBrushSize = (Button)findViewById(R.id.tbBrushSize);
		setBrushButtonImage(true);
		
		tbBrushSize.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				
				BrushSizeDialog dialog = new BrushSizeDialog(context, new BrushSizeDialog.BrushSizeListener() {
					
					@Override
					public void selectBrushSize(int level) {
						setBrushButtonImage(false);
					}
				},workLayerView);
				
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
		
		
		this.tbBack = (Button)findViewById(R.id.tbBack);
		this.tbBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbBack.setSelected(true);
				BackgroundDialog dialog = new BackgroundDialog(context,new BackgroundDialog.BackgroundListener() {
					
					@Override
					public void selectBackground(int bgType) {
						if(bgType == BackgroundDialog.BG_CAMERA){
							photoListener = new PhotoListenerForBackground();
							String tempPath = FileUtil.getTempPath(SketchbookActivity.this) + "/taken.image";
							File file = new File(tempPath);

							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
							startActivityForResult(intent, BigtureEnvironment.REQ_CODE_TAKE_PICTURE);
						}else{
							photoListener = new PhotoListenerForBackground();
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setType("image/*");
							startActivityForResult(intent, BigtureEnvironment.REQ_CODE_PICKUP_PICTURE);
						}
					}
				},workLayerView);
				
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
		this.tbPen = (Button)findViewById(R.id.tbPen); 
		tbPen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbPen.setSelected(true);
				BasicBrushDialog dialog = new BasicBrushDialog(context, workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
		this.tbMBrush = (Button)findViewById(R.id.tbMBrush);
		this.tbMBrush.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbMBrush.setSelected(true);
				MagicBrushDialog dialog = new MagicBrushDialog(context,workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
		this.tbAreaPaint = (Button)findViewById(R.id.tbAreaPaint);
		tbAreaPaint.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbAreaPaint.setSelected(true);
				AreaPaintDialog dialog = new AreaPaintDialog(context,workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
				
			}
		});
		
		this.tbEraser = (Button)findViewById(R.id.tbEraser); 
				
		tbEraser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbEraser.setSelected(true);
				DrawingMode dm = workLayerView.getDrawingMode();
				dm.drawingMode = UserDrawingManager.DRAWING_MODE_ERASE;
				dm.strokeAlpha = 255;
				workLayerView.setDrawingMode(dm);
				
			}
		});
		
		this.tbSticker = (Button)findViewById(R.id.tbSticker);
		tbSticker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbSticker.setSelected(true);
				AwesomeStickerDialog dialog = new AwesomeStickerDialog(context,new AwesomeStickerDialog.AwesomeStickerListener() {
					
					@Override
					public void selectStickerPig(int bgType) {
						DrawingMode dm = workLayerView.getDrawingMode();
						dm.drawingMode = UserDrawingManager.DRAWING_MODE_STICKER;
						dm.assetIds = null;
						
						if(bgType == AwesomeStickerDialog.ST_CAMERA){
							photoListener = new PhotoListenerForSticker();
							String tempPath = FileUtil.getTempPath(SketchbookActivity.this) + "/taken.image";
							File file = new File(tempPath);

							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
							startActivityForResult(intent, BigtureEnvironment.REQ_CODE_TAKE_PICTURE);
						}else{
							photoListener = new PhotoListenerForSticker();
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setType("image/*");
							startActivityForResult(intent, BigtureEnvironment.REQ_CODE_PICKUP_PICTURE);
						}
					}
				},workLayerView);
				
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
				
			}
		});
		
		this.tbYou = (Button)findViewById(R.id.tbYou);
		tbYou.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbYou.setSelected(true);
				YousStickerDialog dialog = new YousStickerDialog(context, workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
				
		this.tbAqua = (Button)findViewById(R.id.tbAqua);
		tbAqua.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbAqua.setSelected(true);
				AquaStickerDialog dialog = new AquaStickerDialog(context, workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
		this.tbIsland = (Button)findViewById(R.id.tbIsland);
		tbIsland.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbIsland.setSelected(true);
				IslandStickerDialog dialog = new IslandStickerDialog(context, workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
		
		this.tbKorean = (Button)findViewById(R.id.tbKorean);
		tbKorean.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbKorean.setSelected(true);
				KoreanStickerDialog dialog = new KoreanStickerDialog(context, workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
		this.tbWorld = (Button)findViewById(R.id.tbWorld);
		tbWorld.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearToolBtn();
				tbWorld.setSelected(true);
				WorldStickerDialog dialog = new WorldStickerDialog(context, workLayerView);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
		
	}

	private void initPalettes(){

	}

	@Override
	public void onChangeDrawing(UserDrawingManager drawingManager) {
		if (drawingManager.canUndo()){
			RelativeLayout btnUndo = (RelativeLayout)findViewById(R.id.btnUndo);
			btnUndo.setEnabled(true);
			findViewById(R.id.leftMenu).invalidate();
		}else{
			RelativeLayout btnUndo = (RelativeLayout)findViewById(R.id.btnUndo);
			btnUndo.setEnabled(false);
			findViewById(R.id.leftMenu).invalidate();
		}

		if (drawingManager.canRedo()){
			RelativeLayout btnRedo = (RelativeLayout)findViewById(R.id.btnRedo);
			btnRedo.setEnabled(true);
			findViewById(R.id.leftMenu).invalidate();
		}else{
			RelativeLayout btnRedo = (RelativeLayout)findViewById(R.id.btnRedo);
			btnRedo.setEnabled(false);
			findViewById(R.id.leftMenu).invalidate();
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		String xml = UserDrawingManager.getInstance().toXML();
		outState.putString("xml", xml);

		if (photoListener != null){
			if (photoListener instanceof PhotoListenerForSticker)
				outState.putInt("photoImportMode", 1);
			else
				outState.putInt("photoImportMode", 2);
		}else	{
			outState.putInt("photoImportMode", 0);			
		}

	//	outState.putInt("selectedToolId", toolbarOnClickListener.getSelectedToolButtonId());

		if (drawingPurpose != null)
			outState.putParcelable("drawingPurpose", drawingPurpose);

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed(){
		goBack();
	}

	private void goBack(){
		if (UserDrawingManager.getInstance().isModified()){
			if (drawingPurpose != null)
				shareAndExit();
			else
				saveAndExit();
		}else{
			if (drawingPurpose != null && drawingPurpose.reason == DrawingPurpose.ARTCLASS){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Notice");
				String msg = getResources().getString(R.string.sketchbook_sp_exit_sketchbook_01) + " " + 
						getResources().getString(R.string.sketchbook_sp_exit_sketchbook_02);
				
				builder.setMessage(msg);
				builder.setPositiveButton(getResources().getString(R.string.common_btn_yes), new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						if (drawingPurpose.puzzleId != null){
							WaitDialog.showWailtDialog(SketchbookActivity.this, false);
							ArtClassHandleTask task = new ArtClassHandleTask(new ArtClassHandleTask.Callback() {
								@Override
								public void onComplete(ArtClassHandleTask task, boolean result){
									WaitDialog.hideWaitDialog();
									finish();
								}
							});
							//그리던 퍼즐조각을 취소시킨다.
							task.cancelReservedPuzzleArtwork(drawingPurpose.classId,drawingPurpose.puzzleId, drawingPurpose.puzzlePart);
						}else{
							setResult(RESULT_CANCELED);
							finish();
						}
					}
				});
				builder.setNegativeButton(getResources().getString(R.string.common_btn_no), null);
				builder.create().show();
			}else{
				finish();
			}
		}
	}
	
	
	
	class PhotoListenerForSticker implements BigtureEnvironment.PhotoPickupListener{
		@Override
		public void onPhotoPickedUp(String filePath)
		{
			String newFilePath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + System.currentTimeMillis() + ".img";
			try
			{
				FileUtil.removeFile(newFilePath);
				FileUtil.moveFile(filePath, newFilePath);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			DrawingMode drawingMode = workLayerView.getDrawingMode();
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_CUSTOM_STICKER;
			drawingMode.bitmapFilePath = newFilePath;
			workLayerView.setDrawingMode(drawingMode);
		}

		@Override
		public void onPhotoCanceled()
		{
		}

		@Override
		public Point getImageSize()
		{
			return new Point(300,300);
		}

		@Override
		public boolean isCropImage()
		{
			return true;
		}

		@Override
		public Point getAspect()
		{
			return new Point(0,0);
		}
	}
	
	class PhotoListenerForBackground implements BigtureEnvironment.PhotoPickupListener{
		@Override
		public void onPhotoPickedUp(String filePath){
			try{
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(filePath, options);

				int scale = 1;
				int imageSize = Math.min(canvasWidth, canvasHeight);

				if (options.outHeight > imageSize || options.outWidth > imageSize){
					scale = (int)Math.pow(2, (int) Math.round(Math.log(imageSize / 
							(double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
				}

			
				options.inJustDecodeBounds = false;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inSampleSize = scale;

				Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

				bitmap = ImageUtil.createBitmap(bitmap, canvasWidth, canvasHeight);

				final String newFilePath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + System.currentTimeMillis() + ".jpg";
				ImageUtil.saveJPEGBitmap(bitmap, newFilePath);

				bitmap.recycle();
				bitmap = null;
				
				new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
				    @Override
				    public void run() {
				        // 실행할 동작 코딩
				 
				    	workLayerView.setBackgroundBitmap(newFilePath);
				    }
				}, 1500);   
				 
			
				
			}catch(OutOfMemoryError e){
				BigtureEnvironment.showAlertMessage(SketchbookActivity.this, "Out of Memory");
			}
		}

		@Override
		public void onPhotoCanceled()
		{
		}

		@Override
		public Point getImageSize()
		{
			return new Point(canvasWidth, canvasHeight);
		}

		@Override
		public boolean isCropImage()
		{
			return false;
		}

		@Override
		public Point getAspect()
		{
			return new Point(0,0);
		}
	}
	
	
	private void openFile(){
		FileOpenDialog dlg = new FileOpenDialog(this);
		dlg.setOnOkListener(new FileOpenDialog.OnOkListener(){
			@Override
			public void onOK(FileOpenDialog dialog, String filename){
				dialog.dismiss();

				int pos = filename.indexOf(".bigture");
				if (pos > 0)
					bigtureFileName = filename.substring(0, pos);
				else
					bigtureFileName = filename;

				workLayerView.clear();
				UserDrawingManager.getInstance().setModified(false);

				String filePath = FileUtil.getDataPath(SketchbookActivity.this) + File.separator + filename;

				FileOpenTask task = new FileOpenTask();
				task.execute(filePath);
			}

			@Override
			public void onClear(FileOpenDialog dialog) {
				dialog.dismiss();
				bigtureFileName = null;
				workLayerView.clear();
			}
			
			
		});
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
	}

	
	
	private void saveToAlbum(){
		//File storageDir = new File(Environment.getExternalStorageDirectory(), "Bigture");
		
		File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Bigture");
		
		if (!storageDir.exists())
			storageDir.mkdirs();
		
		DrawingMode dm = workLayerView.getDrawingMode();
		int oldMode = dm.drawingMode;
		if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_PEN;
			dm.strokeAlpha = 255;
			workLayerView.setDrawingMode(dm);
		}
		
		String fileName = DateUtil.getDateFormatString(new Date(), "yyyyMMddHHmmss") + ".png";
		String filePath = storageDir.getAbsolutePath() + File.separator + fileName;
		workLayerView.saveImage(filePath, true);
		
		if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_ERASE;
			dm.strokeAlpha = 255;
			workLayerView.setDrawingMode(dm);
		}
		
		Toast.makeText(SketchbookActivity.this, getResources().getString(R.string.sketchbook_tp_saved_album), Toast.LENGTH_LONG).show();
		
		if(Build.VERSION.SDK_INT >= 16){
			scanPhoto(filePath);
			
		}else{
		
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                + Environment.getExternalStorageDirectory())));
		
		}
		
		
	}
	
	private void saveAndExit(){
		
		workLayerView.applyActiveSticker();
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(SketchbookActivity.this);
		builder.setTitle(R.string.common_lb_menu_activity);
		builder.setMessage(getResources().getString(R.string.sketchbook_sp_exit_sketchbook_01));
		builder.setCancelable(false);
		builder.setNegativeButton(getResources().getString(R.string.common_btn_yes), new DialogInterface.OnClickListener()
		{	
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		});
		builder.setPositiveButton(getResources().getString(R.string.common_btn_no), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
//				if (bigtureFileName == null){
//					askFileNameAndSave(true);
//				}else{
//					FileSaveTask task = new FileSaveTask();
//					task.execute(bigtureFileName, "1");
//				}
			}
		});

		builder.create().show();
	}

	private void askFileNameAndSave(final boolean exitOnSave){
		workLayerView.applyActiveSticker();
		
		FileNameDialog dialog = new FileNameDialog(this, bigtureFileName);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnOkListener(new FileNameDialog.OnOkListener(){	
			@Override
			public void onOK(FileNameDialog dialog, String filename){
				dialog.dismiss();
				bigtureFileName = filename;;

				String exit = (exitOnSave ? "1" : "0");
				FileSaveTask task = new FileSaveTask();
				task.execute(bigtureFileName, exit);
			}
		});

		dialog.show();
	}

	private void shareAndExit(){
		workLayerView.applyActiveSticker();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(SketchbookActivity.this);
		builder.setTitle(R.string.common_lb_menu_activity);
		String title = getResources().getString(R.string.sketchbook_sp_exit_sketchbook_01) + " " 
				+ getResources().getString(R.string.sketchbook_sp_exit_sketchbook_02);
		
		builder.setMessage(title);
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){	
			@Override
			public void onClick(DialogInterface dialog, int which){
				if (drawingPurpose.reason == DrawingPurpose.ARTCLASS && drawingPurpose.puzzleId != null){
					WaitDialog.showWailtDialog(SketchbookActivity.this, false);
					ArtClassHandleTask task = new ArtClassHandleTask(new ArtClassHandleTask.Callback() {
						@Override
						public void onComplete(ArtClassHandleTask task, boolean result)
						{
							WaitDialog.hideWaitDialog();
							finish();
						}
					});
					//진행하던 퍼즐조작을 취소시킨다.
					task.cancelReservedPuzzleArtwork(drawingPurpose.classId, drawingPurpose.puzzleId, drawingPurpose.puzzlePart);
				}
				finish();
			}
		});
		
		builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				//tryShare();
			}
		});

		builder.create().show();		
	}
	
	private void tryShare(){
		workLayerView.applyActiveSticker();
	
		findViewById(R.id.btnShare).setSelected(false);
		
		final String imagePath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + "upload.jpg";
		final String drawPath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + "upload.bigture";
		
		FileStoreTask task = new FileStoreTask(SketchbookActivity.this, new FileStoreTask.Callback(){	
			@Override
			public boolean saveThumbnail(String thumbPath){
				DrawingMode dm = workLayerView.getDrawingMode();
				int oldMode = dm.drawingMode;
				if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
					dm.drawingMode = UserDrawingManager.DRAWING_MODE_PEN;
					dm.strokeAlpha = 255;
					workLayerView.setDrawingMode(dm);
				}
				
				workLayerView.saveThumbnail(thumbPath);
				
				if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
					dm.drawingMode = UserDrawingManager.DRAWING_MODE_ERASE;
					dm.strokeAlpha = 255;
					workLayerView.setDrawingMode(dm);
				}
				return true;
			}

			@Override
			public void onComplete(FileStoreTask task, Boolean result){
				boolean saved = workLayerView.saveImageForUpload(imagePath);
				
				if (result && saved){
					if(drawingPurpose.reason == DrawingPurpose.POSTCARD){
						//카드일경우에는 저장을 하고 바로 activity를 종료한다.
						
						ArtworkRegisterTask task1 = new ArtworkRegisterTask(new ArtworkRegisterListener(){
							@Override
							public void onComplete(String result){
								Intent intent = new Intent();
								intent.putExtra("artworkId", result);
								setResult(RESULT_OK,intent);
								
								if (drawingPurpose != null)
									finish();
								
							}
						});
					
						File bitmapFile = new File(imagePath);
						String userIndex = AccountManager.getInstance().getUserIndex();
						task1.register(userIndex, "PostCard", "", null, ArtworkType.POSTCARD, ShareType.PRIVATE, bitmapFile, null);
					
					}else if (drawingPurpose.reason == DrawingPurpose.NORMAL || drawingPurpose.reason == DrawingPurpose.CONTEST){
						String refId = (drawingPurpose.reason == DrawingPurpose.CONTEST ? drawingPurpose.contestId : null);
						
						ShareBigtureDialog dialog = new ShareBigtureDialog(SketchbookActivity.this, drawingPurpose.reason, refId, 
								imagePath, drawPath, new ShareBigtureDialog.Callback(){
							@Override
							public void onFinish(ShareBigtureDialog dialog, String artworkIdx){
								shared = true;
								Intent intent = new Intent();
								intent.putExtra("artworkId", artworkIdx);
								setResult(RESULT_OK,intent);
								
								if (drawingPurpose != null)
									finish();
							}
						},false);

						dialog.show();
					}else{
						ShareBigtureDialog2 dialog = new ShareBigtureDialog2(SketchbookActivity.this, new BigtureEnvironment.DialogListener()
						{
							@Override
							public void onCompleted()
							{
								shared = true;
								Intent intent = new Intent();
								intent.putExtra(DrawingPurpose.storeKey, drawingPurpose);

								setResult(RESULT_OK, intent);
								drawingPurpose = null;

								finish();
							}

							@Override
							public void onCanceled()
							{
							}
						}, drawingPurpose, imagePath, drawPath, drawingPurpose.puzzle);

						dialog.show();
					}
				}
			}
		});

		task.save(drawPath);
	}
	
	private void shareOther(){
		workLayerView.applyActiveSticker();
	
		findViewById(R.id.btnShare).setSelected(false);

		String filePath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + "share.jpg";
		
		DrawingMode dm = workLayerView.getDrawingMode();
		int oldMode = dm.drawingMode;
		if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_PEN;
			dm.strokeAlpha = 255;
			workLayerView.setDrawingMode(dm);
		}
		
		if (workLayerView.saveImageForUpload(filePath)){
			if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
				dm.drawingMode = UserDrawingManager.DRAWING_MODE_ERASE;
				dm.strokeAlpha = 255;
				workLayerView.setDrawingMode(dm);
			}
			
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");

			share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));

			String title = "from Bigture";
			if (AccountManager.isLogin())
			{
				AccountEntity entity = AccountManager.getInstance().getAccountEntity();
				title += " " + entity.nickName;
			}
			share.putExtra(Intent.EXTRA_SUBJECT, title);
			startActivity(Intent.createChooser(share, "Share Image"));
		}
	}
	
	class MenuListener implements View.OnClickListener{
		int[] buttonIds = {R.id.btnUndo, R.id.btnRedo, R.id.btnNew, R.id.btnOpen, R.id.btnSave,
				R.id.btnShare};

		public MenuListener(){
			for (int i = 0; i < buttonIds.length; i++)
				findViewById(buttonIds[i]).setOnClickListener(this);

			findViewById(R.id.btnUndo).setEnabled(false);
			findViewById(R.id.btnRedo).setEnabled(false);
		}

		@Override
		public void onClick(View v){
			if (v.getId() == R.id.btnNew){
				AlertDialog.Builder builder = new AlertDialog.Builder(SketchbookActivity.this);
				builder.setTitle(R.string.common_lb_menu_activity);
				builder.setMessage(R.string.message_new_drawing);
				builder.setNegativeButton(getResources().getString(R.string.common_btn_cancel), null);
				builder.setPositiveButton(getResources().getString(R.string.common_btn_ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which){
						bigtureFileName = null;
						workLayerView.clear();
						
//						GradientDrawable drawable = (GradientDrawable)selectedColorView.getBackground();
//						drawable.setColor(getResources().getColor(R.color.black));
						
						if (drawingPurpose != null && drawingPurpose.reason == DrawingPurpose.ARTCLASS){

						}
					}
				});
				builder.create().show();
			}
			else if (v.getId() == R.id.btnUndo){
				workLayerView.undo();
			}
			else if (v.getId() == R.id.btnRedo){
				workLayerView.redo();
			}
			else if (v.getId() == R.id.btnOpen){
				openFile();
			}
			else if (v.getId() == R.id.btnSave){
				
				findViewById(R.id.btnShare).setSelected(false);
				
				showSaveShareMenu(1);
				
			}else if (v.getId() == R.id.btnShare){
				
				findViewById(R.id.btnSave).setSelected(false);
				showSaveShareMenu(2);
			}else if (v.getId() == R.id.btnPrint){
				
				
			}
		}
	};
	
	private void showSaveShareMenu(int type){
		if(drawingPurpose.reason == DrawingPurpose.POSTCARD){
			tryShare();
		}else{
		
			final SketchbookSaveShareMenuDialog dlg = new SketchbookSaveShareMenuDialog(this,type);
			dlg.setSelectMenuListener(new SketchbookSaveShareMenuDialog.SelectMenuListener() {
				
				@Override
				public void selectMenu(int menu) {
					dlg.dismiss();
					if(menu == SketchbookSaveShareMenuDialog.MENU_SAVE_TEMP){
						if (bigtureFileName == null){
							askFileNameAndSave(false);
						}else{
							
							workLayerView.applyActiveSticker();
							FileSaveTask task = new FileSaveTask();
							task.execute(bigtureFileName, "0");
						}
					}else if(menu == SketchbookSaveShareMenuDialog.MENU_ALBUM){
						saveToAlbum();
					}else if(menu == SketchbookSaveShareMenuDialog.MENU_SHARE_ARTWROK){
						tryShare();
					}else if(menu == SketchbookSaveShareMenuDialog.MENU_CARD){
						sendPostCard();
					}else if(menu == SketchbookSaveShareMenuDialog.MENU_SHARE_OTHER){
						shareOther();
					}else if(menu == SketchbookSaveShareMenuDialog.MENU_PRINT){
						
					}
					
				}
			});
			
			dlg.setCanceledOnTouchOutside(true);
			dlg.show();
		}
	}
	
	private void sendPostCard(){
		workLayerView.applyActiveSticker();
		findViewById(R.id.btnShare).setSelected(false);
		
		final String imagePath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + "upload.jpg";
		final String drawPath = FileUtil.getTempPath(SketchbookActivity.this) + File.separator + "upload.bigture";
		
		FileStoreTask task = new FileStoreTask(SketchbookActivity.this, new FileStoreTask.Callback(){	
			@Override
			public boolean saveThumbnail(String thumbPath){
				workLayerView.saveThumbnail(thumbPath);
				return true;
			}

			@Override
			public void onComplete(FileStoreTask task, Boolean result){
				boolean saved = workLayerView.saveImageForUpload(imagePath);
				
				if (result && saved){
					
						ArtworkRegisterTask task1 = new ArtworkRegisterTask(new ArtworkRegisterListener(){
							@Override
							public void onComplete(String result){
								//여기에서 postCard를 보내는 창을 띄운다.
								new GetArtworkTask().execute(result);
							}
						});
					
						File bitmapFile = new File(imagePath);
						String userIndex = AccountManager.getInstance().getUserIndex();
						task1.register(userIndex, "PostCard", "", null, ArtworkType.POSTCARD, ShareType.PRIVATE, bitmapFile, null);
				}
			}
		});
		
		task.save(drawPath);
	}
	
	class GetArtworkTask extends AsyncTask<String, Void, ArtworkEntity>{

		@Override
		protected ArtworkEntity doInBackground(String... params) {
			return ArtworkHandler.loadArtwork(params[0]);
		}

		@Override
		protected void onPostExecute(ArtworkEntity result) {
			super.onPostExecute(result);
			
			Intent intent = new Intent(context,SendPostCardPopupActivity.class);
			intent.putExtra("selectedArtwork", result);
			startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SEND_CARD);
		}
	}
	
	class FileOpenTask extends AsyncTask<String, Void, Boolean>{
		@Override
		protected Boolean doInBackground(String... params){
			boolean result = true;

			String tempDir = FileUtil.getTempPath(SketchbookActivity.this);

			try{
				FileUtil.removeAllFiles(tempDir);
			}catch (Exception e1){
				e1.printStackTrace();
			}

			String filePath = params[0];
			filePath = filePath + ".bigture";

			String xmlPath = tempDir + File.separator + "content.xml";

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			InputStream is = null;
			Document dom = null;
			try{
				ZipUtil.unzip(filePath, tempDir);

				String xml = FileUtil.loadText(xmlPath);

				is = new ByteArrayInputStream(xml.getBytes("UTF-8"));  
				DocumentBuilder builder = factory.newDocumentBuilder();
				dom = builder.parse(is);
				Element root = dom.getDocumentElement();

				UserDrawingManager.getInstance().parse(SketchbookActivity.this, root);
			}catch(Exception e){
				result = false;
				e.printStackTrace();
			}finally{
				try { is.close(); } catch(Exception e) {}
				dom = null;
			}

			return result;
		}

		@Override
		protected void onPostExecute(Boolean result){
			WaitDialog.sweepWaitDialog();

			if (result)
				workLayerView.rebuildCanvas();
			else
				BigtureEnvironment.showAlertMessage(SketchbookActivity.this, "Fail to open bigture file.");

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute(){
			WaitDialog.showWailtDialog(SketchbookActivity.this, false);

			super.onPreExecute();
		}	
	}

	MediaScannerConnection msConn = null;
	public void scanPhoto(final String imageFileName){
		msConn = new MediaScannerConnection(SketchbookActivity.this,new MediaScannerConnectionClient(){
			public void onMediaScannerConnected(){
				msConn.scanFile(imageFileName, null);
			}
			
			public void onScanCompleted(String path, Uri uri){
				msConn.disconnect();
				
			}
		});
		msConn.connect();
	} 
	
	class FileSaveTask extends AsyncTask<String,Void,Boolean>
	{
		boolean exitOnSave;

		@Override
		protected Boolean doInBackground(String... params)
		{
			String fileName = params[0];
			exitOnSave = (params.length == 2 && params[1].equals("1"));

			String dataDirName = FileUtil.getDataPath(SketchbookActivity.this);
			String tempDirName = FileUtil.getTempPath(SketchbookActivity.this);

			DrawingMode dm = workLayerView.getDrawingMode();
			int oldMode = dm.drawingMode;
			if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
				dm.drawingMode = UserDrawingManager.DRAWING_MODE_PEN;
				dm.strokeAlpha = 255;
				workLayerView.setDrawingMode(dm);
			}
			
			String thumbPath = tempDirName + File.separator + "thumb.image";
			workLayerView.saveThumbnail(thumbPath);


			if(oldMode == UserDrawingManager.DRAWING_MODE_ERASE){
				dm.drawingMode = UserDrawingManager.DRAWING_MODE_ERASE;
				dm.strokeAlpha = 255;
				workLayerView.setDrawingMode(dm);
			}
			
			String xml = UserDrawingManager.getInstance().toXML();
			String xmlFilePath = tempDirName + File.separator + "content.xml";

			try
			{
				FileUtil.deleteFile(new File(xmlFilePath));
				FileUtil.saveText(xmlFilePath, xml);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}


			List<File> fileList = UserDrawingManager.getInstance().getResourceFileList();

			File file = new File(xmlFilePath); 
			fileList.add(file);

			file = new File(thumbPath);
			fileList.add(file);

			String filePath = dataDirName + File.separator + fileName + ".bigture";
			ZipUtil.zip(fileList, filePath);



			File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Bigture");
			if (!storageDir.exists())
				storageDir.mkdirs();

			filePath = storageDir.getAbsolutePath() + File.separator + fileName + ".png";
			workLayerView.saveImage(filePath, true);

			if(Build.VERSION.SDK_INT >= 16){
				scanPhoto(filePath);
				
			}else{
				SketchbookActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
	                + Environment.getExternalStorageDirectory())));
			
			}
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result){
			WaitDialog.sweepWaitDialog();

			if (result){
				UserDrawingManager.getInstance().setModified(false);

				if (exitOnSave){
					Toast.makeText(context, getResources().getString(R.string.sketchbook_tp_shared_artworks), Toast.LENGTH_SHORT).show();
					SketchbookActivity.this.finish();
				}else{
					Toast.makeText(context, getResources().getString(R.string.sketchbook_tp_saved_temp), Toast.LENGTH_SHORT).show();
					
				}
			}else{
				BigtureEnvironment.showAlertMessage(SketchbookActivity.this, "Sorry! Fail to save artwork");
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute(){
			WaitDialog.showWailtDialog(SketchbookActivity.this, false);
			super.onPreExecute();
		}	
	}
}
