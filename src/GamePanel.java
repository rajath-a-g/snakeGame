import sun.awt.windows.ThemeReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private static final long serialVersionUID = 1L;
    public static final int HEIGHT = 500, WIDTH = 500;
    private Thread thread;
    private boolean running;
    private boolean right = true, left = false, up = false, down = false;
    private BodyPart b;
    private ArrayList<BodyPart> snake;
    private int xCoor = 10, yCoor = 10, size = 15;
    private int ticks = 0;
     private Apple apple;
     private ArrayList<Apple> apples;
     private Random r;
     private  int score = -1;
    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        snake = new ArrayList<BodyPart>();
        apples = new ArrayList<Apple>();
        r = new Random();
        start();
    }

    public void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        if(snake.size() == 0) {
            b = new BodyPart(xCoor, yCoor, 10);
            snake.add(b);
        }
        ticks++;
        if(ticks > 250000) {
            if(right) xCoor++;
            if(left) xCoor--;
            if(up) yCoor--;
            if(down) yCoor++;

            ticks = 0;
            b = new BodyPart(xCoor, yCoor, 10);
            snake.add(b);
            if(snake.size() > size) {
                snake.remove(0);
            }
        }
        if (apples.size() == 0) {
            int xCoor = r.nextInt(49);
            int yCoor = r.nextInt(49);
            apple = new Apple(xCoor, yCoor, 10);
            apples.add(apple);
            score++;
        }
        for (int i = 0; i < apples.size(); i++) {
            if (xCoor == apples.get(i).getxCo_or() && yCoor == apples.get(i).getyCo_or()){
                size++;
                apples.remove(i);
                i++;
            }
        }
        //collision on snake body
        for(int i = 0; i < snake.size(); i++) {
            if(xCoor == snake.get(i).getxCo_or() && yCoor == snake.get(i).getyCo_or()) {
                if(i != snake.size()-1) {
                    System.out.println("Game Over");
                    stop();
                }
            }
        }
        //collision on border
        if(xCoor < 0 || xCoor > 49 || yCoor < 0 || yCoor > 49){
            System.out.println("Game Over");
            stop();
        }
    }
    public void paint(Graphics graphics) {
        graphics.clearRect(0, 0, WIDTH, HEIGHT);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        for (int i = 0; i < WIDTH/10; i++) {
            graphics.drawLine(i * 10, 0, i * 10, HEIGHT );
        }
        for (int i = 0; i < HEIGHT/10; i++) {
            graphics.drawLine(0, i*10, HEIGHT, i*10);
        }
        for (int i =0 ;i < snake.size(); i++ ) {
            snake.get(i).draw(graphics);
        }
        for (int i = 0; i < apples.size(); i++) {
            apples.get(i).draw(graphics);
        }
        graphics.drawString("Score :" + Integer.toString(score), 10, 10);
    }

    @Override
    public void run() {
        while (running) {
            tick();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT && !left) {
            right = true;
            up = false;
            down = false;
        }
        if (key == KeyEvent.VK_LEFT && !right) {
            left = true;
            up = false;
            down = false;
        }
        if (key == KeyEvent.VK_UP && !down) {
            right = false;
            up = true;
            left = false;
        }
        if (key == KeyEvent.VK_DOWN && !up) {
            right = false;
            down = true;
            left = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}