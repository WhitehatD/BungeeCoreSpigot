package me.whitehatd.BungeeCoreSpigot.Data;

import me.whitehatd.BungeeCoreSpigot.BCS;

public class AsyncTask {

    Thread thread;

    public AsyncTask(Runnable task) {
        this.thread = new Thread(task);
        this.thread.start();
        BCS.asyncThreads.add(this.thread);
    }


}
