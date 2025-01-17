package mobi.esys.upnews_requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mobi.esys.constants.K2Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

public class K2Request {

	private static InputStream inputStream;

	public static JSONObject getJSONFromURL(final String method,
			final String postfix) {

		inputStream = new InputStream() {

			@Override
			public int read() {
				return 0;
			}
		};
		String result = "";
		JSONObject jsonObject = new JSONObject();

		try {

			inputStream = new DefaultHttpClient()
					.execute(
							new HttpGet(K2Constants.VIDEO_URLPREFIX + method
									+ K2Constants.GET_TYPE + postfix))
					.getEntity().getContent();

			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			final StringBuilder stringBuilder = new StringBuilder();
			String line = "";

			while (line != null) {
				line = bufferedReader.readLine();
				stringBuilder.append(line + "\n");
			}

			inputStream.close();

			result = stringBuilder.toString();

			jsonObject = new JSONObject(result);
			Log.d("result", jsonObject.toString());
		} catch (Exception Exception) {

		}

		return jsonObject;
	}


	public void sendDataToServer(Bundle sendParams, String url) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		Log.d("send", "send");
		Log.d("send", url);

		try {
			Log.d("send", sendParams.toString());
			Log.d("params", bundle2nameValuePairs(sendParams).toString());
			httppost.setEntity(new UrlEncodedFormEntity(
					bundle2nameValuePairs(sendParams)));
			HttpResponse response = httpclient.execute(httppost);


            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            StringBuilder builder=new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
            }

            JSONObject jsonObject=new JSONObject(builder.toString());

			Log.d("resp", jsonObject.toString());

		} catch (UnsupportedEncodingException e) {
			Log.d("resp", "uee");

		} catch (ClientProtocolException e) {
			Log.d("resp", "cpe");

		} catch (IOException e) {
			Log.d("resp", "ioe");
		} catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public List<NameValuePair> bundle2nameValuePairs(Bundle procBundle) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : procBundle.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, String
					.valueOf(procBundle.get(key))));
		}
		return nameValuePairs;
	}

}
