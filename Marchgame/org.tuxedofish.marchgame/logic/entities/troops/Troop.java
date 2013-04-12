package logic.entities.troops;

import java.nio.IntBuffer;

import logic.entities.Enemy;

import shader.ShaderHandler;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public interface Troop{
	public Enemy getEnemy();
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util);
	public void shoot(DisplaySetup d);
}
