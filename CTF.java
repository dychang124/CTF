/*
 * Daniel Chang and Sanik Malepati
 * 4/9/18
 * Capture the Flag game is where the player tries to get the flag from the other side. There are enemy
 * players that will try to tag the user. If the user gets tagged, he will have to answer a quadratic
 * equation problem, which is something the user should have learned in the tutorial. There will be a
 * timer that indicates how much longer the user has to get the flag.
 */
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Timer;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.io.PrintWriter;

public class CTF
{    
    private CTFPanel ctfp;
    private CardLayout cards;
    private int score;//score that player has
    private int min;//number of minutes player has left to complete the current equation
    private int sec;//number of seconds left
    private Timer timer;//timer for counting down
    private int x, y;//x and y of character, for movement
    private boolean up, down, right, left;//for checking to see if the arrow keys are held down
    private int enemyX, enemyY, enemy2X, enemy2Y;//x and y of the enemy, for movement
    private Timer enemytm;//timer for enemy movement
    private boolean crossed;//boolean to check if the user has crossed the safety line
    private boolean inside1, inside2, inside3, inside4;//booleans to see if the character has touched each of the treasure chests
    private Timer rttm, lftm, uptm, dntm, uprttm, uplftm, dnlftm, dnrttm;//timers for character movement
    private int treasure1X, treasure1Y, treasure2X, treasure2Y, treasure3X, treasure3Y, treasure4X, treasure4Y;//X and Y values of all treasure chests(mainly for following character)
    private String q, a1, a2, a3, a4;
    private boolean c1, c2, c3, c4;
    private String ans1, ans2, ans3, ans4;
    private boolean en, en2;//boolean to check if enemy should be moving
    private int xReset;
    private PrintWriter output;
    private String fileName;
    private Scanner input;
    private String highscore;
    public CTF()
    {
        q = "";
        a1 = "";
        a2 = "";
        a3 = "";
        a4 = "";
        ans1 = "";
        ans2 = "";
        ans3 = "";
        ans4 = "";
        
        highscore="0";
        
        output=null;
        input=null;
        fileName="Highscore.txt";
        readIt();
        //writeIt();
        getWords();
    }
    public static void main( String[] args )
    {
        CTF ctf = new CTF();
        ctf.run();
    }
    public void run()
    {
        JFrame frame = new JFrame("Capture The Flag");    // Create the JFrame
        frame.setSize(1000, 1050);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(450,0);
        frame.setResizable(false);
        
        ctfp=new CTFPanel();
        
        
        // Add panels to the frame
        frame.getContentPane().add(ctfp);
        // Make the JFrame visible
        frame.setVisible(true);
    }
 
    public class CTFPanel extends JPanel
    {
        public CTFPanel()
        {
            Tutor tutorial=new Tutor();
            Main main=new Main();
            Game game=new Game();
            HowTo hwTo=new HowTo();
            GameOver go = new GameOver();
            cards=new CardLayout();
            
            setLayout(cards);
            
            add(main, "Main");
            add(tutorial, "Tutorial");
            add(game, "Game");
            add(hwTo, "How To Play");
            add(go, "GameOver");//card layout
        }
        
    }
    public class Main extends JPanel implements ActionListener//This is the main panel with buttons to go to all other windows
    {
        private JButton play;//to go to game panel
        private JButton tut;//for how to factor panel
        private JButton howTo;//for how to play panel
        public Main()
        {
            min = 1;
            sec = 0;
            
            setLayout(null);
            setBackground(Color.YELLOW);
            play = new JButton("Play");
            tut = new JButton("Tutorial");
            howTo=new JButton("How To Play");
 
            play.addActionListener(this);
            tut.addActionListener(this);
            howTo.addActionListener(this);
 
                
            play.setBounds(200, 400, 600, 100);
            tut.setBounds(200, 600, 600, 100);
            howTo.setBounds(200, 800, 600, 100);
 
            add(play);
            add(tut);
            add(howTo);
                
        }
        public void paintComponent(Graphics g)
        {
            super.paintComponent (g);
            Font font = new Font ("Serif", Font.BOLD, 60);
            g.setFont( font );
            g.drawString("Welcome to CTF!", 250, 150);
        }
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if(command.equals("Play"))
            {
                cards.show(ctfp, "Game");
                timer.start();
                x = 440;
                y = 830;
                score = 0;
                enemytm.start();
            }
            if(command.equals("Tutorial"))
            {
                cards.show(ctfp, "Tutorial");
            }
            if(command.equals("How To Play"))
            {
                cards.show(ctfp, "How To Play");
            }
        }
    }
    public class Tutor extends JPanel implements ActionListener//window to teach how to factor
    {
        private JButton back;//button to go back to main menu
        public Tutor()
        {
            setLayout(null);
            setBackground(Color.WHITE);
            back = new JButton("<- Back");
            back.addActionListener(this);
            back.setBounds(10, 10, 100, 50);
            add(back);
        }
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Font font = new Font ("Serif", Font.BOLD, 60);
            g.setFont( font );
            g.drawString("How To Factor", 275, 100);
            Font vij = new Font ("Serif", Font.BOLD, 15);
            g.setFont(vij);
            g.drawString("Factoring is a very easy and you can do it in a few easy steps. In this example I will factor the polynomial x^2+x+x+1.", 1, 320);
            g.drawString("Example 1. x^2+2x+1:", 1, 340);
            g.drawString("You need to see the format and make it a ax^2+bx+c format.", 1, 360);
            g.drawString("To factor it, you need to find 2 numbers that are factors of c that multiply with factors of a", 1, 380);
            g.drawString("and add up to b. In this example, a is 1 and c is 1, and the factors of 1 are 1 and 1. 1+1 also adds up to 2.", 1, 400);
            g.drawString("When we factor it, it will be (x+1)(x+1). The factors of a go before the x, and the factors of c are added to the end.", 1, 420);
            g.drawString("Example 2: 3x^2-8x+4", 1, 440);
            g.drawString("This is already in the ax^2+bx+c format. First, we find 2 factors of 4, the c variable. We see that the b variable is", 1, 460);
            g.drawString("negative, and to add to a negative number, the addends have to be negative, so let's use -2 and -2.", 1, 480);
            g.drawString("The only factors of 3, the a variable, are 3 and 1. Then we put the two sets of factors together.", 1, 500);
            g.drawString("This will end up as (3x-2)(x-2)", 1, 520);
            g.drawString("Sometimes there are trinomials that are not able to be factor, for example: x^2+2x+3", 1, 540);
            g.drawString("Trinomials like this are prime, because there are no factors of it.", 1, 560);
            
            //teaching how to factor
        }
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if(command.equals("<- Back"))
            {
                cards.show(ctfp, "Main");
            }
        }
    }
    public void reset()//method for when the game is over(player tagged or ran out of time) to reset all variables to original form
    {
        if(score>400)
        {
            xReset=300;
        }
        else
        {
            xReset=440;
        }
        en=false;
        en2=false;
        x = 440;
        y = 830;
        crossed=false;
        timer.stop();
        min=0;
        sec=30;
        down=false;
        up=false;
        left=false;
        right=false;
        enemyX=xReset;
        enemyY=150;
        enemy2X=600;
        enemy2Y=150;
        rttm.stop();
        lftm.stop();
        uptm.stop();
        dntm.stop();
        uprttm.stop();
        uplftm.stop();
        dnlftm.stop();
        dnrttm.stop();
        inside1 = false;
        inside2 = false;
        inside3 = false;
        inside4 = false;
        treasure1X=50;
        treasure1Y=100;
        treasure2X=316;
        treasure2Y=100;
        treasure3X=582;
        treasure3Y=100;
        treasure4X=850;
        treasure4Y=100;
        
        readIt();
        writeIt();
        getEquation();
        randomizer();
    }
    public void randomizer()//this method gives the 4 answers a random position on the 4 different treasure chests
    {
            int counter = 0;
            int rand1 = 0;
            int rand2 = 0;
            int rand3 = 0;
            int rand4 = 0;
            rand1 = (int)(Math.random()*4+1);
            if(rand1 == 1)
            {
                c1 = true;
                ans1 = a1;
            }
            else if(rand1 == 2)
            {
                c2 = true;
                ans2 = a1;
            }
            else if(rand1 == 3)
            {
                c3 = true;
                ans3 = a1;
            }
            else
            {
                c4 = true;
                ans4 = a1;
            }
            counter = 0;
            while(counter == 0)//Once I give the correct answer a position I have to while loop that keeps making numbers until it is not the same number as the number before I repeat this for all answers.
            {
                rand2 = (int)(Math.random()*4+1);
                if(rand2 != rand1)
                {
                    counter++;
                }
            }
            if(rand2 == 1)
            {
                ans1 = a2;
            }
            else if(rand2 == 2)
            {
                ans2 = a2;
            }
            else if(rand2 == 3)
            {
                ans3 = a2;
            }
            else
            {
                ans4 = a2;
            }
            counter = 0;
            while(counter == 0)
            {
                rand3 = (int)(Math.random()*4+1);
                if(rand3 != rand1&&rand3 != rand2)
                {
                    counter++;
                }
            }
            if(rand3 == 1)
            {
                ans1 = a3;
            }
            else if(rand3 == 2)
            {
                ans2 = a3;
            }
            else if(rand3 == 3)
            {
                ans3 = a3;
            }
            else
            {
                ans4 = a3;
            }
            counter = 0;
            while(counter == 0)
            {
                rand4 = (int)(Math.random()*4+1);
                if(rand4 != rand1&&rand4 != rand2&&rand4 != rand3)
                {
                    counter++;
                }
            }
            if(rand4 == 1)
            {
                ans1 = a4;
            }
            else if(rand4 == 2)
            {
                ans2 = a4;
            }
            else if(rand4 == 3)
            {
                ans3 = a4;
            }
            else
            {
                ans4 = a4;
            }
    }
    public void getEquation() //This method makes a random trinomial and generates 1 correct answer and 3 incorrect answers
    {
            int b=0;
            int c=0;
            int first=0;
            int second=0;
            int diff1 = 0;
            int diff2 = 0;
            int diff3=0;
            int diff4=0;
            int diff5=0;
            int diff6=0;
            
            first=(int)(Math.random()*10+1);
            second=(int)(Math.random()*10+1);
            
            
            c=first*second;
            b=first+second;
            
            
            if(first!=2&&second!=2&&c%2==0)
            {
                
                if(c%4>0)
                {
                    diff1=2;
                    diff2=c/2;
                }
                else
                {
                    diff1=4;
                    diff2=c/4;
                }
            }
            if(first!=3&&second!=3&&c%3==0)
            {
                if(diff1==0)
                {
                    diff1=3;
                    diff2=c/3;
                }
                else
                {
                    diff3=3;
                    diff4=c/3;
                }
            }
            if(first!=5&&second!=5&&c%5==0)
            {
                if(diff1==0)
                {
                    diff1=5;
                    diff2=c/5;
                }
                else if(diff3==0)
                {
                    diff3=5;
                    diff4=c/5;
                }
                else
                {
                    diff5=5;
                    diff6=c/5;
                }
            }
            if(diff1==0)
            {
                do{
                    diff1=(int)(Math.random()*10+1);
                    diff2=(int)(Math.random()*10+1);
                }while((diff1==first&&diff2==second)||(diff1==second&&diff2==first));
                do
                {
                    diff3=(int)(Math.random()*10+1);
                    diff4=(int)(Math.random()*10+1);
                }while((diff3==first&&diff4==second)||(diff3==second&&diff4==first)||(diff3==diff1&&diff4==diff2)||(diff3==diff2&&diff4==diff1));
                do
                {
                    diff5=(int)(Math.random()*10+1);
                    diff6=(int)(Math.random()*10+1);
                }while((diff5==first&&diff6==second)||(diff5==second&&diff6==first)||(diff5==diff1&&diff6==diff2)||(diff5==diff2&&diff6==diff1)||(diff5==diff3&&diff6==diff4)||(diff5==diff4&&diff6==diff3));
            }
            if(diff3==0)
            {
                do
                {
                    diff3=(int)(Math.random()*10+1);
                    diff4=(int)(Math.random()*10+1);
                }while((diff3==first&&diff4==second)||(diff3==second&&diff4==first)||(diff3==diff1&&diff4==diff2)||(diff3==diff2&&diff4==diff1));
                do
                {
                    diff5=(int)(Math.random()*10+1);
                    diff6=(int)(Math.random()*10+1);
                }while((diff5==first&&diff6==second)||(diff5==second&&diff6==first)||(diff5==diff1&&diff6==diff2)||(diff5==diff2&&diff6==diff1)||(diff5==diff3&&diff6==diff4)||(diff5==diff4&&diff6==diff3));
            }
            if(diff5==0)
            {
                do
                {
                    diff5=(int)(Math.random()*10+1);
                    diff6=(int)(Math.random()*10+1);
                }while((diff5==first&&diff6==second)||(diff5==second&&diff6==first)||(diff5==diff1&&diff6==diff2)||(diff5==diff2&&diff6==diff1)||(diff5==diff3&&diff6==diff4)||(diff5==diff4&&diff6==diff3));
            }
            
            q="x^2+"+b+"x+"+c;
            a1="(x+"+first+")(x+"+second+")";
            a2="(x+"+diff1+")(x+"+diff2+")";
            a3="(x+"+diff3+")(x+"+diff4+")";
            a4="(x+"+diff5+")(x+"+diff6+")";
    }
    public void readIt()
    {
        File inFile=new File(fileName);
        try
        {
            input=new Scanner(inFile);
        }
        catch(FileNotFoundException e)
        {
            System.err.printf("\n\n\nERROR: Cannot find/open file %s\n\n", fileName);
            System.exit(1);
        }
    }
    public void writeIt()
    {
        File outFile=new File(fileName);
        try
        {
            output=new PrintWriter(outFile);
        }
        catch(IOException e)
        {
            System.err.println("ERROR: Cannot create "+fileName+" file.\n\n\n");
            System.exit(2);
        }
    }
    public void getWords()
    {
        while(input.hasNext())
        {
            highscore=input.next();
            input.nextLine();
            System.out.println(highscore+"in");
        }
    }
    public void writeHigh()
    {
        output.println(highscore);
        output.close();
    }
    public class Game extends JPanel implements ActionListener, KeyListener
    {
        private JButton back;//button to go back to main panel
        private Image chara;//character image
        private Image treas;//treasure image
        private String charaName;//file name of character
        private String treasName;//file name of treasure
        private Image enemy;//enemy image
        private String enemyName;//file name of enemy
        private int charSpeed, enemySpeed;//speed of character and enemy
        
        public Game()
        {
            q="";
            a1="";
            a2="";
            a3="";
            a4="";
            c1=false;
            c2=false;
            c3=false;
            c4=false;
            
            xReset=440;
            
            en=false;
            en2=false;
            
            enemy=null;
            enemyName="bad.png";
            enemyX=xReset;
            enemyY=150;
            enemy2X=600;
            enemy2Y=150;
            
            treasure1X=50;
            treasure1Y=100;
            treasure2X=316;
            treasure2Y=100;
            treasure3X=582;
            treasure3Y=100;
            treasure4X=850;
            treasure4Y=100;
            
            inside1=false;
            inside2=false;
            inside3=false;
            inside4=false;
            
            en=false;
            
            charSpeed=5;
            enemySpeed=2;
            
            crossed=false;
            up=false;
            down=false;
            right=false;
            left=false;
            x=440;
            y=830;
            setLayout(null);
            back = new JButton("<- Back");
            back.addActionListener(this);
            back.setBounds(10, 10, 100, 50);
            add(back);
            chara=null;
            treas=null;
            charaName="Left_3.png";
            treasName="Treasure.png";
            getCharaImage();
            getTreasImage();
            getEnemyImage();
            min = 0;
            sec = 30;
            score = 0;
            rttm=new Timer(1, this);
            lftm=new Timer(1, this);
            uptm=new Timer(1, this);
            dntm=new Timer(1, this);
            uprttm=new Timer(1, this);
            uplftm=new Timer(1, this);
            dnrttm=new Timer(1, this);
            dnlftm=new Timer(1, this);
            timer = new Timer(1000, this);
            enemytm=new Timer(1, this);
            enemytm.start();
            
            readIt();
            writeIt();
            getWords();
            getEquation();
            randomizer();
            
            addKeyListener(this);
        }
        public void keyPressed(KeyEvent e)
        {
            int key=e.getKeyCode();
            if(key==KeyEvent.VK_UP)
            {
                up=true;
            }
            if(key==KeyEvent.VK_LEFT)
            {
                left=true;
            }
            if(key==KeyEvent.VK_RIGHT)
            {
                right=true;
            }
            if(key==KeyEvent.VK_DOWN)
            {
                down=true;
            }//checking for arrow keys being pressed and making booleans true for character movement
            repaint();
        }
        public void keyReleased(KeyEvent e)
        {
            int key=e.getKeyCode();
            if(key==KeyEvent.VK_UP)
            {
                up=false;
            }
            if(key==KeyEvent.VK_LEFT)
            {
                left=false;
            }
            if(key==KeyEvent.VK_RIGHT)
            {
                right=false;
            }
            if(key==KeyEvent.VK_DOWN)
            {
                down=false;
            }//checking for arrow keys released and making booleans false to stop character movement
            repaint();
        }
        public void keyTyped(KeyEvent e){}
        public void getCharaImage()
        {
            try
            {
                chara=ImageIO.read(new File(charaName));
            }
            catch(IOException e)
            {
                System.err.println("\n\n"+charaName+" can't be found.\n\n");
                e.printStackTrace();//getting image file for character
            }
        }
        public void getEnemyImage()
        {
            try
            {
                enemy=ImageIO.read(new File(enemyName));
            }
            catch(IOException e)
            {
                System.err.println("\n\n"+enemyName+" can't be found.\n\n");
                e.printStackTrace();//getting image file for enemy
            }
        }
        public void getTreasImage()
        {
            try
            {
                treas=ImageIO.read(new File(treasName));
            }
            catch(IOException e)
            {
                System.err.println("\n\n"+treasName+" can't be found.\n\n");
                e.printStackTrace();//getting image file for treasure chests
            }
        }
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setColor(Color.GREEN);
            g.fillRect(50, 100, 900, 960);//green playing field
            g.setColor(Color.BLACK);
            g.drawLine(50, 700, 950, 700);//line for safety boundary
            g.setColor(Color.PINK);
            g.fillRect(350, 1020, 300, 50);//rectangle for the polynomial being solved
            g.drawImage(chara, x, y, this);//drawing character
            if(score>400)
            {
                g.drawImage(enemy, enemy2X, enemy2Y, this);
            }
            g.drawImage(enemy, enemyX, enemyY, this);//drawing enemy
            Font jf = new Font ("Serif", Font.BOLD, 10);
            g.setFont(jf);
            g.setColor(Color.BLACK);
            if(!inside1)
            {
                g.drawImage(treas, treasure1X, treasure1Y, this);
                g.drawString(ans1, treasure1X+17, treasure1Y+59);
            }
            else
            {
                g.drawImage(treas, treasure1X, treasure1Y, 49, 48, this);//if character has touched the treasure chest, it will become a smaller image, and follow the character around.
            }
            if(!inside2)
            {
                g.drawImage(treas, treasure2X, treasure2Y, this);
                g.drawString(ans2, treasure2X+17, treasure2Y+59);
            }    
            else
            {
                g.drawImage(treas, treasure2X, treasure2Y, 49, 48, this);    
            }
            if(!inside3)
            {
                g.drawImage(treas, treasure3X, treasure3Y, this);
                g.drawString(ans3, treasure3X+17, treasure3Y+59);
            }
            else
            {
                g.drawImage(treas, treasure3X, treasure3Y, 49, 48, this);
            }
            if(!inside4)
            {
                g.drawImage(treas, treasure4X, treasure4Y, this);
                g.drawString(ans4, treasure4X+17, treasure4Y+59);
            }
            else
            {
                g.drawImage(treas, treasure4X, treasure4Y, 49, 48, this);    
            }
            Font ft = new Font ("Serif", Font.BOLD, 30);
            g.setFont(ft);
            g.setColor(Color.BLACK);
            g.drawString(q, 350, 1050);
            g.drawString("Score: " + score, 200, 50);
            if(inside1)
            {
                treasure1X=x+40;
                treasure1Y=y-20;//making treasure chest follow character if it has been touched
            }
            if(inside2)
            {
                treasure2X=x+40;
                treasure2Y=y-20;
            }
            if(inside3)
            {
                treasure3X=x+40;
                treasure3Y=y-20;
            }
            if(inside4)
            {
                treasure4X=x+40;
                treasure4Y=y-20;
            }
            
            if(sec > 9)
            {
                g.drawString("Time Remaining: " + min + ":" + sec, 600, 50);
            }
            else
            {
                g.drawString("Time Remaining: " + min + ":" + "0" + sec, 600, 50);
            }
            if(up&&left)
            {
                uplftm.start();
                uptm.stop();
                lftm.stop();
            }
            else if(up&&right)
            {
                uprttm.start();
                uptm.stop();
                rttm.stop();
            }
            else if(down&&left)
            {
                dnlftm.start();
                dntm.stop();
                lftm.stop();
            }
            else if(down&&right)
            {
                dnrttm.start();
                dntm.stop();
                rttm.stop();
            }
            else if(up)
            {
                uptm.start();
            }
            else if(down)
            {
                dntm.start();
            }
            else if(left)
            {
                lftm.start();
            }
            else if(right)
            {
                rttm.start();
            }
            
            
            if(!up||!left)
            {
                uplftm.stop();
            }
            if(!up||!right)
            {
                uprttm.stop();
            }
            if(!down||!left)
            {
                dnlftm.stop();
            }
            if(!down||!right)
            {
                dnrttm.stop();
            }
            if(!up)
            {
                uptm.stop();
            }
            if(!down)
            {
                dntm.stop();
            }
            if(!left)
            {
                lftm.stop();
            }
            if(!right)
            {
                rttm.stop();
            }//turning timers off and on accordingly for character movement
            if(y<685)
            {
                crossed=true;//if character has crossed safety line
            }
            if(y>685&&(inside1||inside2||inside3||inside4))//the booleans are for when the character has gotten the treasure chests, and are on the south side of the line, enemy will stop chasing
            {
                crossed=false;
            }
            if(crossed)
            {
                if(y<enemyY&&en)
                {
                    if(x<enemyX)
                    {
                        if(enemyY-y>enemyX-x)
                        {
                            enemyY-=enemySpeed;
                        }
                        else
                        {
                            enemyX-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(enemyY-y>x-enemyX)
                        {
                            enemyY-=enemySpeed;
                        }
                        else
                        {
                            enemyX+=enemySpeed;
                        }
                    }
                    en=false;
                }
                if(y>enemyY&&en)
                {
                    if(x<enemyX)
                    {
                        if(y-enemyY>enemyX-x)
                        {
                            enemyY+=enemySpeed;
                        }
                        else
                        {
                            enemyX-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(y-enemyY>x-enemyX)
                        {
                            enemyY+=enemySpeed;
                        }
                        else
                        {
                            enemyX+=enemySpeed;
                        }
                    }
                    en=false;
                }
                if(x<enemyX&&en)
                {
                    if(y<enemyY)
                    {
                        if(enemyX-x>enemyY-y)
                        {
                            enemyX-=enemySpeed;
                        }
                        else
                        {
                            enemyY-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(enemyX-x>y-enemyY)
                        {
                            enemyX-=enemySpeed;
                        }
                        else
                        {
                            enemyY+=enemySpeed;
                        }
                    }
                    en=false;
                }
                if(x>enemyX&&en)
                {
                    if(y<enemyY)
                    {
                        if(x-enemyX>enemyY-y)
                        {
                            enemyX+=enemySpeed;
                        }
                        else
                        {
                            enemyY-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(x-enemyX>y-enemyY)
                        {
                            enemyX+=enemySpeed;
                        }
                        else
                        {
                            enemyY+=enemySpeed;
                        }
                    }
                    en=false;
                }
                
                if(y<enemy2Y&&en2)
                {
                    if(x<enemy2X)
                    {
                        if(enemy2Y-y>enemy2X-x)
                        {
                            enemy2Y-=enemySpeed;
                        }
                        else
                        {
                            enemy2X-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(enemy2Y-y>x-enemy2X)
                        {
                            enemy2Y-=enemySpeed;
                        }
                        else
                        {
                            enemy2X+=enemySpeed;
                        }
                    }
                    en2=false;
                }
                if(y>enemy2Y&&en2)
                {
                    if(x<enemy2X)
                    {
                        if(y-enemy2Y>enemy2X-x)
                        {
                            enemy2Y+=enemySpeed;
                        }
                        else
                        {
                            enemy2X-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(y-enemy2Y>x-enemy2X)
                        {
                            enemy2Y+=enemySpeed;
                        }
                        else
                        {
                            enemy2X+=enemySpeed;
                        }
                    }
                    en2=false;
                }
                if(x<enemy2X&&en2)
                {
                    if(y<enemy2Y)
                    {
                        if(enemy2X-x>enemy2Y-y)
                        {
                            enemy2X-=enemySpeed;
                        }
                        else
                        {
                            enemy2Y-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(enemy2X-x>y-enemy2Y)
                        {
                            enemy2X-=enemySpeed;
                        }
                        else
                        {
                            enemy2Y+=enemySpeed;
                        }
                    }
                    en2=false;
                }
                if(x>enemy2X&&en2)
                {
                    if(y<enemy2Y)
                    {
                        if(x-enemy2X>enemy2Y-y)
                        {
                            enemy2X+=enemySpeed;
                        }
                        else
                        {
                            enemy2Y-=enemySpeed;
                        }
                    }
                    else
                    {
                        if(x-enemy2X>y-enemy2Y)
                        {
                            enemy2X+=enemySpeed;
                        }
                        else
                        {
                            enemy2Y+=enemySpeed;
                        }
                    }
                    en2=false;
                }
            }//enemy character movement based on where the character is-if character is farther away horizontally than vertically, it will follow the character horizontally first, and vice versa.
            
            if((inside1||inside2||inside3||inside4)&&!crossed)
            {
                if(inside1&&c1)
                {
                    score+=100;//incrementing score(for testing purposes right now)
                    reset();
                    timer.start();
                }
                else if(inside2&&c2)
                {
                    score+=100;//incrementing score(for testing purposes right now)
                    reset();
                    timer.start();
                }
                else if(inside3&&c3)
                {
                    score+=100;//incrementing score(for testing purposes right now)
                    reset();
                    timer.start();
                }
                else if(inside4&&c4)
                {
                    score+=100;//incrementing score(for testing purposes right now)
                    reset();
                    timer.start();
                }
                else if(inside4&&!c4)
                {
                    inside4=false;
                    treasure4X=85000;
                    treasure4Y=100;
                }
                else if(inside3&&!c3)
                {
                    inside3=false;
                    
                    
                    treasure3X=58200;
                    treasure3Y=100;
                }
                else if(inside2&&!c2)
                {
                    inside2=false;
                    treasure2X=316;
                    treasure2Y=100;
                }
                else if(inside1&&!c1)
                {
                    treasure1Y=10000;
                    treasure1X=50;
                    inside1=false;
                }
            }
            
            requestFocus();
        }
        public void actionPerformed(ActionEvent evt)
        {
            String command = evt.getActionCommand();
            if(command != null)
            {
                if(command.equals("<- Back")) //Takes the player to the main method
                {
                    cards.show(ctfp, "Main");
                    reset();
                }
            }
            else
            {
                if((enemyY+70>y&&enemyY-70<y&&enemyX+70>x&&enemyX-70<x)||((enemy2Y+70>y&&enemy2Y-70<y&&enemy2X+70>x&&enemy2X-70<x)&&score>400))//if the enemy touches the character, game over panel will show, and reset method will be called
                {
                    cards.show(ctfp, "GameOver");
                    reset();
                }
                if(y<130&&x<120&&!inside1&&!inside2&&!inside3&&!inside4)//for whether the character has touched the treasure chests or not
                {
                    inside1 = true;
                }
                if(y<130&&x>260&&x<380&&!inside1&&!inside2&&!inside3&&!inside4)
                {
                    inside2=true;
                }
                if(y<130&&x>510&&x<640&&!inside1&&!inside2&&!inside3&&!inside4)
                {
                    inside3=true;
                }
                if(y<130&&x>790&&!inside1&&!inside2&&!inside3&&!inside4)
                {
                    inside4=true;
                }
                if(evt.getSource()==lftm&&x>30)
                {
                    x-=charSpeed;
                }
                if(evt.getSource()==rttm&&x<860)
                {
                    x+=charSpeed;
                }
                if(evt.getSource()==uptm&&y>85)
                {
                    y-=charSpeed;
                }
                if(evt.getSource()==dntm&&y<980)
                {
                    y+=charSpeed;
                }
                if(evt.getSource()==uplftm&&y>85&&x>30)
                {
                    y-=charSpeed/2;
                    x-=charSpeed/2;
                }
                if(evt.getSource()==uprttm&&y>85&&x<860)
                {
                    y-=charSpeed/2;
                    x+=charSpeed/2;
                }
                if(evt.getSource()==dnlftm&&y<980&&x>30)
                {
                    y+=charSpeed/2;
                    x-=charSpeed/2;
                }
                if(evt.getSource()==dnrttm&&y<980&&x<860)
                {
                    y+=charSpeed/2;
                    x+=charSpeed/2;
                }//timers making character move
                if(evt.getSource()==timer&&sec > 0)
                {
                    sec = sec-1;
                }
                if(evt.getSource()==timer&&sec==0&&min==0)
                {
                    cards.show(ctfp, "GameOver");
                }  
                if(evt.getSource()==timer&&sec <= 0)
                {
                    min = min - 1;
                    sec = 59;
                }//incrementing timer counting down
                if(evt.getSource()==enemytm)
                {
                    en=true;
                    en2=true;
                }//for enemy movement
            }
            repaint();
        }
    }
    public class HowTo extends JPanel implements ActionListener//window to teach user how to play the game
    {
        private JButton back;//button to go back to main menu
        public HowTo()
        {
            setLayout(null);
            setBackground(Color.WHITE);
            back = new JButton("<- Back");
            back.addActionListener(this);
            back.setBounds(10, 10, 100, 50);
            add(back);
        }
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Font font = new Font ("Serif", Font.BOLD, 60);
            g.setFont( font );
            g.drawString("How To Play", 300, 100);
            Font vij = new Font ("Serif", Font.BOLD, 15);
            g.setFont(vij);
            g.drawString("This game is a 1-player bird's eye view capture the flag game with a twist. There is a trinomial that will pop up on the bottom of the", 1, 320);
            g.drawString("playing field. You have a timer for one minute and must get the correct treasure chest with the answer before the timer is up. There are 4 treasure", 1, 340);
            g.drawString("chests with different answers on them, but only one has the correct answer. If you get the right answer, you get to move on to the", 1, 360);
            g.drawString("next question with another minute, and your score goes up 100. If the answer is incorrect, that treasure chest will disappear, but", 1, 380);
            g.drawString("you still have to get the correct answer. You want to get as high of a score as possible, but it progressively gets harder. GOOD LUCK!", 1, 400);
        }//Instructions to teach the user how to play our game incomplete for now but we will finish it after we finish the game
        public void actionPerformed(ActionEvent evt)
        {
            String command = evt.getActionCommand();
            if(command.equals("<- Back"))
            {
                cards.show(ctfp, "Main");
            }
        }
    }
    public class GameOver extends JPanel implements ActionListener//window when the game is over
    {
        private JButton home;//button to go back to main menu
        private JButton replay;
        public GameOver()
        {
            setLayout(null);
            setBackground(Color.BLACK);
            home = new JButton("<- Home");
            home.addActionListener(this);
            home.setBounds(10, 10, 100, 50);
            add(home);
            
            replay = new JButton("Play Again");
            replay.addActionListener(this);
            replay.setBounds(200, 800, 600, 100);
            add(replay);
        }
        public void paintComponent(Graphics g)
        {
            int highNum=0;
            super.paintComponent(g);
            Font big = new Font ("Serif", Font.BOLD, 100);
            g.setFont(big);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 150, 400);
            g.drawString("Score: " + score, 200, 500);//showing user final score
            highNum=Integer.parseInt(highscore);
            if(score>highNum)
            {
                writeIt();
                highscore=""+score;
                writeHigh();
            }
            getWords();
            g.drawString("Highscore: "+highscore, 200, 600);
            input.close();
        }
        public void actionPerformed(ActionEvent evt)
        {
            String command = evt.getActionCommand();
            if(command.equals("<- Home")) //Takes the player home if he is done playing
            {
                cards.show(ctfp, "Main");
                score=0;
                reset();
            }
            if(command.equals("Play Again")) //Lets the player go back to the game panel to restart
            {
                cards.show(ctfp, "Game");
                score=0;
                reset();
                timer.start();
            }
        }
    }
}