package com.channelsoft.ems.register.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.channelsoft.ems.register.constants.EntSessionKeyConstants;

/**
 * Servlet implementation class CheckCodeServlet
 */
public class CheckCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckCodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置响应类型
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		//设置内容类型为PNG图片
		response.setContentType("image/jpeg");
		//生成图片准备
		int width = 86;
		int height = 22;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random rand = new Random();
		Font font = new Font("Arial", Font.BOLD, 16);
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(font);
		
		//随机产生四个验证码，并分别绘制到图片中
		String sRand = new String();	//验证码字符串
		String ctemp;
		char c = 0;
		for (int i = 0; i < 4; i++) {
			switch (i) {
				//第一个和第三个是数字
			case 0:
			case 2:
				c = getRandNumber();
				break;
				//第二个和第四个是字母
			case 1:
			case 3:
				c = getRandLetter();
				break;
			default:		
			}
			sRand += c;
			ctemp = new Character(c).toString();
			Color color = new Color(
					20 + rand.nextInt(110),
					20 + rand.nextInt(110),
					20 + rand.nextInt(110)
			);
			g.setColor(color);
			g.drawString(ctemp, 15 * i + 18, 14);
		}
		//将验证码字符串存入Session
		HttpSession session = request.getSession();
 		session.setAttribute(EntSessionKeyConstants.ENT_REGIST_PIC_CODE, sRand);
		g.dispose();
		//输出图片
		ImageIO.write(image, "JPEG", response.getOutputStream());
	
	}
	
	/**
	 * 随机生成一个颜色
	 * @param start		颜色的最低值
	 * @param end		颜色的最高值
	 * @return			产生的颜色
	 */
	public static Color getRandColor(int start, int end) {
		Random rand = new Random();
		if (start > 255) {
			start = 255;
		}
		if (end > 255) {
			end = 255;
		}
		int scope = end - start;
		int red = start + rand.nextInt(scope);
		int green = start + rand.nextInt(scope);
		int blue = start + rand.nextInt(scope);
		return new Color(red, green, blue);
	}
	
	/**
	 * 随机获得一个大写字母字符
	 * @return
	 */
	public static char getRandLetter() {
		int i = new Random().nextInt(26) + 65;
		return (char) i;
	}
	
	/**
	 * 随机获得一个0——9数字的字符
	 * @return
	 */
	public static char getRandNumber() {
		int i = new Random().nextInt(10) + 48;
		return (char) i;
	}
		

}
