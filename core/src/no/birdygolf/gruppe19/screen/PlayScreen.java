package no.birdygolf.gruppe19.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import no.birdygolf.gruppe19.BirdyGolf;
import no.birdygolf.gruppe19.GameManager;
import no.birdygolf.gruppe19.factory.WorldFactory;
import no.birdygolf.gruppe19.levels.Level;
import no.birdygolf.gruppe19.systems.BoundsSystem;
import no.birdygolf.gruppe19.systems.LevelSystem;
import no.birdygolf.gruppe19.systems.RenderingSystem;

public class PlayScreen extends ScreenAdapter {

    BirdyGolf game;
    WorldFactory world;
    PooledEngine engine;
    FitViewport viewport;
    Stage stage;

    private Table layout;
    private TextureRegion sound, mute;
    private TextureRegionDrawable soundDrawable, muteDrawable;
    private ImageButton soundButton, muteButton;
    private boolean muted = false;
    private Music music;

    private float elapsedTime = 0;

    public PlayScreen(BirdyGolf game) {
        this.game = game;
        this.engine = new PooledEngine();
        this.world = new WorldFactory(engine);

        //engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderingSystem(game.batch));
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new LevelSystem(world));

        engine.getSystem(RenderingSystem.class).setProcessing(true);
        engine.getSystem(BoundsSystem.class).setProcessing(true);

        engine.getSystem(LevelSystem.class).initializeLevel(Level.LEVEL_1);
    }

    private void nextLevel() {
        int currentLevel = ++GameManager.INSTANCE.currentLevel;
        if (currentLevel == Level.values().length) {
            game.setScreen(HighScoreScreen.getInstance(game));
            music.stop();
            return;
        }
        engine.getSystem(LevelSystem.class).initializeLevel(Level.values()[currentLevel]);
    }

    private void createUi() {
        viewport = new FitViewport(480, 800, game.camera);
        stage = new Stage(viewport);

        music = Gdx.audio.newMusic(Gdx.files.internal("music/game_music.mp3"));
        music.play();
        music.setLooping(true);

        sound = new TextureRegion(new Texture("music/sound.png"));
        mute = new TextureRegion(new Texture("music/mute.png"));
        soundDrawable = new TextureRegionDrawable(sound);
        muteDrawable = new TextureRegionDrawable(mute);
        soundButton = new ImageButton(soundDrawable);
        muteButton = new ImageButton(muteDrawable);

        layout = new Table();
        layout.add(muteButton).width(50).padLeft(50);
        layout.add(soundButton).width(45);

        //initializing the buttons
        if (!muted) {
            soundButton.setVisible(false);
        }
        else {
            muteButton.setVisible(false);
        }

        stage.addActor(layout);
        layout.setPosition(
                viewport.getWorldWidth() / 10,
                viewport.getWorldHeight() - 50
        );
    }

    @Override
    public void show() {
        createUi();
        Gdx.input.setInputProcessor(stage);

        soundButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                muted = false;
                music.play();
                music.setLooping(true);
                soundButton.setVisible(false);
                muteButton.setVisible(true);

                return false;
            }
        });

        muteButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                muted = true;
                music.stop();
                soundButton.setVisible(true);
                muteButton.setVisible(false);

                return false;
            }
        });

    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        if (elapsedTime > 5) {
            elapsedTime = 0;
            nextLevel();
        }
        engine.update(delta);
        game.camera.update();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        music.dispose();
    }
}
