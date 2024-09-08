import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Game
{
    private Grid grid;
    private int userRow;
    private int userCol;
    private int blockerRow;
    private int blockerTwoRow;
    private int blockerThreeRow;
    private int blockerFourRow;
    private int blockerFiveRow;
    private int msElapsed;
    private int timesGet;
    private int timesAvoid;
    private boolean isDown = true;
    private boolean isDownTwo = true;
    private boolean isDownThree = true;
    private boolean isDownFour = true;
    private boolean isDownFive = true;
    private boolean isClap = true;

    public Game()
    {
        grid = new Grid(20, 30);
        grid.load("load.png");
        updateTitle();
        sound("bgmusic.wav");
        userRow = 10;
        userCol = 10;
        msElapsed = 0;
        timesGet = 0;
        timesAvoid = 0;
        blockerRow = 11;
        blockerTwoRow = 9;
        blockerThreeRow = 7;
        blockerFourRow = 16;
        blockerFiveRow = 9;
        grid.setImage(new Location(blockerRow, 26), "goalie.png");
        grid.setImage(new Location(blockerTwoRow, 24), "blocker.png");
        grid.setImage(new Location(blockerThreeRow, 22), "blocker.png");
        grid.setImage(new Location(blockerFourRow, 20), "blocker.png");
        grid.setImage(new Location(blockerFiveRow, 18), "blocker.png");
        grid.setImage(new Location(userRow, userCol), "soccer.gif");
    }

    public void play()
    {
        while (!isGameOver())
        {
            grid.pause(100);
            handleKeyPress();
            if(timesGet >= 9){
                if (msElapsed % 100 == 0)
                {
                    scrollLeft();
                    map();
                    if(isClap){
                        audience(true);
                        isClap = false;
                    } else {
                        audience(false);
                        isClap = true;
                    }

                }
            } else if(timesGet >= 5){
                if (msElapsed % 300 == 0)
                {
                    scrollLeft();
                    map();
                    if(isClap){
                        audience(true);
                        isClap = false;
                    } else {
                        audience(false);
                        isClap = true;
                    }

                }
            } else {
                if (msElapsed % 600 == 0)
                {
                    scrollLeft();
                    map();
                    if(isClap){
                        audience(true);
                        isClap = false;
                    } else {
                        audience(false);
                        isClap = true;
                    }

                }
            }

            updateTitle();
            msElapsed += 100;
        }
    }

    public void handleKeyPress()
    {
        int key = grid.checkLastKeyPressed();

        if(key == 38 && userRow > 0 && grid.getImage(new Location(userRow -1, userCol)) != "wall.png"){
            userRow--;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow + 1,userCol), null);
            grid.setImage(new Location(userRow,userCol), "soccer.gif"); 

        } else if(key == 40 && userRow < grid.getNumRows() - 1 && grid.getImage(new Location(userRow + 1, userCol)) != "wall.png"){
            userRow++;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow - 1,userCol), null);
            grid.setImage(new Location(userRow,userCol), "soccer.gif"); 
        } else if(key == 39 && userCol < grid.getNumCols() - 16 && grid.getImage(new Location(userRow , userCol +1)) != "wall.png"){
            userCol++;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow,userCol - 1), null);
            grid.setImage(new Location(userRow,userCol), "soccer.gif"); 
        } else if(key == 37 && userCol > 0 && userCol < grid.getNumCols() - 1 && grid.getImage(new Location(userRow , userCol - 1)) != "wall.png"){
            userCol--;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow,userCol + 1), null);
            grid.setImage(new Location(userRow,userCol), "soccer2.gif"); 
        } else if(key == 32){
            shoot();
        }

    }

    public void audience(boolean x){
        boolean isCheering = x;
        if(isCheering){

            for(int k = 0; k < 3; k+=2){
                for(int i = 0; i < grid.getNumCols(); i++){
                    if( i > 3 && i < grid.getNumCols() - 4 && i % 2 == 0){
                        grid.setImage(new Location(0, i), "audience1.png"); 
                    } else if( i > 3 && i < grid.getNumCols() - 4 && i % 2 != 0){
                        grid.setImage(new Location(0, i), "audience2.png"); 
                    }
                    if( i > 1 && i < grid.getNumCols() - 2 && i % 2 != 0){
                        grid.setImage(new Location(2, i), "audience1.png"); 
                    } else if( i > 1 && i < grid.getNumCols() - 2 && i % 2 == 0){
                        grid.setImage(new Location(2, i), "audience2.png"); 
                    }
                }
            }

        }
        if(!isCheering){
            for(int k = 0; k < 3; k+=2){
                for(int i = 0; i < grid.getNumCols(); i++){
                    if( i > 3 && i < grid.getNumCols() - 4 && i % 2 == 0){
                        grid.setImage(new Location(0, i), "audience1-1.png"); 
                    } else if( i > 3 && i < grid.getNumCols() - 4 && i % 2 != 0){
                        grid.setImage(new Location(0, i), "audience2-1.png"); 
                    }
                    if( i > 1 && i < grid.getNumCols() - 2 && i % 2 != 0){
                        grid.setImage(new Location(2, i), "audience1-1.png"); 
                    } else if( i > 1 && i < grid.getNumCols() - 2 && i % 2 == 0){
                        grid.setImage(new Location(2, i), "audience2-1.png"); 
                    }
                }
            }
        }
    }

    public void shoot(){
        boolean isFinished = false;
        int elapsedTime = 0;
        int shootRange = grid.getNumCols() - userCol;
        int ballPosition = userCol;
        sound("kick.wav");

        while(!isFinished){
            grid.pause(100);
            grid.setImage(new Location(userRow,userCol), "kick.png");
            for(int k = 0; k < 3; k+=2){
                for(int i = 0; i < grid.getNumCols(); i++){
                    if( i > 3 && i < grid.getNumCols() - 4 && i % 2 == 0){
                        grid.setImage(new Location(0, i), "audience1-2.png"); 
                    } else if( i > 3 && i < grid.getNumCols() - 4 && i % 2 != 0){
                        grid.setImage(new Location(0, i), "audience2-2.png"); 
                    }
                    if( i > 1 && i < grid.getNumCols() - 2 && i % 2 != 0){
                        grid.setImage(new Location(2, i), "audience1-2.png"); 
                    } else if( i > 1 && i < grid.getNumCols() - 2 && i % 2 == 0){
                        grid.setImage(new Location(2, i), "audience2-2.png"); 
                    }
                }
            }

            if((shootRange * 100) == elapsedTime){
                isFinished = true;
                if(userRow > 8 && userRow < 16){
                    timesGet++;
                    grid.setImage(new Location(userRow,userCol), "score.png");
                }
            } else {
                ballPosition++;
                if(blockerRow == userRow && ballPosition == 25){
                    isFinished = true; 
                    timesAvoid++;
                    grid.setImage(new Location(userRow,26), "jake.png");
                    grid.setImage(new Location(userRow,userCol), "cry.png");
                }
                if(blockerTwoRow == userRow && ballPosition == 23){
                    isFinished = true; 
                    timesAvoid++;
                    grid.setImage(new Location(userRow,24), "jake.png");
                    grid.setImage(new Location(userRow,userCol), "cry.png");
                }
                if(blockerThreeRow == userRow && ballPosition == 21){
                    isFinished = true; 
                    timesAvoid++;
                    grid.setImage(new Location(userRow,22), "jake.png");
                    grid.setImage(new Location(userRow,userCol), "cry.png");
                }
                if(blockerFourRow == userRow && ballPosition == 19){
                    isFinished = true; 
                    timesAvoid++;
                    grid.setImage(new Location(userRow,20), "jake.png");
                    grid.setImage(new Location(userRow,userCol), "cry.png");
                }
                if(blockerFiveRow == userRow && ballPosition == 17){
                    isFinished = true; 
                    timesAvoid++;
                    grid.setImage(new Location(userRow,18), "jake.png");
                    grid.setImage(new Location(userRow,userCol), "cry.png");
                }
                if(ballPosition > userCol + 1){
                    grid.setImage(new Location(userRow,ballPosition - 1), "null.png");
                }
                if(ballPosition < grid.getNumCols() ){
                    if(timesGet >= 5){
                        grid.setImage(new Location(userRow,ballPosition), "ball2.gif");
                    } else {
                        grid.setImage(new Location(userRow,ballPosition), "ball1.gif");
                    }
                }

                elapsedTime += 100;
            }
        }
        grid.pause(1000);
        if(ballPosition < grid.getNumCols()){
            grid.setImage(new Location(userRow,ballPosition), "null.png");
        }

    }

    public void map(){
        for(int i = 0; i < grid.getNumRows(); i++){
            for(int j = 0; j <grid.getNumCols(); j++){
                //if(j == 0){
                //grid.setImage(new Location(i,j), "wall.jpg");
                //}
                //if(j == grid.getNumCols() -1 && j >5){
                //grid.setImage(new Location(i,j), "wall.jpg");
                //}
                if(i == 5){
                    grid.setImage(new Location(i,j), "wall.png");
                }
                if(i == grid.getNumRows() - 1){
                    grid.setImage(new Location(i,j), "wall.png");
                }
            }

        }
        if(timesGet >= 5){
            grid.bg("bg3.png");
        } else{
            grid.bg("bg.png");
        }
    }

    public void scrollLeft()
    {    
        if(isDown){
            blockerRow++;
            grid.setImage(new Location(blockerRow - 1,26), "null.png");
            grid.setImage(new Location(blockerRow,26), "goalie.png");
            if(blockerRow == 16){
                isDown = false;
            }
        } else {
            blockerRow--;
            grid.setImage(new Location(blockerRow + 1,26), "null.png");
            grid.setImage(new Location(blockerRow,26), "goalie.png");
            if(blockerRow == 8){
                isDown = true;
            }
        }
        if(isDownTwo){
            blockerTwoRow++;
            grid.setImage(new Location(blockerTwoRow - 1,24), "null.png");
            grid.setImage(new Location(blockerTwoRow,24), "blocker.png");
            if(blockerTwoRow == 17){
                isDownTwo = false;
            }
        } else {
            blockerTwoRow--;
            grid.setImage(new Location(blockerTwoRow + 1,24), "null.png");
            grid.setImage(new Location(blockerTwoRow,24), "blocker.png");
            if(blockerTwoRow == 7){
                isDownTwo = true;
            }
        }
        if(isDownThree){
            blockerThreeRow++;
            grid.setImage(new Location(blockerThreeRow - 1,22), "null.png");
            grid.setImage(new Location(blockerThreeRow,22), "blocker.png");
            if(blockerThreeRow == 18){
                isDownThree = false;
            }
        } else {
            blockerThreeRow--;
            grid.setImage(new Location(blockerThreeRow + 1,22), "null.png");
            grid.setImage(new Location(blockerThreeRow,22), "blocker.png");
            if(blockerThreeRow == 6){
                isDownThree = true;
            }
        }
        if(isDownFour){
            blockerFourRow++;
            grid.setImage(new Location(blockerFourRow - 1,20), "null.png");
            grid.setImage(new Location(blockerFourRow,20), "blocker.png");
            if(blockerFourRow == 17){
                isDownFour = false;
            }
        } else {
            blockerFourRow--;
            grid.setImage(new Location(blockerFourRow + 1,20), "null.png");
            grid.setImage(new Location(blockerFourRow,20), "blocker.png");
            if(blockerFourRow == 7){
                isDownFour = true;
            }
        }
        if(isDownFive){
            blockerFiveRow++;
            grid.setImage(new Location(blockerFiveRow - 1,18), "null.png");
            grid.setImage(new Location(blockerFiveRow,18), "blocker.png");
            if(blockerFiveRow == 14){
                isDownFive = false;
            }
        } else {
            blockerFiveRow--;
            grid.setImage(new Location(blockerFiveRow + 1,18), "null.png");
            grid.setImage(new Location(blockerFiveRow,18), "blocker.png");
            if(blockerFiveRow == 8){
                isDownFive = true;
            }
        }
        //if(blockerRow == userRow && userCol == 26){
        //   userCol = 10;
        //    userRow = 10;
        //    timesAvoid++;
        //    grid.setImage(new Location(userRow,userCol), "soccer.gif");
        //}

    }

    public void handleCollision(Location loc)
    {
        String toCollide = grid.getImage(loc);
        if(toCollide == "jake.png"){
            timesGet++;
        } else if(toCollide == "devil.png"){
            timesAvoid++;
        } 
    }

    public int getScore()
    {
        return timesGet;
    }

    public void updateTitle()
    {
        grid.setTitle("Game:  " + getScore());
    }


    public boolean isGameOver()
    {
        if(timesAvoid == 3){
            grid.showMessageDialog("GAME OVER");  
            return true;
        } else if(timesGet == 10){
            grid.showMessageDialog("YOU WON THE WORLD CUP 2077!!");  
            return true;
        } 
        return false;
    }

    public void sound(String sound) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(sound));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void test()
    {
        Game game = new Game();
        game.play();
    }

    public static void main(String[] args)
    {

        test();
    }
}