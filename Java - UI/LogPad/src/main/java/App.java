public class App implements Runnable {
    private Indexer indexer;
    private MainUI mainUI;
    public App(Indexer indexer, MainUI mainUI) {
        this.indexer = indexer;
        this.mainUI = mainUI;
    }
    @Override
    public void run() {
        mainUI.init(mainUI);
        Thread thread = new Thread(new NativeEnd(indexer, mainUI));
        thread.start();
    }
}
