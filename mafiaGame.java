import java.util.Collection;
import java.util.HashMap;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

abstract class player{
    private final int id;
    private float hp;
    private boolean alive;
    private final HashMap<Integer,player> map = new HashMap<>();

    player(int id, float num)
    {this.id = id;
        this.alive=true;
        this.hp = num; }

    abstract String gettype();
    abstract void remove(int n);
    protected float getHP(){return this.hp;}
    protected void setHP(float num){ this.hp = num;}
    protected boolean isAlive(){return this.alive;}
    protected void setBoolean(boolean b){this.alive = b;}
    protected int getid(){return this.id;}
    protected void add(player o) { map.put(o.id,o);}
    protected boolean hasplayer(int i) { return map.containsKey(i); }
    protected player getplayer(int i) {return map.get(i);}

    @Override
    public boolean equals(Object o1) {
        if(o1 != null && getClass() == o1.getClass()) {
            player o = (player) o1; //type casting
            return (this.id==o.getid() && this.gettype().equals(o.gettype())); }
        else { return false; }
    }

}

class detective extends player{

    private final String playertype;
    private final static HashMap<Integer, detective> detectives =  new HashMap<>();
    private final HashMap<Integer, Boolean> testedmafias = new HashMap<>();

    detective(int id, float hp)
    {super(id, hp);
        playertype = "detective";}

    @Override
    protected String gettype(){ return this.playertype; }

    @Override
    protected void remove(int n){
        detectives.remove(n);
    }

    public void addDetectives(detective d){
        detectives.put(d.getid(),d);
    }

    public boolean hasDetective(int i){
        return detectives.containsKey(i);
    }

    public ArrayList<Integer> getdetectives(){
        Set<Integer> keySet = detectives.keySet();
        return new ArrayList<>(keySet);
    }

    public boolean isTested(int i){
        return testedmafias.containsKey(i);
    }

    public void addtoTested(int i){
        testedmafias.put(i,true);
    }

}

class commoner extends player{
    private final String playertype;
    private final static HashMap<Integer, commoner> comm = new HashMap<>();
    commoner(int id, float hp)
    { super(id, hp);
        playertype = "commoner";}

    @Override
    protected String gettype(){ return this.playertype; }

    @Override
    protected void remove(int n){
        comm.remove(n);
    }

    public void addCommoners(commoner c){
        comm.put(c.getid(),c);
    }

}

class healer extends player{
    private final String playertype;
    private final static HashMap<Integer, healer> heal = new HashMap<>();
    healer(int id, float hp)
    { super(id, hp);
        playertype = "healer"; }

    @Override
    protected String gettype(){ return this.playertype; }

    @Override
    protected void remove(int n){
        heal.remove(n);
    }

    public void addHealers(healer h){
        heal.put(h.getid(),h);
    }

    public ArrayList<Integer> gethealers(){
        Set<Integer> keySet = heal.keySet();
        return new ArrayList<>(keySet);

    }

}

class mafia extends player{
    private final String playertype;
    private final static HashMap<Integer, mafia> maf = new HashMap<>();
    mafia(int id, float hp)
    { super(id, hp);
        playertype= "mafia"; }

    @Override
    protected String gettype(){ return this.playertype; }

    @Override
    protected void remove(int n){
        maf.remove(n);
    }

    public void addMafias(mafia m){
        maf.put(m.getid(),m);
    }

    public ArrayList<Integer> getmafias(){
        Set<Integer> keySet = maf.keySet();
        return new ArrayList<>(keySet);
    }

    public ArrayList<mafia> getsortedHPs(){
        Collection<mafia> keySet = maf.values();
        ArrayList<mafia> values = new ArrayList<>(keySet);
        Collections.sort(values, new sortMafia());
        return values;
    }

    public boolean hasaMafia(int n){
        return maf.containsKey(n);
    }

}

class sortMafia implements Comparator<mafia> {
    @Override
    public int compare(mafia one, mafia two){
        if((one.getHP())<(two.getHP())){
            return -1;
        }else{
            return 0;
        }
    }
}

class gamestart <T>{

    static Scanner scn = new Scanner(System.in);
    static Random rand = new Random();
    private ArrayList <T> myList = new ArrayList<>();
    private void add(T o)
    { myList.add(o); }
    private T get(int i)
    { return myList.get(i); }

    gamestart(boolean br) {
        if (br) {
            startgame();
        } else {
            myList = new ArrayList<>();
        }
    }
    private void startgame(){
        System.out.println("Welcome to Mafia!");
        System.out.println("Enter Number of players");
        int n = scn.nextInt();

        while (n<6){
            System.out.println("Minimum number of players is 6. Please enter the number of players again");
            n = scn.nextInt(); }

        System.out.println("Choose a Character\n" +
                "1) Mafia\n" + "2) Detective\n" + "3) Healer\n" + "4) Commoner\n" + "5) Assign Randomly");

        int ch = scn.nextInt();
        while (ch>5 || ch<1){System.out.println("Enter a valid number"); ch = scn.nextInt();}
        player p;

        if (ch==5){ ch = rand.nextInt(4)+1;}
        if (ch==1){ p = new mafia(1, 2500); }
        else if (ch==2){ p = new detective(1,800); }
        else if (ch==3){ p = new healer(1,800); }
        else { p = new commoner(1,1000); }

        //deciding the number of players
        int numMafia = n/5;
        int numDetectives = n/5;
        int numHealers = Math.max(1, n/10);
        int numCommoners = n-(numMafia+numDetectives+numHealers);
        decideplayers(numMafia,numDetectives,numHealers,numCommoners, p);
    }

    protected void decideplayers(int nm, int nd, int nh, int nc, player p){

        System.out.println("You are Player 1");
        p.add(p);
        mafia m = new mafia(0,2500);
        detective d = new detective(0, 800);
        healer h = new healer(0,800);
        commoner c = new commoner(0,1000);

        if ((p.gettype()).equals("mafia")){
            m.addMafias((mafia) p);
        }else if ((p.gettype()).equals("detective")){
            d.addDetectives((detective)p);
        }else if ((p.gettype()).equals("healer")){
            h.addHealers((healer) p);
        }

        int n = nm+nd+nh+nc;

        //ADDING DETECTIVES
        int dtemp = nd;
        if (p.gettype().equals("detective"))
        {dtemp--;
            if (dtemp==0){System.out.println("You are the only detective.");}
            else{ System.out.print("You are a detective. Other detective(s) are ");}
        }
        for (int i=0; i<dtemp;i++){
            int ch = gamestart.rand.nextInt(n)+1;
            while (p.hasplayer(ch)) {
                ch = gamestart.rand.nextInt(n)+1;
            }
            detective d1 = new detective(ch, 800);
            if (p.gettype().equals("detective"))
            {System.out.print(" Player "+ ch+" ");}
            d.addDetectives(d1);
            p.add(d1);
        }
        if (p.gettype().equals("detective")){System.out.println();}


        //ADDING MAFIAS
        int mtemp = nm;
        if (p.gettype().equals("mafia"))
        {mtemp--;
            if (mtemp==0){System.out.println("You are the only mafia.");}
            else{ System.out.print("You are a mafia. Other mafia(s) are ");}
        }
        for (int i=0; i<mtemp;i++){
            int ch = gamestart.rand.nextInt(n)+1;
            while (p.hasplayer(ch)) {
                ch = gamestart.rand.nextInt(n)+1;
            }
            mafia m1 = new mafia(ch, 2500);
            if (p.gettype().equals("mafia")){System.out.print("Player "+ ch+" ");}
            m.addMafias(m1);
            p.add(m1);
        }
        if (p.gettype().equals("mafia")){System.out.println();}


        //ADDING COMMONERS
        int ctemp = nc;
        if (p.gettype().equals("commoner"))
        {ctemp--;
            System.out.println("You are a commoner.");}
        for (int i=0; i<ctemp;i++){
            int ch = gamestart.rand.nextInt(n)+1;
            while (p.hasplayer(ch)) {
                ch = gamestart.rand.nextInt(n)+1;
            }
            commoner c1 = new commoner(ch, 1000);
            c.addCommoners(c1);
            p.add(c1);
        }

        //ADDING HEALERS
        int htemp = nh;
        if (p.gettype().equals("healer"))
        {htemp--;
            if (htemp==0){System.out.println("You are the only healer.");}
            else{ System.out.print("You are a healer. Other healer(s) are ");}}
        for (int i=0; i<htemp;i++){
            int ch = gamestart.rand.nextInt(n)+1;
            while (p.hasplayer(ch)) {
                ch = gamestart.rand.nextInt(n)+1;
            }
            healer h1 = new healer(ch, 800);
            if (p.gettype().equals("healer")){System.out.print("Player "+ ch+" ");}
            h.addHealers(h1);
            p.add(h1);
        }
        if (p.gettype().equals("healer")){System.out.println();}

        startPlaying(nm, (nh+nc+nd),p);
    }

    private void startPlaying(int numMafia, int nonMafia, player p){
        int i=1;
        int nm = numMafia;
        int nn = nonMafia;
        mafia m = new mafia(1, 2500);
        detective d = new detective(1,800);
        healer h = new healer(1,800);
        commoner c = new commoner(1,1000);

        while ((nn>nm) && (nn>0) && (nm>0)){
            boolean mfound = false;  // if mafia is found

            //Round number and players Alive
            System.out.println("---------------------------------------------------------" +
                    "Round "+i+"----------------------------------------------------------");
            System.out.print((nn+nm)+" Players remaining: ");
            for (int j=0; j<(numMafia+nonMafia);j++){
                if (p.getplayer(j+1).isAlive())
                { System.out.print("Player "+(j+1)+", "); }
            }
            System.out.println("are alive.");

            int rint;

            if (((p.gettype()).equals("mafia")) && ((p.getplayer(1)).isAlive())){
                System.out.println("Choose a target");
                rint = scn.nextInt();
                while (m.hasaMafia(rint) || !((p.getplayer(rint)).isAlive())){
                    if (m.hasaMafia(rint)){System.out.println("This is a mafia! Choose target again");}
                    else if (!((p.getplayer(rint)).isAlive())){System.out.println("This player is dead! Choose target again");}
                    else{System.out.println("This player is a dead mafia! Choose target again");}
                    rint = scn.nextInt();
                }

            }else{
                rint = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                while (m.hasaMafia(rint) || !((p.getplayer(rint)).isAlive())){
                    rint = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                }
            }

            player target = p.getplayer(rint);
            if (p.gettype().equals("mafia"))
            {System.out.println("Target is player "+ target.getid());}

            float inithp = target.getHP();
            ArrayList<Integer> mlist = m.getmafias();
            float combinedhp =0;
            int nummaf=0;

            for(int j=0; j< mlist.size();j++){
                if ((p.getplayer(mlist.get(j))).getHP()>0){nummaf++;}
                combinedhp += p.getplayer(mlist.get(j)).getHP();
            }

            if (combinedhp>=target.getHP()){
                target.setHP(0);
            }else{
                target.setHP(target.getHP()-combinedhp);
            }


            float decreased = inithp/nummaf;
            float totamt =0;
            ArrayList<mafia> sortedhps = m.getsortedHPs();
            int cnt=0;
            for(int k=0; k< sortedhps.size();k++){
                player mf = sortedhps.get(k);
                if ((mf.getHP()<(decreased)) && !(mf.getHP()<=0)){
                    totamt+= (decreased-mf.getHP());
                    mf.setHP(0);
                }else if (!(mf.getHP()<=0)){
                    cnt++;
                }
            }
            float newamount = (inithp - totamt)/cnt;
            for(int k=0; k< sortedhps.size();k++){
                player mf = sortedhps.get(k);
                if (mf.getHP()>=0){
                    mf.setHP(mf.getHP()-newamount);
                }
            }

            if (!(p.gettype().equals("mafia"))){System.out.println("Mafias have chosen their target");}

            if (!((p.gettype()).equals("detective"))){System.out.println("Detectives have chosen a player to test");}
            if (d.getdetectives().size()>0){
                int test;
                if (((p.getplayer(1)).gettype().equals("detective")) &&(p.getplayer(1).isAlive())){
                    System.out.println("Choose a player to test");
                    test = scn.nextInt();
                    while (d.hasDetective(test) ||d.isTested(test)|| !((p.getplayer(test)).isAlive())){
                        if (d.hasDetective(test)){System.out.println("You cannot test a detective. Choose a player to test");}
                        else if (d.isTested(test)){System.out.println("Already tested! Not a mafia. Choose again");}
                        else{System.out.println("You cannot test a dead player. Choose again");}
                        test =  scn.nextInt();
                    }
                }
                else{
                    test = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                    while (d.hasDetective(test) ||d.isTested(test)|| !((p.getplayer(test)).isAlive())){
                        test =  gamestart.rand.nextInt(numMafia+nonMafia)+1;
                    }
                }

                if (m.hasaMafia(test)){
                    mfound = true;
//                    if (p.gettype().equals("detective"))
                    {System.out.println("Player "+test +" is a mafia");}
                    (p.getplayer(test)).setBoolean(false);
                    m.remove(test);
                    nm--;
                }else{
                    d.addtoTested(test);
                    if (p.gettype().equals("detective"))
                    {System.out.println("Player "+test +" is not a mafia");}
                }
            }



            //healers save someone
            int save;
            player healed = h;
            if (h.gethealers().size()>0){
                if ((p.getplayer(1).gettype().equals("healer")) && (p.getplayer(1).isAlive())){
                    System.out.println("Choose a person to heal");
                    save = scn.nextInt();
                    while (!(p.getplayer(save).isAlive())){
                        System.out.println("This player is dead! Choose again");
                        save = scn.nextInt();
                    }
                }else{
                    save = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                    while (!(p.getplayer(save).isAlive())){
                        save = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                    }
                }

                healed = p.getplayer(save);
                if (p.gettype().equals("healer"))
                {System.out.println("Player healed is "+ healed.getid());}
                healed.setHP(healed.getHP()+500);
            }
            if (!((p.gettype()).equals("healer"))){System.out.println("Healers have chosen a player to heal");}

            System.out.println("----End of Actions----");
            if ((target.getHP()<=0) && target.isAlive()){
                target.setBoolean(false);
                String type = target.gettype();
                int id = target.getid();
                if (type.equals("commoner")){c.remove(id);}
                else if (type.equals("healer")){h.remove(id);}
                else{d.remove(target.getid());}
                System.out.println(target.getid()+" is dead");
                nn--;
            }else{
                System.out.println("No one died");
            }

            if ((nm>=nn)|| nn==0|| nm==0){break;}

            //voting is done when mafia is not found
            if (!mfound){
                int voted;
                voted = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                while (!(p.getplayer(voted).isAlive())){
                    voted = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                }
                if (p.getplayer(1).isAlive()){
                    System.out.println("Select a person to vote out");
                    int ip = gamestart.scn.nextInt();
                    while (!((p.getplayer(ip)).isAlive())){
                        System.out.println("This player is dead. Choose again:");
                        ip = gamestart.rand.nextInt(numMafia+nonMafia)+1;
                    }
                }

                System.out.println("Player "+voted +" has been voted out");
                if ((p.getplayer(voted)).gettype().equals("mafia")){
                    m.remove(voted);
                    nm--;
                }else{
                    if (((p.getplayer(voted)).gettype()).equals("commoner")){c.remove(voted);}
                    else if (((p.getplayer(voted)).gettype()).equals("healer")){h.remove(voted);}
                    else{d.remove(voted);}
                    nn--;
                }
                p.getplayer(voted).setBoolean(false);
            }

            System.out.println("--------------------------------------------------End of Round "+i+"--------------------------------------------------");
            i++;
        }

        System.out.println("----Game over----");
        if ((nm>=nn) || nn==0){
            System.out.println("The Mafias have won");
        }else{
            System.out.println("The Mafias have lost");
        }
        gameover(numMafia, nonMafia, p);
    }

    private void gameover(int numMafia, int nonMafia, player p){
        gamestart<mafia> mList = new gamestart<>(false);
        gamestart<detective> dList = new gamestart<>(false);
        gamestart<commoner> cList = new gamestart<>(false);
        gamestart<healer> hList = new gamestart<>(false);

        for (int j=0; j<(numMafia+nonMafia);j++){
            if (((p.getplayer(j+1)).gettype()).equals("mafia")){
                mList.add((mafia) (p.getplayer(j+1)));
            }
            else if (((p.getplayer(j+1)).gettype()).equals("detective")){
                dList.add((detective) (p.getplayer(j+1)));
            }
            else if (((p.getplayer(j+1)).gettype()).equals("healer")){
                hList.add((healer) (p.getplayer(j+1)));
            }
            else{
                cList.add((commoner) (p.getplayer(j+1)));
            }
        }

        System.out.println("Mafias:");

        for (int j=0; j<mList.size();j++){
            System.out.print("Player "+ (mList.get(j)).getid()+" ");
            if ((mList.get(j)).getid()==1){
                System.out.print("(User) ");
            }
        }
        System.out.println("\nDetectives:");
        for (int j=0; j<dList.size();j++){
            System.out.print("Player "+ (dList.get(j)).getid()+" ");
            if ((dList.get(j)).getid()==1){
                System.out.print("(User) ");
            }
        }
        System.out.println("\nHealer:");
        for (int j=0; j<hList.size();j++){
            System.out.print("Player "+ (hList.get(j)).getid()+" ");
            if ((hList.get(j)).getid()==1){
                System.out.print("(User) ");
            }
        }System.out.println("\nCommoners:");
        for (int j=0; j<cList.size();j++){
            System.out.print("Player "+ (cList.get(j)).getid()+" ");
            if ((cList.get(j)).getid()==1){
                System.out.print("(User) ");
            }
        }
    }

    private int size() {
        return myList.size();
    }

}

public class mafiaGame {
    mafiaGame(){ new gamestart(true); }
    public static void main(String[] args) {
        new mafiaGame();
    }

}
