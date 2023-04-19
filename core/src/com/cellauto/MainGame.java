package com.cellauto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class MainGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private Vector3 mousePos;
	private ShapeRenderer shape;
	private SpriteBatch batch;
	private Map map;
	private Boolean mapGrid = false;
	private Boolean isPaused = false;

	private float newGenerationTimer = .3f;
	private float newGenerationTimerCount = 0f;

	@Override
	public void create () {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 0);
		camera.update();

		mousePos = new Vector3();
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		map = new Map(200, 100, 20);
	}

	@Override
	public void render () {
		// Обновление положения мыши
		mousePos.x = Gdx.input.getX();
		mousePos.y = Gdx.input.getY();
		mousePos.z = 0;
		camera.unproject(mousePos);

		// обновление поколения
		if (!isPaused) {
			newGenerationTimerCount += Gdx.graphics.getDeltaTime();
			if (newGenerationTimerCount > newGenerationTimer) {
				newGenerationTimerCount -= newGenerationTimer;
				map.update();
			}
		}

		// Обработка событий нажатия клавиш
		if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) mapGrid = !mapGrid;
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) isPaused = !isPaused;

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) setNewGenerationTimer(MathUtils.clamp(newGenerationTimer - 0.1f, 0.1f, 1f));
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) setNewGenerationTimer(MathUtils.clamp(newGenerationTimer + 0.1f, 0.1f, 1f));

		if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.translate(0, -10, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.translate(-10, 0, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.translate(0, 10, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.translate(10, 0, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.zoom = MathUtils.clamp(camera.zoom - .02f, 0.3f, 1.5f);
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.zoom = MathUtils.clamp(camera.zoom + .02f, 0.3f, 1.5f);

		// создание\уничтожение жизни
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			if ((mousePos.x > 0 && mousePos.x < 4000) && (mousePos.y > 0 && mousePos.y < 2000)) {
				map.setCell((int) mousePos.y / map.getCellSize(), (int) mousePos.x / map.getCellSize(), true);
			}
		}
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			if ((mousePos.x > 0 && mousePos.x < 4000) && (mousePos.y > 0 && mousePos.y < 2000)) {
				map.setCell((int) mousePos.y / map.getCellSize(), (int) mousePos.x / map.getCellSize(), false);
			}
		}

		// Очистка экрана
		Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		camera.update();
		shape.setProjectionMatrix(camera.combined);

		shape.begin(ShapeRenderer.ShapeType.Filled);
		// Выделение поля для рисования
		shape.setColor(.25f, .25f, .25f, 1);
		shape.rect(0, 0, (float) map.getWidthSize(), (float) map.getHeightSize());

		// Рисование клеток
		shape.setColor(.8f, .5f, .5f, 1);
		for (int line = 0; line < map.getPresentMap().length; line++) {
			for (int cell = 0; cell < map.getPresentMap()[line].length; cell++) {
				if (map.getPresentMap()[line][cell].getLife()) shape.rect(cell * map.getCellSize(), line * map.getCellSize(), map.getCellSize(), map.getCellSize());
			}
		}

		// Рисование курсора
		if (isPaused) {
			shape.setColor(.8f, .8f, .8f, .7f);
		} else {
			shape.setColor(.8f, .8f, .8f, .1f);
		}
		if ((mousePos.x > 0 && mousePos.x < map.getWidthSize()) && (mousePos.y > 0 && mousePos.y < map.getHeightSize())) {
			shape.rect((float) ((int) mousePos.x / map.getCellSize()) * map.getCellSize(), (float) ((int) mousePos.y / map.getCellSize()) * map.getCellSize(), map.getCellSize(), map.getCellSize());
		}

		// Рисование сетки
		shape.setColor(.5f, .5f, .5f, .5f);
		if (mapGrid) {
			for (int lineX = 0; lineX < map.getWidthSize(); lineX = lineX + 20) {
				shape.rectLine(lineX, 0, lineX, (float) map.getHeightSize(), 1.5f);
			}
			for (int lineY = 0; lineY < map.getHeightSize(); lineY = lineY + 20) {
				shape.rectLine(0, lineY, (float) map.getWidthSize(), lineY, 1.5f);
			}
		}
		shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void setNewGenerationTimer(float time) {
		newGenerationTimer = time;
	}

	@Override
	public void dispose () {
		batch.dispose();
		shape.dispose();
	}
}
