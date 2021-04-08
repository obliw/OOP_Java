
// AUTHOR - Martin klintgården - martin.klintgarden.4571@student.uu.se

//package NimGame;
import java.util.Scanner;



class PileOfSticks{
    private int sticks;

    public void putStickinPile(int number){
        if (number<2){
            System.out.println("You need at least 2 sticks to play.");
            System.exit(0);
        }
        else {
            sticks = number;
        }
    }

    public int getSticksinPile(){
        return sticks;
    }

    public Boolean takeStick(int amount){
        if (amount>sticks/2 || amount <1) {
            invalidMove(amount);
            return false;
        }

        else {
            sticks = sticks-amount;
            return true;
        }


    }

    public void invalidMove(int amount) {
        System.out.println("You are not allowed to take "+amount+" sticks!");

    }
}

abstract class Player {
    public String name;

    public abstract void setName(String s);

    public abstract int draw(PileOfSticks pile);

    @Override
    public String toString() {
        return  name+": "+getClass().getSimpleName();
    }

}
class Human extends Player{

    public void setName(String s){
        name = s;
    }
    public int draw(PileOfSticks pile){
        System.out.println("Your move. There are "+pile.getSticksinPile()+" matches");
        Scanner input = new Scanner(System.in);
        int number = input.nextInt();
        if (!pile.takeStick(number)){
            number = draw(pile);
        }
        return number;
    }

}

class Computer extends Player{

    public void setName(String s){
        name = s;
    }

    public int bestMove(int pile) {

        int deathNumber = 1;
        while (deathNumber*2+1 <= pile){
            deathNumber = deathNumber*2+1;
        }
        if (pile == deathNumber){
            return 1;
        }
        return pile-deathNumber;
            }

    public int draw(PileOfSticks pile){
        int sticksToTake = bestMove(pile.getSticksinPile());
        pile.takeStick(sticksToTake);
        return sticksToTake;
    }
}

class Nm{
    private PileOfSticks pile;

    private Player player1;
    private Player player2;
    private Player[] players;

    public Nm(){
        pile = new PileOfSticks();

        // Välj mellan Human() eller Computer(), beroende på om du eller datorn ska spela.

        player1 = new Computer();
        player2 = new Human();
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;

        player1.setName("Player1");
        player2.setName("Player2");

    }

    // Metod som startar spelet efter initiering av Nm
    public void run(int sticks){
        System.out.println("Welcome to Nm");
        System.out.println(player1);
        System.out.println(player2);

        pile.putStickinPile(sticks);

        System.out.println("Remaining matches: "+pile.getSticksinPile());
        Player lastPlayer = player2;
        while (pile.getSticksinPile() > 1)
        {
            for (Player p:players)
                {
                    int lastDraw = p.draw(pile);
                    System.out.println(p.getClass().getSimpleName()+" removes "+lastDraw+" matches");
                    System.out.println("Remaining matches: "+pile.getSticksinPile());
                    if (pile.getSticksinPile() == 1)
                    {
                        System.out.println(lastPlayer+" loses, "+p+" won");
                        break;
                    }
                    lastPlayer = p;
                }

            }
        }
    }


public class NimGame {
    public static void main(String[] args) {
        Nm nimGame = new Nm();
        int intIn = Integer.parseInt(args[0]);
        nimGame.run(intIn);

    }
}
