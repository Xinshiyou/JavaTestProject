package com.hundun.python;

import java.io.FileNotFoundException;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class PythonTest {

	private static final String exec = "example_image = '%s';" + "input_image = caffe.io.load_image(example_image);"
			+ "prediction = age_net.predict([input_image]) ;" + "pre_age = age_list[prediction[0].argmax()];"
			+ "prediction = gender_net.predict([input_image]);" + "pre_gender = gender_list[prediction[0].argmax()]";

	public static void main(String[] args) throws FileNotFoundException {

		if (args.length < 2) {
			System.out.println("Less parameters: photo's path");
			return;
		}

		long begin = System.nanoTime();

		PythonInterpreter instance = PythonTestMain.getInstance().getPythonInterpreter();
		PySystemState sys = Py.getSystemState();
		sys.path.add("/root/anaconda3/envs/python27/lib/python2.7/site-packages");
		sys.path.add("/root/anaconda3/envs/python27");

		instance.execfile(args[0]);
		System.out.println("Initizlie Time:" + (System.nanoTime() - begin));

		begin = System.nanoTime();
		instance.exec(String.format(exec, args[1]));
		PyObject gender = instance.get("pre_gender");
		PyObject age = instance.get("pre_age");
		System.out.println("Age:" + age + ",Gender:" + gender);
		System.out.println("First proces Time:" + (System.nanoTime() - begin));

		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}

		begin = System.nanoTime();
		instance.exec(String.format(exec, args[2]));
		gender = instance.get("pre_gender");
		age = instance.get("pre_age");
		System.out.println("Age:" + age + ",Gender:" + gender);
		System.out.println("Second proces Time:" + (System.nanoTime() - begin));

		instance.cleanup();
		instance.close();

	}

}
