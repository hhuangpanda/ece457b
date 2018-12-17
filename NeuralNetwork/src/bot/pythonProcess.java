package bot;

import java.lang.Runtime;
import java.lang.Process;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

class pythonProcess
{
	private Process process = null;
	private BufferedWriter out = null;
	private BufferedReader in = null;

	public void pythonProcess() { }

	public void startProcess() {
		try {
			process = Runtime.getRuntime().exec("MLP");
			
			out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			in 	= new BufferedReader(new InputStreamReader(process.getInputStream()));
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public void initProcess() {
		this.writeToProcesss("y");
		this.writeToProcesss("network.xml");
		this.writeToProcesss("y");
	}

	public double readFromProcess() {
		double ret = 0;
		try {
			ret = Double.parseDouble(in.readLine());
			System.err.println("Value is : " + ret);
		} catch (IOException e) {
			System.err.println(e);
		}
		System.err.printf(String.valueOf(ret));
		return ret;
	}

	public void writeToProcesss(String str) {
		System.err.printf("Talking to python process\n");
		try {
			out.write(str);
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void writeToProcesss(double dbl) {
		try {
			out.write(String.valueOf(dbl));
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void writeToProcesss(int intVal) {
		try {
			out.write(String.valueOf(intVal));
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void endProcess() throws IOException {
		process = null;
		out.close();
		in.close();
	}
}
