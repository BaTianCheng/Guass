package com.esb.guass.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import com.esb.guass.common.util.FileUtils;

public class FileTest {
	
	public static void main(String[] args) throws Exception {
		File file = new File("F:\\data2.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		
		
	}

}
