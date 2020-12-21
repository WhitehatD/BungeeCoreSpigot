package me.whitehatd.BungeeCoreSpigot.Utilities;

import me.whitehatd.BungeeCoreSpigot.BCS;

public class AsyncTask {

    Thread thread;

    public AsyncTask(Runnable task) {
        this.thread = new Thread(task);
        this.thread.start();
        BCS.asyncThreads.add(this.thread);
    }


}
