import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.ArrayList;
//import java.util.concurrent.Flow;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The main class for running the Cluedo Game. Responsible for the key functions involved in the game.
 */
public class Game {
    //MEMBER VARIABLES
    private final int boardWidth = 24;
    private Board board = new Board();
    int n = 0;
    private ArrayList<Item> players = new ArrayList<>();
    private Stack<Card> deck = new Stack<>();
    private ArrayList<RoomCard> rooms = new ArrayList<>();
    private ArrayList<Card> tempDeck = new ArrayList<>(); //might not need
    private ArrayList<Suggestion> accusations = new ArrayList<>();
    private ArrayList<Item> tempSuspects = new ArrayList<>();
    private HashMap<Item, RoomCard> prevRoom = new HashMap<Item, RoomCard>();
    //private HashMap<Card, RoomCard> prevRoom = new HashMap<> ();
    private int playerCount = 0;
    List<Suspect> suspectCards;
    HashMap<String, RoomCard> roomMap=new HashMap<String, RoomCard>();
    HashMap<RoomCard,Integer> roomNumMapped=new HashMap<> ();
    Card murderScene[] = new Card[3];
    RoomCard tempRoom;
    boolean moved = false;
    private Item finalMurderer;
    private Item finalWeapon;
    private RoomCard finalRoom; //might need to change to roomCard

    private ArrayList<Weapon> weapons = new ArrayList<>();

    private ArrayList<Item> allWeapons = new ArrayList<>();

    private HashMap<String, String> names = new HashMap<>();
    private String murder = "";

    private boolean won = false;
    int playerCont;

    static JFrame f;


    //CONSTRUCTOR
    public Game() {
       // initialiseBoard();
    }

    //INTERFACE

    public static void main(String[] args) {
        f = new JFrame("Cluedo");

        JMenu menu;
        JMenuItem newGame, quit;

        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Game Options");
        newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog x = new JDialog(f, "New Game");
                JLabel l = new JLabel("<html>All progress will be lost.<br/>Are you sure you want to start a new game?</html>", SwingConstants.CENTER);
                JButton t = new JButton("Yes");
                JButton n = new JButton("No");

                t.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.getContentPane().removeAll();
                        f.repaint();
                        x.dispose();
                        Game game = new Game();
                        game.run();

                    }
                });

                n.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        x.dispose();
                    }
                });

                JPanel panel = new JPanel();
                panel.add(l);
                panel.add(t);
                panel.add(n);
                x.add(panel);
                x.setSize(300, 125);
              //  x.setLocationRelativeTo(null);
                x.setVisible(true);

            }
        });

        quit = new JMenuItem("Quit Game");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog x = new JDialog(f, "Quit Game");
                JLabel l = new JLabel("<html>All progress will be lost.<br/>Are you sure you want to quit?</html>", SwingConstants.CENTER);
                JButton t = new JButton("Yes");
                JButton n = new JButton("No");

                t.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0); //exit the window completely, return exit status of 0
                    }
                });

                n.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        x.dispose();
                    }
                });

                JPanel panel = new JPanel();
                panel.add(l);
                panel.add(t);
                panel.add(n);
                x.add(panel);
                x.setSize(250, 125);
                x.setLocationRelativeTo(null);
                x.setVisible(true);

            }
        });

        menu.add(newGame);
        menu.addSeparator();
        menu.add(quit);

        menuBar.add(menu);
        f.setJMenuBar(menuBar);
        f.setSize(700, 800);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        Game cluedo = new Game();
        cluedo.start();

    }


    public void start(){

        JButton startButton = new JButton("Start");
        JButton quitButton = new JButton("Quit");

        f.add(startButton);
        f.add(quitButton);
        f.setSize(700,800);
     //   f.setLocationRelativeTo(null);
        f.setLayout(new FlowLayout());

        File file = new File("images/welcome.png");
        BufferedImage img;
        try {
            img = ImageIO.read(file);
            ImageIcon i = new ImageIcon(img);
            JLabel l = new JLabel();
           l.setIcon(i);
            f.add(l, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        f.setVisible(true);

        //START BUTTON

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                setup(); // SETUP METHOD
            }
        });


        //QUIT BUTTON

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog x = new JDialog(f, "Quit Game");
                JLabel l = new JLabel("<html>All progress will be lost.<br/>Are you sure you want to quit?</html>", SwingConstants.CENTER);
                JButton t = new JButton("Yes");
                JButton n = new JButton("No");

                t.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0); //exit the window completely, return exit status of 0
                    }
                });

                n.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        x.dispose();
                    }
                });

                JPanel panel = new JPanel();
                panel.add(l);
                panel.add(t);
                panel.add(n);
                x.add(panel);
                x.setSize(250, 125);
                x.setLocationRelativeTo(null);
                x.setVisible(true);
            }
        });
    }

    public void setup(){
       // System.out.println("in setup");
        //initialiseCards();
        //System.out.println("cards");
       // initialiseRooms();
        //System.out.println("rooms");
        getDeck();
        getNumPlayers();
    }

    public void getNumPlayers() {
        System.out.println("num players");

        //COULD BE FORMATTED NICER

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("How many players would like to play?");
        JRadioButton threePlayers = new JRadioButton("3");
        JRadioButton fourPlayers = new JRadioButton("4");
        JRadioButton fivePlayers = new JRadioButton("5");
        JRadioButton sixPlayers = new JRadioButton("6");

        ButtonGroup playerOptions = new ButtonGroup();
        playerOptions.add(threePlayers);
        playerOptions.add(fourPlayers);
        playerOptions.add(fivePlayers);
        playerOptions.add(sixPlayers);

        f.add(label);
        f.add(threePlayers);
        f.add(fourPlayers);
        f.add(fivePlayers);
        f.add(sixPlayers);

        f.setSize(700,800);
        f.setLocationRelativeTo(null);
        f.setLayout(new FlowLayout());
        f.setVisible(true);

        threePlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                n = 3;
                f.getContentPane().removeAll();
                f.repaint();
                System.out.println("3");
                initialisePlayerTokens();
            }
        });

        fourPlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                n = 4;
                f.getContentPane().removeAll();
                f.repaint();
                initialisePlayerTokens();
            }
        });

        fivePlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                n = 5;
                f.getContentPane().removeAll();
                f.repaint();
                initialisePlayerTokens();
            }
        });

        sixPlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                n = 6;
                f.getContentPane().removeAll();
                f.repaint();
                initialisePlayerTokens();
            }
        });

    }

    private void sort() {
        tempSuspects = new ArrayList<Item>();
        if (playerCount < 6) {
            if (!numOfPyr.contains(1)) {
                tempSuspects.add(new Item("Mrs. White", "W", 25, 10));
            }
            if (!numOfPyr.contains(2)) {
                tempSuspects.add(new Item("Miss Scarlett", "S", 1, 8));
            }
            if (!numOfPyr.contains(3)) {
                tempSuspects.add(new Item("Colonel Mustard", "M", 8, 1));
            }
            if (!numOfPyr.contains(4)) {
                tempSuspects.add(new Item("Professor Plum", "P", 6, 24));
            }
            if (!numOfPyr.contains(5)) {
                tempSuspects.add(new Item("Mrs. Peacock", "C", 19, 24));
            }
            if (!numOfPyr.contains(6)) {
                tempSuspects.add(new Item("Mr. Green", "G", 25, 15));
            }

        }
        System.out.println("Temp sus: " + tempSuspects);
    }


    //NEXT STEP TO COMPLETE
    private String[] characters = {null,
            "1: Mrs. White",
            "2: Miss Scarlett",
            "3: Colonel Mustard",
            "4: Professor Plum",
            "5: Mrs. Peacock",
            "6: Mr. Green"
    };
    //list of numbers which represent which players have been taken
    ArrayList<Integer> numOfPyr=new ArrayList<Integer>();
    //String for the players name
    String nameOfPlayers = "";

    /**
     * Players choose what character they want to be.
     * Player can add their name
     * Player is added to the list of players.
     */
    private void initialisePlayerTokens() {//originally getTokens()
        if(playerCount > 0) {
            if(nameOfPlayers == "")
                nameOfPlayers=players.get(playerCount-1).name;
            names.put(players.get(playerCount-1).name,nameOfPlayers);
            nameOfPlayers = "";
        }

        JLabel name = new JLabel("              Player "+(playerCount+1)+" - " + "Enter your name:              ");
        f.add(name);
        JTextField tf = new JTextField();
        tf.setPreferredSize( new Dimension( 200, 24 ) );
        tf.setBounds(100, 100,200, 24);
        tf.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent evt){
                nameOfPlayers = ((JTextField)evt.getSource()).getText() + String.valueOf(evt.getKeyChar());
            }
        });
        f.add(tf);
        JLabel chooseCharacter = new JLabel("               Player "+(playerCount+1)+" - " + "Please choose a character:                ");
        JRadioButton white = new JRadioButton("Mrs. White");
        JRadioButton scarlett = new JRadioButton("Miss Scarlett");
        JRadioButton mustard = new JRadioButton("Colonel Mustard");
        JRadioButton plum = new JRadioButton("Professor Plum");
        JRadioButton peacock = new JRadioButton("Mrs. Peacock");
        JRadioButton green = new JRadioButton("Mr. Green");
        ButtonGroup characterButton = new ButtonGroup();

        f.add(chooseCharacter);
        if(characters[1]!=null) {
            f.add(white);
            characterButton.add(white);
        }
        if(characters[2]!=null) {
            f.add(scarlett);
            characterButton.add(scarlett);
        }
        if(characters[3]!=null) {
            f.add(mustard);
            characterButton.add(mustard);
        }
        if(characters[4]!=null) {
            f.add(plum);
            characterButton.add(plum);
        }
        if(characters[5]!=null) {
            f.add(peacock);
            characterButton.add(peacock);
        }
        if(characters[6]!=null) {
            f.add(green);
            characterButton.add(green);
        }

        f.setSize(600,700);
        f.setLayout(new FlowLayout());
        f.setVisible(true);


        white.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                numOfPyr.add(1);
                players.add(new Item("Mrs. White", "W", 25, 10));
                characters[1] = null;
                f.getContentPane().removeAll();
                f.repaint();
                playerCount++;
                //System.out.println("here");
                if(playerCount < n) {
                    //System.out.println("1");
                    initialisePlayerTokens();
                }
                if(playerCount == n) {
                    sort();
                    playerCount = 1;
                    dealCards();
                }
            }
        });

        scarlett.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                numOfPyr.add(2);
                players.add(new Item("Miss Scarlett", "S", 1, 8));
                characters[2] = null;
                f.getContentPane().removeAll();
                f.repaint();
                playerCount++;
                if(playerCount<n) {
                    initialisePlayerTokens();
                }
                if(playerCount == n) {
                    sort();
                    playerCount = 1;
                    dealCards();
                }
            }
        });

        mustard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                numOfPyr.add(3);
                players.add(new Item("Colonel Mustard", "M", 8, 1));
                characters[3] = null;
                f.getContentPane().removeAll();
                f.repaint();
                playerCount++;
                if(playerCount<n) {
                    initialisePlayerTokens();
                }
                if(playerCount == n) {
                    sort();
                    playerCount = 1;
                    dealCards();
                }
            }
        });

        plum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                numOfPyr.add(4);
                players.add(new Item("Professor Plum", "P", 6, 24));
                characters[4] = null;
                f.getContentPane().removeAll();
                f.repaint();
                playerCount++;
                if(playerCount<n) {
                    initialisePlayerTokens();
                }
                if(playerCount == n) {
                    sort();
                    playerCount = 1;
                    dealCards();
                }

            }
        });

        peacock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                numOfPyr.add(5);
                players.add(new Item("Mrs. Peacock", "C", 19, 24));
                characters[5] = null;
                f.getContentPane().removeAll();
                f.repaint();
                playerCount++;
                if(playerCount<n) {
                    initialisePlayerTokens();
                }
                if(playerCount == n) {
                    sort();
                    playerCount = 1;
                    dealCards();
                }

            }
        });

        green.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numOfPyr.add(6);
                players.add(new Item("Mr. Green", "G", 25, 16));
                characters[6] = null;
                f.getContentPane().removeAll();
                f.repaint();
                playerCount++;
                if(playerCount<n) {
                    initialisePlayerTokens();
                }
                if(playerCount == n) {
                    sort();
                    playerCount = 1;
                    dealCards();
                }
            }
        });

        //the room player was in last
        for(Item player: players) {
            prevRoom.put(player, null);
        }
        Item candlestick = new Item("Candlestick","images/w_candlestick.png",15,13);
        Item leadPipe = new Item("Lead Pipe","images/w_lead_pipe.png",14,14);
        Item knife = new Item("Knife","images/w_knife.png",14,12);
        Item revolver = new Item("Revolver","images/w_revolver.png",15,14);
        Item rope = new Item("Rope","images/w_rope.png",15,12);
        Item wrench = new Item("Wrench", "images/w_wrench.png",14,13);

        allWeapons.add(candlestick);
        allWeapons.add(leadPipe);
        allWeapons.add(knife);
        allWeapons.add(revolver);
        allWeapons.add(rope);
        allWeapons.add(wrench);

    }

    public void getDeck() {
        weapons = new ArrayList<Weapon>();
        suspectCards = new ArrayList<Suspect>();
        rooms = new ArrayList<RoomCard>();
        deck = new Stack<Card>();

        //add weapons
        weapons.add(new Weapon("Candlestick"));
        weapons.add(new Weapon("Lead Pipe"));
        weapons.add(new Weapon("Revolver"));
        weapons.add(new Weapon("Knife"));
        weapons.add(new Weapon("Wrench"));
        weapons.add(new Weapon("Rope"));

        //add suspects
        suspectCards.add(new Suspect("Mrs. White"));
        suspectCards.add(new Suspect("Miss Scarlett"));
        suspectCards.add(new Suspect("Professor Plum"));
        suspectCards.add(new Suspect("Colonel Mustard"));
        suspectCards.add(new Suspect("Mr. Green"));
        suspectCards.add(new Suspect("Mrs. Peacock"));

        //add rooms
        rooms.add(new RoomCard("Kitchen"));
        rooms.add(new RoomCard("Conservatory"));
        rooms.add(new RoomCard("Study"));
        rooms.add(new RoomCard("Billiards Room"));
        rooms.add(new RoomCard("Hall"));
        rooms.add(new RoomCard("Dining Room"));
        rooms.add(new RoomCard("Ballroom"));
        rooms.add(new RoomCard("Lounge"));
        rooms.add(new RoomCard("Library"));

        roomMap.put("Kitchen",rooms.get(0));
        roomMap.put("Conservatory",rooms.get(1));
        roomMap.put("Study",rooms.get(2));
        roomMap.put("Billiards Room",rooms.get(3));
        roomMap.put("Hall",rooms.get(4));
        roomMap.put("Dining Room",rooms.get(5));
        roomMap.put("Ballroom",rooms.get(6));
        roomMap.put("Lounge",rooms.get(7));
        roomMap.put("Library",rooms.get(8));


        roomNumMapped.put(rooms.get(0),8);
        roomNumMapped.put(rooms.get(1),6);
        roomNumMapped.put(rooms.get(2),3);
        roomNumMapped.put(rooms.get(3),5);
        roomNumMapped.put(rooms.get(4),2);
        roomNumMapped.put(rooms.get(5),9);
        roomNumMapped.put(rooms.get(6),7);
        roomNumMapped.put(rooms.get(7),1);
        roomNumMapped.put(rooms.get(8),4);


        //shuffle cards
        Collections.shuffle(weapons);
        Collections.shuffle(suspectCards);
        Collections.shuffle(rooms);

        //assign finals
        RoomCard crimeScene = rooms.get(rooms.size()-1);
        Weapon murderWeapon = weapons.get(weapons.size()-1);
        Suspect murderer = suspectCards.get(suspectCards.size()-1);

        //remove finals from cards
        weapons.remove(weapons.size()-1);
        suspectCards.remove(suspectCards.size()-1);
        rooms.remove(rooms.size()-1);

        //add finals to envelope
        murderScene = new Card[] {murderer, murderWeapon, crimeScene};

        //create the deck
        for(Suspect s : suspectCards) {
            deck.push(s);
        }
        for(Weapon w : weapons) {
            deck.push(w);

        }
        for(RoomCard r : rooms) {
            deck.push(r);
        }

        //shuffle the deck
        Collections.shuffle(deck);

    }

    /**
     * Deals cards to players
     */
    private void dealCards() {
        boolean b = false;
        while (!deck.isEmpty()) {
            for (Item player : players){
                player.getPlayersHand().add(deck.pop());
                if(deck.isEmpty()){
                    b = true;
                }
                if(b){
                    break;
                }
            }


        }
        board.board(players);

        nextPlayer();
    }

    boolean one=false;

    /**
     * Gets next player and allows them to have their turn
     */
    public void nextPlayer() {
        System.out.println("In next player");
        if(!one) {
            if(nameOfPlayers == "")
                nameOfPlayers = players.get(players.size()-1).name;
            names.put(players.get(players.size()-1).name, nameOfPlayers);
            nameOfPlayers = "";
            one = true;
            for(String n: names.keySet())
                System.out.println(n+"		"+names.get(n)	);
        }
        if(playerCount>=n) {
            playerCount=0;
        }
        Item p = players.get(playerCount);
        if(p.lost) {
            playerCount++;
            nextPlayer();
            p = players.get(playerCount);
        }
        else {
            JButton begin = new JButton("Start "+names.get(p.name)+"'s turn");
            f.add(begin);
            f.setSize(600,700);
            f.setLayout(new FlowLayout());
            f.setVisible(true);

            begin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    f.getContentPane().removeAll();
                    f.repaint();
                   displayBoard();
                    System.out.println("Your Hand:");

                    Item p = players.get(playerCount);
                    displayHand(p);

                    roll();
                }
            });
        }

    }

    public void displayBoard(){
        board.draw(players, f, allWeapons, tempSuspects);
        f.setVisible(true);
    }

    public void displayHand(Item p){

        JLayeredPane layeredPane=new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(5+65*p.getPlayersHand().size(), 110));
        layeredPane.setBorder(BorderFactory.createTitledBorder("Hand"));
        int i=0;
        for (Card c : p.getPlayersHand()) {
            System.out.print(c.getName() + " / ");
            File file = new File("images/"+c.getName()+".png");
            BufferedImage img;
            try {
                img = ImageIO.read(file);
                ImageIcon icon = new ImageIcon(img);
                JLabel lbl = new JLabel();
                lbl.setIcon(icon);
                lbl.setBounds(6+65*i, 16,
                        icon.getIconWidth(),
                        icon.getIconHeight());

                layeredPane.add(lbl, 0);
                i++;


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        f.add(layeredPane);
        f.setVisible(true);
    }



    public void roll(){
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setBounds(100, 400, 70, 30);//x axis, y axis, width, height
        f.add(rollButton);
        JLabel l = new JLabel("Press the roll button");
        f.add(l);
        f.setVisible(true);

        rollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                f.getContentPane().removeAll();
                f.repaint();

                f.getContentPane().removeAll();
                f.repaint();
                displayBoard();
                displayHand(players.get(playerCount));
                int roll = rollDice();

                JLabel l = new JLabel("                                           Press the board to move!                                          ");
                f.add(l);
                JLabel a = new JLabel("This is "+names.get(players.get(playerCount).name)+"'s turn they are "+players.get(playerCount).name);
                f.add(a);
                BufferedImage img;
                File file= new File("images/character_"+players.get(playerCount).item+".png");
                try {
                    img = ImageIO.read(file);
                    ImageIcon icon = new ImageIcon(img);
                    //  f.setLayout(new FlowLayout());
                    JLabel lbl = new JLabel();
                    lbl.setIcon(icon);
                    f.add(lbl);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                JLabel b = new JLabel("Position: "+players.get(playerCount).x+", "+players.get(playerCount).x);
                f.add(b);

                f.addMouseListener( new MouseAdapter() {
                    boolean enabled=true;
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (!enabled) {
                            return;
                        }
                        int x=e.getX();
                        int xP=1;
                        int y=e.getY();
                        int yP=1;
                        if(y>74 && y<500 && x>94 && x<504) {
                            x=x-95;
                            while(x>17) {
                                x=x-17;
                                xP++;
                            }
                            y=y-75;
                            while(y>17) {
                                y=y-17;
                                yP++;
                            }
                        }
                        yP=25-yP+1;
                        Item player=players.get(playerCount);
                        move(player, roll, yP,xP);
                        if(moved==true) {
                            enabled=false;
                            playerAct();
                        }
                        else {
                            f.remove(l);
                            JLabel l = new JLabel("Not a valid move!");
                            f.add(l);
                            f.setVisible(true);
                        }

                    }
                });
                f.setVisible(true);
            }
        });
    }

    public void finalAccuse(Item p){

        JButton accusation = new JButton("Make Accusation");
        f.setLayout(new FlowLayout());
        f.add(accusation);
        f.setSize(600,700);
        f.setVisible(true);
        accusation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                chooseFinalMurder(p);
            }
        });

    }

    public void chooseFinalMurder(Item p){
        JLabel sug = new JLabel(p.getPlayerName() + ", please make a suggestion for the murderer: ");
        JRadioButton w = new JRadioButton("Mrs. White");
        JRadioButton s = new JRadioButton("Mrs. Scarlett");
        JRadioButton m = new JRadioButton("Colonel Mustard");
        JRadioButton pl = new JRadioButton("Professor Plum");
        JRadioButton pe = new JRadioButton("Mrs. Peacock");
        JRadioButton g = new JRadioButton("Mr. Green");

        f.add(sug);
        f.add(w);
        f.add(s);
        f.add(m);
        f.add(pl);
        f.add(pe);
        f.add(g);
        f.setLayout(new FlowLayout());
        f.setSize(600,700);
        f.setVisible(true);

        //ACTION LISTENERS FOR JRADIO BUTTONS

        w.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mrs. White";
                boolean chosen = false;
                for (Item i : players){
                    if(i.getPlayerName().equals(murder)){
                        chosen = true;
                        finalMurderer = i;
                        chooseFinalWeapon(p);
                    }
                }
                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            finalMurderer = i;
                            chooseFinalWeapon(p);
                        }
                    }
                }

            }
        });

        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mrs. Scarlett";
                boolean chosen = false;
                for (Item i : players){
                    if(i.getPlayerName().equals(murder)){
                        chosen = true;
                        finalMurderer = i;
                        chooseFinalWeapon(p);
                    }
                }
                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            finalMurderer = i;
                            chooseFinalWeapon(p);
                        }
                    }
                }

            }
        });

        m.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Colonel Mustard";
                boolean chosen = false;
                for (Item i : players){
                    if(i.getPlayerName().equals(murder)){
                        chosen = true;
                        finalMurderer = i;
                        chooseFinalWeapon(p);
                    }
                }
                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            finalMurderer = i;
                            chooseFinalWeapon(p);
                        }
                    }
                }

            }
        });

        pl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Professor Plum";
                boolean chosen = false;
                for (Item i : players){
                    if(i.getPlayerName().equals(murder)){
                        chosen = true;
                        finalMurderer = i;
                        chooseFinalWeapon(p);
                    }
                }
                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            finalMurderer = i;
                            chooseFinalWeapon(p);
                        }
                    }
                }

            }
        });

        pe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mrs. Peacock";
                boolean chosen = false;
                for (Item i : players){
                    if(i.getPlayerName().equals(murder)){
                        chosen = true;
                        finalMurderer = i;
                        chooseFinalWeapon(p);
                    }
                }
                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            finalMurderer = i;
                            chooseFinalWeapon(p);
                        }
                    }
                }

            }
        });

        g.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mr. Green";
                boolean chosen = false;
                for (Item i : players){
                    if(i.getPlayerName().equals(murder)){
                        chosen = true;
                        finalMurderer = i;
                        chooseFinalWeapon(p);
                    }
                }
                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            finalMurderer = i;
                            chooseFinalWeapon(p);
                        }
                    }
                }

            }
        });
    }

    public void chooseFinalWeapon(Item p){

        JLabel sug = new JLabel(p.getPlayerName() + ", please make a suggestion for the murder weapon:");
        JRadioButton k = new JRadioButton("Knife");
        JRadioButton l = new JRadioButton("Lead Pipe");
        JRadioButton r = new JRadioButton("Rope");
        JRadioButton w = new JRadioButton("Wrench");
        JRadioButton c = new JRadioButton("Candle Stick");
        JRadioButton g = new JRadioButton("Revolver");

        f.add(sug);
        f.add(k);
        f.add(l);
        f.add(r);
        f.add(w);
        f.add(c);
        f.add(g);

        //f.setLayout(new FlowLayout());
        f.setSize(600, 700);
        f.setVisible(true);


        k.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Knife";
                for (Item i: allWeapons){
                    if (i.name == murder){
                        finalWeapon = i;
                    }
                }
                chooseFinalRoom(p);
            }
        });

        l.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Lead Pipe";
                for (Item i: allWeapons){
                    if (i.name == murder){
                        finalWeapon = i;
                    }
                }
                chooseFinalRoom(p);
            }
        });

        r.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Rope";
                for (Item i: allWeapons){
                    if (i.name == murder){
                        finalWeapon = i;
                    }
                }
                chooseFinalRoom(p);
            }
        });

        w.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Wrench";
                for (Item i: allWeapons){
                    if (i.name == murder){
                        finalWeapon = i;
                    }
                }
                chooseFinalRoom(p);
            }
        });

        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Candlestick";
                for (Item i: allWeapons){
                    if (i.name == murder){
                        finalWeapon = i;
                    }
                }
                chooseFinalRoom(p);
            }
        });

        g.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Revolver";
                for (Item i: allWeapons){
                    if (i.name == murder){
                        finalWeapon = i;
                    }
                }
                chooseFinalRoom(p);
            }
        });
    }

    public void chooseFinalRoom(Item p){
        JLabel sug = new JLabel(p.getPlayerName()+ ", please make a suggestion for the final murder room:");

        JRadioButton b = new JRadioButton("Ballroom");
        JRadioButton bi = new JRadioButton("Billiards Room");
        JRadioButton c = new JRadioButton("Conservatory");
        JRadioButton d = new JRadioButton("Dining Room");
        JRadioButton h = new JRadioButton("Hall");
        JRadioButton k = new JRadioButton("Kitchen");
        JRadioButton l = new JRadioButton("Library");
        JRadioButton lo = new JRadioButton("Lounge");
        JRadioButton s = new JRadioButton("Study");

        f.add(sug);
        f.add(b);
        f.add(bi);
        f.add(c);
        f.add(d);
        f.add(h);
        f.add(k);
        f.add(l);
        f.add(lo);
        f.add(s);

        f.setSize(600,700);
        f.setVisible(true);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Ballroom";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }
                makeFinalSuggestion(p);
            }
        });

        bi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Billiards Room";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }
                makeFinalSuggestion(p);
            }
        });

        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Conservatory";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }

                makeFinalSuggestion(p);
            }
        });

        d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Dining Room";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }

                makeFinalSuggestion(p);
            }
        });

        h.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Hall";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }
                makeFinalSuggestion(p);
            }
        });

        k.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Kitchen";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }
                makeFinalSuggestion(p);
            }
        });

        l.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Library";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }
                makeFinalSuggestion(p);
            }
        });

        lo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Lounge";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }
                makeFinalSuggestion(p);
            }
        });

        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Study";

                for(RoomCard r: roomNumMapped.keySet()){
                    if(r.getName() == murder){
                        finalRoom = r;
                    }
                }
                makeFinalSuggestion(p);
            }
        });


    }

    private void makeFinalSuggestion(Item p) {
        if(!finalMurderer.name.equals(murderScene[0].getName()) && !finalWeapon.name.equals(murderScene[1].getName()) && !finalRoom.getName().equals(murderScene[2].getName())) {
            JLabel l = new JLabel("You have lost.");
            JButton con = new JButton("Continue");
            p.lost = true;
            f.add(l);
            f.add(con);
            f.setSize(600, 700);
            f.setVisible(true);

            con.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    f.getContentPane().removeAll();
                    f.repaint();
                    playerCont++;
                    nextPlayer();

                }
            });
        }
        else{
            won = true;
            JLabel winner = new JLabel("Congratulations, " + p.name+ " has won!");
            JLabel winningCards = new JLabel("The correct murder scene was " + murderScene[0].getName() + " + " + murderScene[1].getName() + " + " + murderScene[2].getName());

            f.add(winner);
            f.add(winningCards);
            f.setVisible(true);
        }
    }

    private void playerAct() {

        Item p = players.get(playerCount);
        f.getContentPane().removeAll();
        f.repaint();

        JButton turnStart = new JButton("Start " + p.name+"'s Turn");
        f.add(turnStart);
        f.setSize(600,700);
        f.setLayout(new FlowLayout());
        f.setVisible(true);
        displayBoard();
        displayHand(p);
        f.setVisible(true);

        if(getRoom(p.x, p.y) == null) {    //not in room, therefore next player's turn

            f.getContentPane().removeAll();
            f.repaint();
            finalAccuse(p);
            playerCount++;
            nextPlayer();
        }else {    //inside a room, gets to make a suggestion
            f.getContentPane().removeAll();
            f.repaint();
            tempRoom=getRoom(p.x, p.y);
            chooseFinalMurder(p);
        }
    }

    private int rollDice() {
        int diceOne = (int) (Math.random() * 5) + 1;
        int diceTwo = (int) (Math.random() * 5) + 1;
        File file = new File("images/"+"d_"+diceOne+".png");
        BufferedImage img;
        try {
            img = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(img);
            f.setLayout(new FlowLayout());
            JLabel lbl = new JLabel();
            lbl.setIcon(icon);
            f.add(lbl);
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        file = new File("images/"+"d_"+diceTwo+".png");
        try {
            img = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(img);
            f.setLayout(new FlowLayout());
            JLabel lbl = new JLabel();
            lbl.setIcon(icon);
            f.add(lbl);
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return diceOne+diceTwo;
    }

    /**
     * Player being able to make valid moves on the board
     * Player's get to move certain steps and invalid steps are not processed
     * @param p selected player's turn
     * @param diceRoll determining how many steps can be taken
     * @param x,y the position of the current player
     */
    private void move(Item p, int diceRoll, int y, int x){
        int r = p.getX();
        int c = p.getY();

        RoomCard rooms = getRoom(y,x);
        RoomCard curRoom = getRoom(p.getX(), p.getY());

        if(rooms != null){
            if(prevRoom.get(p) != null) {
                if (rooms.getName() == prevRoom.get(p).getName()) {
                    System.out.println("Cannot enter the same room again instantly");
                    moved = false;
                }
            } else{
                List<QueueItem> roomDoors = getDoors(rooms);
                boolean validMove = false;
                //player in room
                if(curRoom == null){
                    for(QueueItem door : roomDoors){
                            int validMoves = board.pathFinding(p.x, p.y, door.row, door.col);
                            if(validMoves != -1 && validMoves < diceRoll){
                                prevRoom.put(p, rooms);
                                validMove = true;
                                moved = true;
                                QueueItem moved = placeInRoom(rooms, p);
                                r = moved.row;
                                c = moved.col;
                                break;
                            }
                        }
                    }
                    else{
                    List<QueueItem> curDoors = getDoors(curRoom);
                    for(QueueItem cd : curDoors) {
                        for (QueueItem door : roomDoors) {
                            int validMoves = board.pathFinding(cd.row, cd.col, door.row, door.col);
                            if (validMoves != -1 && validMoves < diceRoll) {
                                prevRoom.put(p, rooms);
                                validMove = true;
                                QueueItem qMoved = placeInRoom(rooms, p);
                                r = qMoved.row;
                                c = qMoved.col;
                                moved = true;
                                break;
                            }
                        }
                        if (validMove) {
                            break;
                        }
                    }
                }
                if(!validMove){
                    System.out.println("Invalid Move, Entry into the room is rejected");
                    moved = false;
                }
            }
        }else{
            if(curRoom == null){
                int pMove = board.pathFinding(p.x, p.y, x, y);
                System.out.println("You Rolled A: " + diceRoll);
                System.out.println("Least Moves Available: " + pMove);
                if(pMove > diceRoll) {
                    moved = false;
                    System.out.println("Invalid Steps Selected, Try Again");
                }else{
                    System.out.println("Valid Move");
                    moved = true;
                    r = y;
                    c = x;
                }
            }
            else{
                List<QueueItem> doors = getDoors(rooms);
                boolean validStep = false;
                for(QueueItem item: doors){
                    int validMove = board.pathFinding(item.row, item.col, x, y);
                    if((validMove > diceRoll) || (!(board.board[r][c]==(null)||board.board[r][c]==("_"))) || (!(getRoom(r,c)==null))){
                        moved = false;
                    } else{
                        moved = true;
                        validStep = true;
                        break;
                    }
                }
                if(validStep){
                    System.out.println("Valid Move");
                    moved = true;
                    r = y;
                    c = x;
                }else{
                    moved = false;
                    System.out.println("Valid Move taken from: " + curRoom.getName());
                }
            }
        }
        board.board[p.x][p.y] = "_";
        p.y = c;
        p.x = r;
        System.out.println("current X pos: " + p.x);
        System.out.println("current Y pos: " + p.y);
    }

    public void checkPlayer(Item p, Integer n){
        if(n >= players.size()){
            n = 0;
        }

        Integer num = n +1;
        Item nP = players.get(n);
        JButton begin = new JButton("Pass to " + nP.name);
        f.add(begin);
        f.setSize(600,700);
        f.setLayout(new FlowLayout());
        f.setVisible(true);
        int oldN = n;
        begin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(oldN == playerCount){
                    System.out.println("Pass to the player");
                    f.getContentPane().removeAll();
                    f.repaint();
                    revealHand(null,null);
                }
                else{
                    System.out.println("person");
                    f.getContentPane().removeAll();
                    f.repaint();
                    System.out.println("Your hand:");
                    JLabel name = new JLabel(nP.name);
                    f.add(name);
                    ArrayList<Card> theirCards =new ArrayList<>();

                    displayHand(nP);
                    JLabel choose = new JLabel(p.name+" accused "+finalMurderer.name+" with the "+finalWeapon.name+" in the "+finalRoom.name);
                    f.add(choose);
                    for(Card c : nP.getPlayersHand()){
                        if(c.getName() == finalMurderer.name || c.getName() == finalWeapon.name || c.getName() == finalRoom.getName()){
                            theirCards.add(c);
                        }
                    }
                    if(theirCards.size() == 0){
                        JLabel no = new JLabel("No accused cards. Pass turn");
                        f.add(no);
                        JButton begin = new JButton("Pass");
                        f.add(begin);
                        begin.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                f.getContentPane().removeAll();
                                f.repaint();
                                checkPlayer(p, num);
                            }
                        });
                    }
                    else{
                        for(Card c : theirCards){
                            JButton b = new JButton("Show " + c.getName());
                            f.add(b);
                            b.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    f.getContentPane().removeAll();
                                    f.repaint();
                                    revealHand(p, nP);
                                }
                            });
                        }
                    }
                    f.setVisible(true);
                }
            }
        });
    }

    public void revealHand(Item p, Card c, Item p2){
        JButton pass = new JButton("Continue to " + p.name+"'s turn");
        f.add(pass);
        f.setSize(600,700);
        f.setVisible(true);
        pass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                JLabel l = new JLabel(p2.name + "has shown you:");
                f.add(l);

                File file = new File(c.getName()+".png");
                BufferedImage img;

                try {
                    img = ImageIO.read(file);
                    ImageIcon i = new ImageIcon(img);
                    JLabel label = new JLabel();
                    label.setIcon(i);
                    f.add(label);
                    f.setVisible(true);


                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                finalAccuse(p);
                JButton endTurn = new JButton("End Turn");
                f.add(endTurn);
                f.setVisible(true);

                endTurn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.getContentPane().removeAll();
                        f.repaint();
                        playerCont++;
                        nextPlayer();
                    }
                });
            }
        });
    }
    
    public void chooseSuggestionMurderer(Item p){

        JLabel sug = new JLabel(p.getPlayerName() + ", please make a suggestion for the murderer: ");
        JRadioButton w = new JRadioButton("Mrs. White");
        JRadioButton s = new JRadioButton("Mrs. Scarlett");
        JRadioButton m = new JRadioButton("Colonel Mustard");
        JRadioButton pl = new JRadioButton("Professor Plum");
        JRadioButton pe = new JRadioButton("Mrs. Peacock");
        JRadioButton g = new JRadioButton("Mr. Green");

        f.add(sug);
        f.add(w);
        f.add(s);
        f.add(m);
        f.add(pl);
        f.add(pe);
        f.add(g);
        f.setLayout(new FlowLayout());
        f.setSize(600,700);
        f.setVisible(true);

        //ACTION LISTENERS FOR JRADIO BUTTONS

        w.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mrs. White";
                boolean chosen = false;

                for (Item i : players){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        chosen = true;
                        suspect = i;
                        chooseSuggestionWeapon(p);
                    }
                }

                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            QueueItem place = placeInRoom(suspectRoom, p);
                            suspect = i;
                            p.x= place.row;
                            p.y = place.col;
                            chooseSuggestionWeapon(p);
                        }
                    }
                }


            }
        });

        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mrs. Scarlett";
                boolean chosen = false;

                for (Item i : players){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        chosen = true;
                        suspect = i;
                        chooseSuggestionWeapon(p);
                    }
                }

                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            QueueItem place = placeInRoom(suspectRoom, p);
                            suspect = i;
                            p.x= place.row;
                            p.y = place.col;
                            chooseSuggestionWeapon(p);
                        }
                    }
                }


            }
        });

        m.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Colonel Mustard";
                boolean chosen = false;

                for (Item i : players){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        chosen = true;
                        suspect = i;
                        chooseSuggestionWeapon(p);
                    }
                }

                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            QueueItem place = placeInRoom(suspectRoom, p);
                            suspect = i;
                            p.x= place.row;
                            p.y = place.col;
                            chooseSuggestionWeapon(p);
                        }
                    }
                }


            }
        });

        pl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Professor Plum";
                boolean chosen = false;

                for (Item i : players){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        chosen = true;
                        suspect = i;
                        chooseSuggestionWeapon(p);
                    }
                }

                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            QueueItem place = placeInRoom(suspectRoom, p);
                            suspect = i;
                            p.x= place.row;
                            p.y = place.col;
                            chooseSuggestionWeapon(p);
                        }
                    }
                }


            }
        });

        pe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mrs. Peacock";
                boolean chosen = false;

                for (Item i : players){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        chosen = true;
                        suspect = i;
                        chooseSuggestionWeapon(p);
                    }
                }

                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            QueueItem place = placeInRoom(suspectRoom, p);
                            suspect = i;
                            p.x= place.row;
                            p.y = place.col;
                            chooseSuggestionWeapon(p);
                        }
                    }
                }


            }
        });

        g.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Mr. Green";
                boolean chosen = false;

                for (Item i : players){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        chosen = true;
                        suspect = i;
                        chooseSuggestionWeapon(p);
                    }
                }

                if(!chosen){
                    for (Item i : tempSuspects){
                        if(i.getPlayerName().equals(murder)){
                            QueueItem place = placeInRoom(suspectRoom, p);
                            suspect = i;
                            p.x= place.row;
                            p.y = place.col;
                            chooseSuggestionWeapon(p);
                        }
                    }
                }


            }
        });

    }

    public void chooseSuggestionWeapon(Item p){

        JLabel sug = new JLabel(p.getPlayerName() + ", please make a suggestion for the murder weapon:");
        JRadioButton k = new JRadioButton("Knife");
        JRadioButton l = new JRadioButton("Lead Pipe");
        JRadioButton r = new JRadioButton("Rope");
        JRadioButton w = new JRadioButton("Wrench");
        JRadioButton c = new JRadioButton("Candle Stick");
        JRadioButton g = new JRadioButton("Revolver");

        f.add(sug);
        f.add(k);
        f.add(l);
        f.add(r);
        f.add(w);
        f.add(c);
        f.add(g);

        //f.setLayout(new FlowLayout());
        f.setSize(600, 700);
        f.setVisible(true);




        k.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Knife";

                for (Item i : allWeapons){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        suspectWeapon = i;

                    }
                }

                checkPlayer(p, playerCont+1);

            }
        });

        l.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Lead Pipe";

                for (Item i : allWeapons){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        suspectWeapon = i;

                    }
                }

                checkPlayer(p, playerCont+1);

            }
        });

        r.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Rope";

                for (Item i : allWeapons){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        suspectWeapon = i;

                    }
                }

                checkPlayer(p, playerCont+1);

            }
        });

        w.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Wrench";

                for (Item i : allWeapons){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        suspectWeapon = i;

                    }
                }

                checkPlayer(p, playerCont+1);

            }
        });

        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Candlestick";

                for (Item i : allWeapons){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        suspectWeapon = i;

                    }
                }

                checkPlayer(p, playerCont+1);

            }
        });

        g.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.getContentPane().removeAll();
                f.repaint();
                murder = "Revolver";

                for (Item i : allWeapons){
                    if(i.name.equals(murder)){
                        QueueItem place = placeInRoom(suspectRoom, p);
                        p.x= place.row;
                        p.y = place.col;
                        suspectWeapon = i;

                    }
                }

                checkPlayer(p, playerCont+1);

            }
        });

    }

    /**
     * Checks the squares that are allocated per room,
     * returns null when character is not in a room
     * @param row
     * @param col
     * @return
     */
    private RoomCard getRoom(int row, int col) {

        for(int i=0; i<8; i++){
            for(int j=10; j<16; j++){
                if(row==i&&col==j) {
                    return roomMap.get("Hall");
                }
            }
        }

        for(int i=0; i<7; i++){
            for(int j=0; j<8; j++){
                if(row==i&&col==j) {
                    return roomMap.get("Lounge");
                }
            }
        }

        for(int i=0; i<5; i++){
            for(int j=18; j<25; j++){
                if(row==i&&col==j && !(i==1&&j==18)) {
                    return roomMap.get("Study");
                }
            }
        }

        for(int i=7; i<12; i++){
            for(int j=18; j<25; j++){
                if(row==i&&col==j && (!(i==7&&j==24))&&(!(i==7&&j==18))&&(!(i==11&&j==24))&&(!(i==11&&j==18))){
                    return roomMap.get("Library");
                }
            }
        }

        for(int i=13; i<18; i++){
            for(int j=19; j<25; j++){
                if(row==i&&col==j ) {
                    return roomMap.get("Billiards Room");
                }
            }
        }

        for(int i=20; i<25; i++){
            for(int j=19; j<25; j++){
                if(row==i&&col==j&& (!(i==20&&j==24))&&(!(i==20&&j==19))) {
                    return roomMap.get("Conservatory");
                }
            }
        }

        for(int i=18; i<25; i++){
            for(int j=9; j<17; j++){
                if(row==i&&col==j&& (!(i==24&&j==9))&& (!(i==24&&j==10))&& (!(i==24&&j==15)&& (!(i==24&&j==16)))) {
                    return roomMap.get("Ballroom");
                }
            }
        }

        for(int i=19; i<25; i++){
            for(int j=0; j<7; j++){
                if(row==i&&col==j && (!(j==1 && i==19))) {
                    return roomMap.get("Kitchen");
                }
            }
        }

        for(int i=10; i<17; i++){
            for(int j=0; j<9; j++){
                if(row==i&&col==j && (!(j==6 && i==16))&& (!(j==8 && i==16))&& (!(j==7 && i==16))) {
                    return roomMap.get("Dining Room");
                }
            }
        }
        return null;

    }

    /**
     * Creates suggestion for player
     *
     * @param p     the player
     * @param input the scanner to take input
     * @return the created suggestion
     */
    private Suggestion getAccusation(Player p, Scanner input) {
        Card roomC = null;
        Card weaponC = null;
        Card characterC = null;

        int i = 0;
        System.out.println("Pick ONE card of each type to make a Suggestion:");
        for (Card c : deck) {
            System.out.println(i + ": " + c.getName() + " (" + c.getType() + ")");
            i++;
        }

        //checks if the room card is a instance of the roomCard class
        System.out.println("Pick a Room Card: ");
        while (!(roomC instanceof RoomCard)) {
            try {
                roomC = deck.get(Integer.parseInt(input.nextLine()));
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid");
                return null;
            }
        }

        //checks if the weapon card is a instance of the weaponCard class
        System.out.println("Pick a Weapon Card: ");
        while (!(weaponC instanceof RoomCard)) {
            try {
                weaponC = deck.get(Integer.parseInt(input.nextLine()));
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid");
                return null;
            }
        }

        //checks if the character card is a instance of the characterCard class
        System.out.println("Pick a Character Card: ");
        while (!(characterC instanceof RoomCard)) {
            try {
                characterC = deck.get(Integer.parseInt(input.nextLine()));
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid");
                return null;
            }
        }
        return new Suggestion(p, true, (WeaponCard) weaponC, (RoomCard) roomC, (CharacterCard) characterC); // Get the accusation
    }

    public List<QueueItem> getDoors(RoomCard r){
        
        List<QueueItem> q = new ArrayList<>();
        if(r.getName() == "Hall"){
            QueueItem door = new QueueItem(8,12);
            q.add(door);
            door = new QueueItem(8,13);
            q.add(door);
            door = new QueueItem(5,16);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Lounge"){
            QueueItem door = new QueueItem(7,7);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Library"){
            QueueItem door=new QueueItem(9,17);
            q.add(door);
            door=new QueueItem(12,21);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Study"){
            QueueItem door=new QueueItem(5,18);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Ballroom"){
            QueueItem door=new QueueItem(17,10);
            q.add(door);
            door=new QueueItem(17,15);
            q.add(door);
            door=new QueueItem(20,8);
            q.add(door);
            door=new QueueItem(20,17);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Kitchen"){
            QueueItem door=new QueueItem(18,5);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Conservatory"){
            QueueItem door=new QueueItem(20,19);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Billiards Room"){
            QueueItem door=new QueueItem(12,23);
            q.add(door);
            door=new QueueItem(16,18);
            q.add(door);
            return q;
        }
        else if(r.getName() == "Dining Room"){
            QueueItem door=new QueueItem(9,7);
            q.add(door);
            door=new QueueItem(13,9);
            q.add(door);
            return q;
        }
        return null;
        
    }

    /**
     * Player input determines what room they are placed in
     * 1: Lounge, 2: Hall, 3: Study, 4: Library, 5: Billiards Room, 6: Conservatory, 7: Ballroom, 8: Kitchen, 9: Dining Room
     * @param r
     * @param p
     * @return
     */
    private QueueItem placeInRoom(RoomCard r, Item p){
    int x=0;
    int y=0;

		if(r.getName() == "Lounge") {
        for(int i=1; i<6; i++){
            for(int j=1; j<7; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Hall") {
        for(int i=1; i<7; i++){
            for(int j=11; j<15; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Study") {
        for(int i=1; i<4; i++){
            for(int j=19; j<24; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Library") {
        for(int i=8; i<11; i++){
            for(int j=19; j<24; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Billiards Room") {
        for(int i=14; i<17; i++){
            for(int j=20; j<24; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Conservatory") {
        for(int i=21; i<24; i++){
            for(int j=20; j<24; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Ballroom") {
        for(int i=19; i<24; i++){
            for(int j=10; j<16; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Kitchen") {
        for(int i=20; i<24; i++){
            for(int j=1; j<6; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }
		else if(r.getName() == "Dining Room") {
        for(int i=11; i<16; i++){
            for(int j=1; j<8; j++){
                if(board.board[i][j]==null||board.board[i][j]=="_") {
                    x=i;
                    y=j;
                }
            }
        }
    }

		return new QueueItem(x,y);
}

    /**
     * Refutes suggestion for player
     *
     * @param s     the suggestion to be refuted
     * @param input scanner to parse inputs
     * @return true if the suggestion has been refuted
     */
    public boolean refuteSuggestion(Suggestion s, Scanner input) {
        for (Item p : players) {
            if (p.equals(s.getSuggestingPlayer())) {
                continue;
            }
            boolean canRefute = false;
            ArrayList<Card> refuteList = new ArrayList<>();

            //checking if the card in hand of player contains the card apposed,
            //if player has card, refuse the accusation, else accuse
            for (Card c : p.getPlayersHand()) {
                if (s.contains(c)) {
                    canRefute = true;
                    refuteList.add(c);
                }
            }

            //displays the refute card.
            if (refuteList.size() > 0) {
                System.out.println("It is " + p.getPlayerName() + "'s turn to refute");
                printCards(refuteList);
                System.out.println("The card you wish to refute with: ");

                String in = input.nextLine();
                Card c = refuteList.get(Integer.parseInt(in));
                if (s.contains(c)) {
                    return true;
                }
            } else {
                System.out.println(p.getPlayerName() + " cannot refute");

            }
        }
        return false;
    }

   /* public void redraw() {
        System.out.flush(); //write any data stored in the output buffer. Prevents data loss.
        board.draw();
    }*/

/*
    /**
     * Generate new board

    public void initialiseBoard() {
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[1].length; j++) {
                board.getBoard()[i][j] = new Tile(i, j);
            }
        }

        //initialiseRooms(); might not need these
        //initialiseCards();
    }
    */

    /**
     * Generates Rooms
     */
   /* public void initialiseRooms() {

        rooms.add(new Room("Kitchen", board, "K"));
        rooms.add(new Room("Ball Room", board, "B"));
        rooms.add(new Room("Conservatory", board, "C"));
        rooms.add(new Room("Billiard Room", board, "I"));
        rooms.add(new Room("Library", board, "L"));
        rooms.add(new Room("Study", board, "S"));
        rooms.add(new Room("Hall", board, "H"));
        rooms.add(new Room("Lounge", board, "O"));
        rooms.add(new Room("Dining Room", board, "D"));

       // parseLayout();

    }*/

    /**
     * Adds the appropriate tiles to their respective rooms based on the layout string
     * The layout string represents the board with each character representing a different room
     * 'X' represents inaccessible areas of the map, such as walls
     * '_' represents accessible tiles that are not part of a room
     * 'E' represents the end of the string
     */
   /* public void parseLayout() {
        String layout =
                "XXXXXXXXX_XXXX_XXXXXXXXX" +
                        "XXXXXXX___XXXX___XXXXXXX" +
                        "XKKKKX__XXXBBXXX__XCCCCX" +
                        "XKKKKX__XBBBBBBX__XCCCCX" +
                        "XKKKKX__XBBBBBBX__CCCCCX" +
                        "XXKKKX__BBBBBBBB___XXXXX" +
                        "XXXXKX__XBBBBBBX________" +
                        "________XBXXXXBX_______X" +
                        "X_________________XXXXXX" +
                        "XXXXX_____________IIIIIX" +
                        "XDDDXXXX__XXXXX___XIIIIX" +
                        "XDDDDDDX__XXXXX___XIIIIX" +
                        "XDDDDDDD__XXXXX___XXXXIX" +
                        "XDDDDDDX__XXXXX________X" +
                        "XDDDDDDX__XXXXX___XXLXXX" +
                        "XXXXXXDX__XXXXX__XXLLLLX" +
                        "X_________XXXXX__LLLLLLX" +
                        "_________________XXLLLLX" +
                        "X________XXHHXX___XXXXXX" +
                        "XXXXXOX__XHHHHX_________" +
                        "XOOOOOX__XHHHHH________X" +
                        "XOOOOOX__XHHHHX__XSXXXXX" +
                        "XOOOOOX__XHHHHX__XSSSSSX" +
                        "XOOOOOX__XHHHHX__XSSSSSX" +
                        "XXXXXXX_XXXXXXXX_XXXXXXXE";

        for (int i = 0; layout.charAt(i) != 'E'; i++) {
            if (layout.charAt(i) == 'X') {
                board.getBoard()[i / boardWidth][i % boardWidth].setAccessible();
                board.getBoard()[i / boardWidth][i % boardWidth].setPrint("+");
            } else {
                for (Room r : rooms) {
                    if (layout.charAt(i) == r.getPrint().charAt(0)) {
                        r.addTile(board.getBoard()[i / boardWidth][i % boardWidth]);
                    }
                }
            }
        }

        for (Room r : rooms) {
            r.setPrint();
        }
    } */

    /**
     * Generates and adds cards to the deck
     */
    private void initialiseCards() {

        //Character cards
        deck.add(new CharacterCard("Col Mustard", 17, 0));
        deck.add(new CharacterCard("Mrs White", 0, 9));
        deck.add(new CharacterCard("Prof Plum", 19, 23));
        deck.add(new CharacterCard("Miss Scarlett", 24, 7));
        deck.add(new CharacterCard("Mr Green", 0, 14));
        deck.add(new CharacterCard("Mrs Peacock", 6, 23));

        //Weapon Cards
        deck.add(new WeaponCard("Rope"));
        deck.add(new WeaponCard("Dagger"));
        deck.add(new WeaponCard("Lead Pipe"));
        deck.add(new WeaponCard("Spanner"));
        deck.add(new WeaponCard("Revolver"));
        deck.add(new WeaponCard("Candlestick"));

        //Room Cards
        deck.add(new RoomCard("Hall"));
        deck.add(new RoomCard("Billiard Room"));
        deck.add(new RoomCard("Conservatory"));
        deck.add(new RoomCard("Library"));
        deck.add(new RoomCard("Ball Room"));
        deck.add(new RoomCard("Study"));
        deck.add(new RoomCard("Dining Room"));
        deck.add(new RoomCard("Lounge"));
        deck.add(new RoomCard("Kitchen"));

        Collections.shuffle(deck); //added this

    }

    /**
     * Generates the murder scene
     *
     * @return a suggestion containing the murder weapon, the locations of the murder and the identity of the murderer
     */
    private Suggestion createMurderer() {
        WeaponCard mWeapon;
        RoomCard mRoom;
        CharacterCard the_murderer;

        int i = (int) (Math.random() * tempDeck.size());
        while (!(tempDeck.get(i) instanceof WeaponCard)) {
            i = (int) (Math.random() * tempDeck.size());

        }

        mWeapon = (WeaponCard) tempDeck.get(i);
        tempDeck.remove(mWeapon);

        i = (int) (Math.random() * tempDeck.size());
        while (!(tempDeck.get(i) instanceof RoomCard)) {
            i = (int) (Math.random() * tempDeck.size());

        }

        mRoom = (RoomCard) tempDeck.get(i);
        tempDeck.remove(mRoom);

        i = (int) (Math.random() * tempDeck.size());
        while (!(tempDeck.get(i) instanceof CharacterCard)) {
            i = (int) (Math.random() * tempDeck.size());
        }

        the_murderer = (CharacterCard) tempDeck.get(i);
        tempDeck.remove(the_murderer);

        return new Suggestion(mWeapon, mRoom, the_murderer);
    }


    /**
     * Allows player to create suggestion
     *
     * @param p     the player making the suggestion
     * @param input the scanner to take input
     * @return the created suggestion
     */

    private Suggestion suggest(Player p, Scanner input) {
        Card roomC = null;
        Card weaponC = null;
        Card characterC = null;

        int i = 0;
        System.out.println("Pick 1 card of each type to make a suggestion");
        for (Card c : deck) {
            System.out.println(i + ": " + c.getName() + " (" + c.getType() + ")");
            i++;

            if (c instanceof RoomCard && c.getName().equals(p.getPlayerPosition().getRoom().getRoomCard())) {
                roomC = c;
            }
        }

        System.out.println("The room is: " + p.getPlayerPosition().getRoom().getRoomCard());

        System.out.println("Pick 1 weapon card: ");
        while (!(weaponC instanceof WeaponCard)) {
            try {
                weaponC = deck.get(Integer.parseInt(input.nextLine()));
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid Format!");
                return null;
            }
        }

        System.out.println("Pick 1 character card: ");
        while (!(characterC instanceof CharacterCard)) {
            try {
                characterC = deck.get(Integer.parseInt(input.nextLine()));
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid Format!");
                return null;
            }
        }
        return new Suggestion(p, false, (WeaponCard) weaponC, (RoomCard) roomC, (CharacterCard) characterC);
    }

    /**
     * represents rolling two 6-sided dice
     *
     * @return the sum of the two dice rolls
     */
    /* private int rollDice() {
        return (int) ((Math.random() * 5 + 1) + (Math.random() * 5 + 1));
    } */


    /**
     * Prints cards from the list
     *
     * @param cards - the list
     */
    public void printCards(List<Card> cards) {
        int i = 0;
        for (Card c : cards) {
            System.out.println(i + ": " + c.getName() + " (" + c.getType() + ")");
            i++;
        }
    }


    /**
     * @param p     - The player
     * @param input - The scanner
     * @return true if the game is over
     */
    /* private boolean doTurn(Player p, Scanner input) {

        System.out.println("It is " + p.getPlayerName() + "'s (Player " + (p.getPrint()) + ") turn");
        boolean moving = true;
        Room startRoom = p.getPlayerPosition().getRoom();
        boolean suggest = false;

        if (p.getJustMoved()) {//if player has moved
            p.setJustMoved(false);
            System.out.println("You can choose to move or make a suggestion.");
            System.out.println("Do you want to move? (Y/N)");

            String response = input.nextLine().toUpperCase();
            if (response.equals("N")) {
                moving = false;
                suggest = true;
            }
        }

        if (moving) {//if character is moving
           // redraw();

            int numberOfMoves = rollDice();
            System.out.println("You rolled: " + numberOfMoves);

            while (numberOfMoves != 0) {//while player still has turns left
                System.out.println("Enter direction to move: (WASD)");
                System.out.println("or press 'H' to view your hand");
                System.out.println("or press 'G' to make a guess (suggestion) at the murder circumstances");

                String direction = input.nextLine();

                if (direction.toUpperCase().equals("H")) {//if player wants to see whats in hand
                    p.printHand();

                } else if (direction.toUpperCase().equals("G")) {//if the player wants to make a suggestion
                    Room currentRoom = p.getPlayerPosition().getRoom();
                    if (currentRoom != null && !currentRoom.equals(startRoom)) {
                        suggest = true;
                        numberOfMoves = 0;

                    } else {
                        System.out.println("Cannot make a suggestion from this room.");
                    }
                } else if (p.parser(direction)) {//after each move player makes on board
                    numberOfMoves--;
                    System.out.println("You have " + numberOfMoves + " moves left");
                }
                //redraw();

            }
        }

        if (!suggest && p.getPlayerPosition().getRoom() != null && !p.getPlayerPosition().getRoom().equals(startRoom)) {
            //if player is in a room to make a suggestion
            System.out.println("Do you want to make a Suggestion? (Y/N)");
            String response = input.nextLine().toUpperCase();
            if (response.equals("Y")) {
                suggest = true;
            }
        }

        if (suggest) {//if player wants to suggest
            System.out.println("Your Hand: ");
            p.printHand();
            System.out.println(" ");
            Suggestion s = suggest(p, input);

            if (s == null) {
                return false;
            }

            boolean refute = refuteSuggestion(s, input);

          /*  for (Item p1 : players) {
                if (s != null && p1.getPlayerName().equals(s.getCharacter().getName())) {
                    for (Room r : rooms) {
                        if (r.getRoomCard().equals(s.getRoom().getName())) {
                            Tile movingTo = r.getTile().stream().filter(tile -> tile.getContains() == null).findFirst().get();
                            p1.movePlayer(movingTo);
                            p1.setJustMoved(true);
                        }
                    }
                }
            } */

            //if the player makes an accusation
           /* if (!refute) {
                assert s != null;
                s.setIsAccusation(true);
                accusations.add(s);
            }
        }

        //if the accusation is made
        if (!accusations.isEmpty()) {
            System.out.println("Do you want to make an accusation? (Y/N");
            String response = input.nextLine().toUpperCase();
            if (response.equals("Y")) {
                Suggestion accuse = getAccusation(p, input);
                if (accuse.equals(murder)) {//if the murder is found
                    System.out.println("Congratulations!");
                    return true;
                } else {//if wrongly accused
                    System.out.println("Sorry! You've Lost.");
                    p.lose();
                }
            }
        }
        return false;
    } */

    /**
     * Main game
     */
    private void run() {
        //getting the user input and boundary testing it
      /*  Scanner input = new Scanner(System.in);
        do {
            System.out.println("Please enter the number of players between 3 and 6: ");
            try {
                numberOfPlayers = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid. Please enter a number between 3 and 6.");
            }
            System.out.println("You entered: " + numberOfPlayers);


       // }
      //  while (numberOfPlayers > 6 || numberOfPlayers < 3);

        //adding the players
        players.add(new Player(board, new CharacterCard("Mr Green", 0, 14)));
        players.add(new Player(board, new CharacterCard("Mrs White", 0, 9)));
        players.add(new Player(board, new CharacterCard("Mrs Peacock", 6, 23)));
        if (numberOfPlayers > 3) {
            players.add(new Player(board, new CharacterCard("Col Mustard", 17, 0)));
        }
        if (numberOfPlayers > 4) {
            players.add(new Player(board, new CharacterCard("Prof Plum", 19, 23)));
        }
        if (numberOfPlayers > 5) {
            players.add(new Player(board, new CharacterCard("Miss Scarlett", 24, 7)));
        }

        for (Player p : players) {
            p.setPrint(String.valueOf(players.indexOf(p) + 1));
        }

        tempDeck.addAll(deck);
        murder = createMurderer();
        dealCards(numberOfPlayers);

        //checking if the player has won or not
        int count = 0;
        boolean won = false;
        while (!won) {
            for (Player p : players) {
                won = doTurn(p, input);
                if (won) {
                    System.out.println("Congratulations " + p.getPlayerName() + ". You Won.");
                    System.out.println("The Murder Circumstances:\n It was " + murder.getCharacter().getName() + " in the " + murder.getRoom().getName() + " with the " + murder.getWeapon().getName() + ".");
                }
            }

            if (count == 20) {
                break;
            }
            count++;
        }

        System.out.println("Please enter the number of players between 3 and 6: ");

        numberOfPlayers = Integer.parseInt(input.nextLine());
*/

    }














}