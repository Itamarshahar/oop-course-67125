import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;


public class Main {

    /*DO NOT CHANGE!!!*/
    public static void main(String[] args) {
        Child[] children = {
                new Child("Edan", "04-02-2018", "Soccer"),
                new Child("Esther", "07-10-2018", "Frisbee"),
                new Child("Ori", "13-12-2018", "Acting"),
                new Child("Noa", "01-01-2018", "Math"),
                new Child("Lior", "07-07-2018", "Soccer"),
                new Child("Erel", "08-08-2018", "Computer Games"),
                new Child("Eldar", "09-09-2018", "Baseball"),
                new Child("Omri", "10-10-2018", "Painting"),
                new Child("Rachel", "11-11-2018", "Lego"),
                new Child("Dan", "12-12-2018", "Dancing"),
                new Child("Roy", "02-02-2018", "Reading"),
        };
        KindergartenTeacher teacher = new KindergartenTeacher(children.length);

        Kindergarten kindergarten = new Kindergarten(children, teacher);

        List<LocalDate> listOfDates = getListOfDates();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (var date : listOfDates) {
            teacher.isItTimeToCelebrate(formatter.format(date));
        }
    }

    private static List<LocalDate> getListOfDates() {
        // Our code is using some built-in java library, can you figure out what it does even without reading the documentation?
        // Food for thought: What kinds of decisions go into naming variables and methods so that they are easy to understand
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.with(firstDayOfYear());
        LocalDate endDate = now.with(lastDayOfYear());
        List<LocalDate> listOfDates = startDate.datesUntil(endDate).collect(Collectors.toList());
        return listOfDates;
    }
}


/*Implement all your classes below this line*/

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;


public class Main {

    /*DO NOT CHANGE!!!*/
    public static void main(String[] args) {
        Child[] children = {
                new Child("Edan", "04-02-2018", "Soccer"),
                new Child("Esther", "07-10-2018", "Frisbee"),
                new Child("Ori", "13-12-2018", "Acting"),
                new Child("Noa", "01-01-2018", "Math"),
                new Child("Lior", "07-07-2018", "Soccer"),
                new Child("Erel", "08-08-2018", "Computer Games"),
                new Child("Eldar", "09-09-2018", "Baseball"),
                new Child("Omri", "10-10-2018", "Painting"),
                new Child("Rachel", "11-11-2018", "Lego"),
                new Child("Dan", "12-12-2018", "Dancing"),
                new Child("Roy", "02-02-2018", "Reading"),
        };
        KindergartenTeacher teacher = new KindergartenTeacher(children.length);

        Kindergarten kindergarten=new Kindergarten(children,teacher);

        List<LocalDate> listOfDates = getListOfDates();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for(var date: listOfDates){
            teacher.isItTimeToCelebrate(formatter.format(date));
        }
    }

    private static List<LocalDate> getListOfDates() {
        // Our code is using some built-in java library, can you figure out what it does even without reading the documentation?
        // Food for thought: What kinds of decisions go into naming variables and methods so that they are easy to understand
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.with(firstDayOfYear());
        LocalDate endDate = now.with(lastDayOfYear());
        List<LocalDate> listOfDates = startDate.datesUntil(endDate).collect(Collectors.toList());
        return listOfDates;
    }
}


/*Implement all your classes below this line*/
class Child {
    // A Child should have information about his birth date.
    // A birth date should be a String with the format of “dd-mm-yyyy” - no need to check.
    // A Child should have a String for his name.
    // A Child should have a String for his favorite activity.
    // A Child should have a constructor and getters for all of his fields.
    private String name;
    private String favoriteActivity;
    private String birthDate; // "dd-mm-yyyy"
    public Child(String name, String birthDate,  String favoriteActivity) {
        this.name = name;
        this.favoriteActivity = favoriteActivity;
        this.birthDate = birthDate;
    }
    public String getName( ) {
        return this.name;
    }

    public String getFavoriteActivity() {
        return this.favoriteActivity;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setfavoriteActivity(String newFavoriteActivity) {
        this.favoriteActivity = newFavoriteActivity;
    }

    public void setbirthDate(String newBirthDate) {
        this.birthDate = newBirthDate;
    }
}

class BirthdayEvent {
    private String childName;
    private String favoriteActivity;
    private String birthDay;

    public BirthdayEvent(Child child) {
        this.childName = child.getName();
        this.favoriteActivity = child.getFavoriteActivity();

        // Ensure birth date string has at least 5 characters before extracting the substring
        if (child.getBirthDate().length() >= 5) {
            this.birthDay = child.getBirthDate().substring(0, 5);
        } else {
            this.birthDay = child.getBirthDate(); // Use full birth date if substring extraction is not possible
        }
    }


    public String getChildName() {
        return this.childName;
    }

    public String getChildBirthDate() {
        return this.birthDay;
    }

    public String getChildFavoriteActivity() {
        return this.favoriteActivity;
    }
} // TODO maybe add fields?

class Calendar {
    private int maxSize;
    private BirthdayEvent[] birthdayEvents;

    public Calendar(int maxSize) {
        this.maxSize = maxSize;
        this.birthdayEvents = new BirthdayEvent[maxSize];
    }

    public void addBirthdayEvent(BirthdayEvent event) {
        for (int i = 0; i < maxSize; i++) {
            if (birthdayEvents[i] == null) {
                birthdayEvents[i] = event;
                // Once the event is added, exit the loop
                break;
            }
        }
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public BirthdayEvent[] getBirthdayEvents() {
        return birthdayEvents;
    }
}
class KindergartenTeacher {
    private int maxNumOfChildrens;
    private Calendar calendar;

    public KindergartenTeacher(int maxNumOfChildrens) {
        this.maxNumOfChildrens = maxNumOfChildrens;
        // Initialize the calendar with the maximum number of children
        this.calendar = new Calendar(maxNumOfChildrens);
    }

    public void addBirthdayEvent(BirthdayEvent event) {
        calendar.addBirthdayEvent(event);
    }

    public void isItTimeToCelebrate(String date) {
        // Check if calendar is null before accessing its birthdayEvents array
        String dateFormated = date.substring(0, 5);
        for (BirthdayEvent event : calendar.getBirthdayEvents()) {
            if (event.getChildBirthDate().equals(dateFormated)) {
                    System.out.println("For " + event.getChildName() + "'s Birthday party I need to prepare his favorite activity: " + event.getChildFavoriteActivity());
        }
            }
        }
    }
class Kindergarten {
    private KindergartenTeacher teacher;
    private Child[] children;

    //A Kindergarten should have a KindergartenTeacher and an array of Children that it receives in its constructor.
    public Kindergarten(Child[] children, KindergartenTeacher teacher) {
        this.children = children;
        this.teacher = teacher;
        this.addBirthdayEvents();
    }

    // The Kindergarten should take care of adding a BirthdayEvent to the KindergartenTeacher’s Calendar for every one of the children in the array.
    public void addBirthdayEvents() {
        for (Child child : children) {
            BirthdayEvent event = new BirthdayEvent(child);
            teacher.addBirthdayEvent(event);
        }
    }
}

