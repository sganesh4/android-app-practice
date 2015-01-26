package com.sushil.stockquoteapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class StockInfoActivity extends Activity {
	private static final String TAG = "STOCK_QUOTE";
	private TextView companyNameTextView;
	private TextView yearLowTextView;
	private TextView yearHighTextView;
	private TextView daysLowTextView;
	private TextView daysHighTextView;
	private TextView lastTradePriceOnlyTextView;
	private TextView daysRangeTextView;
	private TextView changeTextView;
	
	private static final String KEY_ITEM="quote";
	private static final String KEY_NAME="Name";
	private static final String KEY_YEAR_LOW="YearLow";
	private static final String KEY_YEAR_HIGH="YearHigh";
	private static final String KEY_DAYS_LOW="DaysLow";
	private static final String KEY_DAYS_HIGH="DaysHigh";
	private static final String KEY_LAST_TRADE_PRICE="LastTradePriceOnly";
	private static final String KEY_CHANGE="Change";
	private static final String KEY_DAYS_RANGE="DaysRange";
	
	private String name="";
	private String yearLow="";
	private String yearHigh="";
	private String daysLow="";
	private String daysHigh="";
	private String lastTradePriceOnly="";
	private String daysRange="";
	private String change="";
	
	private String yahooURLFirst = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
	private String yahooURLSecond="%22)&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_info);
		Intent intent= getIntent();
		String stockSymbol=intent.getStringExtra(MainActivity.STOCK_SYMBOL);
		
		companyNameTextView=(TextView)findViewById(R.id.companyNameTextView);
		yearLowTextView=(TextView)findViewById(R.id.yearLowTextView);
		yearHighTextView=(TextView)findViewById(R.id.yearHighTextView);
		daysLowTextView=(TextView)findViewById(R.id.daysLowTextView);
		daysHighTextView=(TextView)findViewById(R.id.daysHighTextView);
		lastTradePriceOnlyTextView=(TextView)findViewById(R.id.lastTradePriceOnlyTextView);
		daysRangeTextView=(TextView)findViewById(R.id.daysRangeTextView);
		changeTextView=(TextView)findViewById(R.id.changeTextView);
		
		Log.d(TAG, "Before URL creation" +stockSymbol);
		final String yqlURL=yahooURLFirst+stockSymbol+yahooURLSecond;
		
		new MyAsyncTask().execute(yqlURL);
		
	}
	private class MyAsyncTask extends AsyncTask<String, String, String>{

		protected String doInBackground(String... args) {
			try {
				URL url = new URL(args[0]);
				URLConnection connection=url.openConnection();
				HttpsURLConnection httpsURLConnection=(HttpsURLConnection)connection;
				int responseCode=httpsURLConnection.getResponseCode();
				if (responseCode==HttpsURLConnection.HTTP_OK) {
					InputStream in = httpsURLConnection.getInputStream();
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db =dbf.newDocumentBuilder();
					Document dom = db.parse(in);
					Element docEle = dom.getDocumentElement();
					NodeList nl=docEle.getElementsByTagName(KEY_ITEM);
					if (nl!=null&&nl.getLength()>0) {
						for (int i = 0; i < nl.getLength(); i++) {
							StockInfo theStock=getStockInformation(docEle);
							name=theStock.getName();
							yearLow=theStock.getYearLow();
							yearHigh=theStock.getYearHigh();
							daysLow=theStock.getDaysLow();
							daysHigh=theStock.getDaysHigh();
							lastTradePriceOnly=theStock.getLastTradePririceOnly();
							daysRange=theStock.getDaysRange();
							change=theStock.getChange();

						}
					}
				}
				
			} catch (MalformedURLException e) {
				Log.d(TAG, "MalformedURLException", e);
			} catch (IOException e) {
				Log.d(TAG, "IOException", e);
			} catch (ParserConfigurationException e) {
				Log.d(TAG, "ParserConfigurationException", e);
			} catch (SAXException e) {
				Log.d(TAG, "SAXException", e);
			}
			finally{}
			return null;
		}
		protected void onPostExecute(String result)
		{
			companyNameTextView.setText(name);
			yearLowTextView.setText("Year Low: "+yearLow);
			yearHighTextView.setText("Year High: "+yearHigh);
			daysHighTextView.setText("Days High: "+daysHigh);
			daysLowTextView.setText("Days Low: "+daysLow);
			changeTextView.setText("Change: "+change);
			lastTradePriceOnlyTextView.setText("Last Price: "+lastTradePriceOnly);
			daysRangeTextView.setText("Daily Price Range: "+daysRange);
			
		}
		private StockInfo getStockInformation(Element entry) {
			String stockName=getTextValue(entry,"Name");
			String stockYearLow=getTextValue(entry,"YearLow");
			String stockYearHigh=getTextValue(entry,"YearHigh");
			String stockDaysLow=getTextValue(entry,"DaysLow");
			String stockDaysHigh=getTextValue(entry,"DaysHigh");
			String stockLastTradePriceOnly=getTextValue(entry,"LastTradePriceOnly");
			String stockDaysRange=getTextValue(entry,"DaysRange");
			String stockChange=getTextValue(entry,"Change");
			
			StockInfo theStock=new StockInfo(stockDaysHigh, stockYearLow, stockYearHigh, stockName, stockLastTradePriceOnly, stockChange,
					stockDaysRange, stockDaysLow);
			return theStock;
		}
		private String getTextValue(Element entry, String tagName) {
			String tagValuetoReturn=null;
			NodeList nl = entry.getElementsByTagName(tagName);
			if (nl!=null&&nl.getLength()>0) {
				Element element = (Element) nl.item(0);
				tagValuetoReturn=element.getFirstChild().getNodeValue();
			}
			return tagValuetoReturn;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
