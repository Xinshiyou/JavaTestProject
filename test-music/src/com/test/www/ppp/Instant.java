package com.test.www.ppp;

public class Instant {

	private static volatile Instant instance = new Instant();

	private Instant() {
	}

	public static Instant getInstance() {
		return instance;
	}

}
