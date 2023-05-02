public class NativeEnd implements Runnable{
    private Indexer indexer;
    private MainUI mainUI;
    public NativeEnd(Indexer indexer, MainUI mainUI) {
        this.indexer = indexer;
        this.mainUI = mainUI;
    }
    @Override
    public void run() {
        while(true) {
            if(mainUI.isInterrupted() == 1) {
                indexer.setInterrupt(1);
                break;
            }
        }
    }
}