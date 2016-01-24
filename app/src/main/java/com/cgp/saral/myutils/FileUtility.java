package com.cgp.saral.myutils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileUtility {
	public static final String logs = "/SaralVaastu_ex_logs/";
	public static void writeexceptionInfile(String object, String strFileName) {
		try {
			String path = Environment.getExternalStorageDirectory()
					+ FileUtility.logs;
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			File outputFile = new File(f, strFileName);
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFile,
					true));
			out.write(DateProcessor.getDateDDMMYYYY() + " date----------->");
			out.write("Exception----------->" + object);
			out.write("------------------------");
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
	}
	
	
}
