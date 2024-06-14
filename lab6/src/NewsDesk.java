import java.util.ArrayList;
import java.util.List;

public class NewsDesk implements NewsSubject {
    private List<NewsObserver> observers = new ArrayList<>();
    private List<String> news = new ArrayList<>();

    public boolean registerObserver(NewsObserver observer) {
        if (observers.size() < 10) {
            observers.add(observer);
            return true;
        }
        return false;
    }

    public boolean unregisterObserver(NewsObserver observer) {
        return observers.remove(observer);
    }

    public void pushNotification(String newsFlash) {
        news.add(newsFlash);
        for (NewsObserver obs : observers) {
            obs.pushNewsFlash(newsFlash);
        }
    }

    public void printNews() {
        System.out.println("News Summary:");
        for (String newsFlash : news) {
            System.out.println(newsFlash);
        }
    }
}
