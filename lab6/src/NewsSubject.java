interface NewsSubject {
    /**
     * Registers new observer
     *
     * @return true if successful, false otherwise
     */
    public boolean registerObserver(NewsObserver observer);

    /**
     * Unregisters observer
     *
     * @return true if successful, false otherwise
     */
    public boolean unregisterObserver(NewsObserver observer);

    /**
     * Notifies all observers with the new newsFlash
     */
    public void pushNotification(String newsFlash);
}
