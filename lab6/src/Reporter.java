class Reporter {
    private NewsDesk myNewsDesk;

    public Reporter(NewsDesk newsDesk) {
        this.myNewsDesk = newsDesk;
    }

    public void discoveredScoop(String newsScoop) {
        myNewsDesk.pushNotification(newsScoop);
    }
}
