public class LogPad {
    public static void main(String[] args) {
        MainUI mainUI = new MainUI();
        Indexer indexer = new Indexer(mainUI);
        Thread thread = new Thread(new App(indexer, mainUI));
        thread.start();
        indexer.start(indexer);
    }
}