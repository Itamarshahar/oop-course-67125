class BrowserNewsApp implements NewsObserver {
    public void pushNewsFlash(String newsFlash) {
        System.out.println(String.format("Browser News App News Flash: %s", newsFlash));
    }
}
