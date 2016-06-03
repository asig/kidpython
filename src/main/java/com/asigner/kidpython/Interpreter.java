package com.asigner.kidpython;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Interpreter {

    private Process process;
    private InputStream stdErr, stdOut;
    private OutputStream stdIn;

    class StreamReader implements Runnable {
        private final InputStream is;

        public StreamReader(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            byte buf[] = new byte[10240];
            int read;
            try {
                while ((read = is.read(buf)) > 0) {
                    onRead(buf, 0, read);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Interpreter() {
    }

    public void write(String str) throws IOException {
        stdIn.write(str.getBytes());
        stdIn.flush();
    }

    public void start() throws IOException {
        this.process = new ProcessBuilder()
                .command("/bin/bash")
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .start();
//        this.process = new ProcessBuilder().command("/usr/bin/python", "-u").start();
//        this.process = Runtime.getRuntime().exec("/bin/ls -l /home/asigner");
        stdErr = this.process.getErrorStream();
        stdOut = this.process.getInputStream();
        stdIn = this.process.getOutputStream();
        Thread errReader = new Thread(new StreamReader(stdErr));
        errReader.setDaemon(true);
        errReader.start();
        Thread outReader = new Thread(new StreamReader(stdOut));
        outReader.setDaemon(true);
        outReader.start();
    }

    public void stop() {
        process.destroy();
    }

    private void onRead(byte data[], int start, int len) {
        System.out.write(data, start, len);
    }

}
