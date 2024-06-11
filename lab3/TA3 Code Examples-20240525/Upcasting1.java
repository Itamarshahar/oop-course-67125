interface Renderable {
    void render();
}

class Photo implements Renderable {
    private String photo = "~ ☼ \n≈≈≈≈";

    //implement render();
    @Override
    public void render() {
        System.out.println(photo);
    }
}

class Document implements Renderable {
    private String[] text;

    public Document(String[] text) {
        this.text = text;
    }

    //implement render();
    @Override
    public void render() {
        for (String line:text ) {
            System.out.println(line);
        }
    }
}





class Example1 {
    public static void main(String[] args) {
        Photo p = new Photo();
        Document d = new Document(new String[]{"This is an important, albeit empty, document"});
        //p.render();
        //d.render();

        //change to more general code with upcasting
        Renderable[] renderables=new Renderable[]{p,d };
        for(Renderable r: renderables){
            r.render();
        }
    }
}
