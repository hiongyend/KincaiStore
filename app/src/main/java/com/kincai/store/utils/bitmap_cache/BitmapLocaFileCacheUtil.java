package com.kincai.store.utils.bitmap_cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;

import com.kincai.store.utils.LogTest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 文件操作工具类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:37:45
 *
 */
public class BitmapLocaFileCacheUtil {
	private static final String TAG = "BitmapLocaFileCacheUtil";
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存Image的目录名
	 */
	private final static String FOLDER_NAME = "/kincaiStoreImage";
	
	
	public BitmapLocaFileCacheUtil(Context context){
		mDataRootPath = context.getCacheDir().getPath();
	}
	

	/**
	 * 获取储存Image的目录
	 * @return
	 */
	private static String getStorageDirectory(){
		//先判断存不存在sd卡
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	
	/**
	 * 保存Image的方法，有sd卡存在 就存储到sd卡，没有就存储到手机目录
	 * @param fileName 
	 * @param bitmap   
	 * @throws IOException
	 */
	public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{
		if(bitmap == null){
			return;
		}
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		File file = new File(getFiledirPath(fileName));
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, fos);//第二个参数表示压缩率 100表示不压缩
		fos.flush();
		fos.close();
	}
	
	public void savaFile(String fileName, String text) throws IOException{
		
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		File file = new File(getFiledirPath(fileName));
		 try {
				OutputStream outputStream1 = new FileOutputStream(file);
				outputStream1.write(text.getBytes());
				outputStream1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 从手机或者sd卡获取Bitmap
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName){
		LogTest.LogMsg(TAG, "-使用文件缓存-");
		return BitmapFactory.decodeFile(getFiledirPath(fileName));
	}
	
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		if(null != fileName) {
			return getFiledir(fileName).exists();
		} else {
			return false;
		}
		
	}
	
	/**
	 * 获取文件的大小
	 * @param fileName
	 * @return long
	 */
	public long getFileSize(String fileName) {
		return getFiledir(fileName).length();

	}
	/**
	 * 获取文件夹的大小
	 * @param fileName
	 * @return long
	 */
	public long getFileDirectorySize() {
		File file = new File(getStorageDirectory());
		try {
			return getFileSizes(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	/**
	 * 获取文件夹的大小 格式化为字符串
	 * @param fileName
	 * @return String
	 */
	public String getFileDirectorySizeStr() {
		return FormetFileSize(getFileDirectorySize());
		
	}
	
	/**
	 * 获取文件夹大小 
	 * @param f
	 * @return long
	 * @throws Exception
	 */
	public long getFileSizes(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		int flistLenght = flist.length;
		for (int i = 0; i < flistLenght; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 将文件大小 格式化
	 * @param fileS
	 * @return
	 */
	private static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = new StringBuilder().append(df.format((double) fileS)).append("B").toString();
		} else if (fileS < 1048576) {
			fileSizeString = new StringBuilder().append(df.format((double) fileS / 1024)).append("KB").toString();
		} else if (fileS < 1073741824) {
			fileSizeString = new StringBuilder().append(df.format((double) fileS / 1048576)).append("MB").toString();
		} else {
			fileSizeString = new StringBuilder().append(df.format((double) fileS / 1073741824)).append("GB").toString();
		}
		return fileSizeString;
	}

	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if(! dirFile.exists()){
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			int childrenLength = children.length;
			for (int i = 0; i < childrenLength; i++) {
				new File(dirFile, children[i]).delete();
			}
		}
		
		//不要删除目录 不然的话清除缓存后 把目录删除了 bitmaputils加载的图片就储存不进来
//		dirFile.delete();
	}
	
	
	/**
	 * 删除指定图片
	 * @param fileName
	 * @return
	 */
	public boolean deleteFile(String fileName) {
		if(null != fileName) {
			File file = getFiledir(fileName);
			if(!file.exists()) {
				return false;
			}
			if(file.isFile()) {
				file.delete();
				
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	
	/**
	 * 获得指定缓存图片最后修改时间
	 * @return
	 */
	public long getFileLastTime(String imgName) {
	
		if(imgName!=null) {
			if(isFileExists(imgName)) {
				
				return getFiledir(imgName).lastModified();
			} 
			
			return 0;
			
		} else {
			return 0;
		}
		
		
		
	}
	
	private File getFiledir(String filename) {
		try {
			return new File(getFiledirPath(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String getFiledirPath(String filename) {
		try {
			return new StringBuffer()
			.append(getStorageDirectory())
			.append(File.separator)
			.append(filename).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
