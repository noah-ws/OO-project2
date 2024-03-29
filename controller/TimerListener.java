package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import model.Bullet;
import model.Shooter;
import view.GameBoard;

public class TimerListener implements ActionListener {

	public enum EventType {
		KEY_RIGHT, KEY_LEFT, KEY_SPACE
	}

	private GameBoard gameBoard;
	private LinkedList<EventType> eventQueue; // event queue
	private final int BOMB_DROP_FREQ = 20;
	private int frameCounter = 0;

	public TimerListener(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		eventQueue = new LinkedList<>();
	}

	// our game loop
	@Override
	public void actionPerformed(ActionEvent e) {
		frameCounter++;
		update();
		queuePlayerMovement();
		processEventQueue();
		processCollision();
		gameBoard.getCanvas().repaint();

		if (GameBoard.isGameOver)
			gameBoard.gameOver();
	}

	private void queuePlayerMovement() {
		if (PressedKeys.isLeftPressed)
			eventQueue.add(TimerListener.EventType.KEY_LEFT);
		else if (PressedKeys.isRightPressed)
			eventQueue.add(TimerListener.EventType.KEY_RIGHT);
	}

	private void processEventQueue() {
		while (!eventQueue.isEmpty()) {
			var e = eventQueue.getFirst();
			eventQueue.removeFirst();

			Shooter shooter = gameBoard.getShooter();
			if (shooter == null)
				return;
			
			
			switch (e) {
				case KEY_LEFT:
					shooter.moveLeft();
					break;
				case KEY_RIGHT:
					shooter.moveRight();
					break;
				case KEY_SPACE:
					// fires from main shooter
					if (shooter.canFireMoreBullets())
						shooter.getWeapons().add(new Bullet(shooter.x, shooter.y));

					// fires from extra shooter
					Shooter extra = shooter.getExtraShooter();
					if (extra != null) {
						if (extra.canFireMoreBullets())
							extra.getWeapons().add(new Bullet(extra.x, extra.y));
					}
					break;
			}
			
		}

		if (frameCounter == BOMB_DROP_FREQ) {
			gameBoard.getEnemyComposite().dropBombs();
			frameCounter = 0;
		}
	}

	private void processCollision() {
		var shooter = gameBoard.getShooter();
		var enemyComposite = gameBoard.getEnemyComposite();
		
		shooter.removeBulletsOutOfBound();
		if (shooter.getExtraShooter() != null)
			shooter.getExtraShooter().removeBulletsOutOfBound();
		enemyComposite.removeBombsOutOfBound();
		enemyComposite.processCollision(shooter);
	}

	private void update() {
		var shooter = gameBoard.getShooter();
		var enemyComposite = gameBoard.getEnemyComposite();

		shooter.checkPlayerComponents();
		
		// if changing level, no enemies, so don't check
		if (!GameBoard.changingLevel)
			enemyComposite.checkAllEnemiesKilled();

		for (var e: gameBoard.getCanvas().getGameElements())
			e.animate();
	}

	public LinkedList<EventType> getEventQueue() {
		return eventQueue;
	}
	
}
