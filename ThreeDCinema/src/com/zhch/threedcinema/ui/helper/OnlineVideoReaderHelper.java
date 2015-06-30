package com.zhch.threedcinema.ui.helper;

import java.io.IOException;
import java.util.ArrayList;




import android.content.Context;

import com.zhch.threedcinema.database.OnlineVideoDbHelper;
import com.zhch.threedcinema.database.VideoListData;
import com.zhch.threedcinema.po.VideoList;

public class OnlineVideoReaderHelper {

	/** 获取所有电视分类 */
	public static ArrayList<VideoList> getAllCategory(final Context context) {
		ArrayList<VideoList> result = new ArrayList<VideoList>();
		OnlineVideoDbHelper videodbOpenHelper=new OnlineVideoDbHelper(context);
		videodbOpenHelper.getWritableDatabase();
		VideoListData videoDataService=new VideoListData(context);
		for (int i = 0; i < videoDataService.getCount(); i++) {
			VideoList ov = new VideoList();
			ov=videoDataService.find(i);		        
			result.add(ov);
			// Read Node
		}
		   
		return result;
	}

	/** 读取分类下所有电视地址 */
//	public static ArrayList<OnlineVideo> getVideos(final Context context,
//			String categoryId) {
//		ArrayList<OnlineVideo> result = new ArrayList<OnlineVideo>();
//		DocumentBuilderFactory docBuilderFactory = null;
//		DocumentBuilder docBuilder = null;
//		Document doc = null;
//		try {
//			docBuilderFactory = DocumentBuilderFactory.newInstance();
//			docBuilder = docBuilderFactory.newDocumentBuilder();
//			// xml file 放到 assets目录中的
//			doc = docBuilder.parse(context.getResources().getAssets()
//					.open("onlinet.xml"));
//			// root element
//			Element root = doc.getElementById(categoryId);
//			if (root != null) {
//				NodeList nodeList = root.getChildNodes();
//				for (int i = 0, j = nodeList.getLength(); i < j; i++) {
//					Node baseNode = nodeList.item(i);
//
//					if (!"item".equals(baseNode.getNodeName()))
//						continue;
//					String id = baseNode.getFirstChild().getNodeValue();
//					if (id == null)
//						continue;
//					OnlineVideo ov = new OnlineVideo();
//					ov.id = id;
//
//					Element el = doc.getElementById(ov.id);
//					if (el != null) {
//						ov.title = el.getAttribute("title");
//						ov.icon_url = el.getAttribute("image");
//						ov.level = 3;
//						ov.category = 1;
//						NodeList nodes = el.getChildNodes();
//						for (int m = 0, n = nodes.getLength(); m < n; m++) {
//							Node node = nodes.item(m);
//							if (!"ref".equals(node.getNodeName()))
//								continue;
//							String href = node.getAttributes()
//									.getNamedItem("href").getNodeValue();
//							if (ov.url == null) {
//								ov.url = href;
//							} else {
//								if (ov.backup_url == null)
//									ov.backup_url = new ArrayList<String>();
//								ov.backup_url.add(href);
//							}
//						}
//						if (ov.url != null)
//							result.add(ov);
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} finally {
//			doc = null;
//			docBuilder = null;
//			docBuilderFactory = null;
//		}
//		return result;
//	}

}
