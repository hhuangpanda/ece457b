class pythonProcess
{
	private Process process = null;
	private BufferWriter out = null;
	private BufferedReader in = null;

	public void startProcess()
	{
		try
		{
			process = Runtime.exec(new String[]{"python3", "script.py"});
			
			out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			in 	= new BufferedReader(new InputStreamReader(process.getInputStream()));
		} 
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public double readFromProcess()
	{
		double ret = new Integer(in.readLine()).doubleValue();
		System.out.println("Value is : " + ret);
	}

	public void writeToProcesss(string str)
	{
		out.write(str);
		out.newLine();
	}

	public void endProcess()
	{
		process = null;
		out.close();
		in.close();
	}

}