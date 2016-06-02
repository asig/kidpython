package com.asigner.kidpython;

import java.io.IOException;
import java.io.InputStream;

public class Interpreter {
	
	private Process process;
	private InputStream errStream, outStream;
	
	public Interpreter() {
	}
	
	public void start() throws IOException {
		this.process = Runtime.getRuntime().exec("/usr/bin/python");
		final InputStream errStream = this.process.getErrorStream();
		Thread errReader = new Thread(new Runnable() {
			@Override
			public void run() {
				byte buf[] = new byte[10240];
				int read;
				try {
					while ( (read = errStream.read(buf)) > 0 ) {
						onRead(buf, 0, read);						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// TODO Auto-generated method stub
				
			}});
		errReader.setDaemon(true);
		errReader.start();
		
	}
	
	private void onRead(byte data[], int start, int len) {
		
	}

}
