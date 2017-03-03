package com.kincai.store.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.kincai.store.utils.LogTest;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description TODO
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.common
 *
 * @time 2015-7-24 下午7:52:41
 *
 */
public class AddAssetsSqlite {
	
	public static SQLiteDatabase getAddressSql(Context context) {
		
		 String filePath = "data/data/com.kincai.store/databases/china_Province_city_zone.db";  
//		 String pathStr = context.getFilesDir().getName()+"/databases";  
		    File jhPath=new File(filePath);  
		      //查看数据库文件是否存在  
		      if(jhPath.exists()){  
		        LogTest.LogMsg("test", "存在数据库");
		        //存在则直接返回打开的数据库  
		        return SQLiteDatabase.openOrCreateDatabase(jhPath, null);  
		      }else{  
		        //不存在先创建文件夹  
//		        File path=new File(pathStr);  
//		        LogTest.LogMsg("test", "pathStr="+path);
//		        if (path.mkdir()){  
//		        	 LogTest.LogMsg("test", "创建成功"); 
//		        }else{  
//		        	 LogTest.LogMsg("test", "创建失败");
//		        };  
		        try {  
		          //得到资源  
		          AssetManager am= context.getAssets();  
		          //得到数据库的输入流  
		          InputStream is=am.open("china_Province_city_zone.db");  
		          LogTest.LogMsg("test", is+"");
		          //用输出流写到SDcard上面	
		          FileOutputStream fos=new FileOutputStream(jhPath);  
		          LogTest.LogMsg("test", "fos="+fos);
		          LogTest.LogMsg("test", "jhPath="+jhPath);
		          //创建byte数组  用于1KB写一次  
		          byte[] buffer=new byte[1024];  
		          int count = 0;  
		          while((count = is.read(buffer))>0){  
		        	  LogTest.LogMsg("test", "得到");
		            fos.write(buffer,0,count);  
		          }  
		          //最后关闭就可以了  
		          fos.flush();  
		          fos.close();  
		          is.close();  
		        } catch (IOException e) {  
		          // TODO Auto-generated catch block  
		          e.printStackTrace();  
		          return null;
		        }  
		        //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了  
		        return SQLiteDatabase.openOrCreateDatabase(jhPath, null);  
		        
		      
		      }
	}
}
