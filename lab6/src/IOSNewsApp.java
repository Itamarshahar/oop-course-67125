class IOSNewsApp implements NewsObserver {
    public void pushNewsFlash(String newsFlash) {
        System.out.println(String.format("IOS News App News Flash: %s", newsFlash));
    }
}
