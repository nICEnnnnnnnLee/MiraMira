package top.nicelee.mirai.miramira.app.gif.bean;

import java.awt.Color;
import java.awt.Font;

/**
 * 从第begin - end帧，添加文字content
 */
public class TextOption {

	public static Font defaultFont;
	public static Color defaultColor;
	public static Font[] fonts;
	public static Color[] colors;
	
	static {
		fonts = new Font[3];
		fonts[0] = new Font("", Font.BOLD, 12);
		fonts[1] = new Font("黑体", Font.BOLD, 24);
		fonts[2] = new Font("黑体", Font.BOLD, 18);
		colors = new Color[3];
		colors[0] = Color.white;
		colors[1] = Color.black;
		colors[2] = null;
		defaultFont = fonts[0];
		defaultColor = colors[0];
		//TextOption.setDefault(defaultFont, defaultColor);
	}
	
	public static void setDefault(Font defaultFont, Color defaultColor) {
		TextOption.defaultFont = defaultFont;
		TextOption.defaultColor = defaultColor;
	}

	int begin;
	int end;
	String content;
	int offsetX;
	int offsetY;
	Font font;
	Color color;
	Color colorShadow;
	
	
	public TextOption(int begin, int end, String content, int offsetX, int offsetY) {
		this(begin, end, content, offsetX, offsetY, defaultFont, defaultColor, null);
	}
	
	public TextOption(int begin, int end, String content, int offsetX, int offsetY, Font font, Color color, Color colorShadow) {
		this.begin = begin;
		this.end = end;
		this.content = content;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.font = font;
		this.color = color;
		this.colorShadow = colorShadow;
	}
	
	public TextOption clone() {
		TextOption option = new TextOption(begin, end, content, offsetX, offsetY, defaultFont, color, colorShadow);
		return option;
	}
	
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getOffsetX() {
		return offsetX;
	}
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	public int getOffsetY() {
		return offsetY;
	}
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColorShadow() {
		return colorShadow;
	}

	public void setColorShadow(Color colorShadow) {
		this.colorShadow = colorShadow;
	}
	
}
