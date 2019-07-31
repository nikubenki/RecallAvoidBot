package me.xuxiaoxiao.chatapi.wechat.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtils {
	/**
	 * 对象序列化
	 * 
	 * @param obj
	 * @param name
	 */
	public static void serialize(Object obj, String name) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(name));
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对象反序列化
	 * 
	 * @param name
	 * @return
	 */
	public static Object deserialize(String name) {
		Object obj = null;

		File file = new File(name);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			serialize(obj, name);
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
}
