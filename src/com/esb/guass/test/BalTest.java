package com.esb.guass.test;

import org.redkale.convert.Convert;

import com.esb.guass.common.util.ConvertUtils;

public class BalTest {
	public static void main(String[] args) throws Exception {
		Double usableSum = ConvertUtils.toDouble2(Double.valueOf("2.30") * 100);
		Long balance = usableSum.longValue();
		System.out.println(balance);
	}
}
