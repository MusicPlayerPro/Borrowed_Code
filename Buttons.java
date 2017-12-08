package gregcousin.sonictouch;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import static gregcousin.sonictouch.wham.VolumeStatew.Muted;
import static gregcousin.sonictouch.wham.VolumeStatew.UnMuted;

public class wham extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    MediaPlayer sound_aw, sound_bw, sound_cw, sound_dw, sound_ew, sound_fw;
    Button play, pause, aw, bw, cw, dw, ew, fw;
    enum VolumeStatew {Muted, UnMuted};

    wham.VolumeStatew volumeStatew = UnMuted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_wham );

        Intent wham = new Intent( this, wham.class );

        play=(Button) findViewById(R.id.play);
        pause=(Button) findViewById( R.id.pause );
        aw = (Button) findViewById( R.id.aw );
        bw = (Button) findViewById( R.id.bw );
        cw = (Button) findViewById( R.id.cw );
        dw = (Button) findViewById( R.id.dw );
        ew = (Button) findViewById( R.id.ew );
        fw = (Button) findViewById( R.id.fw );

        sound_aw = MediaPlayer.create( wham.this, R.raw.drumnbassw );
        sound_bw = MediaPlayer.create( wham.this, R.raw.vocalsw );
        sound_cw = MediaPlayer.create( wham.this, R.raw.organw );
        sound_dw = MediaPlayer.create( wham.this, R.raw.brassw );
        sound_ew = MediaPlayer.create( wham.this, R.raw.gtrw );
        sound_fw = MediaPlayer.create( wham.this, R.raw.magicw );



       // play
        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Thread firstth = new Thread(new Runnable()
                {
                    public void run()
                    {
                        Log.i( "SONG", "in run" );
                        play_tune();
                    }
                });
                firstth.start();
            }
        });

        // pause
        pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Thread seccon = new Thread(new Runnable()
                {
                    public void run()
                    {
                        Log.i( "SONG", "in run" );
                        pause_tune();
                    }
                });
                seccon.start();
            }
        });


        aw.setOnClickListener( new View.OnClickListener()
        {@Override public void onClick(View v)
        {if (volumeStatew == Muted)
        {sound_aw.setVolume(1.0f , 1.0f);
        volumeStatew = UnMuted;}
        else {sound_aw.setVolume(0, 0);
        volumeStatew = Muted;}}});

        bw.setOnClickListener( new View.OnClickListener()
        {@Override public void onClick(View v)
        {if (volumeStatew == Muted)
        {sound_bw.setVolume(1.0f , 1.0f);
            volumeStatew = UnMuted;}
        else {sound_bw.setVolume(0, 0);
            volumeStatew = Muted;}}});

        cw.setOnClickListener( new View.OnClickListener()
        {@Override public void onClick(View v)
            {if (volumeStatew == Muted)
            {sound_cw.setVolume(1.0f , 1.0f);
                volumeStatew = UnMuted;}
            else {sound_cw.setVolume(0, 0);
                volumeStatew = Muted;}}});

        dw.setOnClickListener( new View.OnClickListener()
        {@Override public void onClick(View v)
        {if (volumeStatew == Muted)
        {sound_dw.setVolume(1.0f , 1.0f);
            volumeStatew = UnMuted;}
        else {sound_dw.setVolume(0, 0);
            volumeStatew = Muted;}}});

        ew.setOnClickListener( new View.OnClickListener()
        {@Override public void onClick(View v)
        {if (volumeStatew == Muted)
        {sound_ew.setVolume(1.0f , 1.0f);
            volumeStatew = UnMuted;}
        else {sound_ew.setVolume(0, 0);
            volumeStatew = Muted;}}});

        fw.setOnClickListener( new View.OnClickListener()
        {@Override public void onClick(View v)
        {if (volumeStatew == Muted)
        {sound_fw.setVolume(1.0f , 1.0f);
            volumeStatew = UnMuted;}
        else {sound_fw.setVolume(0, 0);
            volumeStatew = Muted;}}});


    }

    private void play_tune(){
        sound_aw.start();
        sound_bw.start();
        sound_cw.start();
        sound_dw.start();
        sound_ew.start();
        sound_fw.start();
    }

    private void pause_tune(){
        sound_aw.pause();
        sound_bw.pause();
        sound_cw.pause();
        sound_dw.pause();
        sound_ew.pause();
        sound_fw.pause();
    }
}