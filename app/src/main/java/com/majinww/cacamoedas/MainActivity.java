package com.majinww.cacamoedas;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.content.*;
import java.util.*;

public class MainActivity extends Activity {
    Game game;
    @Override public void onCreate(Bundle b) { super.onCreate(b); game = new Game(this); setContentView(game); }
    @Override protected void onPause() { super.onPause(); game.pause(); }
    @Override protected void onResume() { super.onResume(); if(game != null) game.last = 0; }
    static class Drop { float x,y; boolean bomb; Drop(float x,boolean bomb){this.x=x;this.y=95;this.bomb=bomb;} }
    static class Game extends View {
        final Paint p = new Paint(3);
        final Random rng = new Random();
        final ArrayList<Drop> drops = new ArrayList<>();
        final SharedPreferences prefs;
        int score=0, lives=3, best;
        float player=180, timer=0, height=640;
        long last=0;
        boolean started=false, paused=false, ended=false;
        Game(Context c){super(c); prefs=c.getSharedPreferences("recorde",0); best=prefs.getInt("best",0); setContentDescription("Caça Moedas: arraste para pegar moedas e desviar de bombas");}
        void pause(){if(started&&!ended)paused=true;last=0;invalidate();}
        void start(){score=0;lives=3;timer=0;player=180;drops.clear();started=true;ended=false;paused=false;last=0;invalidate();}
        void text(Canvas c,String s,float x,float y,float size,int color){p.setColor(color);p.setTextSize(size);p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));c.drawText(s,x,y,p);}
        void center(Canvas c,String s,float y,float size,int color){p.setTextSize(size);p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));text(c,s,180-p.measureText(s)/2,y,size,color);}
        @Override protected void onDraw(Canvas c){
            super.onDraw(c); if(getWidth()==0)return;
            float scale=getWidth()/360f; height=getHeight()/scale;
            c.save();c.scale(scale,scale);
            c.drawColor(Color.rgb(12,17,38));
            p.setColor(Color.rgb(27,35,65));
            for(int i=0;i<30;i++){float x=(i*73)%360,y=(i*127)%((int)height);c.drawCircle(x,y,2,p);}
            long now=System.nanoTime();float dt=last==0?0:Math.min((now-last)/1e9f,.05f);last=now;
            if(started&&!paused&&!ended){
                timer+=dt;float interval=Math.max(.24f,.8f-score*.004f);
                if(timer>=interval){timer=0;drops.add(new Drop(22+rng.nextFloat()*316,rng.nextFloat()<.23f));}
                Iterator<Drop> it=drops.iterator();
                while(it.hasNext()){
                    Drop d=it.next();d.y+=(140+Math.min(score*2,260))*dt;
                    if(Math.abs(d.x-player)<41&&Math.abs(d.y-(height-65))<26){
                        it.remove();if(d.bomb){lives--;}else{score+=10; if(score>best){best=score;prefs.edit().putInt("best",best).apply();}}
                        if(lives<=0){ended=true;break;}
                    }else if(d.y>height+25)it.remove();
                }
            }
            for(Drop d:drops){
                p.setColor(d.bomb?Color.rgb(246,85,112):Color.rgb(255,199,57));c.drawCircle(d.x,d.y,15,p);
                if(d.bomb){p.setColor(Color.WHITE);p.setStrokeWidth(3);c.drawLine(d.x-5,d.y-5,d.x+5,d.y+5,p);c.drawLine(d.x+5,d.y-5,d.x-5,d.y+5,p);}
                else{text(c,"$",d.x-6,d.y+6,18,Color.rgb(131,83,4));}
            }
            p.setColor(Color.rgb(64,222,189));c.drawRoundRect(player-29,height-79,player+29,height-51,9,9,p);
            text(c,"MOEDAS "+score,16,32,18,Color.WHITE);
            text(c,"VIDAS "+lives,16,58,14,Color.rgb(246,120,147));
            text(c,"II",320,38,24,Color.WHITE);
            center(c,"RECORDE "+best,height-15,13,Color.rgb(152,169,202));
            if(!started||paused||ended){
                p.setColor(Color.argb(230,12,17,38));c.drawRect(0,75,360,height-35,p);
                center(c,ended?"FIM DE JOGO":paused?"PAUSADO":"CAÇA MOEDAS",height*.36f,28,Color.rgb(255,199,57));
                center(c,ended?"Você fez "+score+" pontos":"Pegue moedas. Desvie das bombas.",height*.36f+45,16,Color.WHITE);
                center(c,"Arraste o dedo para mover",height*.36f+74,15,Color.rgb(152,169,202));
                p.setColor(Color.rgb(64,222,189));c.drawRoundRect(55,height*.58f,305,height*.58f+55,14,14,p);
                center(c,paused?"CONTINUAR":ended?"JOGAR DE NOVO":"JOGAR",height*.58f+35,18,Color.rgb(12,17,38));
            }
            c.restore();if(started&&!paused&&!ended)postInvalidateOnAnimation();
        }
        @Override public boolean onTouchEvent(MotionEvent e){
            float scale=getWidth()/360f,x=e.getX()/scale,y=e.getY()/scale;
            if(e.getAction()==MotionEvent.ACTION_DOWN){
                if(!started||ended||paused){if(x>=55&&x<=305&&y>=height*.58f&&y<=height*.58f+55){if(paused){paused=false;last=0;invalidate();}else start();}return true;}
                if(x>295&&y<65){pause();return true;}
            }
            if(started&&!paused&&!ended&&(e.getAction()==MotionEvent.ACTION_DOWN||e.getAction()==MotionEvent.ACTION_MOVE)){player=Math.max(30,Math.min(330,x));invalidate();}
            return true;
        }
    }
}
