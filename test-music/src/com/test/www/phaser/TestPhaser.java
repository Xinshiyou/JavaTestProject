package com.test.www.phaser;

import java.util.concurrent.Phaser;

public class TestPhaser extends Phaser {

	@Override
	protected boolean onAdvance(int phase, int registeredParties) {
		switch (phase) {
		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
		return super.onAdvance(phase, registeredParties);
	}
	
	private boolean firstStage() {
		System.out.println("第一阶段完成："+getRegisteredParties());
		return true;
	}
	
	private boolean secondStage() {
		System.out.println("第二阶段完成："+getRegisteredParties());
		return true;
	}
	
	

}
