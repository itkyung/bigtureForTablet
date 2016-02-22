package com.clockworks.android.tablet.bigture.views.sketchbook.popup;

import java.io.UnsupportedEncodingException;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * EditText ���� �ʵ忡 �ؽ�Ʈ �Է�/������ 
 * �Է¹��ڿ��� ����Ʈ ���̸� üũ�Ͽ� �Է��� �����ϴ� ����.
 *
 */
public class ByteLengthFilter implements InputFilter {


	private String mCharset; //���ڵ� ���ڼ�

	protected int mMaxByte; // �Է°����� �ִ� ����Ʈ ����

	public ByteLengthFilter(int maxbyte, String charset) {
		this.mMaxByte = maxbyte;
		this.mCharset = charset;
	}

	/**
	 * �� �޼ҵ�� �Է�/���� �� �ٿ��ֱ�/�߶󳻱��� ������ ����ȴ�.
	 *
	 * - source : ���� �Է�/�ٿ��ֱ� �Ǵ� ���ڿ�(����/�߶󳻱� �ÿ��� "")
	 * - dest : ���� �� �� ���ڿ�
	 */
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
			int dend) {

		// ���� �� ����Ǵ� ���ڿ�
		String expected = new String();
		expected += dest.subSequence(0, dstart);
		expected += source.subSequence(start, end);
		expected += dest.subSequence(dend, dest.length());

		int keep = calculateMaxLength(expected) - (dest.length() - (dend - dstart));

		if (keep <= 0) {
			return ""; // source �Է� �Ұ�(�� ���ڿ� ���� ����)
		} else if (keep >= end - start) {
			return null; // keep original. source �״�� ���
		} else {
			return source.subSequence(start, start + keep); // source�� �Ϻθ� �Է� ���
		}
	}

	/**
	 * �Է°����� �ִ� ���� ����(�ִ� ����Ʈ ���̿� �ٸ�!).
	 */
	protected int calculateMaxLength(String expected) {
		return mMaxByte - (getByteLength(expected) - expected.length());
	}    

	/**
	 * ���ڿ��� ����Ʈ ����.
	 * ���ڵ� ���ڼ¿� ��� ����Ʈ ���� �޶���.
	 * @param str
	 * @return
	 */
	private int getByteLength(String str) {
		try {
			return str.getBytes(mCharset).length;
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		return 0;
	}
}    
