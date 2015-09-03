package search.pkg;

import ip.cureturn.pkg.IPReturn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import prosess.time.pkg.TimeWatch;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import edu.sfsu.cs.orange.ocr.CaptureActivity;
import edu.sfsu.cs.orange.ocr.R;
import edu.sfsu.cs.orange.ocr.company.pkg.ProductContentActivity;
import edu.sfsu.cs.orange.ocr.intent.adt.IntentSerial;
import edu.sfsu.cs.orange.ocr.intent.adt.ProductIntent;
import edu.sfsu.cs.orange.ocr.intent.adt.ProductListData;

public class TotalSearchActivity extends Activity {
	private Handler mHandler;
	private ProgressDialog mProgressDialog;
	private IntentSerial Intentse;
	private SearchView Total_search;
	private ListView total_Listview;
	private ImageView total_camera_imageview;
	private DataInputStream in; 
	private DataOutputStream out;
	private final Activity ToAc = TotalSearchActivity.this;
	private String MaxCount; 
								
	private String differentMaxCount; 
	private bAseaDapter baseadapter;
	private String product_name;
	private String str;
	private int oneCount = 0;
	private int prossing=0;
	private boolean resumeT = true;
	
	private class ViewHolder {
		public ImageView mIcon;

		public TextView mText;

		public TextView mDate;
	}

	static class Holder {

		ImageView listview_image;
		TextView listview_name; 
		TextView listview_price; 
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		oneCount=0;
		try {
			in.close();
			out.close();
			finish();
		} catch (Exception e) {

			// TODO: handle exception
		}
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try { 

			String data;
			product_name = Total_search.getQuery().toString(); 

			if (Intentse.getProName().length() >= 1)
				data = "select count(name) from snack where name like '%"
						+ product_name + "%';";
			else
				data = "select count(name) from snack;"; 
															
			out.writeUTF(data); 
			MaxCount = in.readUTF();
			differentMaxCount = MaxCount; 

		} catch (IOException e) {

		}
		baseadapter = new bAseaDapter(this);
		
		TimeWatch time = new TimeWatch();
		if (oneCount == 0) {
		
			time.run(
					ToAc,
					((int) System.currentTimeMillis() - Intentse.getSecond()) * 10);
			oneCount=1;
		}
		else if(resumeT== true){
		time.run(
				ToAc,
				((int) System.currentTimeMillis() - prossing) * 10);
		
		}
		total_Listview.setAdapter(baseadapter);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_total_search);

		Total_search = (SearchView) findViewById(R.id.total_search);
		total_Listview = (ListView) findViewById(R.id.total_listview);
		total_camera_imageview = (ImageView) findViewById(R.id.imageButton);

		Intent intent = getIntent();
		Intentse = (IntentSerial) intent.getSerializableExtra("proname");

		Total_search.setQuery(Intentse.getProName(), true);

		int id = Total_search.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) Total_search.findViewById(id);
		textView.setTextColor(Color.BLACK);

		baseadapter = new bAseaDapter(this);

		IPReturn ipreturn = new IPReturn();
		try { 
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.permitNetwork().build());
			Socket socket = new Socket(ipreturn.getIP(), 1);
			in = new DataInputStream(socket.getInputStream()); 
			out = new DataOutputStream(socket.getOutputStream()); 
			String data;
			product_name = Intentse.getProName();

			if (Intentse.getProName().length() >= 1)
				data = "select count(name) from snack where name like '%"
						+ product_name + "%';"; 
			else
				data = "select count(name) from snack;"; 
			out.writeUTF(data);
			MaxCount = in.readUTF(); 
			differentMaxCount = MaxCount;

		} catch (IOException e) {

		}
		

		total_Listview.setAdapter(baseadapter);

		

		Total_search
				.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
					@Override
					public boolean onQueryTextSubmit(String s) {
						resumeT=true;
						prossing = (int)System.currentTimeMillis();
						onResume();
						return true;
					}

					@Override
					public boolean onQueryTextChange(String s) {

						return false;
					}
				});

		total_Listview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long l) {
						resumeT = false;
						ProductIntent productintent = new ProductIntent();
						Intent TotalIntent = new Intent(
								TotalSearchActivity.this,
								ProductContentActivity.class);
						ProductListData productlistdata = baseadapter.mListData
								.get(position);
						productintent.setUrl(productlistdata.url);
						productintent.setName(productlistdata.name);
						productintent.setCompany(productlistdata.company);
						TotalIntent.putExtra("productIntent", productintent);
						startActivity(TotalIntent);
					}
				});

		total_camera_imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivityForResult(new Intent(TotalSearchActivity.this,
						CaptureActivity.class), 9999);

			}
		});

	}

	public class bAseaDapter extends BaseAdapter { 
													
		Context context; 
		ArrayList<ProductListData> mListData = new ArrayList<ProductListData>(); 
		String tempporURL; 
		String tempporName; 
		String tempporCompany;
		String temporPrice;
		Bitmap bit = null;

		public bAseaDapter(Context con) { 
			// TODO Auto-generated constructor stub
			context = con; 

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			

			return Integer.parseInt(MaxCount);
		}

		@Override
		public Object getItem(int position) { 
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			Holder holder;

			if (convertView == null) { 
				holder = new Holder();

				LayoutInflater inflater = (LayoutInflater) context // layout¿ª
																	
																	
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.activity_total_listview, null); 
																	

				holder.listview_image = (ImageView) convertView
																
						.findViewById(R.id.total_listview_image);
				holder.listview_name = (TextView) convertView 
						.findViewById(R.id.total_listview_title);
				holder.listview_price = (TextView) convertView
						.findViewById(R.id.total_listview_content);

				convertView.setTag(holder); 
			} else {
				holder = (Holder) convertView.getTag(); 
			}

			try {
				StrictMode
						.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
								.permitNetwork().build());
				
				String data = "select d.url, d.name, d.company, d.price from (select url,name,company,price from snack  where name like'%"+ product_name +"%' ) d order by d.name ASC limit " + position + ",1;"; 

				out.writeUTF(data);
				out.flush(); 

				tempporURL = in.readUTF();
				tempporName = in.readUTF(); 
				tempporCompany = in.readUTF();
				temporPrice = in.readUTF(); 

				try {
					URL url = new URL(tempporURL);
					Bitmap bmp = BitmapFactory.decodeStream(url
							.openConnection().getInputStream());
					holder.listview_image.setImageBitmap(bmp);
				} catch (Exception e) {
					// TODO: handle exception
				}

				

				
				holder.listview_name.setText(tempporName); 
				holder.listview_price.setText("∞°∞› : " + temporPrice); 
				ProductListData addInfo = null; 
				addInfo = new ProductListData(); 
				addInfo.url = tempporURL;
				addInfo.name = tempporName; 
								
				addInfo.company = tempporCompany;
								
				addInfo.price = temporPrice;

				mListData.add(addInfo); 
							

			} catch (IOException e) {

			}

			return convertView; 
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 9999) {
			if (resultCode == RESULT_OK) {

				str = data.getStringExtra("ocr");
				Total_search.setQuery(str, true);
			}
		}
	}

}