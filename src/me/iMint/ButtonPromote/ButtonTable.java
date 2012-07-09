package me.iMint.ButtonPromote;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "bp_button")
public class ButtonTable {
	@Id
	private int id;
	
	@NotEmpty
	private String world;
	
	@NotNull
	private double x;
	
	@NotNull
	private double y;
	
	@NotNull
	private double z;
	
	@NotEmpty
	private String message;
	
	@NotEmpty
	private String command;
	
	@NotEmpty
	private String groupName;
	
	@NotEmpty
	private String warpWorld;
	
	@NotNull
	private double warpX;
	
	@NotNull
	private double warpY;
	
	@NotNull
	private double warpZ;
	
	@NotNull
	private float warpYaw;

	@NotNull
	private float warpPitch;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getWarpWorld() {
		return warpWorld;
	}

	public void setWarpWorld(String warpWorld) {
		this.warpWorld = warpWorld;
	}

	public double getWarpX() {
		return warpX;
	}

	public void setWarpX(double warpX) {
		this.warpX = warpX;
	}

	public double getWarpY() {
		return warpY;
	}

	public void setWarpY(double warpY) {
		this.warpY = warpY;
	}

	public double getWarpZ() {
		return warpZ;
	}

	public void setWarpZ(double warpZ) {
		this.warpZ = warpZ;
	}

	public float getWarpYaw() {
		return warpYaw;
	}

	public void setWarpYaw(float warpYaw) {
		this.warpYaw = warpYaw;
	}

	public float getWarpPitch() {
		return warpPitch;
	}

	public void setWarpPitch(float warpPitch) {
		this.warpPitch = warpPitch;
	}
}
