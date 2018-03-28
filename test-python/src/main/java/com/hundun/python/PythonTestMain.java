package com.hundun.python;

import java.util.Properties;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 * @desc Jython环境,生存python解释器
 * @author webim
 */
public final class PythonTestMain {

	private static PythonTestMain INSTANCE = new PythonTestMain();

	private PythonTestMain() {
	}

	/**
	 * @DESC 获取单例
	 * @return JythonEnvironment
	 */
	public static PythonTestMain getInstance() {
		return INSTANCE;
	}

	/**
	 * @DESC 获取python系统状态,可根据需要指定classloader/sys.stdin/sys.stdout等
	 * @return PySystemState
	 */
	private PySystemState getPySystemState() {
		PySystemState.initialize();
		final PySystemState py = new PySystemState();
		py.setClassLoader(getClass().getClassLoader());
		return py;
	}

	/**
	 * @DESC 获取python解释器
	 * @return PythonInterpreter
	 */
	public PythonInterpreter getPythonInterpreter() {

		Properties props = new Properties();
		props.put("python.home", "/root/anaconda3/envs/python27/lib");
		props.put("python.console.encoding", "UTF-8");
		props.put("python.security.respectJavaAccessibility", "false");
		props.put("python.import.site", "false");
		Properties preprops = System.getProperties();

		PythonInterpreter.initialize(preprops, props, new String[0]);
		PythonInterpreter inter = new PythonInterpreter(null, getPySystemState());
		return inter;

	}
}
