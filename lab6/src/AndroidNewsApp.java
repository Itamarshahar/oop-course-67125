class AndroidNewsApp implements NewsObserver {
    public void pushNewsFlash(String newsFlash) {
        System.out.println(String.format("Android News App News Flash: %s", newsFlash));
    }
}
