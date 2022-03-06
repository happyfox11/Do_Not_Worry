package com.android.myappproject.api;

import static com.android.myappproject.activity.HospitalMainActivity.memberList;

import android.util.Log;

import com.android.myappproject.vo.CustomHospitalVo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HospitalSearchApi
{
	// 네이버 검색 API Client ID
	private final String clientId = "1N9vFwnAE4PhBoaIw_JG";

	// 네이버 검색 API Client Secret
	private final String clientSecret = "KX0QiZ8p4t";

	public void getSearchData(List<CustomHospitalVo> hospitalList, String searchTarget)
	{
		String target;

		try
		{
			target = URLEncoder.encode(searchTarget + " 정신과", "UTF-8");

			String responseData = requestHospitalData(target);

			Log.i("test", responseData);

			parseData(hospitalList, responseData);
		}
		catch(Exception ex)
		{
			Log.i(this.getClass().getName(), ex.getMessage(), ex);
		}
	}

	private String requestHospitalData(String target) throws Exception
	{
		String apiUrl;

		URL url;

		InputStream inputStream = null;

		InputStreamReader inputStreamReader = null;

		BufferedReader bufferedReader = null;

		StringBuilder stringBuilder;

		String resBody = null;

		try
		{
			apiUrl = "https://openapi.naver.com/v1/search/local.json?query=" + target + "&display=15";

			url = new URL(apiUrl);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			Map<String, String> reqHeaderMap = new HashMap<>();

			reqHeaderMap.put("X-Naver-Client-Id", clientId);
			reqHeaderMap.put("X-Naver-Client-Secret", clientSecret);

			for(Map.Entry<String, String> header : reqHeaderMap.entrySet())
			{
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int resCode = con.getResponseCode();

			if (resCode == HttpURLConnection.HTTP_OK)
			{
				inputStream = con.getInputStream();
			}
			else
			{
				inputStream = con.getErrorStream();
			}

			inputStreamReader = new InputStreamReader(inputStream);

			bufferedReader = new BufferedReader(inputStreamReader);

			stringBuilder = new StringBuilder();

			String line;

			while ((line = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(line);
			}

			resBody = stringBuilder.toString();

			if (resCode != HttpURLConnection.HTTP_OK)
			{
				throw new Exception(resBody);
			}
		}
		catch(Exception ex)
		{
			throw new Exception(ex.getMessage(), ex);
		}
		finally
		{
			try
			{
				if(inputStream != null)
				{
					inputStream.close();
				}

				if(inputStreamReader != null)
				{
					inputStreamReader.close();
				}

				if(bufferedReader != null)
				{
					bufferedReader.close();
				}
			}
			catch(IOException ioEx){}
		}

		return resBody;
	}

	private List<CustomHospitalVo> parseData(List<CustomHospitalVo> hospitalList, String parseData) throws Exception
	{
		JSONObject jsonObject;

		JSONArray jsonArray;

		JSONObject item;

		String title;

		String address;

		String link;

		try
		{
			jsonObject = new JSONObject(parseData);

			jsonArray = jsonObject.getJSONArray("items");

			hospitalList.clear();

			for (int i = 0; i < jsonArray.length(); i++)
			{
				item = jsonArray.getJSONObject(i);

				title = item.getString("title");

				address = item.getString("address");

				if(item.getString("link").equals(""))
				{
					link = "-";
				}
				else
				{
					link = item.getString("link");
				}

				hospitalList.add(new CustomHospitalVo(title, address, link));
			}
		}
		catch(Exception ex)
		{
			throw new Exception("데이터 변환 중 에러가 발생하였습니다.", ex);
		}

		return hospitalList;
	}
}
