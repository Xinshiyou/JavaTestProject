package com.hundun.python;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.python.core.PyCode;
import org.python.util.PythonInterpreter;

public class PythonTest {

	private static final String config = "config.py";
	private static final String exec = "example_image = " + "'%s';"
			+ "input_image = caffe.io.load_image(example_image);" + "prediction = age_net.predict([input_image]) ;"
			+ "print 'predicted age:', age_list[prediction[0].argmax()];"
			+ "prediction = gender_net.predict([input_image]) ;"
			+ "print 'predicted gender:', gender_list[prediction[0].argmax()]";

	public static void main(String[] args) throws FileNotFoundException {

		if (args.length < 1) {
			System.out.println("Less parameters: photo's path");
			return;
		}

		InputStream in = PythonTest.class.getResourceAsStream(config);
		InputStreamReader isr = new InputStreamReader(in);
		PythonInterpreter instance = PythonTestMain.getInstance().getPythonInterpreter();
		PyCode pcode = instance.compile(isr);
		instance.exec(String.format(exec, args[0]));
		System.out.println("Result:" + instance.getSystemState().toString());
		instance.close();

	}

}
