package com.manoj.holdit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class holdit extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] player;

	Circle playerCircle;
	//ShapeRenderer shapeRenderer;

    BitmapFont font;

	Rectangle[] topHurdleRectangle;
	Rectangle[] bottomHurdleRectangle;

	Texture topHurdle;
	Texture bottomHurdle;
	Texture gameOver;
	Texture startImg;


	int playerState=0;
	int numberOfStates=3;
	int score=0;
	int activeHurdle=0;


	float playerY=0;
	float velocity=0;
	int gameState=0;
	float gravity= (float) 1;

	float pathWay=260;
	float maxHurdleDeviation;
	float hurdleVelocity=10;
	int numberOfHurdles=5;
    float[] hurdleX=new float[numberOfHurdles];
    float[] hurdleDeviation=new float[numberOfHurdles];
	float distanceBwHurdles;


	Random random;


	
	@Override
	public void create () {

		batch = new SpriteBatch();
        background=new Texture("testbg.jpg");
        playerCircle=new Circle();
       // shapeRenderer=new ShapeRenderer();
        gameOver=new Texture("gameover.png");
        startImg=new Texture("start.png");
        topHurdleRectangle=new Rectangle[numberOfHurdles];
        bottomHurdleRectangle=new Rectangle[numberOfHurdles];
        player=new Texture[numberOfStates];
        player[0]=new Texture("bird.png");
        player[1]=new Texture("bird2.png");
        player[2]=new Texture("bird.png");


        font=new BitmapFont();
        font.setColor(Color.GOLDENROD);
        font.getData().setScale(3);

        topHurdle=new Texture("toptube.png");
        bottomHurdle=new Texture("bottomtube.png");
        maxHurdleDeviation=Gdx.graphics.getHeight()/2-pathWay/2+100;
        distanceBwHurdles=Gdx.graphics.getWidth();

        random=new Random();
        newGame();

	}

	public void newGame(){


        playerY= Gdx.graphics.getHeight()/2-player[0].getHeight()/2;
        for(int i=0;i<numberOfHurdles;i++){

            hurdleDeviation[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-pathWay-200);
            hurdleX[i]=Gdx.graphics.getWidth()/2-topHurdle.getWidth()/2+ i*distanceBwHurdles+Gdx.graphics.getWidth()/2;

            topHurdleRectangle[i]=new Rectangle();
            bottomHurdleRectangle[i]=new Rectangle();


        }

    }


	@Override
	public void render () {



	    batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        if(score>=10000)
        {
            pathWay=400;
            hurdleVelocity=1000;
        }




        if(score>=300) {
            hurdleVelocity = 13;

        }
        if(score>600) {
            hurdleVelocity = 16;


        }
        if(score>900) {
            hurdleVelocity = (float)18.5;


        }
        if(score>1200){
            hurdleVelocity=20;
        }

        if(score>1700) {
            hurdleVelocity = (float) 23;
        }




            if(gameState==1) {


            if(hurdleX[activeHurdle]<75)//Gdx.graphics.getWidth()/2)
            {
                score+=100;



                if(activeHurdle<numberOfHurdles-1)
                    activeHurdle++;
                else
                    activeHurdle=0;
            }




            if(Gdx.input.isTouched())
            {
                velocity=(float)-7;


            }

            for(int i=0;i<numberOfHurdles;i++) {

                if(hurdleX[i]<-topHurdle.getWidth()) {
                    hurdleDeviation[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-pathWay-200);
                    hurdleX[i] = hurdleX[i] + numberOfHurdles * distanceBwHurdles;
                }
                else {
                    hurdleX[i] = hurdleX[i] - hurdleVelocity;

                }

                batch.draw(topHurdle, hurdleX[i], Gdx.graphics.getHeight() / 2 + pathWay / 2 + hurdleDeviation[i]);
                batch.draw(bottomHurdle, hurdleX[i], Gdx.graphics.getHeight() / 2 - pathWay / 2 - bottomHurdle.getHeight() + hurdleDeviation[i]);

                topHurdleRectangle[i]=new Rectangle(hurdleX[i],Gdx.graphics.getHeight() / 2 + pathWay / 2 + hurdleDeviation[i],topHurdle.getWidth(),topHurdle.getHeight());
                bottomHurdleRectangle[i]=new Rectangle(hurdleX[i],Gdx.graphics.getHeight() / 2 - pathWay / 2 - bottomHurdle.getHeight() + hurdleDeviation[i],bottomHurdle.getWidth(),bottomHurdle.getHeight());

            }

            if(playerY>0) {

                velocity = velocity + gravity;
                playerY = playerY - velocity;
            }
            else {

                gameState=2;

            }


        }
        else if(gameState==0){

            batch.draw(startImg,Gdx.graphics.getWidth()/2-startImg.getWidth()/2,Gdx.graphics.getHeight()/2-startImg.getHeight()/2);

	        if(Gdx.input.justTouched())
	            gameState=1;
        }
        else if(gameState==2) {

            batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
            if(Gdx.input.justTouched()) {
                hurdleVelocity=10;
                gameState = 1;
                score=0;
                velocity=0;
                activeHurdle=0;
                newGame();



            }


        }


		if(playerState==numberOfStates-1)
			playerState=0;

		playerState++;


       // batch.draw(player[playerState],Gdx.graphics.getWidth()/2-player[playerState].getWidth()/2,playerY);
        batch.draw(player[playerState],75,playerY);


        if(score<1000)
            font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()-100,Gdx.graphics.getHeight()-50);
        else if(score<10000)
            font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()-120,Gdx.graphics.getHeight()-50);
        else if(score>10000)
            font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()-150,Gdx.graphics.getHeight()-50);


        batch.end();


        //playerCircle.set(Gdx.graphics.getWidth()/2,playerY+player[playerState].getHeight()/2,player[playerState].getWidth()/2-20);
        playerCircle.set(75+player[playerState].getWidth()/2,playerY+player[playerState].getHeight()/2,player[playerState].getWidth()/2-20);


		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);*/
		for(int i=0;i<numberOfHurdles;i++){
		    /*shapeRenderer.rect(hurdleX[i],Gdx.graphics.getHeight() / 2 + pathWay / 2 + hurdleDeviation[i],topHurdle.getWidth(),topHurdle.getHeight());
		    shapeRenderer.rect(hurdleX[i],Gdx.graphics.getHeight() / 2 - pathWay / 2 - bottomHurdle.getHeight() + hurdleDeviation[i],bottomHurdle.getWidth(),bottomHurdle.getHeight());*/

		    if(Intersector.overlaps(playerCircle,topHurdleRectangle[i])||Intersector.overlaps(playerCircle,bottomHurdleRectangle[i])){

		        gameState=2;

            }

        }


		/*shapeRenderer.circle(playerCircle.x,playerCircle.y,playerCircle.radius);
		shapeRenderer.end();*/






	}

}
