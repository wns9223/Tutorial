package edu.sfsu.cs.orange.ocr.company.pkg;

import ip.cureturn.pkg.IPReturn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import prosess.time.pkg.TimeWatch;
import search.pkg.TotalSearchActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import edu.sfsu.cs.orange.ocr.R.drawable;
import edu.sfsu.cs.orange.ocr.R.id;
import edu.sfsu.cs.orange.ocr.R.layout;
import edu.sfsu.cs.orange.ocr.intent.adt.IntentSerial;
import edu.sfsu.cs.orange.ocr.intent.adt.ProductIntent;
import edu.sfsu.cs.orange.ocr.intent.adt.ProductListData;
import edu.sfsu.cs.orange.ocr.intent.adt.ProsessingTemperary;

public class CompanySearchActivity extends Activity {
	private IntentSerial intentse;
	private SearchView Company_searchview;
	private ImageView Company_imageview;
	private ImageView Company_camera_image;
	private ListView Company_liseview;
	private bAseaDapter baseadapter; 
	private Socket socket; 
	private DataInputStream in;
	private DataOutputStream out;
	private int add_position = 0;
	private String MaxCount; 
				
	private String differentMaxCount; 
	private Intent intent;
	private IntentSerial intentserial;
	private String product_name;
	// private SearchbAseaDapter searchbAseaDapter;
	private int resume = 0;
	private String data;
	private String str;
	private final Activity ToAc = CompanySearchActivity.this;
	private int oneCount = 0;
	private int prossing=0;
	private boolean resumeT = true;
	static class Holder {

		ImageView listview_image;
		TextView listview_name; 
		TextView listview_price;
	}

	private class ViewHolder {
		public ImageView mIcon;

		public TextView mText;

		public TextView mDate;
	}

	@Override
	protected void onResume() {
		prossing = (int)System.currentTimeMillis();
		// TODO Auto-generated method stub
		super.onResume();
		try { 
			
			String data;
			product_name = Company_searchview.getQuery().toString();

			data = "select count(name) from snack where name like '%"
					+ product_name + "%' and company = '"
					+ intentse.getProName() + "';"; 

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
					((int) System.currentTimeMillis() - intentse.getSecond()) * 10);		
			oneCount=1;
		}
		else if(resumeT== true){
		time.run(
				ToAc,
				((int) System.currentTimeMillis() - prossing) * 10);
		
		
		}
        
		
		Company_liseview.setAdapter(baseadapter);

	}

	public void onBackPressed() { // back event
		super.onBackPressed();
		oneCount=0;
		try { 
			in.close();
			out.close();
		} catch (IOException e) {

		}
		finish();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_company_search);


        Company_searchview = (SearchView) findViewById(id.different_company_search);
        Company_imageview = (ImageView) findViewById(id.different_company_image);
        Company_camera_image = (ImageView) findViewById(R.id.different_company_imageButton);
        Company_liseview = (ListView) findViewById(id.different_company_listview);
        baseadapter = new bAseaDapter(this);
        
        Intent intent = getIntent();
        intentse = (IntentSerial) intent.getSerializableExtra("individualname");

        int id = Company_searchview.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) Company_searchview.findViewById(id);
        textView.setTextColor(Color.BLACK);


        if(intentse.getProName().compareTo("해태제과")==0){
            Company_imageview.setImageResource(drawable.getee);
        }
        if(intentse.getProName().compareTo("크라운제과")==0){
            Company_imageview.setImageResource(drawable.crownw);
        }
        if(intentse.getProName().compareTo("롯데제과")==0){
            Company_imageview.setImageResource(drawable.lotte);
        }
        if(intentse.getProName().compareTo("빙그레")==0){
            Company_imageview.setImageResource(drawable.bingre);
        }
        if(intentse.getProName().compareTo("오리온")==0){
            Company_imageview.setImageResource(drawable.orionno);
        }
        if(intentse.getProName().compareTo("농심")==0){
            Company_imageview.setImageResource(drawable.noingsim);
        }
        if(intentse.getProName().compareTo("청우식품")==0){
            Company_imageview.setImageResource(drawable.cunguimage);
        }
        if(intentse.getProName().compareTo("오뚜기")==0){
            Company_imageview.setImageResource(drawable.odugiimage);
        }
        if(intentse.getProName().compareTo("삼양")==0){
            Company_imageview.setImageResource(drawable.samyang);
        }
        
        
        IPReturn ipreturn  = new IPReturn();
        try {  
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
            Socket socket = new Socket(ipreturn.getIP(), 1);
            in = new DataInputStream(socket.getInputStream()); 
            out = new DataOutputStream(socket.getOutputStream()); 


            String data = "select count(company) from snack where company = '"+intentse.getProName()+"';";             

            out.writeUTF(data);     
            MaxCount = in.readUTF(); 
            differentMaxCount=MaxCount; 

        }catch (IOException e){

        }
        
		
	 	
        Company_liseview.setAdapter(baseadapter);
        

        
        
        
        Company_camera_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivityForResult(new Intent(CompanySearchActivity.this,CaptureActivity.class), 9999);
				
			}
		});
        
        
        
        
        
 
        
        
        
        Company_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
            	prossing = (int)System.currentTimeMillis();
            	onResume();											
            	resume=1;
            	resumeT=true;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        
        
        
        Company_liseview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int positoin, long l) {
                                                    	
                                                    		resumeT = false;
	                                                    	ProductIntent productintent = new ProductIntent();
	                                                        Intent DetailIntent = new Intent(CompanySearchActivity.this,ProductContentActivity.class);
	                                                        ProductListData productlistdata = baseadapter.mListData.get(positoin);
	                                                        productintent.setUrl(productlistdata.url);
	                                                        productintent.setName(productlistdata.name);
	                                                        productintent.setCompany(productlistdata.company);
	                                                        DetailIntent.putExtra("productIntent", productintent);
	                                                        startActivity(DetailIntent);
	                                                        
                                                    	
                                                    	
                                                        
                                                    }
                                                }
        );
        
        



    }

	public class bAseaDapter extends BaseAdapter { 

		Context context; 
		ArrayList<ProductListData> mListData = new ArrayList<ProductListData>(); // ListData의
																					




		String tempporURL; 
		String tempporName; 
		String tempporCompany;
		String temporPrice; 
		Bitmap bit = null;

		public bAseaDapter(Context con) { // bAseDapter part.
			// TODO Auto-generated constructor stub
			context = con; /

		}

		@Override
		public int getCount() { 
					
			

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

				LayoutInflater inflater = (LayoutInflater) context 
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.activity_total_listview, null); 

				holder.listview_image = (ImageView) convertView // activity_user_board_listview에
																
																
																
						.findViewById(R.id.total_listview_image);
				holder.listview_name = (TextView) convertView // activity_user_board_listview에
											
											
											
						.findViewById(R.id.total_listview_title);
				holder.listview_price = (TextView) convertView // activity_user_board_listview에


						.findViewById(R.id.total_listview_content);

				convertView.setTag(holder); 
			} else {
				holder = (Holder) convertView.getTag();

			}

			try {
				StrictMode
						.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
								.permitNetwork().build()); 

				
				
				if (resume == 0) 
					data = "select d.url,d.name,d.company,d.price from (select url, name, company,price from snack  where company='"+ intentse.getProName()+ "') d order by d.name ASC limit "+ position+ ",1;"; 

				if (resume == 1)
					data = "select d.url,d.name,d.company,d.price from (select url, name, company, price from snack  where company='"+ intentse.getProName()+ "' and name like '%"+ product_name + "%') d order by d.name ASC limit " + position + ",1;"; 
																		
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



				holder.listview_name.setText(tempporName); // holder TextView의
														
				holder.listview_price.setText("가격 : " + temporPrice);
											
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
				Company_searchview.setQuery(str, true);
			}
		}
		
		
	}
}
