interface Printable {
    void Print(String strToPrint);
}

interface Faxable {
    void Fax(String strToFax);
}

interface Scannable {
    void Scan(String strToScan);
}

class CanDoAllPrinter implements Printable, Faxable, Scannable {

    @Override
    public void Print(String strToPrint) {
        System.out.println("Printing... \n" + strToPrint);
    }

    @Override
    public void Fax(String strToFax) {
        System.out.println("Faxing... \n" + strToFax);
    }
    @Override
    public void Scan(String strToScan) {
        System.out.println("Scanning... \n" + strToScan);
    }
}

class Secretary {
    public void printSomething(Printable printable) {
        printable.Print("This is just a printer");
    }

    public void faxSomething(Faxable faxable) {
        faxable.Fax("This is just a fax machine");
    }

    public void scanSomething(Scannable scannable) {
        scannable.Scan("This is just a scanner");
    }

    public static void main(String[] args) {
        Secretary secretary = new Secretary();
        CanDoAllPrinter canDoAllPrinter = new CanDoAllPrinter();
        //call all options of interfaces
        secretary.printSomething(canDoAllPrinter);
        secretary.faxSomething(canDoAllPrinter);
        secretary.scanSomething(canDoAllPrinter);

    }
}

